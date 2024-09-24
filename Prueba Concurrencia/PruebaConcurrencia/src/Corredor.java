import java.util.Random;


class Corredor extends Thread {
   private final int numeroCorredor;
   private final Random random = new Random();


   public Corredor(int numeroCorredor) {
       this.numeroCorredor = numeroCorredor;
   }


   @Override
   public void run() {
       System.out.println("Corredor " + numeroCorredor + " comienza la carrera.");
       try {
           // Simula el tiempo que tarda cada corredor en terminar la carrera (entre 1 y 5 segundos)
           int tiempoCarrera = random.nextInt(5000) + 1000;
           Thread.sleep(tiempoCarrera); 
// Simula la duración de la carrera
           System.out.println("Corredor " + numeroCorredor + " ha terminado la carrera en " + tiempoCarrera + " ms.");
       } catch (InterruptedException e) {
           System.out.println("Corredor " + numeroCorredor + " fue interrumpido.");
       }
   }


   public static void main(String[] args) {
       // Crear y empezar diez hilos (corredores)
       for (int i = 1; i <= 10; i++) {
           new Corredor(i).start();
       }
   }
}
