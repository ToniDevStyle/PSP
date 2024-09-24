public class EjemploVariableCompartidaSinSincronizacion extends Thread {
   private static int contador = 0; // Variable compartida


   @Override
   public void run() {
       for (int i = 0; i < 5; i++) { // Cada hilo incrementa el contador 5 veces
           contador++; // Incrementar el contador
       }
   }


   public static void main(String[] args) {
       // Crear 5 hilos
       Thread hilo1 = new EjemploVariableCompartidaSinSincronizacion();
       Thread hilo2 = new EjemploVariableCompartidaSinSincronizacion();
       Thread hilo3 = new EjemploVariableCompartidaSinSincronizacion();
       Thread hilo4 = new EjemploVariableCompartidaSinSincronizacion();
       Thread hilo5 = new EjemploVariableCompartidaSinSincronizacion();


       // Iniciar los hilos
       hilo1.start();
       hilo2.start();
       hilo3.start();
       hilo4.start();
       hilo5.start();


       // Esperar a que todos los hilos terminen
       try {
           hilo1.join();
           hilo2.join();
           hilo3.join();
           hilo4.join();
           hilo5.join();
       } catch (InterruptedException e) {
           e.printStackTrace();
       }


       // Imprimir el valor final del contador
       System.out.println("Valor final del contador (sin sincronización): " + contador);
   }
}
