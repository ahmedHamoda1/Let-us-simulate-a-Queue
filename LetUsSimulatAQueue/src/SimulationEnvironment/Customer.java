/**
 * Author: Ahmed Hamoda Elhanafy
 */

package SimulationEnvironment;
public class Customer {
    private final int customerNumber;
    private final double arrivalTime;
    private final double serviceTime;
    private double serviceStartTime;
    private double serviceEndTime;

    public Customer(int customerNumber, double arrivalTime, double serviceTime) {
        this.customerNumber = customerNumber;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public void startService(double lastServiceEnd) {
        serviceStartTime = Math.max(arrivalTime, lastServiceEnd);
        serviceEndTime = serviceStartTime + serviceTime;
    }


    public int getCustomerNumber() {
        return customerNumber;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public double getServiceTime() {
        return serviceTime;
    }

    public double getServiceStartTime() {
        return serviceStartTime;
    }

    public double getServiceEndTime() {
        return serviceEndTime;
    }

    public double getWaitTime() {
        return serviceStartTime - arrivalTime;
    }

    public double getTimeInSystem() {
        return serviceEndTime - arrivalTime;
    }
}
