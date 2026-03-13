import java.io.*;
import java.util.*;

/**
 * Book My Stay App
 * Use Case 12: Data Persistence & System Recovery
 *
 * Demonstrates saving and restoring system state (inventory and bookings)
 * using Java Serialization.
 *
 * Author: Developer
 * Version: 1.0
 */

// ---------------- RESERVATION ----------------
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public String getRoomId() { return roomId; }

    @Override
    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType + " | Room ID: " + roomId;
    }
}

// ---------------- ROOM INVENTORY ----------------
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void incrementRoom(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public void decrementRoom(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " Rooms Available: " + inventory.get(type));
        }
    }

    public Map<String, Integer> getInventoryMap() { return inventory; }
    public void setInventoryMap(Map<String, Integer> map) { inventory = map; }
}

// ---------------- BOOKING HISTORY ----------------
class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    public void addReservation(Reservation r) {
        reservations.add(r);
        System.out.println("Booking confirmed: " + r.getReservationId());
    }

    public void displayHistory() {
        System.out.println("\n===== Booking History =====");
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    public List<Reservation> getReservations() { return reservations; }
    public void setReservations(List<Reservation> res) { reservations = res; }
}

// ---------------- PERSISTENCE SERVICE ----------------
class PersistenceService {

    private static final String FILE_NAME = "book_my_stay_data.ser";

    // Save state
    public static void saveState(RoomInventory inventory, BookingHistory history) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(inventory);
            out.writeObject(history);
            System.out.println("\nSystem state saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    // Load state
    public static Object[] loadState() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No saved data found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            RoomInventory inventory = (RoomInventory) in.readObject();
            BookingHistory history = (BookingHistory) in.readObject();
            System.out.println("\nSystem state loaded successfully!");
            return new Object[]{inventory, history};
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading state: " + e.getMessage());
            return null;
        }
    }
}

// ---------------- MAIN APPLICATION ----------------
public class BookMyStayApp {

    public static void main(String[] args) {

        // Load persisted state if available
        Object[] persistedState = PersistenceService.loadState();
        RoomInventory inventory = persistedState != null ? (RoomInventory) persistedState[0] : new RoomInventory();
        BookingHistory history = persistedState != null ? (BookingHistory) persistedState[1] : new BookingHistory();

        // Simulate new bookings
        Reservation r1 = new Reservation("R201", "Alice", "Single", "S1");
        Reservation r2 = new Reservation("R202", "Bob", "Double", "D1");

        inventory.decrementRoom(r1.getRoomType());
        inventory.decrementRoom(r2.getRoomType());

        history.addReservation(r1);
        history.addReservation(r2);

        // Display current state
        inventory.displayInventory();
        history.displayHistory();

        // Save state for future recovery
        PersistenceService.saveState(inventory, history);

        // Simulate application restart
        System.out.println("\n--- Simulating System Restart ---");
        Object[] recoveredState = PersistenceService.loadState();
        if (recoveredState != null) {
            RoomInventory recoveredInventory = (RoomInventory) recoveredState[0];
            BookingHistory recoveredHistory = (BookingHistory) recoveredState[1];

            recoveredInventory.displayInventory();
            recoveredHistory.displayHistory();
        }
    }
}