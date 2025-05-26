package Eventos;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.HashMap; // Make sure HashMap is imported

public class Teatro extends Sede {
    private String[] sectores;
    private int[] capacidadPorSector;
    private int[] porcentajeIncremento;

    public Teatro(String nombre, String direccion, int capacidadTotal, int asientosPorFila, String[] sectores, int[] capacidadPorSector, int[] porcentajeIncremento) {
        super(nombre, direccion, capacidadTotal);
        this.asientosPorFila = asientosPorFila; 
        this.sectores = sectores;
        this.capacidadPorSector = capacidadPorSector;
        this.porcentajeIncremento = porcentajeIncremento;
    }

    
    public int obtenerCapacidadPorSector(String sector) {
        for (int i = 0; i < sectores.length; i++) {
            if (sectores[i].equals(sector)) {
                return capacidadPorSector[i];
            }
        }
        throw new RuntimeException("Sector '" + sector + "' no encontrado en el teatro " + nombre);
    }

   
    public String[] obtenerSectores() {
        return sectores;
    }

   
    public int obtenerPorcentajeIncremento(String sector) {
        for (int i = 0; i < sectores.length; i++) {
            if (sectores[i].equals(sector)) {
                return porcentajeIncremento[i];
            }
        }
        throw new RuntimeException("Sector '" + sector + "' no encontrado en el teatro " + nombre);
    }

    @Override
    public double obtenerPrecioBase(Funcion funcion, String sector) {
        for (int i = 0; i < sectores.length; i++) {
            if (sectores[i].equals(sector)) {
                return funcion.getPrecioBase() * (1 + porcentajeIncremento[i] / 100.0);
            }
        }
        throw new RuntimeException("Sector '" + sector + "' no válido para la función en " + nombre);
    }
    
    @Override
    public Entrada venderEntrada(Funcion funcion, Usuario usuario, String sector, int asiento) {
        if (funcion == null) throw new IllegalArgumentException("Función no puede ser nula.");
        if (usuario == null) throw new IllegalArgumentException("Usuario no puede ser nulo.");
        if (sector == null || sector.isEmpty()) throw new IllegalArgumentException("Sector inválido.");
        if (asiento <= 0) throw new IllegalArgumentException("Número de asiento inválido.");

      
        int capacidadMaximaSector = 0;
        boolean sectorEncontrado = false;
        for (int i = 0; i < sectores.length; i++) {
            if (sectores[i].equals(sector)) {
                capacidadMaximaSector = capacidadPorSector[i];
                sectorEncontrado = true;
                break;
            }
        }

        if (!sectorEncontrado) {
            throw new RuntimeException("El sector '" + sector + "' no existe en este teatro.");
        }

       
        if (estaAsientoOcupado(funcion, sector, asiento)) {
            throw new RuntimeException("El asiento " + asiento + " en el sector " + sector + " ya está ocupado para esta función.");
        }
        
       
        if (getAsientosOcupadosEnSector(funcion, sector) >= capacidadMaximaSector) {
            throw new RuntimeException("El sector " + sector + " ha alcanzado su capacidad máxima.");
        }

        marcarAsientoOcupado(funcion, sector, asiento);

       
        return new Entrada(funcion, usuario, sector, asiento);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString()); 
        sb.append(" | Tipo: Teatro")
          .append(" | Asientos por fila: ").append(asientosPorFila)
          .append(" | Sectores: ");
        for (int i = 0; i < sectores.length; i++) {
            sb.append(sectores[i]);
            if (i < sectores.length - 1) sb.append(", ");
        }
        return sb.toString();
    }
}