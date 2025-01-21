import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class Flight {
    int flightNumber;
    String departure;
    String arrival;
    int day;
    final int capacity = 20; 
    List<Order> orders = new ArrayList<>();

    /* This method initializes the Flight object */
    public Flight(int flightNumber, String departure, String arrival, int day) {
        this.flightNumber = flightNumber;
        this.departure = departure;
        this.arrival = arrival;
        this.day = day;
    }

    /* This method adds order to the flight and checks if the capacity is hit. 
    Returns true if not hit, else false */
    public boolean addOrder(Order order) {
        if(orders.size() < capacity) {
            orders.add(order);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Flight: " + flightNumber + ", departure: " + departure + ", arrival: " + arrival + ", day: " + day;
    }
}

class Order {
    String orderId;
    String destination;

    /* This method initializes the Order object */
    public Order(String orderId, String destination) {
        this.orderId = orderId;
        this.destination = destination;
    }
}

class FlightScheduler {
    List<Flight> flights = new ArrayList<>();

    /* This method initializes FlightScheduler with the given flight information */
    public FlightScheduler() {
        flights.add(new Flight(1, "YUL", "YYZ", 1));
        flights.add(new Flight(2, "YUL", "YYC", 1));
        flights.add(new Flight(3, "YUL", "YVR", 1));
        flights.add(new Flight(4, "YUL", "YYZ", 2));
        flights.add(new Flight(5, "YUL", "YYC", 2));
        flights.add(new Flight(6, "YUL", "YVR", 2));
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void printFlights() {
        for(Flight flight: flights){
            System.out.println(flight.toString());
        }
    }
}

class OrderSorter {
    /* This method when given the json filePath and list of flights, sorts the orders to which flight they belong to */
    public void sortOrders(String filePath, List<Flight> flights) {
        try{
            String data = new String(Files.readAllBytes(Paths.get(filePath)));
            Gson gson = new Gson();
            TypeToken<Map<String, Map<String, String>>> token = new TypeToken<Map<String, Map<String, String>>>() {};
            Map<String, Map<String, String>> orders = gson.fromJson(data, token.getType());

            for(Map.Entry<String, Map<String, String>> entry : orders.entrySet()) {
                String orderId = entry.getKey();
                String destination = entry.getValue().get("destination");

                boolean scheduled = false;
                for(Flight flight : flights) {
                    if(flight.arrival.equals(destination) && flight.addOrder(new Order(orderId, destination))) {
                        System.out.println("Order: " + orderId + ", flightNumber: " + flight.flightNumber + ", departure: " + flight.departure + ", arrival: " + flight.arrival + ", day: " + flight.day);
                        scheduled = true;
                        break;
                    }
                }

                if(!scheduled) {
                    System.out.println("Order: " + orderId + ", flightNumber: not scheduled");
                }
            }
        }catch (Exception e) {
            System.out.println("Failed to parse JSON file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        //User Story #1
        FlightScheduler scheduler = new FlightScheduler();
        scheduler.printFlights();

        //User Story #2
        OrderSorter sorter = new OrderSorter();
        sorter.sortOrders("coding-assigment-orders.json", scheduler.getFlights());
    }
}
