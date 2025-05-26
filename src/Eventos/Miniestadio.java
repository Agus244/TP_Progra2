package Eventos;

class Miniestadio extends Teatro {
    private double costoAdicional; // El costo adicional fijo para todas las entradas de este miniestadio

    
    Miniestadio(String nombre, String direccion, int capacidadTotal, int asientosPorFila,
                String[] sectores, int[] capacidadPorSector, int[] porcentajeIncremento, double costoAdicional) {
        // Llama al constructor de la clase padre (Teatro) con los parámetros correspondientes.
        super(nombre, direccion, capacidadTotal, asientosPorFila, sectores, capacidadPorSector, porcentajeIncremento);
        this.costoAdicional = costoAdicional;
    }

    
    @Override
    public double obtenerPrecioBase(Funcion funcion, String sector) {
        // Llama al método obtenerPrecioBase de la superclase (Teatro)
        // y le suma el costo adicional específico de este Miniestadio.
        return super.obtenerPrecioBase(funcion, sector) + costoAdicional;
    }
    
    
    @Override
    public String toString() {
        // Usa el toString de la superclase (Teatro) para obtener la información común
        // y luego añade la información específica de Miniestadio.
        StringBuilder sb = new StringBuilder(super.toString());
        sb.insert(0, "Miniestadio: "); // Cambia el prefijo "Teatro: " a "Miniestadio: "
        sb.append(" | Costo adicional: $").append(String.format("%.2f", costoAdicional));
        return sb.toString();
    }

    // Nuevo método para obtener el costo adicional, si es necesario consultarlo.
    public double getCostoAdicional() {
        return costoAdicional;
    }
}