package Eventos;

class Estadio extends Sede {
    Estadio(String nombre, String direccion, int capacidadTotal) {
        super(nombre, direccion, capacidadTotal);
    }

    double obtenerPrecioBase(Funcion funcion, String sector) {
        return funcion.precioBase;
    }
    
    @Override
    public String toString() {
        return "Estadio: " + nombre + " | Dirección: " + direccion + " | Capacidad: " + capacidadTotal;
    }

	@Override
	protected String getNombre() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int getCapacidadTotal() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getAsientosPorFila() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected String getDireccion() {
		// TODO Auto-generated method stub
		return null;
	}

	protected Entrada venderEntrada(Funcion funcion, Usuario usuario, int[] asiento) {
		if (funcion == null) throw new IllegalArgumentException("Función no puede ser nula.");
        if (usuario == null) throw new IllegalArgumentException("Usuario no puede ser nulo.");
        
        // Calcular el costo total de la entrada
        double costoTotal = obtenerPrecioBase(funcion, "CAMPO");

        // Crear y retornar la entrada
        return new Entrada(funcion, usuario.email,"CAMPO", asiento, "CAMPO");
	}

}