import java.util.HashMap;

/**
 * Book My Stay App
 * Use Case 4: Room Search & Availability Check
 *
 * Demonstrates read-only search functionality where guests
 * can view available rooms without modifying system state.
 *
 * @author Developer
 * @version 1.0
 */

// ---------------- ROOM DOMAIN MODEL ----------------
abstract class Room {

    protected String roomType;
    protected int beds;
    protected double price;

    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    public String getRoomType() {
        return roomType;
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

// ---------------- INVENTORY CLASS ----------------
class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 0); // Example: suite currently unavailable
    }

    // Read-only availability retrieval
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

// ---------------- SEARCH SERVICE ----------------
class RoomSearchService {

    public void searchAvailableRooms(RoomInventory inventory, Room[] rooms) {

        System.out.println("===== Available Rooms =====\n");

        for (Room room : rooms) {

            int available = inventory.getAvailability(room.getRoomType());

            // Defensive check: show only available rooms
            if (available > 0) {
                room.displayRoomDetails();
                System.out.println("Available Rooms: " + available);
                System.out.println("---------------------------");
            }
        }
    }
}

// ---------------- MAIN APPLICATION ----------------
public class BookMyStayApp {

    public static void main(String[] args) {

        // Initialize room objects
        Room single = new SingleRoom();
        Room dbl = new DoubleRoom();
        Room suite = new SuiteRoom();

        Room[] rooms = {single, dbl, suite};

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Search service (read-only operation)
        RoomSearchService searchService = new RoomSearchService();

        // Guest performs room search
        searchService.searchAvailableRooms(inventory, rooms);
    }
}