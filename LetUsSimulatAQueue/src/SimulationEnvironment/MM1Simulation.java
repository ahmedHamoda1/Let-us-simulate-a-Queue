/**
 * Author: Ahmed Hamoda Elhanafy
 */

package SimulationEnvironment;


import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import java.util.*;

/**
 * Provides GUI and logic for simulating an M/M/1 queueing system.
 */

public class MM1Simulation {
    /**
     * Creates the GUI pane for M/M/1 simulation.
     * @return A BorderPane containing input fields, results, and a chart.
     */
    public static BorderPane createSimulateQueuePane() {
        // Inputs for inter-arrival time, service time, and number of customers
        Label meanInterarrivalLabel = new Label("Mean Inter-arrival Time (1/λ):");
        TextField meanInterarrivalField = new TextField();
        Label meanServiceLabel = new Label("Mean Service Time (1/μ):");
        TextField meanServiceField = new TextField();
        Label numCustomersLabel = new Label("Number of Customers:");
        TextField numCustomersField = new TextField();

        // Button to start the simulation
        Button simulateButton = new Button("Simulate");

        // TextArea to display results
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);

        // Chart to visualize simulation results
        final NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Time");

        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of Customers");

        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Queue Simulation - Customers Over Time");

        simulateButton.setOnAction(e -> {
            try {
                // Parse inputs
                double meanInterarrival = Double.parseDouble(meanInterarrivalField.getText());
                double meanService = Double.parseDouble(meanServiceField.getText());
                int numCustomers = Integer.parseInt(numCustomersField.getText());

                if (meanInterarrival <= 0 || meanService <= 0 || numCustomers <= 0) {
                    lineChart.getData().clear();
                    resultArea.setText("Please enter positive values for all inputs.");
                    return;
                }

                // Clear old data and run simulation
                customers.clear();

                String simulationResult = simulateQueue(meanInterarrival, meanService, numCustomers);
                resultArea.setText(simulationResult);

                // Clear previous data
                lineChart.getData().clear();

                // Generate simulation results
                XYChart.Series<Number, Number> series = new XYChart.Series<>();
                series.setName("Customers in System");

                // Populate the series with data
                for (int i = 0; i < extractTimePoints().size(); i++) {
                    series.getData().add(new XYChart.Data<>(extractTimePoints().get(i), extractCustomerCount(extractTimePoints()).get(i)));
                }
                lineChart.getData().add(series);

            } catch (NumberFormatException ex) {
                resultArea.setText("Please enter valid numerical values.");
            }
        });

        // Layout setup
        GridPane inputGrid = new GridPane();
        inputGrid.setVgap(10);
        inputGrid.add(meanInterarrivalLabel, 0, 0);
        inputGrid.add(meanInterarrivalField, 1, 0);
        inputGrid.add(meanServiceLabel, 0, 1);
        inputGrid.add(meanServiceField, 1, 1);
        inputGrid.add(numCustomersLabel, 0, 2);
        inputGrid.add(numCustomersField, 1, 2);
        inputGrid.add(simulateButton, 0, 3);
        inputGrid.add(resultArea, 0, 5, 4, 1);

        BorderPane pane = new BorderPane();
        pane.setTop(inputGrid);
        pane.setCenter(lineChart);

        return pane;
    }

    // A static list to store all the customer objects for simulation
    static List<Customer> customers = new ArrayList<>();
    private static String simulateQueue(double meanInterarrival, double meanService, int numCustomers) {
        // Random number generator for generating exponential random variables
        Random random = new Random();

        // StringBuilder to store the formatted output table
        StringBuilder table = new StringBuilder();

        // Add column headers to the output table
        table.append("C.No.     A.T.     S.T.     S.B.     W.T.     " +
                "S.E.     T.inS.     Idle T.\n");

        // Initialize variables for tracking simulation metrics
        double currentTime = 0; // Tracks the current time in the simulation
        double lastServiceEnd = 0; // Tracks when the last customer's service ended
        double totalWaitTime = 0, totalTimeInSystem = 0, totalServiceTime = 0, totalIdleTime = 0; // Accumulators for metrics

        // Loop through the number of customers to simulate
        for (int i = 0; i < numCustomers; i++) {
            // Generate inter-arrival and service times (exponential distribution)
            double interarrivalTime = i == 0 ? 0 : -meanInterarrival * Math.log(1 - random.nextDouble());
            double serviceTime = -meanService * Math.log(1 - random.nextDouble());

            // Calculate the customer's arrival time
            double arrivalTime = currentTime + interarrivalTime;

            // Create a new Customer object
            Customer customer = new Customer(i + 1, arrivalTime, serviceTime);
            customers.add(customer);

            // Update the current time to the arrival time of the new customer
            currentTime = arrivalTime;

            // Calculate the time when the customer's service starts
            customer.startService(lastServiceEnd);

            // Calculate individual metrics for the customer
            double waitTime = customer.getWaitTime();
            double timeInSystem = customer.getTimeInSystem();
            double idleTime = Math.max(0,(currentTime-lastServiceEnd)); // Time-server was idle before this customer

            // Accumulate metrics for calculating averages and performance metrics
            totalWaitTime += waitTime;
            totalTimeInSystem += timeInSystem;
            totalServiceTime += serviceTime;
            totalIdleTime += idleTime;

            // Append the customer's details to the output table
            table.append(String.format("%d          %.2f     %.2f     %.2f     %.2f     %.2f     %.2f     %.2f\n",
                    customer.getCustomerNumber(),
                    customer.getArrivalTime(),
                    customer.getServiceTime(),
                    customer.getServiceStartTime(),
                    waitTime,
                    customer.getServiceEndTime(),
                    timeInSystem,
                    idleTime));

            // Update the last service end time to this customer's service end time
            lastServiceEnd = customer.getServiceEndTime();

        }

        // Calculate performance metrics
        double avgWaitTime = totalWaitTime / numCustomers; // Average time customers wait in the queue
        double avgTimeInSystem = totalTimeInSystem / numCustomers; // Average time customers spend in the system
        double serverUtilization = totalServiceTime / lastServiceEnd; // Proportion of time the server is busy

        // Append performance metrics to result
        table.append("\nPerformance Metrics:\n");
        table.append(String.format("Average Waiting Time: %.2f\n", avgWaitTime));
        table.append(String.format("Average Time in System: %.2f\n", avgTimeInSystem));
        table.append(String.format("Server Utilization: %.2f%%\n", serverUtilization * 100));

        // Return the complete simulation report
        return table.toString();
    }

    /**
     * Extracts all unique time points (arrival times and service end times) from the customer data.
     *
     * @return A sorted list of unique time points.
     */

    private static List<Double> extractTimePoints(){
        // Use a TreeSet to automatically sort and store unique time points
        Set<Double> timePointsSet = new TreeSet<>();

        // Add arrival times and service end times for all customers
        for (Customer customer : customers) {
            timePointsSet.add(customer.getArrivalTime()); // Add arrival time
            timePointsSet.add(customer.getServiceEndTime()); // Add service end time
        }
        // Convert the sorted set to a list and return
        return new ArrayList<>(timePointsSet);
    }

    /**
     * Calculates the number of customers in the system at each time point.
     *
     * @param timePoints A list of unique time points.
     * @return A list of customer counts corresponding to each time point.
     */
    private static List<Integer> extractCustomerCount(List<Double> timePoints){
        List<Integer> customerCounts = new ArrayList<>(); // List to store customer counts at each time point

        // Iterate through each time point
        for (double time : timePoints) {
            int count = 0;  // Counter for customers present in the system at the current time

            // Check each customer to see if they are in the system at the current time
            for (Customer customer : customers) {
                if (customer.getArrivalTime() <= time && customer.getServiceEndTime() > time) {
                    count++;  // Increment count if the customer is in the system
                }
            }

            // Add the count for this time point to the list
            customerCounts.add(count);
        }
        // Return the list of customer counts
        return customerCounts;
    }
}