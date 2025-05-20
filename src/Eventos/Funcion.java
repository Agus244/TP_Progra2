package Eventos;

class Funcion {
    String fecha;
    double precioBase;
    Sede sede;
    Espectaculo espectaculo;

    Funcion(String fecha, double precioBase, Sede sede, Espectaculo espectaculo) {
        this.fecha = fecha;
        this.precioBase = precioBase;
        this.sede = sede;
        this.espectaculo = espectaculo;
    }
    
    @Override
    public String toString() {
        return "(" + fecha + ") " + sede.nombre + " - " + espectaculo.nombre;
    }

}
