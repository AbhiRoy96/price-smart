package com.travelerinsider.pricesmart.controllers;

import com.travelerinsider.pricesmart.domain.entity.PriceResponse;
import com.travelerinsider.pricesmart.service.PricingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/*
 * Copyright 2026 Abhishek Roy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PricingController {

    private final PricingService pricingService;

    @GetMapping("/price")
    public ResponseEntity<PriceResponse> getPrice(@RequestParam String productId) {
        log.info("Received price request for product ID: {}", productId);
        PriceResponse response = pricingService.calculatePrice(productId);
        log.info("Returning price response for product ID: {}: {}", productId, response.price());
        return ResponseEntity.ok(response);
    }



}