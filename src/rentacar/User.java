package rentacar;

import java.util.LinkedList;
import java.util.List;


public class User {

    private int userId;
    private String name;
    private String city;
    private List<Reservation> reservations = new LinkedList<Reservation>();
    private List<Double>  points = new LinkedList<>();

    public User(int userId, String name, String city) {
        this.userId = userId;
        this.name = name;
        this.city = city;

    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public int getUserId() {
        return userId;
    }

    public void addPoints(double point){
        points.add(point);
    }


    public double getAverageDurations() {	return reservations.stream()
            .mapToInt(Reservation::getDuration)
            .average().orElse(0); }

    public void addReservation(Reservation r) {
        reservations.add(r);
    }

    public List<Double> getPoints() {
        return points;
    }

    public double getTotalPoints(){
        return points.stream().mapToDouble(Double::doubleValue).sum();
    }

        
}
