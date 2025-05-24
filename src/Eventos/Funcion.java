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
    
	public Sede getSede() {
		// TODO Auto-generated method stub
		return sede;
	}
	
	public double getPrecioBase() {
		// TODO Auto-generated method stub
		return precioBase;
	}
	
	public String getFecha() {
		// TODO Auto-generated method stub
		return fecha;
	}
	
	public Espectaculo getEspectaculo() {
		// TODO Auto-generated method stub
		return espectaculo;
	}
    
    @Override
    public String toString() {
        return "(" + fecha + ") " + sede.nombre + " - " + espectaculo.nombre;
    }



}
