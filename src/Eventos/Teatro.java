package Eventos;

class Teatro extends Sede {
    String[] sectores;
    int[] capacidadPorSector;
    int[] porcentajeIncremento;

    Teatro(String nombre, String direccion, int capacidadTotal, int asientosPorFila, String[] sectores, int[] capacidadPorSector, int[] porcentajeIncremento) {
        super(nombre, direccion, capacidadTotal);
        this.asientosPorFila = asientosPorFila;
        this.sectores = sectores;
        this.capacidadPorSector = capacidadPorSector;
        this.porcentajeIncremento = porcentajeIncremento;
    }

    double obtenerPrecioBase(Funcion funcion, String sector) {
        for (int i = 0; i < sectores.length; i++) {
            if (sectores[i].equals(sector)) {
                return funcion.precioBase * (1 + porcentajeIncremento[i] / 100.0);
            }
        }
        throw new RuntimeException("Sector no encontrado");
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Teatro: ").append(nombre)
          .append(" | Dirección: ").append(direccion)
          .append(" | Capacidad: ").append(capacidadTotal)
          .append(" | Asientos por fila: ").append(asientosPorFila)
          .append(" | Sectores: ");
        for (int i = 0; i < sectores.length; i++) {
            sb.append(sectores[i]);
            if (i < sectores.length - 1) sb.append(", ");
        }
        return sb.toString();
    }

}
