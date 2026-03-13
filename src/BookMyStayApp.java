import java.util.*;

/**
 * Book My Stay App
 * Use Case 7: Add-On Service Selection
 *
 * Demonstrates attaching optional services to existing reservations
 * without modifying booking allocation or inventory logic.
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
}

// ---------------- ADD-ON SERVICE ----------------
class AddOnService {

    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }
}

// ---------------- ADD-ON SERVICE MANAGER ----------------
class AddOnServiceManager {

    // Map reservation ID -> list of services
    private Map<String, List<AddOnService>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    // Add service to reservation
    public void addService(String reservationId, AddOnService service) {

        reservationServices.putIfAbsent(reservationId, new ArrayList<>());
        reservationServices.get(reservationId).add(service);

        System.out.println("Service added: " + service.getServiceName() +
                " for Reservation ID: " + reservationId);
    }

    // Display services for reservation
    public void displayServices(String reservationId) {

        System.out.println("\nServices for Reservation ID: " + reservationId);

        List<AddOnService> services = reservationServices.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No services selected.");
            return;
        }

        for (AddOnService s : services) {
            System.out.println(s.getServiceName() + " - $" + s.getCost());
        }
    }

    // Calculate total additional cost
    public double calculateTotalServiceCost(String reservationId) {

        List<AddOnService> services = reservationServices.get(reservationId);
        double total = 0;

        if (services != null) {
            for (AddOnService s : services) {
                total += s.getCost();
            }
        }

        return total;
    }
}

// ---------------- MAIN APPLICATION ----------------
public class BookMyStayApp {

    public static void main(String[] args) {

        // Existing reservation (from previous booking process)
        Reservation reservation = new Reservation("R101", "Alice", "Single");

        // Add-on service manager
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Available services
        AddOnService breakfast = new AddOnService("Breakfast", 20);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 40);
        AddOnService spa = new AddOnService("Spa Access", 50);

        // Guest selects services
        serviceManager.addService(reservation.getReservationId(), breakfast);
        serviceManager.addService(reservation.getReservationId(), airportPickup);
        serviceManager.addService(reservation.getReservationId(), spa);

        // Display selected services
        serviceManager.displayServices(reservation.getReservationId());

        // Calculate additional cost
        double totalCost = serviceManager.calculateTotalServiceCost(reservation.getReservationId());

        System.out.println("\nTotal Add-On Service Cost: $" + totalCost);
    }
}