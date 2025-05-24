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

	@Override
	protected Entrada venderEntrada(Funcion funcion, Usuario usuario, String sector, int asiento) {
		// TODO Auto-generated method stub
		return null;
	}

}