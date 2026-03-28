# Port Management System

A Java Swing GUI application for managing port operations, integrated with a MySQL database.

## Features
- **Administrative Login:** Access for authorized personnel (demo uses hardcoded credentials in `Portm.java`). Note: This implementation is for demonstration purposes and is not production-secure.
- **Customer Management:** Full CRUD (Create, Read, Update, Delete) operations for customer profiles.
- **Shipping Logistics:** Manage import and export records, specifying item details, container types, and origin/destination countries (Ethiopia, India, USA, UK, China, Taiwan, Germany, Kenya).
- **Automated Billing:**
  - Cost calculation based on weight brackets and travel distance.
  - Integrated service charges and tax rate application.
  - Detailed receipt generation.
- **Financial Summary:** View total spending history for individual customers.
- **Data Persistence:** Reliable storage using MySQL database.

## Prerequisites
- Java Development Kit (JDK) 8 or higher.
- MySQL Server.

## Setup
1. **Database Setup:**
   - Create a MySQL database named `port`.
   - Execute the `port.sql` script located in the root directory to initialize tables (`Customer`, `Item`, `cost`) and views (`item_cost`, `item_buyer`).
2. **Connection Configuration:**
   - **Development Only:** The application connects to MySQL at `localhost` with the default username `root` and password `password`.
   - **Hardening:** For any non-local deployment, change these credentials in `Portm/src/portm/Portm.java`. It is strongly advised to:
     - Use a dedicated non-root MySQL account.
     - Use a strong, unique password.
     - Externalize configuration using environment variables or external configuration files in a production environment.

## Usage
1. **Default Login:**
   - **Username:** `admin`
   - **Password:** `password`
2. **Running the Application:**
   - This is a NetBeans/Ant project. Open the `Portm` folder in NetBeans to build and run.
   - Alternatively, compile and run from the command line, ensuring the MySQL JDBC driver (e.g., `mysql-connector-j-8.1.0.jar`) is included in the classpath.
