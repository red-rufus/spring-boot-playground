package com.eventdriven.app.repository;

import com.eventdriven.app.entity.OrderEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderEventRepository extends MongoRepository<OrderEvent,String> {
}
