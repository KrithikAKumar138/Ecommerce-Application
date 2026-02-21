🛒 E-Commerce Web Application (Spring Boot)

A full-stack E-Commerce Web Application built using Spring Boot, Thymeleaf, Spring Security, and MySQL with features like user authentication, product management, cart, orders, and Razorpay payment integration.

•	Spring Boot (backend)

•	Thymeleaf (frontend UI)

•	MySQL (database)

•	Spring Security (authentication & authorization)

•	Razorpay (online payment gateway)

The system supports two roles:

•	👤 User → can browse products, add to cart, place orders, and pay

•	🛠️ Admin → can manage products and orders

The project covers the complete shopping flow:

Login → Browse Products → Add to Cart → Checkout → Pay → Place Order → Track Order

________________________________________
🏗️ 2. Architecture (How the Project is Structured)

Your project follows a layered architecture:

✅ Controller Layer

•	Handles HTTP requests from the browser

•	Example:

o	ProductController → handles product pages

o	CartController → handles cart actions

o	OrderController → handles order actions

o	AuthController → handles login & registration

✅ Service Layer

•	Contains business logic

•	Example:

o	ProductService → product operations

o	CartService → cart calculations

o	OrderService → order creation logic

o	PaymentService → payment handling

✅ Repository Layer

•	Talks to the database using Spring Data JPA

•	Example:

o	UserRepository

o	ProductRepository

o	OrderRepository

o	CartRepository

✅ Entity Layer

•	Represents database tables

•	Example:

o	User

o	Product

o	CartItem

o	Order

o	OrderItem

o	Payment

✅ View Layer (Thymeleaf)

•	HTML pages for:

o	Login / Register

o	Product listing

o	Cart

o	Checkout

o	Orders

o	Admin dashboard

________________________________________
🔐 3. Authentication & Security (Spring Security)

Your project uses Spring Security for:

•	User login & logout

•	Password encryption using BCrypt

•	Role-based access:

o	ROLE_USER

o	ROLE_ADMIN

How it works:

1.	User registers → password is encrypted

2.	User logs in → Spring Security authenticates


3.	Based on role:

o	Admin → can access /admin/**

o	User → can access shopping pages


4.	Unauthorized users cannot access protected pages

So this makes your app secure and professional.

________________________________________
📦 4. Product Module

Admin can:

•	Add new products

•	Edit existing products

•	Delete / deactivate products

•	Upload product images

•	Manage stock quantity

User can:

•	View all products

•	Search products

•	Filter products

•	View product details

In backend:

•	Product entity stores:

o	name, price, description, stock, image, active status

•	ProductService handles:

o	save, update, delete, fetch products

•	ProductRepository talks to DB

________________________________________
🛒 5. Cart Module

Each logged-in user has their own cart.

User can:

•	Add product to cart

•	Increase / decrease quantity

•	Remove product

•	Clear cart

•	View total price

In backend:

•	CartItem entity links:

o	User ↔ Product ↔ Quantity

•	CartService:

o	Calculates total price

o	Updates quantity

o	Removes items

•	Cart is always user-specific

________________________________________

📦 6. Checkout & Order Module

Flow:

1.	User goes to checkout

2.	Enters/selects address

3.	Proceeds to payment

4.	After successful payment → order is created

5.	Cart is cleared

6.	Stock is reduced

Order contains:

•	Order ID

•	User details

•	List of ordered items

•	Total amount

•	Order status:

o	PLACED / PAID / SHIPPED / DELIVERED / CANCELLED

Admin can:

•	View all orders

•	Update order status

User can:

•	View my orders

•	Track order status

________________________________________
💳 7. Payment Module (Razorpay)

Your project integrates Razorpay for online payment.

Flow:

1.	Backend creates Razorpay order

2.	Razorpay popup opens in browser


3.	User pays using:

o	UPI / Card / NetBanking

4.	Razorpay returns:

o	Payment ID

o	Signature


5.	Backend verifies payment

6.	If payment is successful:


o	Order is saved in DB

7.	If payment fails:

o	Order is not created

This ensures secure and real payment flow.


________________________________________
🧪 8. Testing (JUnit + Mockito)

You have tests for:

•	Controllers → using @WebMvcTest

•	Services → using Mockito

•	Repositories → using @DataJpaTest

Why this is important:

•	Checks business logic works correctly

•	Prevents bugs

•	Makes your project industry-standard

You can run:

mvn test
________________________________________
🗄️ 9. Database (MySQL)

Main tables:

•	users

•	products

•	cart_items

•	orders

•	order_items

•	payments

Spring Data JPA + Hibernate:

•	Automatically creates/updates tables

•	Maps Java objects → DB tables

 
