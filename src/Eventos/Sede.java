
package Eventos;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Sede {
    protected String nombre;
    protected String direccion;
    protected int capacidadTotal;
    protected int asientosPorFila; 
   
    protected Map<Funcion, Map<String, Set<Integer>>> asientosOcupadosPorFuncion;

    public Sede(String nombre, String direccion, int capacidadTotal) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.capacidadTotal = capacidadTotal;
        this.asientosOcupadosPorFuncion = new HashMap<>();
    }

  
    public abstract double obtenerPrecioBase(Funcion funcion, String sector);
    public abstract Entrada venderEntrada(Funcion funcion, Usuario usuario, String sector, int valorAsientoOCantidad);
    
   
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public int getCapacidadTotal() { return capacidadTotal; }
    public int getAsientosPorFila() { return asientosPorFila; } 

   
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
    
   
    public boolean estaAsientoOcupado(Funcion funcion, String sector, int asiento) {
        Map<String, Set<Integer>> ocupadosPorSector = asientosOcupadosPorFuncion.get(funcion);
        return ocupadosPorSector != null && ocupadosPorSector.get(sector) != null && ocupadosPorSector.get(sector).contains(asiento);
    }

   
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