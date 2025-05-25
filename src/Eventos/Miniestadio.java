package Eventos;

// La clase Miniestadio extiende Teatro, por lo que hereda sus propiedades y métodos.
// Esto permite aplicar un costo adicional a los precios base definidos en Teatro.
class Miniestadio extends Teatro {
    private double costoAdicional; // El costo adicional fijo para todas las entradas de este miniestadio

    /**
     * Constructor para la clase Miniestadio.
     *
     * @param nombre             El nombre del miniestadio.
     * @param direccion          La dirección del miniestadio.
     * @param capacidadTotal     La capacidad total de público del miniestadio.
     * @param asientosPorFila    El número de asientos por fila en el miniestadio.
     * @param sectores           Un array de nombres de los sectores del miniestadio (ej: "Platea", "Popular").
     * @param capacidadPorSector Un array con la capacidad de cada sector, en el mismo orden que 'sectores'.
     * @param porcentajeIncremento Un array con el porcentaje de incremento de precio por cada sector.
     * @param costoAdicional     El costo adicional fijo que se aplica a cada entrada en este miniestadio.
     */
    Miniestadio(String nombre, String direccion, int capacidadTotal, int asientosPorFila,
                String[] sectores, int[] capacidadPorSector, int[] porcentajeIncremento, double costoAdicional) {
        // Llama al constructor de la clase padre (Teatro) con los parámetros correspondientes.
        super(nombre, direccion, capacidadTotal, asientosPorFila, sectores, capacidadPorSector, porcentajeIncremento);
        this.costoAdicional = costoAdicional;
    }

    /**
     * Sobreescribe el método obtenerPrecioBase de la clase Teatro para añadir un costo adicional.
     * Calcula el precio base del sector usando el método de la superclase y le suma un costo fijo.
     *
     * @param funcion La función para la cual se está calculando el precio.
     * @param sector  El nombre del sector para el cual se quiere obtener el precio base.
     * @return El precio base de la entrada para la función y sector dados, más el costo adicional del miniestadio.
     */
    @Override
    public double obtenerPrecioBase(Funcion funcion, String sector) {
        // Llama al método obtenerPrecioBase de la superclase (Teatro)
        // y le suma el costo adicional específico de este Miniestadio.
        return super.obtenerPrecioBase(funcion, sector) + costoAdicional;
    }
    
    /**
     * Devuelve una representación en cadena del objeto Miniestadio.
     * Incluye la información básica y el costo adicional.
     *
     * @return Una cadena que describe el miniestadio.
     */
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