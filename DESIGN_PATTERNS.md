# Design Patterns — Food Ordering System Application

## 4 Implemented Patterns (All Creational as per requirement)

---

### 1. 🏭 Factory Pattern
**Location:** `pattern/factory/FoodItemFactory.java`
**Used in:** `MenuService`, `FoodFactoryService`

Creates `FoodItem` objects by **type** without exposing instantiation details.
Each type (VEG, NON_VEG, BEVERAGE, DESSERT) auto-assigns category and imageUrl.

```java
FoodItem item = foodItemFactory.createFoodItem("VEG", "Paneer Tikka", 180.0, "Grilled paneer");
```

---

### 2. 🔷 Singleton Pattern
**Location:** `pattern/singleton/CartManager.java`
**Used in:** `CartService`, `OrderService`

Ensures **one CartManager instance** per application context.
Maps `customerId → cartId` to guarantee each customer has exactly one active cart.

```java
CartManager.getInstance().registerCart(customerId, cartId);
```

---

### 3. 🧱 Builder Pattern
**Location:** `pattern/builder/OrderBuilder.java`
**Used in:** `OrderService.placeOrder()`

Constructs complex `Order` objects **step-by-step** using a fluent API.
Validates required fields (customer, restaurant, address) before building.

```java
Order order = orderBuilder.reset()
    .withCustomer(customer)
    .withRestaurant(restaurant)
    .withDeliveryAddress(address)
    .withTotalAmount(cart.calculateTotal())
    .withItems(cartItems)
    .withStatus(OrderStatus.PLACED)
    .build();
```

---

### 4. 🔁 Prototype Pattern
**Location:** `pattern/prototype/FoodItemPrototype.java`, `pattern/prototype/Cloneable.java`
**Used in:** `FoodFactoryService.cloneFoodItem()`, `FoodFactoryController`

Clones existing `FoodItem` objects as templates for rapid variation creation.
Copies category, description, and imageUrl — only name and price are customized.

```java
FoodItemPrototype prototype = new FoodItemPrototype(source.getName(), ...);
FoodItemPrototype cloned = prototype.cloneObject();
cloned.setName("Paneer Burger (Spicy)");
cloned.setPrice(220.0);
```

---

## FoodFactory Portal
**URL:** `/restaurant/factory`
**Controller:** `FoodFactoryController.java`
**Service:** `FoodFactoryService.java`
**Template:** `templates/restaurant/factory.html`

The FoodFactory portal provides a dedicated UI for restaurant owners to:
- **Create** new items using the Factory Pattern (type-based creation)
- **Clone** existing items using the Prototype Pattern (template-based creation)
- **Edit** and **Delete** food items
- View category-wise stats (Veg / Non-Veg / Beverage / Dessert counts)

---

## Added/Fixed Classes (vs original UML)

| Class | Package | Status |
|-------|---------|--------|
| `OrderBuilder` | `pattern.builder` | ✅ New |
| `FoodItemPrototype` | `pattern.prototype` | ✅ New |
| `Cloneable<T>` | `pattern.prototype` | ✅ New |
| `FoodFactoryController` | `controller` | ✅ New |
| `FoodFactoryService` | `service` | ✅ New |
| `RefundService` | `service` | ✅ New |
| `RefundRepository` | `repository` | ✅ New |
| `PaymentService` | `service` | ✅ New |
| `factory.html` | `templates/restaurant` | ✅ New |
| `refunds.html` | `templates/admin` | ✅ New |
