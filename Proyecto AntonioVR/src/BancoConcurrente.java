import java.util.concurrent.*;

public class BancoConcurrente {

    // Clase que representa una cuenta compartida.
    static class CuentaCompartida {
        protected double saldo;  // Saldo de la cuenta compartida.
        protected final Semaphore semaforoOperacion = new Semaphore(1, true);  // Semaphore para controlar acceso a ingresos y retiros.
        protected final Semaphore semaforoConsulta = new Semaphore(1, true);   // Semaphore para evitar consultas durante ingresos/retiros.
        protected int consultasActivas = 0;  // Contador de consultas activas.
        protected long id;  // ID de operación específico.

        // Constructor que inicializa el saldo de la cuenta.
        public CuentaCompartida(double saldoInicial) {
            this.saldo = saldoInicial;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getId() {
            return this.id;
        }
        
        // Método sincronizado para obtener el saldo actual.
        public synchronized double obtenerSaldo() {
            return saldo;
        }

        // Método sincronizado para actualizar el saldo.
        public synchronized void actualizarSaldo(double cantidad) {
            saldo += cantidad;
        }
    }

    // Clase para operaciones de ingreso
    static class Ingreso implements Callable<Boolean> {
        private final CuentaCompartida cuenta;
        private final double cantidad;
        private final long id;

        public Ingreso(CuentaCompartida cuenta, double cantidad, long id) {
            this.cuenta = cuenta;
            this.cantidad = cantidad;
            this.id = id;
        }

        @Override
        public Boolean call() throws InterruptedException {
            cuenta.semaforoOperacion.acquire();
            cuenta.semaforoConsulta.acquire();

            System.out.println("Inicio ingreso en operación ID " + id + ": " + cantidad);
            Thread.sleep(500);  // Simula el tiempo que toma realizar la operación.

            cuenta.actualizarSaldo(cantidad);
            System.out.println("Ingreso realizado en operación ID " + id + ": " + cantidad + ", Saldo actual: " + cuenta.obtenerSaldo());

            cuenta.semaforoConsulta.release();
            cuenta.semaforoOperacion.release();
            System.out.println("Fin ingreso en operación ID " + id);
            return true;
        }
    }

    // Clase para operaciones de retiro
    static class Retiro implements Callable<Boolean> {
        private final CuentaCompartida cuenta;
        private final double cantidad;
        private final long id;

        public Retiro(CuentaCompartida cuenta, double cantidad, long id) {
            this.cuenta = cuenta;
            this.cantidad = cantidad;
            this.id = id;
        }

        @Override
        public Boolean call() throws InterruptedException {
            cuenta.semaforoOperacion.acquire();
            cuenta.semaforoConsulta.acquire();

            System.out.println("Inicio retiro en operación ID " + id + ": " + cantidad);
            Thread.sleep(500);

            if (cuenta.obtenerSaldo() >= cantidad) {
                cuenta.actualizarSaldo(-cantidad);
                System.out.println("Retiro realizado en operación ID " + id + ": " + cantidad + ", Saldo actual: " + cuenta.obtenerSaldo());
            } else {
                System.out.println("Fondos insuficientes para retirar en operación ID " + id + ": " + cantidad);
            }

            cuenta.semaforoConsulta.release();
            cuenta.semaforoOperacion.release();
            System.out.println("Fin retiro en operación ID " + id);
            return true;
        }
    }

    // Clase para operaciones de consulta
    static class Consulta implements Callable<Boolean> {
        private final CuentaCompartida cuenta;
        private final long id;

        public Consulta(CuentaCompartida cuenta, long id) {
            this.cuenta = cuenta;
            this.id = id;
        }

        @Override
        public Boolean call() throws InterruptedException {
            //sincronyzed para evitar conflictos de carrera
            synchronized (cuenta) {
                cuenta.consultasActivas++;
            }

            cuenta.semaforoConsulta.acquire();
            System.out.println("Inicio consulta en operación ID " + id);
            Thread.sleep(2000);
            System.out.println("Consulta realizada en operación ID " + id + ": Saldo actual " + cuenta.obtenerSaldo());
            cuenta.semaforoConsulta.release();
            System.out.println("Fin consulta en operación ID " + id);

            synchronized (cuenta) {
                cuenta.consultasActivas--;
            }
            return true;
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CuentaCompartida cuenta = new CuentaCompartida(1000.0);
        int numOperaciones = 200;
        int contadorIngresos = 0;
        int contadorRetiros = 0;
        int contadorConsultas = 0;
        long idOperacion = 1;  // Contador de ID único para cada operación

        ExecutorService executor = Executors.newFixedThreadPool(200);

        while (contadorIngresos + contadorRetiros + contadorConsultas < numOperaciones) {
            double random = Math.random();
            Future<Boolean> resultado;

            if (random < 0.1 && contadorIngresos < numOperaciones * 0.1) {
                resultado = executor.submit(new Ingreso(cuenta, 100.0, idOperacion++));
                contadorIngresos++;
            } else if (random < 0.2 && contadorRetiros < numOperaciones * 0.1) {
                resultado = executor.submit(new Retiro(cuenta, 50.0, idOperacion++));
                contadorRetiros++;
            } else if (contadorConsultas < numOperaciones * 0.8) {
                resultado = executor.submit(new Consulta(cuenta, idOperacion++));
                contadorConsultas++;

                // Cada 5 consultas realizadas, se ejecuta una operación adicional de ingreso o retiro
                if (contadorConsultas % 5 == 0) {
                    //50% probabilidad de ingreso
                    if (Math.random() < 0.5) {
                        resultado = executor.submit(new Ingreso(cuenta, 100.0, idOperacion++));
                        contadorIngresos++;
                    } else if (contadorRetiros < numOperaciones * 0.1) {
                        resultado = executor.submit(new Retiro(cuenta, 50.0, idOperacion++));
                        contadorRetiros++;
                    }
                }
            } else {
                break;
            }

            System.out.println("Resultado de la operación: " + resultado.get());
            Thread.sleep(400);
        }

        executor.shutdown();
    }
}
