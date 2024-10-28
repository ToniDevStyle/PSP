import java.util.concurrent.*; 

public class BancoConcurrente {

    // Clase que representa una cuenta compartida.
    static class CuentaCompartida {
        protected double saldo;  // Saldo de la cuenta, variable protegida para acceso en subclases.
        protected final Semaphore semaforoOperacion = new Semaphore(1, true);  // Semaphore para controlar acceso a ingresos y retiros (uno a la vez).
        protected final Semaphore semaforoConsulta = new Semaphore(1, true);   // Semaphore para evitar consultas durante ingresos/retiros.
        protected int consultasActivas = 0;  // Contador de consultas activas.

        // Constructor que inicializa el saldo de la cuenta al momento de crearla.
        public CuentaCompartida(double saldoInicial) {
            this.saldo = saldoInicial;
        }
    }

    // Clase que representa una operación de ingreso, hereda de CuentaCompartida.
    static class Ingreso implements Callable<Boolean> {
        private final CuentaCompartida cuenta;  // Cuenta compartida en la que se realizará el ingreso.
        private final double cantidad;  // Cantidad a ingresar en la cuenta.

        public Ingreso(CuentaCompartida cuenta, double cantidad) {
            this.cuenta = cuenta;  // Asignamos la cuenta compartida.
            this.cantidad = cantidad;
        }

        @Override
        public Boolean call() throws InterruptedException {
            // Esperamos a que no haya consultas activas y adquirimos el semáforo de operación.
            cuenta.semaforoOperacion.acquire();
            cuenta.semaforoConsulta.acquire();  // Bloqueamos nuevas consultas.
            System.out.println("Inicio ingreso en hilo ID " + Thread.currentThread().getId() + ": " + cantidad);
            Thread.sleep(500);  // Simula el tiempo que toma realizar la operación.

            // Actualizamos el saldo de la cuenta.
            cuenta.saldo += cantidad;
            System.out.println("Ingreso realizado en hilo ID " + Thread.currentThread().getId() + ": " + cantidad);

            // Liberamos el semáforo de operación y permitimos consultas nuevamente.
            cuenta.semaforoConsulta.release();
            cuenta.semaforoOperacion.release();
            System.out.println("Fin ingreso en hilo ID " + Thread.currentThread().getId());
            return true;  // Devuelve true para indicar que la operación fue exitosa.
        }
    }

    // Clase que representa una operación de retiro, hereda de CuentaCompartida.
    static class Retiro implements Callable<Boolean> {
        private final CuentaCompartida cuenta;  // Cuenta compartida en la que se realizará el retiro.
        private final double cantidad;  // Cantidad a retirar de la cuenta.

        public Retiro(CuentaCompartida cuenta, double cantidad) {
            this.cuenta = cuenta;  // Asignamos la cuenta compartida.
            this.cantidad = cantidad;
        }

        @Override
        public Boolean call() throws InterruptedException {
            // Esperamos a que no haya consultas activas y adquirimos el semáforo de operación.
            cuenta.semaforoOperacion.acquire();
            cuenta.semaforoConsulta.acquire();  // Bloqueamos nuevas consultas.
            System.out.println("Inicio retiro en hilo ID " + Thread.currentThread().getId() + ": " + cantidad);
            Thread.sleep(500);  // Simula el tiempo que toma realizar la operación.

            // Verificamos si hay suficiente saldo para realizar el retiro.
            if (cuenta.saldo >= cantidad) {
                cuenta.saldo -= cantidad;  // Actualizamos el saldo de la cuenta.
                System.out.println("Retiro realizado en hilo ID " + Thread.currentThread().getId() + ": " + cantidad);
                cuenta.semaforoConsulta.release();
                cuenta.semaforoOperacion.release();
                System.out.println("Fin retiro en hilo ID " + Thread.currentThread().getId());
                return true;  // Retorna true para indicar que el retiro fue exitoso.
            } else {
                // Mensaje que indica que no hay fondos suficientes.
                System.out.println("Fondos insuficientes para retirar en hilo ID " + Thread.currentThread().getId() + ": " + cantidad);
                cuenta.semaforoConsulta.release();
                cuenta.semaforoOperacion.release();
                return false;  // Retorna false para indicar que el retiro falló.
            }
        }
    }

    // Clase que representa una operación de consulta, hereda de CuentaCompartida.
    static class Consulta implements Callable<Boolean> {
        private final CuentaCompartida cuenta;  // Cuenta compartida que se consulta.

        public Consulta(CuentaCompartida cuenta) {
            this.cuenta = cuenta;  // Asignamos la cuenta compartida.
        }

        @Override
        public Boolean call() throws InterruptedException {
            synchronized (cuenta) {
                cuenta.consultasActivas++;  // Incrementa el contador de consultas activas.
            }

            cuenta.semaforoConsulta.acquire();  // Adquirimos el semáforo para bloquear ingresos/retiros.
            System.out.println("Inicio consulta en hilo ID " + Thread.currentThread().getId());
            Thread.sleep(2000);  // Simulamos el tiempo que toma realizar la consulta.
            System.out.println("Consulta realizada en hilo ID " + Thread.currentThread().getId() + ": Saldo actual " + cuenta.saldo);
            cuenta.semaforoConsulta.release();  // Liberamos el semáforo.
            System.out.println("Fin consulta en hilo ID " + Thread.currentThread().getId());

            synchronized (cuenta) {
                cuenta.consultasActivas--;  // Decrementa el contador de consultas activas.
            }
            return true;  // Retorna true para indicar que la consulta fue exitosa.
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CuentaCompartida cuenta = new CuentaCompartida(1000.0);  // Creamos una cuenta compartida con un saldo inicial.
        int numOperaciones = 200;  // Número total de operaciones a realizar.
        
        int contadorIngresos = 0;
        int contadorRetiros = 0;
        int contadorConsultas = 0;

        ExecutorService executor = Executors.newFixedThreadPool(10);  // Pool de hilos.

        while (contadorIngresos + contadorRetiros + contadorConsultas < numOperaciones) {
            double random = Math.random();  // Genera un número aleatorio para determinar la operación
            Future<Boolean> resultado;  // Variable para almacenar el resultado de la operación.

            // Determina el tipo de operación a realizar según el número aleatorio generado.
            if (random < 0.1 && contadorIngresos < numOperaciones * 0.1) {  // 10% para un ingreso.
                resultado = executor.submit(new Ingreso(cuenta, 100.0));  // Crea un nuevo hilo para realizar el ingreso.
                contadorIngresos++;  // Aumenta el contador de ingresos.
            } else if (random < 0.2 && contadorRetiros < numOperaciones * 0.1) {  // 10% para un retiro.
                resultado = executor.submit(new Retiro(cuenta, 50.0));  // Crea un nuevo hilo para realizar el retiro.
                contadorRetiros++;  // Aumenta el contador de retiros.
            } else if (contadorConsultas < numOperaciones * 0.8) {  // 80% para una consulta.
                resultado = executor.submit(new Consulta(cuenta));  // Realiza una consulta.
                contadorConsultas++;  // Aumenta el contador de consultas.

                // Cada 5 consultas, realiza un ingreso o retiro.
                if (contadorConsultas % 5 == 0) {
                    if (Math.random() < 0.5) {  // 50% probabilidad de ingreso o retiro.
                        resultado = executor.submit(new Ingreso(cuenta, 100.0));
                        contadorIngresos++;
                    } else if (contadorRetiros < numOperaciones * 0.1) {
                        resultado = executor.submit(new Retiro(cuenta, 50.0));
                        contadorRetiros++;
                    }
                }
            } else {
                break;
            }

            System.out.println("Resultado de la operación: " + resultado.get());
            Thread.sleep(400);  // Pausa de 0.4 segundos antes de crear el siguiente hilo.
        }

        executor.shutdown();  // Cierra el ExecutorService al finalizar las operaciones.
    }
}
