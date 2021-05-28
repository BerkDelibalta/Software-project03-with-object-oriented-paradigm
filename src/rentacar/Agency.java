package rentacar;


import java.util.*;

import static java.util.stream.Collectors.*;
import static java.util.Comparator.*;


public class Agency {
	// to be given, remains public
	public static final int NO_VEHICLE = -1;
	private  int vehicleId=-1;
	private  int userId=-1;
	private Map<Character,Double> pointsOfCategories = new HashMap<>();
	private List<Vehicle> vehicles = new LinkedList<>();
	private Map<Integer,User> users = new HashMap<>();
	private List<Reservation> reservations = new LinkedList<>();

	

	// R1
	/**
	 * Define points for vehicle categories (A, B, C, etc.). Number of categories is
	 * equal to the number of points provided as arguments.
	 * 
	 * @param **points
	 * @throws AgencyException is thrown in case points are not given in ascending
	 *                         order
	 */


	

	public void definePoints(double... points) throws AgencyException {

		int index=0;

		for(double point : points){

			if(index>=1 && points[index]<=points[index-1]) throw new AgencyException();
			char category = (char)('A' + index);
			pointsOfCategories.put(category,point);

			++index;

		}

	}

	/**
	 * Retrieves the number of points for the given category.
	 * 
	 * @param category
	 * @return number of points for the given category
	 * @throws AgencyException thrown in case category has not been defined
	 */
	public double getPointsOfCategory(char category) throws AgencyException {
		if(!pointsOfCategories.containsKey(category)) throw new AgencyException();

		return pointsOfCategories.get(category);
	}

	/**
	 * Registers a new car to the agency with the following attributes:
	 * 
	 * @param manufacturer
	 * @param model
	 * @param year
	 * @param gear
	 * @param color
	 * @param category
	 * @param seats
	 * @return a unique vehicle id, that is assigned progressively to each vehicle,
	 *         starting from 0
	 * @throws AgencyException thrown in case the category doesn't exist
	 */

	public int addCar(String manufacturer, String model, int year, String gear, String color, char category, int seats)
			throws AgencyException {

		if(!pointsOfCategories.containsKey(category)) throw new AgencyException();

		++vehicleId;
		vehicles.add(new Vehicle(vehicleId,manufacturer,model,year,color,category,seats));


		return vehicleId;
	}

	/**
	 * Registers a new van to the agency with the following attributes:
	 * 
	 * @param manufacturer
	 * @param model
	 * @param year
	 * @param gear
	 * @param color
	 * @param category
	 * @param seats
	 * @param limit
	 * @return a unique vehicle id, that is assigned progressively to each vehicle,
	 *         starting from 0
	 * @throws AgencyException thrown in case the category doesn't exist
	 */
	public int addVan(String manufacturer, String model, int year, String gear, String color, char category, int seats,
			int limit) throws AgencyException {
		if(!pointsOfCategories.containsKey(category)) throw new AgencyException();

		++vehicleId;
		vehicles.add(new Vehicle(vehicleId,manufacturer,model,year,color,category,seats));


		return vehicleId;
	}

	/**
	 * Retrieves the vehicle information produced by the given manufacturer.
	 * 
	 * The list is sorted based on the vehicle id.
	 * 
	 * @param manufacturer
	 * @return the list of Strings in the format `[model]:[year]:[color]`. An empty
	 *         list is returned if for a given manufacturer no car has been defined.
	 */
	public List<String> getVehiclesOfAManufacturer(String manufacturer) {
		if(vehicles.stream().noneMatch(e->e.getManufacturer().equals(manufacturer)))
		return new LinkedList<>();

		return vehicles.stream().filter(e->e.getManufacturer().equals(manufacturer))
				.sorted(comparing(Vehicle::getVehicleId))
				.map(e->e.getModel()+":"+e.getYear()+":"+e.getColor())
				.collect(toList());
	}

	// R2
	/**
	 * Registers a user to the agency with its name and city
	 * 
	 * @param name
	 * @param city
	 * 
	 * @return unique id that each user is assigned to, progressively, starting from
	 *         0
	 * @throws AgencyException thrown when a user with both same name and city has
	 *                         already been defined
	 */
	public int registerUser(String name, String city) throws AgencyException {

		if(users.values().stream().filter(e->e.getName().equals(name)).anyMatch(e->e.getCity().equals(city)))
			throw new AgencyException();

		++userId;
		users.put(userId,new User(userId,name,city));

		return userId;
	}

	/**
	 * 
	 * Retrieves the user information in the form of a map, associating cities with
	 * user names living in those cities.
	 * 
	 * Cities are sorted alphabetically, while user names in the list are sorted in
	 * reversed alphabetical order
	 * 
	 * @return the map associating cities with the users
	 */
	public Map<String, List<String>> getUserInfo() {
		return users.values().stream()
				.sorted(comparing(User::getCity).thenComparing(User::getName).reversed())
				.collect(groupingBy(User::getCity,TreeMap::new,mapping(User::getName,toList())));
	}

	/**
	 * Counts registered users
	 * 
	 * @return the number of registered users
	 */
	public int countUsers() {
		return users.size();
	}

	// R3
	/**
	 * Adds a vehicle reservation. The reservation is made for the first free
	 * vehicle that satisfies the criteria, belonging to the desired category and
	 * having the number of seats higher or equal to the given one. If such vehicle
	 * exists it is immediately set to occupied. If more vehicles satisfy such
	 * criteria, the one that has been registered first is taken. On the other hand,
	 * if no such vehicle is available, no reservation is made.
	 * 
	 * For each successful reservation, the user is given a number of points
	 * associated to the category of the rented vehicle.
	 * 
	 * Note: more than one rent can be associated with a user.
	 * 
	 * @param uid
	 * @param category
	 * @param seats
	 * @param duration
	 * @return the vehicle's unique id in case a vehicle satisfying the criteria is
	 *         found otherwise, NO_VEHICLE constant is returned.
	 * @throws AgencyException thrown if a user with the given id does not exist;
	 *                         additionally, if a category does not exist, an
	 *                         exception is thrown.
	 * 
	 */
	public int makeReservation(int uid, char category, int seats, int duration) throws AgencyException {

		if(users.values().stream().noneMatch(e->e.getUserId()==uid) || !pointsOfCategories.containsKey(category))
			throw new AgencyException();

		int vehicleId = vehicles.stream()
				.filter(e->e.getSeats()>=seats && e.getCategory()==category
				&& e.getVehicleStatus().equals(Vehicle.VehicleStatus.Libero))
				.max(comparing(Vehicle::getVehicleId).reversed())
				.map(Vehicle::getVehicleId)
				.orElse(NO_VEHICLE);




		if(vehicleId!=NO_VEHICLE) {

			vehicles.get(vehicleId).setVehicleStatus(Vehicle.VehicleStatus.Occupied);
			users.get(uid).addPoints(pointsOfCategories.get(category));
			reservations.add(new Reservation(users.get(uid).getName(),vehicles.get(vehicleId),duration));
			users.get(uid).addReservation(new Reservation(users.get(uid).getName(),vehicles.get(vehicleId),duration));

				return vehicleId;
			
		}

			return NO_VEHICLE;

	}

	/**
	 * Retrieves information about users that reserved cars of the given category.
	 * 
	 * The list is sorted alphabetically.
	 * 
	 * @param category
	 * @return a list of names of the users who booked cars of the given category
	 */
	public List<String> getUserNamesForTakenCars(char category) {
		List<String> occupiedVehicles=reservations.stream()
				.filter(e->e.getVehicle().getCategory()==category
				&& e.getVehicle().getVehicleStatus().equals(Vehicle.VehicleStatus.Occupied))
				.sorted(comparing(Reservation::getName))
				.map(Reservation::getName)
				.collect(toList());

		if(occupiedVehicles.isEmpty())
			return new LinkedList<>();

		return occupiedVehicles;
	}

	/**
	 * Retrieves information about vehicles that have number of seats higher or
	 * equal to the given one.
	 * 
	 * String format for each vehicle `### [category]:[manufacturer]:[model]`, where
	 * ### stands for vehicle id (printed on 3 characters).
	 * 
	 * List is sorted alphabetically based on the vehicle category and then based on
	 * vehicle id in ascending manner.
	 * 
	 * @param seats
	 * @return the list of Strings containing vehicle information
	 */
	public List<String> getAvailableVehicles(int seats) {


		List<String> availableVehicles=vehicles.stream()
				.filter(e->e.getVehicleStatus().equals(Vehicle.VehicleStatus.Libero)
						&& e.getSeats()>=seats)
				.sorted(Comparator.comparing(Vehicle::getCategory).thenComparing(Vehicle::getVehicleId))
				.map(e->String.format("  "+e.getVehicleId()+" ")+e.getCategory() +":"+e.getManufacturer()+":"+e.getModel())
				.collect(toList());

		if(availableVehicles.isEmpty())
			return new LinkedList<>();


		return availableVehicles;

	}

	// R4

	/**
	 * Retrieves a map that associates number of points with the user names having
	 * that number of points.
	 * 
	 * Number of points for one user is equal to the sum of points for all of the
	 * reservations the user made. Users with zero points are discarded. Points are
	 * sorted in the descending manner.
	 * 
	 * @return a map associating points with user names.
	 */
	public Map<Double, List<String>> getUsersPerPoints() {

		return users.values().stream()
				.filter(e->!e.getPoints().isEmpty())
				.collect(groupingBy(User::getTotalPoints,
						()-> new TreeMap<>(reverseOrder())
						,mapping(User::getName,toList())));

	}

	/**
	 * Retrieves a map that associates user id & name,`[id]: [name]` with with the
	 * average number of rent days for that user.
	 * 
	 * The map is sorted based on the average number of rent days in descending
	 * order and then based on the user names alphabetically.
	 * 
	 * @return a map that associates user info with the average number of rent days
	 *         for that user
	 */
	public Map<String, Double> evaluateUsers() {

		return users.values().stream()
				.sorted(Comparator.comparing(User::getAverageDurations).reversed().thenComparing(User::getName))
				.collect(toMap(p-> p.getUserId() +": "+p.getName(), User::getAverageDurations, (u1, u2) -> u1, LinkedHashMap::new));


	}

	/**
	 * Retrieves a map associating a year and information for the cars manufactured
	 * in that year.
	 * 
	 * Car info is in the following format `[manufacturer]:[model]:[color]`.
	 * 
	 * The years are sorted in the descending order.
	 * 
	 * @return a map associating year of production with the car information
	 */
	public Map<Integer, List<String>> getCarInfoForYears() {
		return vehicles.stream()
				.collect(groupingBy(Vehicle::getYear,
						()-> new TreeMap<>(reverseOrder())
						,mapping(Vehicle::toString,toList())));
	}

	/**
	 * Retrieves a map associating vehicle number with the categories of those
	 * vehicles.
	 * 
	 * The number of vehicles is sorted in ascending manner, while the categories
	 * are sorted alphabetically. The categories with 0 vehicles should also be
	 * considered.
	 * 
	 * @return a map associating number of vehicles with the vehicle categories
	 */
	

	public Map<Long, List<String>> getCategoriesForVehicleNumber() {


		Map<String, Long> categories =  vehicles.stream()
				.collect(groupingBy(p -> String.valueOf(p.getCategory()),
						TreeMap::new,
						counting()));

		pointsOfCategories.keySet().stream()
				.forEach(r -> categories.putIfAbsent(String.valueOf(r), 0L));

		return categories.entrySet().stream()
				.collect(groupingBy(Map.Entry::getValue,
						TreeMap::new,
						mapping(Map.Entry::getKey, toList())));

	}

}