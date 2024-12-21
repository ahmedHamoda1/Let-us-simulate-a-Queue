/**
 * Author: Ahmed Hamoda Elhanafy
 */
package SimulationEnvironment;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Main class that launches the Queueing Models Simulation application.
 * It includes two tabs: one for analyzing queueing models and another for simulating an M/M/1 queue.
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Queueing Models Simulation");

        // Create Tabs for Analysis and Simulation
        TabPane tabPane = new TabPane();
        Tab analyzeTab = new Tab("Queue Analysis", QueueAnalyzing.createAnalyzeQueuePane());
        Tab simulateTab = new Tab("Queue Simulation", MM1Simulation.createSimulateQueuePane());

        // Ensure tabs are not closable
        analyzeTab.setClosable(false);
        simulateTab.setClosable(false);

        tabPane.getTabs().addAll(analyzeTab, simulateTab);

        // Add CSS Styling
        Scene scene = new Scene(tabPane, 600, 500);
        scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());

        primaryStage.setScene(scene); // Set the scene
        primaryStage.show(); // Set the scene
    }
}
