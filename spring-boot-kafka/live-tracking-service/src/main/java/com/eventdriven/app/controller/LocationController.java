package com.eventdriven.app.controller;

import jakarta.annotation.PostConstruct;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.ReadOnlyWindowStore;
import org.apache.kafka.streams.state.WindowStoreIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/location")
public class LocationController {

    private static final Logger log = LoggerFactory.getLogger(LocationController.class);

    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    @Autowired
    public LocationController(StreamsBuilderFactoryBean streamsBuilderFactoryBean) {
        this.streamsBuilderFactoryBean = streamsBuilderFactoryBean;
    }

    @PostConstruct
    public void postConstruct() {
        log.info("Delivery Topology Description:");
        Topology topology = streamsBuilderFactoryBean.getTopology();
        if (topology != null) {
            log.info(topology.describe().toString());
        }
    }

    @GetMapping("/cities/{city}/orders")
    public Long getOrderCount(@PathVariable String city) {
        KafkaStreams kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
        if (kafkaStreams == null) return 0L;
        
        ReadOnlyKeyValueStore<String, Long> store =
                kafkaStreams.store(
                        StoreQueryParameters.fromNameAndType(
                                "city-order-count",
                                QueryableStoreTypes.keyValueStore()
                        )
                );

        return store.get(city);
    }

    @GetMapping("/cities/orders")
    public Map<String, Long> getAllCounts() {
        KafkaStreams kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
        if (kafkaStreams == null) return new HashMap<>();
        
        ReadOnlyKeyValueStore<String, Long> store =
                kafkaStreams.store(
                        StoreQueryParameters.fromNameAndType(
                                "city-order-count",
                                QueryableStoreTypes.keyValueStore()
                        )
                );

        Map<String, Long> result = new HashMap<>();
        try (KeyValueIterator<String, Long> iterator = store.all()) {
            while (iterator.hasNext()) {
                KeyValue<String, Long> next = iterator.next();
                result.put(next.key, next.value);
            }
        }
        return result;
    }

    @GetMapping("/drivers/{driverId}/distance")
    public Double getDistance(
            @PathVariable String driverId) {

        KafkaStreams streams =
                streamsBuilderFactoryBean.getKafkaStreams();
        if (streams == null) return 0.0;
        
        ReadOnlyKeyValueStore<String, Double> store =
                streams.store(
                        StoreQueryParameters.fromNameAndType(
                                "total-distance-per-driver",
                                QueryableStoreTypes.keyValueStore()
                        )
                );

        return store.get(driverId);
    }

    @GetMapping("/drivers/{driverId}/overspeeding")
    public Map<String, Long> getOverspeedingCount(@PathVariable String driverId) {
        KafkaStreams kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
        if (kafkaStreams == null) return new HashMap<>();
        
        ReadOnlyWindowStore<String, Long> store =
                kafkaStreams.store(
                        StoreQueryParameters.fromNameAndType(
                                "overspeeding-count-2min",
                                QueryableStoreTypes.windowStore()
                        )
                );

        Map<String, Long> result = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
        
        // Fetch all windows for the given driverId from the beginning of time until now
        try (WindowStoreIterator<Long> iterator = store.fetch(driverId, Instant.ofEpochMilli(0), Instant.now())) {
            while (iterator.hasNext()) {
                KeyValue<Long, Long> next = iterator.next();
                Long windowStartTs = next.key;
                String windowString = formatter.format(Instant.ofEpochMilli(windowStartTs));
                result.put(windowString, next.value);
            }
        }
        return result;
    }

}
