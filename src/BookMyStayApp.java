/**
 * Book My Stay App
 * Use Case 2: Basic Room Types & Static Availability
 *
 * This program demonstrates object-oriented design using
 * abstraction, inheritance, and polymorphism for different
 * hotel room types.
 *
 * @author Developer
 * @version 1.0
 */

// Abstract Room class
abstract class Room {

    protected String roomType;
    protected int beds;
    protected double price;

    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    // Method to display room details
    public void displayRoomDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Price per night: $" + price);
    }
}

// Single Room class
class SingleRoom extends Room {

    public SingleRoom() {
        super("Single Room", 1, 80.0);
    }
}

// Double Room class
class DoubleRoom extends Room {

    public DoubleRoom() {
        super("Double Room", 2, 120.0);
    }
}

// Suite Room class
class SuiteRoom extends Room {

    public SuiteRoom() {
        super("Suite Room", 3, 250.0);
    }
}

// Main Application Class
public class BookMyStayApp {

    public static void main(String[] args) {

        // Create room objects using polymorphism
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        // Static availability variables
        int singleRoomAvailability = 5;
        int doubleRoomAvailability = 3;
        int suiteRoomAvailability = 2;

        System.out.println("===== Book My Stay - Room Availability =====\n");

        singleRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + singleRoomAvailability);
        System.out.println("-----------------------------------");

        doubleRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + doubleRoomAvailability);
        System.out.println("-----------------------------------");

        suiteRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + suiteRoomAvailability);
        System.out.println("-----------------------------------");

        System.out.println("Thank you for using Book My Stay!");
    }
}