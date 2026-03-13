import java.util.*;

/**
 * Book My Stay App
 * Use Case 9: Error Handling & Validation
 *
 * Demonstrates validation and structured error handling
 * using custom exceptions to prevent invalid bookings.
 *
 * @author Developer
 * @version 1.0
 */

// ---------------- CUSTOM EXCEPTION ----------------
class InvalidBookingException extends Exception {

    public InvalidBookingException(String message) {
        super(message);
    }
}

// ---------------- RESERVATION ----------------
class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
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

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementRoom(String roomType) throws InvalidBookingException {

        int available = getAvailability(roomType);

        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }

        inventory.put(roomType, available - 1);
    }

    public void displayInventory() {

        System.out.println("\nCurrent Inventory:");

        for (String type : inventory.keySet()) {
            System.out.println(type + " Rooms Available: " + inventory.get(type));
        }
    }
}

// ---------------- VALIDATOR ----------------
class InvalidBookingValidator {

    public static void validateReservation(Reservation reservation, RoomInventory inventory)
            throws InvalidBookingException {

        if (reservation.getGuestName() == null || reservation.getGuestName().trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        if (!inventory.isValidRoomType(reservation.getRoomType())) {
            throw new InvalidBookingException("Invalid room type selected: " + reservation.getRoomType());
        }

        if (inventory.getAvailability(reservation.getRoomType()) <= 0) {
            throw new InvalidBookingException("Requested room type is currently unavailable.");
        }
    }
}

// ---------------- BOOKING SERVICE ----------------
class BookingService {

    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void confirmBooking(Reservation reservation) {

        try {

            // Validate input first
            InvalidBookingValidator.validateReservation(reservation, inventory);

            // Allocate room
            inventory.decrementRoom(reservation.getRoomType());

            System.out.println("Reservation confirmed for "
                    + reservation.getGuestName()
                    + " (" + reservation.getRoomType() + " Room)");

        } catch (InvalidBookingException e) {

            // Graceful failure handling
            System.out.println("Booking Failed: " + e.getMessage());
        }
    }
}

// ---------------- MAIN APPLICATION ----------------
public class BookMyStayApp {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);

        // Valid booking
        Reservation r1 = new Reservation("Alice", "Single");

        // Invalid room type
        Reservation r2 = new Reservation("Bob", "Deluxe");

        // Invalid guest name
        Reservation r3 = new Reservation("", "Double");

        // Process bookings
        bookingService.confirmBooking(r1);
        bookingService.confirmBooking(r2);
        bookingService.confirmBooking(r3);

        inventory.displayInventory();
    }
}