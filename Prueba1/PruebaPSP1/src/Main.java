public class Main {
    public static void main(String[] args) {
        // Tiempo de inicio en milisegundos
        long startTime = System.currentTimeMillis();

        // Definir el número de primos que queremos encontrar (10 millones)
        int nPrimos = 10000000;

        // Estimamos un límite superior para asegurarnos de que encontramos 10 millones de primos que son los que necesitamos.
        //Esto es necesario porque no sabemos de antemano cuál es el último número que deberíamos verificar para obtener exactamente 10 millones de primos.
        int limite = estimarLimite(nPrimos);

        // Llamamos a la función cribaEratostenes que nos devuelve un array booleano con los números primos
        boolean[] esPrimo = cribaEratostenes(limite);

        int[] ultimosDos = new int[2];

        // Inicializamos un contador para llevar la cuenta de cuántos primos hemos encontrado
        int contador = 0;

        // Recorremos el array esPrimo, comenzando desde 2, ya que el 0 y el 1 no son primos
        for (int i = 2; i < esPrimo.length && contador < nPrimos; i++) {
            // Si esPrimo[i] es true, significa que i es un número primo
            if (esPrimo[i]) {
                contador++; // Incrementamos el contador de primos

                // Mostramos los ultimos dos numeros primos
                if (contador >= 9999999) {
                    ultimosDos[0] = ultimosDos[1]; // Mover el último primo al penúltimo
                    ultimosDos[1] = i; // Asignar el nuevo primo como el último
                }
            }
        }

        
        // Tiempo de finalización en milisegundos
        long endTime = System.currentTimeMillis();

        // Calculamos la duración restando el tiempo de inicio al tiempo de finalización
        long duration = endTime - startTime;

        // Imprimimos el tiempo total de ejecución
        System.out.println("Tiempo de ejecución en milisegundos: " + duration + " milisegundos");
        System.out.println("Los ultimos dos numeros son: "+ ultimosDos[0] + " y " + ultimosDos[1]);
        System.out.println("Numero total de primos almacenados: " + contador);

    }






    // Función que implementa la Criba de Eratóstenes para encontrar números primos
    public static boolean[] cribaEratostenes(int limite) {
        // Creamos un array booleano donde cada índice representa un número
        // Inicialmente, asumimos que todos los números desde 2 hasta 'limite' son primos
        boolean[] esPrimo = new boolean[limite + 1];
        for (int i = 2; i <= limite; i++) {
            esPrimo[i] = true; // Marcamos todos los números como primos
        }

        // Empezamos a cribar los números primos desde el 2
        for (int p = 2; p * p <= limite; p++) {
            // Si esPrimo[p] es true, significa que p es primo
            if (esPrimo[p]) {
                // Marcamos todos los múltiplos de p como no primos (false)
                for (int i = p * p; i <= limite; i += p) {
                    esPrimo[i] = false; // Los múltiplos de p no son primos
                }
            }
        }

        // Devolvemos el array donde esPrimo[i] indica si i es primo
        return esPrimo;
    }






    // Función para estimar el límite superior necesario para encontrar n números primos
    public static int estimarLimite(int n) {
        
        // Estimamos el límite superior usando la fórmula n * (log(n) + log(log(n)))
        return (int) (n * (Math.log(n) + Math.log(Math.log(n))));
        
    }
}
