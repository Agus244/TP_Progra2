package Eventos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects; 
public class Espectaculo {
    private String nombre; // Nombre único del espectáculo
    // Mapa para almacenar las funciones del espectáculo, la clave es la fecha de la función.
    private Map<LocalDate, Funcion> funciones;

    
    public Espectaculo(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del espectáculo no puede ser nulo o vacío.");
        }
        this.nombre = nombre;
        this.funciones = new HashMap<>(); // Se inicializa un mapa vacío para las funciones
    }

    
    public void agregarFuncion(Funcion funcion) {
        if (funcion == null) {
            throw new IllegalArgumentException("La función a agregar no puede ser nula.");
        }
        if (funciones.containsKey(funcion.getFecha())) {
            throw new IllegalArgumentException("Ya existe una función para este espectáculo en la fecha: " + funcion.getFecha());
        }
        funciones.put(funcion.getFecha(), funcion);
    }

    
    public Funcion getFuncion(LocalDate fecha) {
        return funciones.get(fecha);
    }

  
    public double calcularRecaudacionTotal() {
        double recaudacionTotal = 0.0;
        // Uso de for-each para recorrer las funciones
        for (Funcion funcion : funciones.values()) {
            recaudacionTotal += funcion.calcularRecaudacion();
        }
        return recaudacionTotal;
    }

    
    public List<IEntrada> listarEntradasVendidas() {
        List<IEntrada> todasLasEntradas = new ArrayList<>();
        // Uso de for-each para recorrer las funciones y sus entradas
        for (Funcion funcion : funciones.values()) {
            todasLasEntradas.addAll(funcion.getEntradasVendidas());
        }
        return todasLasEntradas;
    }

  

    public String getNombre() {
        return nombre;
    }

    
    public Map<LocalDate, Funcion> getFunciones() {
        // Devuelve una copia defensiva del mapa para evitar modificaciones externas directas.
        return new HashMap<>(funciones);
    }

    
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

   
     
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Espectaculo that = (Espectaculo) o;
        return Objects.equals(nombre, that.nombre);
    }

   
    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}