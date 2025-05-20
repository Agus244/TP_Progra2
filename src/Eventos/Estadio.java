package Eventos;

class Estadio extends Sede {
    Estadio(String nombre, String direccion, int capacidadTotal) {
        super(nombre, direccion, capacidadTotal);
    }

    double obtenerPrecioBase(Funcion funcion, String sector) {
        return funcion.precioBase;
    }
}