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



    protected abstract String getNombre();
	
	protected abstract int getCapacidadTotal();
	
	protected abstract int getAsientosPorFila();
	
	protected abstract String getDireccion();



	protected abstract Entrada venderEntrada(Funcion funcion, Usuario usuario, String sector, int asiento);
	
}