package rentacar;


public class Vehicle {
    

    enum VehicleStatus {Libero,Occupied};

    private int vehicleId;
    private String manufacturer;
    private String model;
    private int year;
    private String color;
    private char category;
    private int seats;
    private VehicleStatus status;



    public Vehicle(int vehicleId, String manufacturer, String model,
                   int year, String color, char category, int seats) {
        this.vehicleId = vehicleId;
        this.manufacturer = manufacturer;
        this.model = model;
        this.year = year;
        this.color = color;
        this.category = category;
        this.seats = seats;
        this.status=VehicleStatus.Libero;
    }
    
    public int getVehicleId() {
        return vehicleId;
    }
    
    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public String getColor() {
        return color;
    }

    public char getCategory() {
        return category;
    }

    public int getSeats() {
        return seats;
    }

    public VehicleStatus getVehicleStatus(){
        return status;
    }

    public void setVehicleStatus(VehicleStatus status){
        this.status=status;
    }


    @Override
    public String toString(){
        return manufacturer+":"+model+":"+color;
    }
}
