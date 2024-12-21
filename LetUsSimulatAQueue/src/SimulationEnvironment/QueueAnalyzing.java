/**
 * Author: Ahmed Hamoda Elhanafy
 */

package SimulationEnvironment;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

/**
 * Provides static methods to create
 * a GUI for analyzing queueing models
 * and perform calculations for M/M/1, M/M/1/K, M/M/c, and M/M/c/K models.
 */

public class QueueAnalyzing {


    /**
     * Creates the GUI pane for queue analysis.
     * @return A GridPane containing input fields and result display area.
     */

    public static GridPane createAnalyzeQueuePane() {
        // Input labels and fields for queue parameters
        Label lambdaLabel = new Label("Arrival rate (λ):");
        TextField lambdaField = new TextField();
        Label muLabel = new Label("Service rate (μ):");
        TextField muField = new TextField();
        Label serversLabel = new Label("Number of servers (c):");
        TextField serversField = new TextField();
        Label capacityLabel = new Label("System capacity (K):");
        TextField capacityField = new TextField();

        // Button to calculate queue metrics
        Button calculateButton = new Button("Calculate");

        // TextArea to display results
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);

        // Event handler for the Calculate button
        calculateButton.setOnAction(e -> {
            try {
                // Parse user inputs
                double lambda = Double.parseDouble(lambdaField.getText());
                double mu = Double.parseDouble(muField.getText());
                double rho = 0;

                String result = "";
                int c = serversField.getText().isEmpty() ? 1 : Integer.parseInt(serversField.getText());
                int k = capacityField.getText().isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(capacityField.getText());

                // Determine the appropriate model based on inputs
                if (c == 1 && k == Integer.MAX_VALUE) {
                    rho = lambda / mu;
                    result = QueueAnalyzing.mm1(lambda, mu);
                } else if (c == 1) {
                    rho = lambda / mu;
                    result = QueueAnalyzing.mm1k(lambda, mu, k);
                } else if (k == Integer.MAX_VALUE) {
                    rho = (lambda / (mu * c));
                    result = QueueAnalyzing.mmc(lambda, mu, c);
                } else {
                    rho = (lambda / (mu * c));
                    result = QueueAnalyzing.mmck(lambda, mu, c, k);
                }


                // Warn if the system is unstable
                if (rho >= 1) {
                    System.out.println("System is unstable (rho >= 1). Results may not be meaningful.");
                    resultArea.setText("System is unstable (ρ ≥ 1). Results may not be meaningful.\n"+result);
                }
                else resultArea.setText(result);
            } catch (NumberFormatException ex) {
                resultArea.setText("Please enter valid numerical values.");
            }
        });

        // Layout setup
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.add(lambdaLabel, 0, 0);
        grid.add(lambdaField, 1, 0);
        grid.add(muLabel, 0, 1);
        grid.add(muField, 1, 1);
        grid.add(serversLabel, 0, 2);
        grid.add(serversField, 1, 2);
        grid.add(capacityLabel, 0, 3);
        grid.add(capacityField, 1, 3);
        grid.add(calculateButton, 0, 4);
        grid.add(resultArea, 0, 5, 2, 1);

        return grid;
    }


    public static String mm1(double lambda, double mu) {
        double rho = lambda / mu;
        double l = rho / (1 - rho);
        double lq = Math.pow(rho, 2) / (1 - rho);
        double w = 1 / (mu - lambda);
        double wq = rho / (mu - lambda);

        return String.format("M/M/1 Model:\nL: %.3f\nLq: %.3f\nW: %.3f\nWq: %.3f\n", l, lq, w, wq);
    }

    public static String mm1k(double lambda, double mu, int k) {
        double rho = lambda / mu;
        double sum = 0;
        for (int n = 0; n <= k; n++) {
            sum += Math.pow(rho, n);
        }
        double pk = 0;
        if (rho != 1) {
            pk = Math.pow(rho, k) * ((1 - rho) / (1 - Math.pow(rho, k + 1)));
        } else {
            pk = 1.0 / (k + 1);
        }
        double l = 0;
        if (rho != 1) {
            l = rho * (1 - ((k + 1) * Math.pow(rho, k)) + (k * Math.pow(rho, k + 1))) /
                    ((1 - rho) * (1 - Math.pow(rho, k + 1)));
        } else {
            l = (double) k / 2;
        }
        double lambdaEff = lambda * (1 - pk);
        double lq = l - (lambdaEff / mu);
        double w = l / lambdaEff;
        double wq = lq / lambdaEff;

        return String.format("M/M/1/K Model:\nL: %.3f\nLq: %.3f\nW: %.3f\nWq: %.3f\n", l, lq, w, wq);
    }

    public static String mmc(double lambda, double mu, int c) {
        double r = lambda / mu;
        double rho = r / c;
        double ci = (rho < 1) ? c - r : 0; // Idle capacity
        if (rho >= 1) {
            System.out.println("System is unstable (rho >= 1). Results may not be meaningful.");
        }
        double p0 = calculateP0(lambda, mu, c);
        double lq = (Math.pow(r, c) * rho * p0) / (factorial(c) * Math.pow(1 - rho, 2));
        double l = lq + r;
        double wq = lq / lambda;
        double w = wq + 1 / mu;

        return String.format("M/M/c Model:\nL: %.3f\nLq: %.3f\nW: %.3f\nWq: %.3f\nCi: %.3f\n", l, lq, w, wq, ci);
    }


    public static String mmck(double lambda, double mu, int c, int k) {
        double r = lambda / mu;
        double rho = r / c;
        double ci = (rho < 1) ? c - r : 0; // Idle capacity

        if (rho >= 1) {
            System.out.println("System is unstable (rho >= 1). Results may not be meaningful.");
        }

        double p0 = calculateP0(lambda, mu, c, k);
        double pk = (Math.pow(r, k) / (Math.pow(c, k - c) * factorial(c))) * p0;

        // Calculate Lq
        double lq = 0;
        if (rho != 1) {
            lq = (rho * Math.pow(r, c) * p0) /
                    (factorial(c) * Math.pow(1 - rho, 2)) *
                    (1 - Math.pow(rho, k - c + 1) - (1 - rho) *
                            (k - c + 1) * Math.pow(rho, k - c));

        } else {
            lq = 0; // Handle rho = 1 case.
        }

        // Calculate L
        double sumL = 0;
        for (int n = 0; n < c; n++) {
            sumL += (c - n) * Math.pow(r, n) / factorial(n);
        }
        double l = lq + c - p0 * sumL;

        // Calculate W and Wq
        double lambdaEff = lambda * (1 - pk);
        double w = l / lambdaEff;
        double wq = lq / lambdaEff;

        return String.format("M/M/c/K Model:\nL: %.3f\nLq: %.3f\nW: %.3f\nWq: %.3f\nCi: %.3f\n", l, lq, w, wq, ci);
    }

    private static double calculateP0(double lambda, double mu, int c) {
        double r = lambda / mu;
        double p0Inverse = 0;
        for (int n = 0; n < c; n++) {
            p0Inverse += Math.pow(r, n) / factorial(n);
        }
        p0Inverse += (Math.pow(r, c) / factorial(c)) * (c / (c - r));
        return 1 / p0Inverse;
    }


    private static double calculateP0(double lambda, double mu, int c, int k) {
        double r = lambda / mu;
        double rho = r / c;
        double sum1 = 0;
        for (int n = 0; n < c; n++) {
            sum1 += Math.pow(r, n) / factorial(n);
        }
        if (rho != 1) {
            double sum2 = (Math.pow(r, c) / factorial(c)) * (1 - Math.pow(rho, k - c + 1)) / (1 - rho);
            return 1 / (sum1 + sum2);
        } else {
            double sum2 = (Math.pow(r, c) / factorial(c)) * (k - c + 1);
            return 1 / (sum1 + sum2);
        }
    }


    private static long factorial(int n) {
        if (n == 0 || n == 1) return 1;
        long fact = 1;
        for (int i = 2; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }
}
