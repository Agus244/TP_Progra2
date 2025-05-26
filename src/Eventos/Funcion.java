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

    
    public void agregarEntrada(Entrada entrada) {
        if (entrada == null) {
            throw new IllegalArgumentException("La entrada a agregar no puede ser nula.");
        }
        this.entradasVendidas.add(entrada);
    }

   
    public boolean eliminarEntrada(Entrada entrada) {
        if (entrada == null) {
            throw new IllegalArgumentException("La entrada a eliminar no puede ser nula.");
        }
        return this.entradasVendidas.remove(entrada);
    }

    
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

   
    public int getCantidadEntradasVendidas() {
        return entradasVendidas.size();
    }

    
    public int getEntradasVendidasPorSector(String sector) {
        int count = 0;
        for (Entrada entrada : entradasVendidas) {
            if (entrada.getSector().equalsIgnoreCase(sector) && !entrada.estaAnulada()) {
                count++;
            }
        }
        return count;
    }

   
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

   
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Funcion funcion = (Funcion) o;
        // Dos funciones son iguales si su espectáculo y fecha son iguales.
        return Objects.equals(espectaculo, funcion.espectaculo) &&
               Objects.equals(fecha, funcion.fecha);
    }

  
    @Override
    public int hashCode() {
        return Objects.hash(espectaculo, fecha);
    }
}