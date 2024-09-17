public class Main {
    public static void main(String[] args) {
        // Starting time on ms
        long startTime = System.currentTimeMillis();

        
        // Defining the number of prime numbers that we want to find (10M)
        int nPrime = 10000000;

        // With a higher limit we are sure that we can find exactly 10M prime numbers because we do not know wich is the last number that we have to verify to obtain exactly 10M prime numbers.
        int limit = estimarlimit(nPrime);

        // We call the cribaErastotenes function that return us a boolean array with the prime numbers that we need
        boolean[] isPrime = cribaEratostenes(limit);

        //We create a 2 integer numbers length array to save the last 2 prime numbers that we are going to find.
        int[] lastTwo = new int[2];

      
        // We create a counter to know how many prime numbers we are founding
        int counter = 0;

        // Recorremos el array isPrime, comenzando desde 2, ya que el 0 y el 1 no son primos
        // We create a loop to run the isPrime array, starting from 2 because 0 and 1 are not prime numbers.
        for (int i = 2; i < isPrime.length && counter < nPrime; i++) {
            
            //If isPrime[i] is true, that means that i is a prime number.
            if (isPrime[i]) {
                counter++; 

                
                // Showing the last 2 prime numbers.
                if (counter >= 9999999) {
                    lastTwo[0] = lastTwo[1]; 
                    lastTwo[1] = i; 
                }
            }
        }

        
        
        // Finishing time in ms.
        long endTime = System.currentTimeMillis();

        
        // Calculating how much time spends from starting poing to finish.
        long duration = endTime - startTime;

    
        System.out.println("Tiempo de ejecución en milisegundos: " + duration + " milisegundos");
        System.out.println("Los ultimos dos numeros son: "+ lastTwo[0] + " y " + lastTwo[1]);
        System.out.println("Numero total de primos almacenados: " + counter);

    }






    
    // Function that implements the Erastotenes sieve to find the prime numbers
    public static boolean[] cribaEratostenes(int limit) {
        
        // We create a boolean array where each index represents a number.
        // For the begining we are going to say that all the numbers from 2 to "limit" are prime numbers
        boolean[] isPrime = new boolean[limit + 1];
        for (int i = 2; i <= limit; i++) {
            isPrime[i] = true; // ""All numbers are prime""
        }

        // Sifting the prime numbers
        for (int p = 2; p * p <= limit; p++) {
            
            // If isPrime[p] is true, then p is a prime number
            if (isPrime[p]) {
                
                // Then, all his multiple numbers are not prime numbers
                for (int i = p * p; i <= limit; i += p) {
                    isPrime[i] = false; // Multiples of p are not prime
                }
            }
        }

       
        // We return the array where isPrime[i] says if i is a prime number
        return isPrime;
    }






  
    public static int estimarlimit(int n) {
        
       // We estimate the upper limit using the formula n * (log(n) + log(log(n)))
        return (int) (n * (Math.log(n) + Math.log(Math.log(n))));
        
    }
}
