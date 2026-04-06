# DEMO_SCRIPT.md
## CSCN72040 Term Project – Group 06
### Group Members
- Devki Nandan Sharma
- Pratham Rao
- Nifemi

## Demo Flow
This demo is run from `Main.java` in IntelliJ IDEA.

## 1. Introduction
Hello, we are Group 06. Our project is a CLI-based Room Booking System developed in Java. The system demonstrates layered architecture, persistence, reporting, lifecycle enforcement, validation, policy comparison, and performance evidence. We will run the project from `Main.java` and show how each requirement is demonstrated through the console output.

## 2. Startup / Architecture Summary
Run `Main.java`.

Show:
- `[REQ-2.2] Architecture Summary`
- Presentation Layer: `Main.java`
- Application Layer: `BookingService.java`
- Domain Layer: `Booking`, `Room`, `User`, `BookingStatus`
- Infrastructure Layer: `InMemoryBookingRepository`
- Dependency direction: `Main -> BookingService -> Domain -> Repository`

Explain:
This demonstrates clear architecture boundaries and maintainability through separation of concerns.

## 3. Runnable Interface
Show the CLI menu.

Explain:
This satisfies REQ-2.1 because the system runs from a single entry point and provides an interactive command-line interface.

## 4. Query 1 – Available Rooms Report
Choose option:
`1`

Show:
- `[REQ-2.4] Query 1: Available Rooms Report`

Explain:
This demonstrates one reporting/query feature by listing current rooms and capacity information.

## 5. Query 2 – Booking Status Report
Choose option:
`3`

Show:
- `[REQ-2.4] Query 2: Booking Status Report`

Explain:
This demonstrates a second report/query by listing all bookings and their current statuses.

## 6. Validation / Robustness
Choose option:
`2`

Enter an invalid room ID.

Show:
- `Room Not Found. (REQ-2.8 - error handling/messages)`

Explain:
This demonstrates predictable validation and meaningful error handling.

## 7. Persistence
Choose option:
`5`

Add a room and save it.

Show:
- `[REQ-2.3] SUCCESS: Rooms saved to rooms.txt`

Then restart the application and choose:
`1`

Show that the added room still appears.

Explain:
This demonstrates persistence and reload behavior.

## 8. Lifecycle / State Transition
Choose option:
`2`

Create a booking for a restricted room such as `Room2` or another approval-required room.

Show:
- `SUCCESS: Booking Submitted and awaiting Admin Approval`

Then choose:
`4`

Approve the booking.

Show:
- `Before approval: PENDING_APPROVAL`
- `After approval: APPROVED`

Then show:
- invalid transition test
- `Error: Cannot approve a booking in APPROVED state.`

Explain:
This demonstrates both a valid lifecycle transition and an invalid transition rejection.

## 9. Policy Comparison
Choose option:
`7`

Show:
- Policy A: standard room booking -> `REQUESTED`
- Policy B: restricted room booking -> `PENDING_APPROVAL`

Explain:
This demonstrates two different policy outcomes based on room configuration.

## 10. Performance Evidence
Choose option:
`6`

Show:
- `[REQ-2.9] Performance Test`
- dataset size
- execution time in milliseconds
- trade-off explanation

Explain:
This demonstrates basic performance/scalability evidence and a design trade-off.

## 11. Design Patterns
Point to startup output:
- `[REQ-2.7] Pattern Map`

Explain:
- Creational: `BookingFactory`
- Structural: `BookingRepository / InMemoryBookingRepository`
- Behavioral: booking lifecycle handling
- Behavioral: policy-based booking behavior

## 12. UML Alignment
Show the two UML excerpts prepared from Phase 1:
1. Class Diagram excerpt
2. State Diagram excerpt

Explain:
- Class diagram maps to `Main`, `BookingService`, `Booking`, `Room`, `User`, and repository classes
- State diagram maps to booking status transitions such as `REQUESTED`, `PENDING_APPROVAL`, and `APPROVED`

## 13. Conclusion
This project demonstrates a working Java room booking system with architecture separation, persistence, reporting, lifecycle enforcement, validation, policy comparison, and performance evidence. Thank you.

