package com.travelerinsider.pricesmart.service;

import com.travelerinsider.pricesmart.domain.entity.FoodProduct;
import com.travelerinsider.pricesmart.domain.entity.PriceDetails;
import com.travelerinsider.pricesmart.domain.entity.PriceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
@Service
public class PricingService {

    @Value("${app.version:GLOBAL}")
    private String version;

    private static final List<FoodProduct> MENU = List.of(
            new FoodProduct("Truffle Mushroom Pizza", 120.0),
            new FoodProduct("Spicy Miso Ramen", 45.0),
            new FoodProduct("Wagyu Beef Burger", 85.0),
            new FoodProduct("Avocado Toast Deluxe", 30.0),
            new FoodProduct("Honey Glazed Salmon", 110.0)
    );

    public PriceResponse calculatePrice(String productId) {
        // Pick a random food item from our menu
        FoodProduct product = MENU.get(ThreadLocalRandom.current().nextInt(MENU.size()));

        // Run the logic based on the version
        PriceDetails details = switch (version.toUpperCase()) {
            case "GLOBAL" -> new PriceDetails(product.basePrice(), "Standard Global Pricing");
            case "CUSTOMER" -> calculateCustomerPrice(product.basePrice());
            case "SUBSCRIPTION" -> calculateSubscriptionPrice(product.basePrice());
            default -> new PriceDetails(product.basePrice(), "Default Fallback");
        };

        return new PriceResponse(
                productId,
                product.name(),
                Math.round(details.finalPrice() * 100.0) / 100.0, // Round to 2 decimals
                version.toUpperCase(),
                details.note(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }

    private PriceDetails calculateCustomerPrice(double base) {
        int hour = LocalTime.now().getHour();
        // Random "Flash Sale" logic: 1 in 3 chance of an extra discount
        boolean isFlashSale = ThreadLocalRandom.current().nextInt(3) == 0;

        if (hour >= 14 && hour <= 16) {
            return new PriceDetails(base * 0.8, "Happy Hour 20% Discount!");
        } else if (isFlashSale) {
            return new PriceDetails(base * 0.9, "Random Flash Sale! 10% Off");
        }
        return new PriceDetails(base, "Standard Customer Rate");
    }

    private PriceDetails calculateSubscriptionPrice(double base) {
        simulateLatency();
        // Subscribers get a base 25% discount + a random "Loyalty Bonus" between $1-$5
        double loyaltyBonus = ThreadLocalRandom.current().nextDouble(1, 5);
        double finalPrice = (base * 0.75) - loyaltyBonus;
        return new PriceDetails(finalPrice, "Premium Tier + Loyalty Bonus Applied");
    }

    private void simulateLatency() {
        try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
