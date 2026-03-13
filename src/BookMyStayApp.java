import java.util.*;

/**
 * Book My Stay App
 * Use Case 8: Booking History & Reporting
 *
 * Demonstrates how confirmed reservations are stored in
 * booking history and used to generate administrative reports.
 *
 * @author Developer
 * @version 1.0
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

    public void displayReservation() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType);
    }
}

// ---------------- BOOKING HISTORY ----------------
class BookingHistory {

    // List to store confirmed bookings
    private List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    // Add confirmed reservation
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        System.out.println("Reservation stored in history: " + reservation.getReservationId());
    }

    // Retrieve all reservations
    public List<Reservation> getReservations() {
        return reservations;
    }
}

// ---------------- BOOKING REPORT SERVICE ----------------
class BookingReportService {

    // Display booking history
    public void displayBookingHistory(List<Reservation> reservations) {

        System.out.println("\n===== Booking History =====");

        for (Reservation r : reservations) {
            r.displayReservation();
        }
    }

    // Generate summary report
    public void generateSummaryReport(List<Reservation> reservations) {

        System.out.println("\n===== Booking Summary Report =====");

        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation r : reservations) {
            String type = r.getRoomType();
            roomTypeCount.put(type, roomTypeCount.getOrDefault(type, 0) + 1);
        }

        for (String type : roomTypeCount.keySet()) {
            System.out.println(type + " Bookings: " + roomTypeCount.get(type));
        }

        System.out.println("Total Bookings: " + reservations.size());
    }
}

// ---------------- MAIN APPLICATION ----------------
public class BookMyStayApp {

    public static void main(String[] args) {

        // Initialize booking history
        BookingHistory history = new BookingHistory();

        // Simulate confirmed reservations
        Reservation r1 = new Reservation("R101", "Alice", "Single");
        Reservation r2 = new Reservation("R102", "Bob", "Double");
        Reservation r3 = new Reservation("R103", "Charlie", "Suite");
        Reservation r4 = new Reservation("R104", "David", "Single");

        // Store bookings in history
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);
        history.addReservation(r4);

        // Admin requests reports
        BookingReportService reportService = new BookingReportService();

        reportService.displayBookingHistory(history.getReservations());

        reportService.generateSummaryReport(history.getReservations());
    }
}