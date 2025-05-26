package Eventos;

import java.util.*;

public class Estadio extends Sede {
    private final String CAMPO_SECTOR_NAME = "CAMPO";

    // Cantidad total de entradas vendidas por función
    private Map<Funcion, Integer> cantidadVendidaPorFuncion = new HashMap<>();

    // Contador para asignar número único de entrada por función
    private Map<Funcion, Integer> numeroEntradaPorFuncion = new HashMap<>();

    public Estadio(String nombre, String direccion, int capacidadTotal) {
        super(nombre, direccion, capacidadTotal);
        this.asientosPorFila = 0; // no aplica para campo
    }

    @Override
    public double obtenerPrecioBase(Funcion funcion, String sector) {
        if (!CAMPO_SECTOR_NAME.equals(sector)) {
            throw new RuntimeException("Solo se puede vender en el sector 'CAMPO' para este Estadio.");
        }
        return funcion.getPrecioBase(); // precio base directo
    }

    public Entrada venderEntrada(Funcion funcion, Usuario usuario, String sector, int cantidad) {
        if (funcion == null) throw new IllegalArgumentException("Función no puede ser nula.");
        if (usuario == null) throw new IllegalArgumentException("Usuario no puede ser nulo.");
        if (!CAMPO_SECTOR_NAME.equals(sector)) throw new IllegalArgumentException("Sector inválido para Estadio. Debe ser 'CAMPO'.");
        if (cantidad <= 0) throw new IllegalArgumentException("Cantidad de entradas inválida.");

        int vendidas = getEntradasVendidas(funcion);
        if (vendidas + cantidad > capacidadTotal) {
            throw new RuntimeException("Se excede la capacidad del estadio. Capacidad restante: " + (capacidadTotal - vendidas));
        }

        incrementarEntradasVendidas(funcion, cantidad);

        Entrada entradas = new Entrada(funcion, usuario, CAMPO_SECTOR_NAME, obtenerSiguienteNumeroEntrada(funcion));
        return entradas;
    }

    private int getEntradasVendidas(Funcion funcion) {
        return cantidadVendidaPorFuncion.getOrDefault(funcion, 0);
    }

    private void incrementarEntradasVendidas(Funcion funcion, int cantidad) {
        cantidadVendidaPorFuncion.put(funcion, getEntradasVendidas(funcion) + cantidad);
    }

    private int obtenerSiguienteNumeroEntrada(Funcion funcion) {
        int actual = numeroEntradaPorFuncion.getOrDefault(funcion, 0);
        numeroEntradaPorFuncion.put(funcion, actual + 1);
        return actual + 1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" | Tipo: Estadio")
          .append(" | Sector: CAMPO");
        return sb.toString();
    }
}
