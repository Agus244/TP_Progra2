package Eventos;

class Miniestadio extends Teatro {
    double costoAdicional;

    Miniestadio(String nombre, String direccion, int capacidadTotal, int asientosPorFila, int cantidadFilas, double costoAdicional, String[] sectores, int[] capacidadPorSector, int[] porcentajeIncremento) {
        super(nombre, direccion, capacidadTotal, asientosPorFila, sectores, capacidadPorSector, porcentajeIncremento);
        this.costoAdicional = costoAdicional;
    }

    double obtenerPrecioBase(Funcion funcion, String sector) {
        return super.obtenerPrecioBase(funcion, sector) + costoAdicional;
    }
}