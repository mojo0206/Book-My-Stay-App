import java.util.*;
import java.util.concurrent.*;

/**
 * Book My Stay App
 * Use Case 11: Concurrent Booking Simulation (Thread Safety)
 *
 * Demonstrates multi-threaded booking processing with synchronized
 * access to shared inventory and booking queue to prevent race conditions.
 *
 * Author: Developer
 * Version: 1.0
 */

// ---------------- RESERVATION ----------------
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
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
}

// ---------------- ROOM INVENTORY ----------------
class RoomInventory {

    private final Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    // Thread-safe inventory check and allocation
    public synchronized boolean allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);
        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public synchronized void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " Rooms Available: " + inventory.get(type));
        }
    }
}

// ---------------- BOOKING QUEUE ----------------
class BookingQueue {
    private final Queue<Reservation> queue;

    public BookingQueue() {
        queue = new LinkedList<>();
    }

    public synchronized void addReservation(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Booking request queued: " + reservation.getReservationId()
                + " (" + reservation.getRoomType() + ")");
    }

    public synchronized Reservation pollReservation() {
        return queue.poll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}

// ---------------- BOOKING PROCESSOR ----------------
class BookingProcessor implements Runnable {

    private final BookingQueue bookingQueue;
    private final RoomInventory inventory;

    public BookingProcessor(BookingQueue bookingQueue, RoomInventory inventory) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {
            Reservation reservation;
            synchronized (bookingQueue) {
                if (bookingQueue.isEmpty()) {
                    break;
                }
                reservation = bookingQueue.pollReservation();
            }

            if (reservation != null) {
                boolean allocated = inventory.allocateRoom(reservation.getRoomType());
                if (allocated) {
                    System.out.println(Thread.currentThread().getName()
                            + " confirmed booking for " + reservation.getGuestName()
                            + " (" + reservation.getRoomType() + ")");
                } else {
                    System.out.println(Thread.currentThread().getName()
                            + " could NOT allocate room for " + reservation.getGuestName()
                            + " (" + reservation.getRoomType() + ") - Sold Out");
                }
            }
        }
    }
}

// ---------------- MAIN APPLICATION ----------------
public class BookMyStayApp {

    public static void main(String[] args) throws InterruptedException {

        RoomInventory inventory = new RoomInventory();
        BookingQueue bookingQueue = new BookingQueue();

        // Simulate multiple booking requests
        bookingQueue.addReservation(new Reservation("R101", "Alice", "Single"));
        bookingQueue.addReservation(new Reservation("R102", "Bob", "Double"));
        bookingQueue.addReservation(new Reservation("R103", "Charlie", "Suite"));
        bookingQueue.addReservation(new Reservation("R104", "David", "Single"));
        bookingQueue.addReservation(new Reservation("R105", "Eve", "Double"));
        bookingQueue.addReservation(new Reservation("R106", "Frank", "Suite"));

        // Create multiple threads to simulate concurrent bookings
        int numberOfThreads = 3;
        List<Thread> threads = new ArrayList<>();
        for (int i = 1; i <= numberOfThreads; i++) {
            Thread t = new Thread(new BookingProcessor(bookingQueue, inventory), "BookingThread-" + i);
            threads.add(t);
            t.start();
        }

        // Wait for all threads to finish
        for (Thread t : threads) {
            t.join();
        }

        // Display final inventory
        inventory.displayInventory();
    }
}