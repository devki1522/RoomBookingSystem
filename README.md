# CSCN72040 Term Project – Phase 2 & 3
## Group 06
### Group Members
- Devki Nandan Sharma
- Pratham Rao
- Nifemi

## Project Overview
This project is a CLI-based Room Booking System developed in Java. It demonstrates booking creation, validation, persistence, reporting, approval workflow, policy comparison, and performance evidence through a runnable `Main` program.

The system was designed to support the Phase 2 and Phase 3 requirements by providing a single executable entry point and labeled outputs for each required demonstration item.

## Prerequisites
1. Java 17 or higher
2. IntelliJ IDEA recommended

## How to Run
1. Open the project folder in IntelliJ IDEA.
2. Make sure the project SDK is set to Java 17 or higher.
3. Open:
   `src/main/java/com/booking/Main.java`
4. Right click `Main.java`
5. Select `Run 'Main.main()'`
6. The CLI demo menu will appear in the Run console.

## Project Structure
```text
src/
└── main/
    └── java/
        └── com/
            └── booking/
                ├── Main.java
                ├── application/
                │   └── BookingService.java
                ├── domain/
                │   ├── Booking.java
                │   ├── BookingFactory.java
                │   ├── BookingRepository.java
                │   ├── BookingStatus.java
                │   ├── RecurrenceRule.java
                │   ├── Room.java
                │   └── User.java
                └── infrastructure/
                    └── InMemoryBookingRepository.java

src/
└── test/
    └── java/
        └── com/
            └── booking/
                └── domain/
                    └── BookingTest.java