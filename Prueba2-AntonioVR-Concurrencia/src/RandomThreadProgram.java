import java.util.Random;

public class RandomThreadProgram {

    // Type1 Thread class
    public static class Type1Thread extends Thread {
        @Override
        public void run() {
            try {
                System.out.println("Type 1 thread starting.");
                int sleepTime = new Random().nextInt(5000); // Random sleep between 0 and 5 seconds
                System.out.println("Type 1 thread in process, sleeping for " + sleepTime + "ms.");
                Thread.sleep(sleepTime); // Simulate work with random sleep
                System.out.println("Type 1 thread finished.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Type2 Thread class
    public static class Type2Thread extends Thread {
        @Override
        public void run() {
            try {
                System.out.println("Type 2 thread starting.");
                int sleepTime = new Random().nextInt(5000); // Random sleep between 0 and 5 seconds
                System.out.println("Type 2 thread in process, sleeping for " + sleepTime + "ms.");
                Thread.sleep(sleepTime); // Simulate work with random sleep
                System.out.println("Type 2 thread finished.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Random random = new Random();
        int numThreads = random.nextInt(10) + 1; // Random number of threads between 1 and 10
        System.out.println("Creating " + numThreads + " threads.");

        Thread[] threads = new Thread[numThreads];

        // Randomly create threads of Type1 and Type2
        for (int i = 0; i < numThreads; i++) {
            if (random.nextBoolean()) {
                threads[i] = new Type1Thread(); // Create a Type 1 thread
            } else {
                threads[i] = new Type2Thread(); // Create a Type 2 thread
            }
            threads[i].start(); // Start each thread
        }

        // Wait for all threads to finish
        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("All threads have finished execution.");
    }
}
