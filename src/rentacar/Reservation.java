package rentacar;

public class Reservation {

    private String name;
    private Vehicle vehicle;
    private int duration;


    public Reservation(String name, Vehicle vehicle, int duration) {
        this.name = name;
        this.vehicle = vehicle;
        this.duration = duration;
    }


    public Vehicle getVehicle() {
        return vehicle;
    }
    
    public String getName() {
        return name;
    }


    public int getDuration() {
        return duration;
    }
}
