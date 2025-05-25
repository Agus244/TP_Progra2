package Eventos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects; // Para el uso de Objects.equals en equals() y hashCode()

public class Funcion {
    private Espectaculo espectaculo; // El espectáculo al que pertenece esta función
    private Sede sede;               // La sede donde se realiza esta función
    private LocalDate fecha;         // La fecha en que se realiza esta función
    private double precioBase;       // El precio base de la entrada para esta función (sin recargos por sector)
    private List<Entrada> entradasVendidas; // Lista de todas las entradas vendidas para esta función

    /**
     * Constructor para crear una nueva instancia de Funcion.
     *
     * @param espectaculo El objeto Espectaculo al que pertenece esta función.
     * @param sede        El objeto Sede donde se realizará la función.
     * @param fecha       La fecha de la función.
     * @param precioBase  El precio base de la entrada para esta función.
     * @throws IllegalArgumentException Si alguno de los parámetros obligatorios es nulo,
     * o si el precioBase es negativo o cero.
     */
    public Funcion(Espectaculo espectaculo, Sede sede, LocalDate fecha, double precioBase) {
        if (espectaculo == null) {
            throw new IllegalArgumentException("El espectáculo no puede ser nulo.");
        }
        if (sede == null) {
            throw new IllegalArgumentException("La sede no puede ser nula.");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula.");
        }
        if (precioBase <= 0) {
            throw new IllegalArgumentException("El precio base debe ser un valor positivo.");
        }

        this.espectaculo = espectaculo;
        this.sede = sede;
        this.fecha = fecha;
        this.precioBase = precioBase;
        this.entradasVendidas = new ArrayList<>(); // Inicializa la lista de entradas vendidas
    }

    /**
     * Agrega una entrada a la lista de entradas vendidas para esta función.
     * Esta entrada debe haber sido validada y creada previamente (ej. por un método de la Sede).
     *
     * @param entrada La entrada a agregar a la lista de vendidas.
     * @throws IllegalArgumentException Si la entrada es nula.
     */
    public void agregarEntrada(Entrada entrada) {
        if (entrada == null) {
            throw new IllegalArgumentException("La entrada a agregar no puede ser nula.");
        }
        this.entradasVendidas.add(entrada);
    }

    /**
     * Elimina una entrada de la lista de entradas vendidas para esta función.
     * Se usa típicamente al anular una entrada.
     *
     * @param entrada La entrada a eliminar.
     * @return true si la entrada fue eliminada con éxito, false si no se encontró.
     * @throws IllegalArgumentException Si la entrada es nula.
     */
    public boolean eliminarEntrada(Entrada entrada) {
        if (entrada == null) {
            throw new IllegalArgumentException("La entrada a eliminar no puede ser nula.");
        }
        return this.entradasVendidas.remove(entrada);
    }

    /**
     * Calcula la recaudación total generada por esta función.
     * Suma el precio total de cada entrada vendida que no esté anulada.
     *
     * @return La recaudación total de la función.
     */
    public double calcularRecaudacion() {
        double recaudacion = 0.0;
        // Uso de for-each para recorrer las entradas
        for (Entrada entrada : entradasVendidas) {
            if (!entrada.estaAnulada()) { // Solo suma si la entrada no está anulada
                recaudacion += entrada.precio();
            }
        }
        return recaudacion;
    }

    // --- Getters ---

    public Espectaculo getEspectaculo() {
        return espectaculo;
    }

    public Sede getSede() {
        return sede;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public List<Entrada> getEntradasVendidas() {
        // Devuelve una copia defensiva para evitar modificaciones externas directas a la lista.
        return new ArrayList<>(entradasVendidas);
    }

    /**
     * Obtiene el número total de entradas vendidas para esta función (incluyendo anuladas).
     *
     * @return La cantidad de entradas vendidas.
     */
    public int getCantidadEntradasVendidas() {
        return entradasVendidas.size();
    }

    /**
     * Obtiene el número de entradas vendidas para un sector específico de esta función.
     *
     * @param sector El nombre del sector.
     * @return La cantidad de entradas vendidas en ese sector.
     */
    public int getEntradasVendidasPorSector(String sector) {
        int count = 0;
        for (Entrada entrada : entradasVendidas) {
            if (entrada.getSector().equalsIgnoreCase(sector) && !entrada.estaAnulada()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Representación en cadena del objeto Funcion.
     *
     * @return Una cadena que describe la función.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Función: ").append(espectaculo.getNombre());
        sb.append(" | Fecha: ").append(fecha);
        sb.append(" | Sede: ").append(sede.getNombre());
        sb.append(" | Precio Base: $").append(String.format("%.2f", precioBase));
        sb.append(" | Entradas Vendidas: ").append(entradasVendidas.size());
        sb.append(" | Recaudación: $").append(String.format("%.2f", calcularRecaudacion()));
        return sb.toString();
    }

    /**
     * Compara esta función con otro objeto para determinar si son iguales.
     * Dos funciones se consideran iguales si pertenecen al mismo espectáculo y se realizan en la misma fecha.
     *
     * @param o El objeto a comparar.
     * @return true si el objeto es una Funcion con el mismo espectáculo y fecha, false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Funcion funcion = (Funcion) o;
        // Dos funciones son iguales si su espectáculo y fecha son iguales.
        return Objects.equals(espectaculo, funcion.espectaculo) &&
               Objects.equals(fecha, funcion.fecha);
    }

    /**
     * Genera un valor hash para esta función, basado en el espectáculo y la fecha.
     *
     * @return Un valor hash entero.
     */
    @Override
    public int hashCode() {
        return Objects.hash(espectaculo, fecha);
    }
}