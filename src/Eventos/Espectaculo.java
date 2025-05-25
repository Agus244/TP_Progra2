package Eventos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects; // Para el uso de Objects.equals en equals() y hashCode()

public class Espectaculo {
    private String nombre; // Nombre único del espectáculo
    // Mapa para almacenar las funciones del espectáculo, la clave es la fecha de la función.
    private Map<LocalDate, Funcion> funciones;

    /**
     * Constructor para crear una nueva instancia de Espectaculo.
     *
     * @param nombre El nombre único del espectáculo.
     * @throws IllegalArgumentException Si el nombre es nulo o vacío.
     */
    public Espectaculo(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del espectáculo no puede ser nulo o vacío.");
        }
        this.nombre = nombre;
        this.funciones = new HashMap<>(); // Se inicializa un mapa vacío para las funciones
    }

    /**
     * Agrega una nueva función a este espectáculo.
     * Una función se identifica por su fecha, por lo que no puede haber dos funciones
     * del mismo espectáculo en la misma fecha.
     *
     * @param funcion La función a agregar.
     * @throws IllegalArgumentException Si la función es nula o si ya existe una función para esa fecha.
     */
    public void agregarFuncion(Funcion funcion) {
        if (funcion == null) {
            throw new IllegalArgumentException("La función a agregar no puede ser nula.");
        }
        if (funciones.containsKey(funcion.getFecha())) {
            throw new IllegalArgumentException("Ya existe una función para este espectáculo en la fecha: " + funcion.getFecha());
        }
        funciones.put(funcion.getFecha(), funcion);
    }

    /**
     * Obtiene una función de este espectáculo por su fecha.
     *
     * @param fecha La fecha de la función a buscar.
     * @return La función asociada a la fecha dada, o null si no se encuentra.
     */
    public Funcion getFuncion(LocalDate fecha) {
        return funciones.get(fecha);
    }

    /**
     * Calcula la recaudación total de este espectáculo, sumando la recaudación de todas sus funciones.
     *
     * @return La recaudación total del espectáculo.
     */
    public double calcularRecaudacionTotal() {
        double recaudacionTotal = 0.0;
        // Uso de for-each para recorrer las funciones
        for (Funcion funcion : funciones.values()) {
            recaudacionTotal += funcion.calcularRecaudacion();
        }
        return recaudacionTotal;
    }

    /**
     * Lista todas las entradas vendidas para todas las funciones de este espectáculo.
     *
     * @return Una lista de objetos IEntrada que representan todas las entradas vendidas.
     */
    public List<IEntrada> listarEntradasVendidas() {
        List<IEntrada> todasLasEntradas = new ArrayList<>();
        // Uso de for-each para recorrer las funciones y sus entradas
        for (Funcion funcion : funciones.values()) {
            todasLasEntradas.addAll(funcion.getEntradasVendidas());
        }
        return todasLasEntradas;
    }

    // --- Getters ---

    public String getNombre() {
        return nombre;
    }

    /**
     * Retorna un mapa inmutable de las funciones para evitar modificaciones externas.
     *
     * @return Un mapa de solo lectura de las funciones.
     */
    public Map<LocalDate, Funcion> getFunciones() {
        // Devuelve una copia defensiva del mapa para evitar modificaciones externas directas.
        return new HashMap<>(funciones);
    }

    /**
     * Representación en cadena del objeto Espectaculo.
     * Muestra el nombre, la cantidad de funciones y la recaudación total.
     *
     * @return Una cadena que describe el espectáculo.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Espectáculo: ").append(nombre);
        sb.append(" | Funciones: ").append(funciones.size());
        sb.append(" | Recaudación Total: $").append(String.format("%.2f", calcularRecaudacionTotal()));
        
        if (!funciones.isEmpty()) {
            sb.append("\n  Detalle de Funciones:");
            // Uso de iterador implícito con for-each para mayor claridad
            for (Map.Entry<LocalDate, Funcion> entry : funciones.entrySet()) {
                sb.append("\n    - Fecha: ").append(entry.getKey());
                sb.append(", Sede: ").append(entry.getValue().getSede().getNombre());
                sb.append(", Entradas Vendidas: ").append(entry.getValue().getEntradasVendidas().size());
                sb.append(", Recaudación: $").append(String.format("%.2f", entry.getValue().calcularRecaudacion()));
            }
        }
        return sb.toString();
    }

    /**
     * Compara este espectáculo con otro objeto para determinar si son iguales.
     * La igualdad se basa únicamente en el nombre del espectáculo.
     *
     * @param o El objeto a comparar.
     * @return true si el objeto es un Espectaculo y tiene el mismo nombre, false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Espectaculo that = (Espectaculo) o;
        return Objects.equals(nombre, that.nombre);
    }

    /**
     * Genera un valor hash para este espectáculo, basado en el nombre.
     *
     * @return Un valor hash entero.
     */
    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}