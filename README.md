Room booking System Phase 02-03
**Group Number -06**
**Group Members: Devki Nandan Sharma, Pratham Rao, Nifemi**

**Prerequisites**
1. JAVA version 17 or higher
2. IDE: IntelliJ IDEA

**Execution Instructions**
1. Open project folder in your IDE.
2. Navigate to src/com/booking/main.java
3. Right click on main.java and select run Main.main()
4. The console will present a CLI menu. 
               
**Project Structure**

src/

    └── com/

        └── booking/

            ├── Main.java (The Entry Point)

            ├── application/

            │       └── BookingService.java (Business Logic)

            ├── domain/

            │       ├── Booking.java

            │       ├── Room.java

            │       ├── User.java

            │       ├── BookingStatus.java (Enum)

            │       ├── RecurrenceRule.java

            │       └── BookingRepository.java (Interface)

            └── infrastructure/

                    └── InMemoryBookingRepository.java (File Persistence)

**Design Patterns Used**
1. Creational: Used BookingFactory methjod to handle complex object creations.
2. Structural: Used BookingRepository to decouple logic from storage.
3. State Pattern: Use BookingStatus to manage room approval workflow.