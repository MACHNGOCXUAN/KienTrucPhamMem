package factoryMethod;

public class Main {
    public static void main(String[] args) {

        Vehicle v1 = VehicleFactory.createVehicle("car");
        v1.run();

        Vehicle v2 = VehicleFactory.createVehicle("bike");
        v2.run();

    }
}
