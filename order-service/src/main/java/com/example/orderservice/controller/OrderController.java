package com.example.orderservice.controller;

import static com.example.orderservice.common.MapUtils.mapList;

import com.example.orderservice.common.MapUtils;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.entity.Order;
import com.example.orderservice.messagequeue.KafkaProducer;
import com.example.orderservice.messagequeue.OrderProducer;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;
import java.util.List;
import java.util.UUID;
import javax.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/order-service")
@RequiredArgsConstructor
public class OrderController {

    private final Environment env;
    private final ModelMapper mapper;
    private final OrderService orderService;
    private final KafkaProducer kafkaProducer;
    private final OrderProducer orderProducer;

    @GetMapping("/health-check")
    public String status() {
        return "Order Service is now working on PORT " + env.getProperty("local.server.port");
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId,
        @RequestBody RequestOrder order) {
        log.info("before add orders data");
        OrderDto orderDto = mapper.map(order, OrderDto.class);
        orderDto.setUserId(userId);

        // JPA
        OrderDto createdOrder = orderService.createOrder(orderDto);
        ResponseOrder result = mapper.map(createdOrder, ResponseOrder.class);

        // kafka -> orderService.createOrder의 로직을 바로 구현 -> 따로 서비스로 빼는 것이 더 좋지 않을지?
//        orderDto.setOrderId(UUID.randomUUID().toString());
//        orderDto.setTotalPrice(orderDto.getUnitPrice() * orderDto.getQty());
//
//        kafkaProducer.send("example-catalog-topic", orderDto);
//        orderProducer.send("orders", orderDto);

//        ResponseOrder result = mapper.map(orderDto, ResponseOrder.class);
        log.info("after add orders data");

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable("userId") String userId) {
        log.info("before retrieve orders data");

        List<Order> findOrders = orderService.getOrdersByUserId(userId);
        List<ResponseOrder> responseOrders = mapList(findOrders, ResponseOrder.class);
        log.info("after received orders data");

        return ResponseEntity.status(HttpStatus.OK).body(responseOrders);

    }
}
