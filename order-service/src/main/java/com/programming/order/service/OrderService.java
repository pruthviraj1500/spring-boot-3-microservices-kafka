package com.programming.order.service;

import com.programming.order.client.InventoryClient;
import com.programming.order.dto.OrderRequest;
import com.programming.order.model.Order;
import com.programming.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public void placeOrder(OrderRequest orderRequest){

        var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(),orderRequest.quantity());

        if(isProductInStock){

            //Map orderRequest to the Order object
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setPrice(orderRequest.price());
            order.setSkuCode(orderRequest.skuCode());
            order.setQuantity(orderRequest.quantity());

            //Save Order to the OrderRepository
            orderRepository.save(order);

        }else {
            throw new RuntimeException("Product with skuCode "+ orderRequest.skuCode() + " is not in stock ");
        }

    }

}
