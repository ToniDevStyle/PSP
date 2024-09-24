public class EjemploSecuencial {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        int contador = 0;
        
        while(contador<1000000){
            contador++;
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
    
        System.out.println("Final counter value: " + contador);
        System.out.println("Execution time in milliseconds: " + duration + " milliseconds");
    
    
    }
    
}
