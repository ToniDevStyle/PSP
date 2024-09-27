public class EjemploVariableCompartida8threads extends Thread {
    private static int counter = 0; // Shared variable (counter)
    private static final int LIMITE = 1000000; // Maximum value for the counter

    @Override
    public void run() {
        while (true) {
            // Call the synchronized method to increment the counter if it hasn't reached the limit
            if (!incrementarCounter()) {
                break; // Exit the loop if the counter has reached the limit
            }
        }
    }

    // Synchronized method to increment the counter and check if the limit has been reached
    private static synchronized boolean incrementarCounter() {
        if (counter < LIMITE) {
            counter++; // Increment the counter by 1
            return true; // The counter has not yet reached the limit
        } else {
            return false; // The limit has been reached, stop incrementing
        }
    }

    public static void main(String[] args) {
        int numThreads = 8; // Number of threads to create
        Thread[] threads = new Thread[numThreads]; // Array to store the threads
        
        // Record the starting time in milliseconds
        long startTime = System.currentTimeMillis();

        // Create and start the threads
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new EjemploVariableCompartida8threads();
            threads[i].start(); // Start each thread
        }

        // Wait for all threads to finish
        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join(); // Ensure the main thread waits for each thread to finish
            } catch (InterruptedException e) {
                e.printStackTrace(); // Handle potential interruption
            }
        }

        // Record the ending time in milliseconds
        long endTime = System.currentTimeMillis();

        // Calculate the execution time by subtracting the start time from the end time
        long duration = endTime - startTime;

        // Print the final value of the counter
        System.out.println("Final counter value (with synchronization): " + counter);
        // Print the execution time in milliseconds
        System.out.println("Execution time in milliseconds: " + duration + " milliseconds");
    }
}
