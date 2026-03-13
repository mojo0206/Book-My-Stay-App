import java.util.HashMap;

/**
 * Book My Stay App
 * Use Case 3: Centralized Room Inventory Management
 *
 * This program demonstrates centralized room availability
 * management using a HashMap data structure.
 *
 * @author Developer
 * @version 1.0
 */

// ---------------- ABSTRACT ROOM CLASS ----------------
abstract class Room {

    protected String roomType;
    protected int beds;
    protected double price;

    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    public void displayRoomDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Price per night: $" + price);
    }
}

// ---------------- ROOM TYPES ----------------
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 80.0);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 120.0);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 250.0);
    }
}

// ---------------- ROOM INVENTORY CLASS ----------------
class RoomInventory {

    // Centralized inventory storage
    private HashMap<String, Integer> inventory;

    // Constructor initializes room availability
    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Retrieve availability
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability
    public void updateAvailability(String roomType, int newCount) {
        inventory.put(roomType, newCount);
    }

    // Display current inventory
    public void displayInventory() {
        System.out.println("===== Current Room Inventory =====");

        for (String roomType : inventory.keySet()) {
            System.out.println(roomType + " : " + inventory.get(roomType) + " available");
        }
    }
}

// ---------------- MAIN APPLICATION ----------------
public class BookMyStayApp {

    public static void main(String[] args) {

        // Create room objects
        Room single = new SingleRoom();
        Room dbl = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Initialize centralized inventory
        RoomInventory inventory = new RoomInventory();

        System.out.println("===== Book My Stay - Room Details =====\n");

        single.displayRoomDetails();
        System.out.println("Available: " + inventory.getAvailability("Single Room"));
        System.out.println("--------------------------------");

        dbl.displayRoomDetails();
        System.out.println("Available: " + inventory.getAvailability("Double Room"));
        System.out.println("--------------------------------");

        suite.displayRoomDetails();
        System.out.println("Available: " + inventory.getAvailability("Suite Room"));
        System.out.println("--------------------------------");

        // Display full inventory
        System.out.println();
        inventory.displayInventory();
    }
}