# Fawry Quantum Internship Challenge README

## Project Overview
This repository contains an e-commerce system designed for the Fawry Quantum Internship Challenge. The system implements the specified features to manage products, carts, customer transactions, and shipping processes.

## Features

### Product Management
- **Product Definition**: Products are defined with a `name`, `price`, and `quantity`.
- **Expiry Status**: Some products (e.g., Cheese, Biscuits) may expire, while others (e.g., TV, Mobile) do not expire.
- **Shipping Requirements**: 
  - Certain products (e.g., Cheese, TV) require shipping, while others (e.g., Mobile, Scratch Cards) do not.
  - Every shippable item provides its `weight` (in kilograms).

### Cart Functionality
- **Add to Cart**: Customers can add a product to their cart with a specific quantity, limited to the available product quantity.

### Checkout Process
- **Checkout Execution**: Customers can complete a checkout with items in their cart.
- **Console Output**:
  - **Order Subtotal**: Sum of all items' prices.
  - **Shipping Fees**: Additional cost based on shippable items (flat fee of 30 if applicable).
  - **Paid Amount**: Total amount (subtotal + shipping fees).
  - **Customer Balance**: Updated balance after payment.
- **Error Handling**:
  - Throws an error if the cart is empty.
  - Throws an error if the customer's balance is insufficient.
  - Throws an error if any product is out of stock or expired.
- **Shipping Integration**: 
  - If applicable (i.e., when shippable items exist), collects all items that need to be shipped.
  - Sends these items to a `ShippingService` interface, which accepts a list of objects implementing `String getName()` and `double getWeight()` methods.

## Implementation Details
- **Language**: Java
- **File**: `Fawry.java`
- **Classes**:
  - `Product`: Manages product attributes and stock.
  - `Cart`: Handles item additions and subtotal calculation.
  - `Customer`: Manages customer balance.
  - `ShippingService`: Interface for shipping objects.
  - `DefaultShipping`: Implements `ShippingService` for products.
  - `Checkout`: Orchestrates the checkout process and output.
