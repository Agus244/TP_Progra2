package Eventos;

abstract class Sede {
    String nombre, direccion;
    int capacidadTotal, asientosPorFila;

    Sede(String nombre, String direccion, int capacidadTotal) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.capacidadTotal = capacidadTotal;
    }

    abstract double obtenerPrecioBase(Funcion funcion, String sector);
}