
package Eventos;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Sede {
    protected String nombre;
    protected String direccion;
    protected int capacidadTotal;
    protected int asientosPorFila; // Might be 0 or irrelevant for Estadio
    // Map to store occupied seats for each function in this venue
    // Key: Funcion object, Value: Map of sector -> Set of occupied seat numbers
    protected Map<Funcion, Map<String, Set<Integer>>> asientosOcupadosPorFuncion;

    public Sede(String nombre, String direccion, int capacidadTotal) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.capacidadTotal = capacidadTotal;
        this.asientosOcupadosPorFuncion = new HashMap<>();
    }

    // Abstract methods to be implemented by concrete Sede types
    public abstract double obtenerPrecioBase(Funcion funcion, String sector);
    public abstract Entrada venderEntrada(Funcion funcion, Usuario usuario, String sector, int valorAsientoOCantidad);
    
    // Getters for common properties
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public int getCapacidadTotal() { return capacidadTotal; }
    public int getAsientosPorFila() { return asientosPorFila; } // Concrete implementation for common field

    // Method to mark/unmark seat as occupied (could be used by vender/anular)
    protected void marcarAsientoOcupado(Funcion funcion, String sector, int asiento) {
        asientosOcupadosPorFuncion.computeIfAbsent(funcion, k -> new HashMap<>())
                                 .computeIfAbsent(sector, k -> new HashSet<>())
                                 .add(asiento);
    }

    protected void desmarcarAsientoOcupado(Funcion funcion, String sector, int asiento) {
        Map<String, Set<Integer>> ocupadosPorSector = asientosOcupadosPorFuncion.get(funcion);
        if (ocupadosPorSector != null) {
            Set<Integer> asientosEnSector = ocupadosPorSector.get(sector);
            if (asientosEnSector != null) {
                asientosEnSector.remove(asiento);
                if (asientosEnSector.isEmpty()) {
                    ocupadosPorSector.remove(sector);
                }
            }
            if (ocupadosPorSector.isEmpty()) {
                asientosOcupadosPorFuncion.remove(funcion);
            }
        }
    }
    
    // Check if a seat is occupied
    public boolean estaAsientoOcupado(Funcion funcion, String sector, int asiento) {
        Map<String, Set<Integer>> ocupadosPorSector = asientosOcupadosPorFuncion.get(funcion);
        return ocupadosPorSector != null && ocupadosPorSector.get(sector) != null && ocupadosPorSector.get(sector).contains(asiento);
    }

    // Get count of occupied seats for a given function and sector
    public int getAsientosOcupadosEnSector(Funcion funcion, String sector) {
        Map<String, Set<Integer>> ocupadosPorSector = asientosOcupadosPorFuncion.get(funcion);
        if (ocupadosPorSector != null && ocupadosPorSector.containsKey(sector)) {
            return ocupadosPorSector.get(sector).size();
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Sede: " + nombre + " | Dirección: " + direccion + " | Capacidad Total: " + capacidadTotal;
    }
}