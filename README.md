# PriceSmart Service

PriceSmart is a dynamic pricing engine built with Spring Boot, designed to demonstrate Istio's traffic management capabilities in a Kubernetes environment. The service provides a REST API to calculate product prices based on different pricing models (Global, Customer, and Subscription) controlled via environment variables.

## Features

- **Dynamic Pricing Models**:
  - `GLOBAL`: Standard pricing for all users.
  - `CUSTOMER`: Applies "Happy Hour" (2 PM - 4 PM) and "Flash Sale" (random 1 in 3 chance) discounts.
  - `SUBSCRIPTION`: Premium tier pricing with base discounts and loyalty bonuses.
- **Istio Ready**: Designed for Canary deployments and traffic splitting experiments.
- **Observability**: Structured logging for tracking price calculations and discount applications.
- **Actuator Enabled**: Built-in health checks and metrics.

## Tech Stack

- Java 21
- Spring Boot 4.0.2
- Maven
- Docker
- Kubernetes & Istio (for deployment)

## API Endpoints

### Get Product Price
`GET /api/v1/price?productId={id}`

**Response Example:**
```json
{
  "id": "123",
  "name": "The Big Mac",
  "description": "The iconic double-decker with two 100% chicken patties and Special Sauce.",
  "price": 5.99,
  "model": "GLOBAL",
  "note": "Standard Global Pricing",
  "timestamp": "2026-02-05 19:15:00"
}
```

## Getting Started

### Prerequisites
- JDK 21
- Maven 3.9+
- Docker (optional)

### Running Locally
```bash
./mvnw spring-boot:run
```
The service will be available at `http://localhost:8080`.

### Building the JAR
```bash
./mvnw clean package
```

### Docker Build
```bash
docker build -t pricing-service:latest .
```

## Deployment with Istio

The repository includes Istio manifests in `devops-setup/pricing-gitops` to demonstrate advanced traffic routing:

1. **Deploy the Service**:
   ```bash
   kubectl apply -f devops-setup/pricing-gitops/deployment-v1.yaml
   kubectl apply -f devops-setup/pricing-gitops/deployment-v2.yaml
   kubectl apply -f devops-setup/pricing-gitops/deployment-v3.yaml
   ```

2. **Configure Networking**:
   ```bash
   kubectl apply -f devops-setup/pricing-gitops/gateway.yaml
   kubectl apply -f devops-setup/pricing-gitops/service.yaml
   kubectl apply -f devops-setup/pricing-gitops/destination-rule.yaml
   kubectl apply -f devops-setup/pricing-gitops/virtual-service.yaml
   ```

This setup will split traffic across three versions (v1: 40%, v2: 40%, v3: 20%).

## License

Copyright 2026 Abhishek Roy. Licensed under the Apache License, Version 2.0.
