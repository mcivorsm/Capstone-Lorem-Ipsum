#Capstone - Lorem Ipsum

  Kelly Ortega
  Nathan Thomas
  McIvor Saint-Maxens

  Game Review Web Application
A full-stack CRUD web application that allows users to create, read, update, and delete video game reviews.

Tech Stack
Frontend: React.js

Backend: Java with Spring Boot

Database: PostgreSQL (Amazon RDS)

Cloud & Deployment: AWS S3 (static hosting), AWS EC2 (backend hosting)

Features
User authentication and session management

Create, edit, delete, and view reviews for video games

Responsive UI built with React

RESTful API with Spring Boot and PostgreSQL

Deployed on AWS for scalability and availability

System Architecture
css
Copy
Edit
         ┌───────────────┐
         │   React.js    │
         │   Frontend    │
         └───────┬───────┘
                 │ HTTP/REST
                 ▼
         ┌───────────────┐
         │ Spring Boot   │
         │   Backend     │
         └───────┬───────┘
                 │ JDBC
                 ▼
         ┌───────────────┐
         │ PostgreSQL     │
         │   (AWS RDS)    │
         └───────────────┘

   Static Assets → AWS S3  
   Backend/API   → AWS EC2  
Additional Information
API documentation is available through Swagger/OpenAPI.

Unit and integration testing implemented with JUnit and Mockito.

Application follows standard Spring Boot layered architecture (Controller → Service → Repository).

Designed for scalability and maintainability with clear separation of concerns.

  


