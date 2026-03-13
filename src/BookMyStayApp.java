import java.util.*;

/**
 * Book My Stay App
 * Use Case 10: Booking Cancellation & Inventory Rollback
 *
 * Demonstrates safe cancellation of confirmed bookings,
 * inventory restoration, and rollback using a Stack.
 *
 * Author: Developer
 * Version: 1.0
 */

// ---------------- RESERVATION ----------------
class Reservation {

    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId; // Allocated room ID

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }
}

// ---------------- ROOM INVENTORY ----------------
class RoomInventory {

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
        int current = inventory.getOrDefault(roomType, 0);
        inventory.put(roomType, current - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " Rooms Available: " + inventory.get(type));
        }
    }
}

// ---------------- BOOKING HISTORY ----------------
class BookingHistory {

    private List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    public void addReservation(Reservation r) {
        reservations.add(r);
        System.out.println("Booking confirmed: " + r.getReservationId());
    }

    public void removeReservation(String reservationId) {
        reservations.removeIf(r -> r.getReservationId().equals(reservationId));
        System.out.println("Booking cancelled: " + reservationId);
    }

    public Reservation getReservation(String reservationId) {
        for (Reservation r : reservations) {
            if (r.getReservationId().equals(reservationId)) {
                return r;
            }
        }
        return null;
    }

    public void displayHistory() {
        System.out.println("\n===== Booking History =====");
        for (Reservation r : reservations) {
            System.out.println(r.getReservationId() + " | " + r.getGuestName()
                    + " | " + r.getRoomType() + " | Room ID: " + r.getRoomId());
        }
    }
}

// ---------------- CANCELLATION SERVICE ----------------
class CancellationService {

    private RoomInventory inventory;
    private BookingHistory history;
    private Stack<String> releasedRoomIds; // Track rolled-back room IDs

    public CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
        releasedRoomIds = new Stack<>();
    }

    public void cancelReservation(String reservationId) {

        Reservation reservation = history.getReservation(reservationId);

        if (reservation == null) {
            System.out.println("Cancellation Failed: Reservation not found or already cancelled: " + reservationId);
            return;
        }

        // Step 1: Restore inventory
        inventory.incrementRoom(reservation.getRoomType());

        // Step 2: Track released room ID
        releasedRoomIds.push(reservation.getRoomId());

        // Step 3: Remove from history
        history.removeReservation(reservationId);

        System.out.println("Cancellation successful for reservation: " + reservationId
                + " | Released Room ID: " + reservation.getRoomId());
    }

    public void displayReleasedRooms() {
        System.out.println("\nReleased Room IDs (Most recent first): " + releasedRoomIds);
    }
}

// ---------------- MAIN APPLICATION ----------------
public class BookMyStayApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();
        CancellationService cancellationService = new CancellationService(inventory, history);

        // Simulate confirmed reservations
        Reservation r1 = new Reservation("R101", "Alice", "Single", "S1");
        Reservation r2 = new Reservation("R102", "Bob", "Double", "D1");
        Reservation r3 = new Reservation("R103", "Charlie", "Suite", "SU1");

        // Add reservations to history
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        history.displayHistory();
        inventory.displayInventory();

        // Perform cancellations
        cancellationService.cancelReservation("R102"); // Cancel Bob
        cancellationService.cancelReservation("R105"); // Non-existent
        cancellationService.cancelReservation("R103"); // Cancel Charlie

        // Display updated state
        history.displayHistory();
        inventory.displayInventory();
        cancellationService.displayReleasedRooms();
    }
}