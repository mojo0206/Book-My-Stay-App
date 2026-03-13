import java.util.*;

/**
 * Book My Stay App
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * Demonstrates processing booking requests from a queue,
 * allocating unique room IDs, preventing double booking,
 * and synchronizing inventory updates.
 *
 * @author Developer
 * @version 1.0
 */

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

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementRoom(String roomType) {
        int current = inventory.get(roomType);
        inventory.put(roomType, current - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " Rooms Available: " + inventory.get(type));
        }
    }
}

// ---------------- BOOKING QUEUE ----------------
class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    public void addRequest(Reservation r) {
        queue.add(r);
        System.out.println("Booking request added: " + r.getGuestName() + " -> " + r.getRoomType());
    }

    public Reservation getNextRequest() {
        return queue.poll();
    }

    public boolean hasRequests() {
        return !queue.isEmpty();
    }
}

// ---------------- BOOKING SERVICE ----------------
class BookingService {

    private RoomInventory inventory;

    // Map room type -> allocated room IDs
    private HashMap<String, Set<String>> allocatedRooms;

    // Track all allocated IDs globally
    private Set<String> allocatedRoomIds;

    private int roomCounter = 1;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        allocatedRooms = new HashMap<>();
        allocatedRoomIds = new HashSet<>();
    }

    // Process booking requests
    public void processBookings(BookingRequestQueue queue) {

        while (queue.hasRequests()) {

            Reservation request = queue.getNextRequest();
            String roomType = request.getRoomType();

            System.out.println("\nProcessing reservation for " + request.getGuestName());

            if (inventory.getAvailability(roomType) > 0) {

                String roomId = generateRoomId(roomType);

                // Store allocated room ID
                allocatedRoomIds.add(roomId);

                allocatedRooms.putIfAbsent(roomType, new HashSet<>());
                allocatedRooms.get(roomType).add(roomId);

                // Update inventory immediately
                inventory.decrementRoom(roomType);

                System.out.println("Reservation Confirmed!");
                System.out.println("Guest: " + request.getGuestName());
                System.out.println("Room Type: " + roomType);
                System.out.println("Assigned Room ID: " + roomId);

            } else {

                System.out.println("Reservation Failed: No rooms available for " + roomType);
            }
        }
    }

    // Generate unique room ID
    private String generateRoomId(String roomType) {

        String roomId;

        do {
            roomId = roomType.substring(0, 1).toUpperCase() + roomCounter++;
        } while (allocatedRoomIds.contains(roomId));

        return roomId;
    }

    public void displayAllocations() {
        System.out.println("\nRoom Allocations:");

        for (String type : allocatedRooms.keySet()) {

            System.out.println(type + " -> " + allocatedRooms.get(type));
        }
    }
}

// ---------------- MAIN APPLICATION ----------------
public class BookMyStayApp {

    public static void main(String[] args) {

        // Initialize services
        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue queue = new BookingRequestQueue();
        BookingService bookingService = new BookingService(inventory);

        // Guests submit booking requests
        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Double"));
        queue.addRequest(new Reservation("Charlie", "Single"));
        queue.addRequest(new Reservation("David", "Suite"));
        queue.addRequest(new Reservation("Eva", "Suite")); // May fail if unavailable

        // Process reservations
        bookingService.processBookings(queue);

        // Show results
        bookingService.displayAllocations();
        inventory.displayInventory();
    }
}