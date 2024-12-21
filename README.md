هو# Queueing Models Simulation

#### *Author: Ahmed Hamoda Elhanafy*

## Overview
*The Queueing Models Simulation project is a JavaFX application designed to analyze and simulate queueing models. It supports calculating performance metrics for different queueing scenarios and simulating an M/M/1 queue system. The application is divided into two main functionalities:*

  - Queue Analysis: Provides calculations for M/M/1, M/M/1/K, M/M/c, and M/M/c/K models.

  - Queue Simulation: Simulates an M/M/1 queue system and visualizes customer behavior over time.

## Features

### Queue Analysis

Analyze common queueing models:
- **M/M/1**: Single server, infinite capacity.
- **M/M/1/K**: Single server, finite capacity.
- **M/M/c**: Multiple servers, infinite capacity.
- **M/M/c/K**: Multiple servers, finite capacity.

Input parameters:
- Arrival rate (λ)
- Service rate (μ)
- Number of servers (c) (optional)
- System capacity (K) (optional)

Output metrics:
- Average number of customers in the system (**L**).
- Average number of customers in the queue (**Lq**).
- Average time spent in the system (**W**).
- Average time spent in the queue (**Wq**).

### Queue Simulation (M/M/1)

Simulate an **M/M/1** queue with adjustable parameters:
- Mean inter-arrival time (1/λ)
- Mean service time (1/μ)
- Number of customers

Results:
- Detailed report for each customer.
- Performance metrics (e.g., average waiting time, server utilization).
- Graphical visualization of customer counts over time.

## Screenshots

- Queue Analysis: 

    ![Queue Analysis](https://github.com/user-attachments/assets/2ac9a58c-0f9c-4893-b368-00af55509a0c)

- Queue Simulation:

    ![Queue Simulation](https://github.com/user-attachments/assets/82e967c8-c020-4829-a7d6-8bf531edc87c) 


## Installation

### Prerequisites:

- Java 11 or higher
- JavaFX 15 or higher
- IDE with JavaFX support (e.g., IntelliJ IDEA or Eclipse with e(fx)clipse plugin)

### Steps:

1. Clone the repository:
   ```bash
   git clone https://github.com/ahmedHamoda1/Let-us-simulate-a-Queue.git
   cd Let-us-simulate-a-Queue
2. Open the project in your IDE.

3. Ensure the javafx-sdk is properly configured.

4. Run the Main class from the SimulationEnvironment package.

## Usage
### Queue Analysis
1. Select the "Queue Analysis" tab.
2. Enter parameters:
   - Arrival rate (λ)
   - Service rate (μ)
   - Number of servers (c) (optional)
   - System capacity (K) (optional)
3. Click "Calculate" to display results in the text area.
### Queue Simulation
1. Select the "Queue Simulation" tab.
2. Enter parameters:
   - Mean inter-arrival time (1/λ)
   - Mean service time (1/μ)
   - Number of customers
3. Click "Simulate" to display:
   - Detailed report of simulation results.
   - A line chart visualizing customers in the system over time.

## License
This project is licensed under the MIT License. See the LICENSE file for details.

## Contact
For questions or feedback, please email me at ahmdelhanafy50@gmail.com.
