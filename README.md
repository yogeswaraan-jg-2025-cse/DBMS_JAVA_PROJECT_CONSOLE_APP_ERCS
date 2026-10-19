# ERCS — Emergency Response & Resource Coordination System

**Project title:** Emergency Response & Resource Coordination System (ERCS)

ERCS is a **Core Java and MySQL console application** that demonstrates the practical application of object-oriented programming, relational database design, JDBC database connectivity, and CRUD operations. It records simulated emergency cases, calculates a transparent rule-based priority score, and recommends suitable sample ambulance, hospital, and blood-bank resources.

> **Safety note:** ERCS is an academic simulation using sample data. It is not a medical device, does not provide clinical advice, and must not be used to coordinate real emergencies. Real emergencies require contacting local emergency services and qualified medical professionals.

## Table of Contents

- [Project Objectives](#project-objectives)
- [Problem Statement](#problem-statement)
- [Basic Functionality](#basic-functionality)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [System Architecture](#system-architecture)
- [Project Structure](#project-structure)
- [Database Design](#database-design)
- [Decision Logic](#decision-logic)
- [Prerequisites](#prerequisites)
- [Setup and Execution](#setup-and-execution)
- [Testing Scenarios](#testing-scenarios)
- [Limitations](#limitations)
- [Future Enhancements](#future-enhancements)
- [Learning Outcomes](#learning-outcomes)

## Project Objectives

- Demonstrate core OOP concepts: classes and objects, encapsulation through model classes, abstraction through service and DAO layers, and separation of responsibilities.
- Design and implement a normalized MySQL database with related patient, emergency, hospital, blood-bank, ambulance, response, and priority-rule tables.
- Connect the Java application to MySQL using JDBC, `Connection`, `PreparedStatement`, `ResultSet`, and transaction handling.
- Implement CRUD operations for emergency-response data: create patient and emergency reports, read resources and active cases, update emergency/resource status and stock-related records, and support database-managed response records.
- Prioritize simulated emergencies using transparent, configurable database rules.
- Recommend sample resources using availability, distance, and configured facility capabilities, then produce a clear response plan.

## Problem Statement

In a simulated emergency-response environment, patient details, emergency reports, ambulances, hospitals, and blood-bank inventory must be tracked in one connected system. Without structured records and consistent prioritization, it is difficult to view pending cases, identify suitable available resources, or maintain a history of planned responses.

ERCS addresses this academic problem by providing a console-based Java application connected to MySQL. It stores related emergency-response data, applies predefined demonstration priority rules, retrieves available sample resources, and records response-plan decisions. The system is intended for learning database-backed Java development only; it must not be used for real emergency or medical decisions.

## Basic Functionality

- Create and store patient and emergency reports.
- Calculate and save a simulated priority score and severity level.
- Read and display pending emergencies through a priority queue.
- Read and recommend available ambulances, hospitals, and matching blood-bank stock.
- Create response-plan records and update the related emergency and ambulance status when a dispatch plan is ready.
- Complete a planned response to release its ambulance back to the available resource pool.
- Display active emergencies and current resource data.
- Use input validation, prepared statements, exception handling, and transactions for safe demonstration workflows.

## Features

1. **Emergency reporting** — enter patient and incident details and save them to MySQL.
2. **Severity and priority scoring** — calculate a demonstration score from configured condition rules.
3. **Emergency priority queue** — order active cases by severity and score using Java's `PriorityQueue`.
4. **Ambulance recommendation** — filter available sample ambulances and rank them by suitability and distance.
5. **Hospital recommendation** — evaluate configured distance, emergency-service, trauma-unit, and ICU availability.
6. **Blood-bank search** — search sample stock by the patient's recorded blood group and available units; flag severe-blood-loss cases for urgent review.
7. **Response plan** — combine the case priority and available resource recommendations into one console report.
8. **Resource overview** — display configured hospitals, ambulances, blood-bank inventory, and active cases.
9. **Input validation and error handling** — handle invalid entries and database errors.
10. **Persistent storage** — use JDBC and prepared SQL statements to interact with MySQL.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java (Core Java) | Application logic, models, collections, and console interface |
| MySQL | Persistent relational database |
| JDBC | Java-to-MySQL connectivity |
| MySQL Connector/J | JDBC driver |
| CMD | Build and run the application on Windows |

No web frontend, Spring, Hibernate, or external AI API is required.

## System Architecture

```text
User
  |
  v
Console Interface (Main.java)
  |
  v
Service Layer
  |-- EmergencyService
  |-- HospitalService
  |-- BloodBankService
  |-- AmbulanceService
  `-- ResourceService
  |
  v
Decision Logic
  |-- Rule-based priority scoring
  |-- PriorityQueue ordering
  |-- Hospital suitability scoring
  |-- Ambulance availability/distance ranking
  `-- Blood-bank stock lookup
  |
  v
DAO Layer
  |-- PatientDAO
  |-- EmergencyDAO
  |-- HospitalDAO
  |-- BloodBankDAO
  `-- AmbulanceDAO
  |
  v
JDBC (PreparedStatement / ResultSet)
  |
  v
MySQL Database
```

## Project Structure

```text
ERCS/
├── src/
│   ├── Main.java
│   ├── model/
│   │   ├── Patient.java
│   │   ├── Emergency.java
│   │   ├── Hospital.java
│   │   ├── BloodBank.java
│   │   ├── Ambulance.java
│   │   └── ResponsePlan.java
│   ├── dao/
│   │   ├── PatientDAO.java
│   │   ├── EmergencyDAO.java
│   │   ├── HospitalDAO.java
│   │   ├── BloodBankDAO.java
│   │   └── AmbulanceDAO.java
│   ├── service/
│   │   ├── EmergencyService.java
│   │   ├── HospitalService.java
│   │   ├── BloodBankService.java
│   │   ├── AmbulanceService.java
│   │   └── ResourceService.java
│   └── util/
│       ├── DBConnection.java
│       ├── InputValidator.java
│       └── Constants.java
├── lib/
│   └── mysql-connector-j-9.7.0.jar
├── sql/
│   ├── database.sql
│   └── sample_data.sql
└── README.md
```

*The tree describes the intended structure; filenames may differ slightly as implementation evolves.*

## Database Design

The database is named `ercs`.

| Table | Purpose |
|---|---|
| `patient` | Patient details, recorded blood group, blood-loss category, and condition description |
| `emergency` | Incident type, location, severity, priority score, and status |
| `hospital` | Sample hospital distance, ICU bed count, trauma capability, and emergency availability |
| `blood_bank` | Sample stock by blood group, location, distance, and units available |
| `ambulance` | Ambulance type, location, distance, and availability status |
| `response` | Links an emergency to the recommended resources and response status |
| `priority_rule` | Editable academic scoring keywords and point values |

The `emergency` table references `patient`. The `response` table references the emergency and the selected resource records.

## Decision Logic

### 1. Demonstration priority scoring

The initial configurable example assigns points to reported conditions:

| Condition | Example points |
|---|---:|
| Severe blood loss | +5 |
| Moderate blood loss | +3 |
| Mild blood loss | +1 |
| Unconsciousness | +5 |
| Breathing difficulty | +5 |
| Chest injury | +4 |
| Head bleeding / head injury | +4 |
| Nose bleeding / nosebleed | +2 |
| Major fracture | +3 |
| Minor injury | +1 |

Example score bands:

| Score | Simulated priority |
|---:|---|
| 10 or above | CRITICAL |
| 6–9 | HIGH |
| 3–5 | MEDIUM |
| 0–2 | LOW |

These rules are **illustrative project logic only**, not a clinically validated triage protocol. The implementation should define how overlapping conditions are counted and should not infer a medical diagnosis.

The scoring catalog is stored in `PRIORITY_RULE`. Add, deactivate, or adjust demonstration keywords and their points in that table; restart the application after changing rules.

### 2. Ambulance selection

- Filter for records marked `AVAILABLE`.
- Apply any configured vehicle suitability rules.
- Rank suitable candidates by recorded distance.
- Report clearly when no suitable sample ambulance is available.

### 3. Hospital selection

- Consider emergency-service availability.
- Check configured ICU and trauma capabilities against the simulated case requirements.
- Rank eligible hospitals using a documented score that includes distance.
- Report when no suitable hospital exists in the sample data.

### 4. Blood-bank lookup

- Flag severe-blood-loss cases for urgent professional review in the simulation.
- Search stock records for the recorded blood group and positive available-unit count.
- Rank matching sample records by distance.
- Report when no matching stock is recorded.

**Important:** Real transfusion compatibility and the amount/type of blood required must be determined by qualified clinicians and blood-bank professionals. A recorded blood group alone is not sufficient to authorize transfusion. ERCS must not make clinical transfusion decisions.

### 5. Multiple emergencies

Use Java's `PriorityQueue` with an explicit comparator so higher simulated severity and score are ordered first. Add a stable tie-breaker, such as earlier creation time or emergency ID, to make demonstrations predictable.

## Prerequisites

- A supported Java JDK installed; `java` and `javac` available in CMD.
- MySQL Server installed and running.
- MySQL command-line client available, or an equivalent SQL client.
- MySQL Connector/J JAR placed in `lib/`.
- A MySQL account with permission to create/use the `ercs` database.

Check the installations in Windows CMD:

```cmd
java -version
javac -version
mysql --version
```

## Setup and Execution

### Quick start on Windows

From the `ERCS` folder, double-click `run-ercs.bat`, or run this one command in CMD:

```cmd
run-ercs.bat
```

The script compiles the source files, uses local demo credentials of `root` / `root` unless you have already set `ERCS_DB_USER` and `ERCS_DB_PASSWORD`, and starts the application.

### 1. Create the database

Open the MySQL client:

```cmd
mysql -u root -p
```

Then create the database:

```sql
CREATE DATABASE ercs;
USE ercs;
```

### 2. Create the tables and sample data

Run the SQL scripts in order. From Windows CMD, if the scripts contain the complete schema and seed data:

```cmd
mysql -u root -p ercs < sql\database.sql
mysql -u root -p ercs < sql\sample_data.sql
```

If `database.sql` itself creates the database, run it without specifying `ercs`:

```cmd
mysql -u root -p < sql\database.sql
```

Use whichever method matches the actual SQL scripts; do not create the same tables twice.

### 3. Configure database access

Set the JDBC URL, database username, and password with environment variables. Do not commit real credentials to a public repository.

Typical local URL:

```text
jdbc:mysql://localhost:3306/ercs?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
```

In Windows CMD, configure credentials for the current session before running the program:

```cmd
set ERCS_DB_URL=jdbc:mysql://localhost:3306/ercs?useSSL=false^&allowPublicKeyRetrieval=true^&serverTimezone=UTC
set ERCS_DB_USER=root
set ERCS_DB_PASSWORD=your_password
```

### 4. Compile from CMD

Run these commands from the `ERCS` project root. The commands below assume all `.java` files are under `src/` and package declarations match the folder structure.

```cmd
javac -cp "lib\mysql-connector-j-9.7.0.jar" -d out @sources.txt
```

### 5. Run the application

```cmd
java -cp "out;lib\mysql-connector-j-9.7.0.jar" Main
```

If `Main` is declared inside a package, use its fully qualified class name instead (for example, `java -cp "out;lib\mysql-connector-j-9.7.0.jar" app.Main`). Adjust commands to match the actual source structure.

## Suggested Console Menu

```text
========================================
 ERCS - EMERGENCY RESPONSE COORDINATION
========================================
1. Report Emergency
2. Analyze Emergency
3. View Priority Queue
4. Find Ambulance
5. Find Hospital
6. Check Blood Availability
7. Generate Response Plan
8. View Active Emergencies
9. View Resources
0. Exit
========================================
```

Menu options should call service methods; `Main.java` should primarily coordinate input, menu navigation, and output.

## Testing Scenarios

| Test | Setup | Expected application behaviour |
|---|---|---|
| Critical simulated accident | Severe blood-loss flag and other configured conditions | High simulated priority and resource lookup |
| Lower-priority case | Minor condition, no severe blood-loss flag | Lower score according to configured rules |
| No matching stock | No blood-bank row with the recorded group and stock | Explicitly report no matching sample stock |
| No available ambulance | Mark all sample ambulances busy | Report no available ambulance |
| No suitable hospital | Sample hospitals lack required configured capabilities | Report no suitable sample hospital |
| Multiple emergencies | Insert cases at different priority levels | Queue orders cases by comparator rules |
| Invalid input | Negative age, invalid menu choice, malformed number | Reject input or ask for correction |
| Database unavailable | Stop MySQL or use invalid credentials in a test environment | Show a clear database error without crashing unexpectedly |

## Limitations

- Console-only interface; no map, GPS, live traffic, or real-time hospital integration.
- Distances and availability are sample values and may not represent real conditions.
- Rule-based scores are educational examples, not medical triage.
- Blood-stock records do not authorize transfusion or establish clinical compatibility.
- Recommendations depend on the completeness and accuracy of the configured data.
- The prototype is not designed for production use or handling real patient data.

## Future Enhancements

- Integrate verified geolocation and route-distance data.
- Add authenticated roles for dispatcher and resource operator.
- Maintain resource allocation history and audit logs.
- Add transaction-safe allocation and concurrency handling.
- Add configurable resource suitability rules and reports.
- Integrate authorized hospital/ambulance APIs only with appropriate permissions and validation.

## Learning Outcomes

By completing ERCS, the developer can demonstrate:

- Core Java classes, objects, encapsulation, and separation of concerns.
- Collections and comparator-based `PriorityQueue` ordering.
- JDBC connectivity, prepared statements, result sets, and SQL CRUD operations.
- Relational database design, foreign keys, joins, and transactions.
- Input validation, exception handling, testing, and technical documentation.

---

**Project status:** Update this section as implementation progresses (for example, `In development`, `Core features completed`, or `Tested locally`). Do not claim a feature is implemented until it has been built and tested.
