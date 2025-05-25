// Eventos/Estadio.java
package Eventos;

import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

public class Estadio extends Sede {
    // Estadio implicitly has a single "CAMPO" sector.
    private final String CAMPO_SECTOR_NAME = "CAMPO";

    public Estadio(String nombre, String direccion, int capacidadTotal) {
        super(nombre, direccion, capacidadTotal);
        this.asientosPorFila = 0; // Not applicable for stadium's CAMPO
    }

    @Override
    public double obtenerPrecioBase(Funcion funcion, String sector) {
        if (!CAMPO_SECTOR_NAME.equals(sector)) {
            throw new RuntimeException("Solo se puede vender en el sector '" + CAMPO_SECTOR_NAME + "' para este Estadio.");
        }
        return funcion.getPrecioBase(); // No increment for CAMPO
    }

    @Override
    public Entrada venderEntrada(Funcion funcion, Usuario usuario, String sector, int cantidad) {
        if (funcion == null) throw new IllegalArgumentException("Función no puede ser nula.");
        if (usuario == null) throw new IllegalArgumentException("Usuario no puede ser nulo.");
        if (!CAMPO_SECTOR_NAME.equals(sector)) throw new IllegalArgumentException("Sector inválido para Estadio. Debe ser 'CAMPO'.");
        if (cantidad <= 0) throw new IllegalArgumentException("Cantidad de entradas inválida.");        
        	int ticketsVendidosCampo = getAsientosOcupadosEnSector(funcion, CAMPO_SECTOR_NAME);
        	if (ticketsVendidosCampo + cantidad > this.capacidadTotal) {
        		throw new RuntimeException("Se excede la capacidad del sector CAMPO en este Estadio. Capacidad restante: " + (this.capacidadTotal - ticketsVendidosCampo));
        }
        for (int i = 0; i < cantidad; i++) {
            int dummySeatNumber = ticketsVendidosCampo + i + 1;
            marcarAsientoOcupado(funcion, CAMPO_SECTOR_NAME, dummySeatNumber);
        }

        // Calculate the total cost of the ticket (for one ticket)
        double costoTotalUnitario = obtenerPrecioBase(funcion, CAMPO_SECTOR_NAME);
        
      
        return new Entrada(funcion, usuario.getEmail(), costoTotalUnitario, CAMPO_SECTOR_NAME, 1);
    }
    
   
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" | Tipo: Estadio")
          .append(" | Sector: CAMPO");
        return sb.toString();
    }
}