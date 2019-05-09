# discountApplication
This is Spring Boot application so it can be run from IDE as simple Java application or from console with command: 
$ mvn spring-boot:run
After that the output will be available as REST endpoint (GET method) under URL: http://localhost:8080/api/discounts
Input data are hardcoded in the following classes: 
- list of products (with names and prices) at ProductRepository
- discount at DiscountRepository
