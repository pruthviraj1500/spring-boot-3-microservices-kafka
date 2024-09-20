package com.programming.order.client;

import groovy.util.logging.Slf4j;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@FeignClient(value = "inventory", url = "${inventory.url}")
public interface InventoryClient {

    Logger log = LoggerFactory.getLogger(InventoryClient.class);

    @CircuitBreaker(name = "inventory", fallbackMethod = "fallBackMethod")
    @Retry(name = "inventory")
    @RequestMapping(method = RequestMethod.GET,value = "/api/inventory")
    boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity);

    default boolean fallBackMethod(String code, Integer quantity, Throwable throwable ){
        log.info("Can't get inventory for skuCode {} , failure reason : {}" ,code,throwable.getMessage());
        return false;
    }

}
