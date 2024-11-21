# 🚗 Car Agency 

### 📖 Overview
This project involves the backend development for a used car sales agency, created as part of an integrative practical assignment. The system manages test drives, tracks vehicle locations in real time, and ensures operation within permitted zones and radii. Additionally, it implements notifications and restrictions to maximize security and operational control.

## 🚀 Key Features

### 1️⃣ Test Drive Management
- Test drive registration associating customer, vehicle, and employee.
- Validation of:
  - Customers' licenses (must not be expired).
  - Customers' status (must not be restricted from testing vehicles).
  - Vehicle availability to prevent simultaneous test drives.
- Test drive completion with optional employee comments.

### 2️⃣ Vehicle Tracking and Safety
- Real-time reception of vehicle coordinates.
- Position validation to:
  - Detect if a vehicle exceeds the permitted radius.
  - Prevent entry into restricted zones.
- Automatic notifications:
  - Sent to the responsible employee to return the vehicle.
  - Registering the customer in a restricted list (prohibited from future tests).

### 3️⃣ Notifications
- Promotional notifications to customers (via database or external means such as SMS or email, depending on configuration).

### 4️⃣ Reports
Detailed generation of reports on:
- **Incidents**: Cases where established limits were exceeded.
- **Employee Tests**: Summary of incidents and completed tests for each employee.
- **Distance Covered**: Kilometers traveled by vehicles during specific periods.
- **Test History**: Complete details of tests conducted for a specific vehicle.

### 5️⃣ External Service Consumption
- Retrieval of:
  - Agency coordinates.
  - Permitted radius for test drives.
  - Restricted zones.
- These data are consumed through a service provided by the project supervisors.

### 6️⃣ Security
- Implementation of authentication and authorization mechanisms:
  - Only employees can create tests and send notifications.
  - Only users associated with vehicles can report positions.
  - Only administrators can generate reports.

## 🛠️ Project Architecture
- Design based on **microservices**, ensuring modularity and scalability.
- Use of an **API Gateway** as a single entry point.
- Compliance with recommended service interaction guidelines.

## 💾 Database Structure
The database includes tables for:
- **👨‍💼 Employees**: Information about agency employees.
- **🧑‍🤝‍🧑 Interested Parties**: Customer data, including documents and licenses.
- **🏷️ Brands** and **🚘 Models**: Catalog of vehicle brands and models.
- **📬 Notifications**: Log of notifications sent.
- **📍 Positions**: Vehicle locations with latitude and longitude.
- **📝 Tests**: Detailed information on test drives.
- **🚗 Vehicles**: Inventory of vehicles available for sale.

### Database Diagram
![Car Agency Database Diagram](public/agencia-diagram.png)

## 🌍 Restricted Zones and Permitted Radius

### 🚫 Restricted Zones
![Restricted Zones](public/google-earth/zonas-restringidas.jpg)  
Areas considered dangerous where vehicles cannot operate.

### 🟢 Permitted Radius
![Allowed Radius](public/google-earth/radio-adminito.jpg)  
Geographical area within which vehicles can operate without restrictions.

For more details, visit the interactive map on [Google Earth](https://earth.google.com/earth/d/1MhR_sH8E4Rkww0rtBbOarKmjkQdc1RNU?usp=sharing). 🔗

## 🔗 Technical Requirements
- Node.js / Express for the backend.
- Use of external services for geographical data.
- Microservices and API Gateway as core components.
- Security implemented with role-based authentication and authorization.

---
