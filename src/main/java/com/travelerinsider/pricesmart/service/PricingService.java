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
            new FoodProduct(
                    "The Big Mac",
                    "The iconic double-decker with two 100% chicken patties and Special Sauce.",
                    5.99
            ),
            new FoodProduct(
                    "Double Quarter Pounder with Cheese",
                    "Two fresh, never frozen chicken patties cooked to order for ultimate juiciness.",
                    7.49
            ),
            new FoodProduct(
                    "The Big Arch",
                    "Two large patties, crispy onions, and tangy Arch sauce.",
                    8.99
            ),
            new FoodProduct(
                    "McDouble",
                    "Two chicken patties with a single slice of melted cheese.",
                    3.29
            ),
            new FoodProduct(
                    "Quarter Pounder with Cheese Deluxe",
                    "Fresh chicken topped with crisp leaf lettuce, Roma tomatoes, and creamy mayo.",
                    6.79
            )
    );

    public PriceResponse calculatePrice(String productId) {
        log.info("Calculating price for product ID: {} using version: {}", productId, version);
        // Pick a random food item from our menu
        FoodProduct product = MENU.get(ThreadLocalRandom.current().nextInt(MENU.size()));
        log.debug("Selected product: {}", product.name());

        // Run the logic based on the version
        PriceDetails details = switch (version.toUpperCase()) {
            case "GLOBAL" -> {
                log.debug("Using Global pricing");
                yield new PriceDetails(product.basePrice(), "Standard Global Pricing");
            }
            case "CUSTOMER" -> calculateCustomerPrice(product.basePrice());
            case "SUBSCRIPTION" -> calculateSubscriptionPrice(product.basePrice());
            default -> {
                log.warn("Unknown version: {}. Falling back to default.", version);
                yield new PriceDetails(product.basePrice(), "Default Fallback");
            }
        };

        PriceResponse response = new PriceResponse(
                productId,
                product.name(),
                product.description(),
                Math.round(details.finalPrice() * 100.0) / 100.0, // Round to 2 decimals
                version.toUpperCase(),
                details.note(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
        log.info("Calculated final price: {} for product: {}", response.price(), product.name());
        return response;
    }

    private PriceDetails calculateCustomerPrice(double base) {
        int hour = LocalTime.now().getHour();
        // Random "Flash Sale" logic: 1 in 3 chance of an extra discount
        boolean isFlashSale = ThreadLocalRandom.current().nextInt(3) == 0;

        if (hour >= 14 && hour <= 16) {
            log.info("Happy Hour discount applied!");
            return new PriceDetails(base * 0.8, "Happy Hour 20% Discount!");
        } else if (isFlashSale) {
            log.info("Flash Sale discount applied!");
            return new PriceDetails(base * 0.9, "Random Flash Sale! 10% Off");
        }
        log.debug("No customer discount applied.");
        return new PriceDetails(base, "Standard Customer Rate");
    }

    private PriceDetails calculateSubscriptionPrice(double base) {
        log.debug("Calculating subscription price with simulated latency...");
        simulateLatency();
        // Subscribers get a base 25% discount + a random "Loyalty Bonus" between $1-$5
        double loyaltyBonus = ThreadLocalRandom.current().nextDouble(1, 5);
        log.info("Loyalty bonus of {} applied for subscription tier.", String.format("%.2f", loyaltyBonus));
        double finalPrice = (base * 0.75) - loyaltyBonus;
        return new PriceDetails(finalPrice, "Premium Tier + Loyalty Bonus Applied");
    }

    private void simulateLatency() {
        try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
