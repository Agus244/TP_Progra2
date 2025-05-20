package Eventos;

class Entrada implements IEntrada {
    Funcion funcion;
    String email;
    String sector;
    int[] asientos;
    String tipo;
    boolean anulada;
    boolean fueUsada;

    Entrada(Funcion funcion, String email, String sector, int[] asientos, String tipo) {
        this.funcion = funcion;
        this.email = email;
        this.sector = sector;
        this.asientos = asientos;
        this.tipo = tipo;
        this.anulada = false;
        this.fueUsada = false;
    }

    public double precio() {
        return funcion.sede.obtenerPrecioBase(funcion, sector);
    }

    public String toString() {
        return "- " + funcion.toString() + " - " + sector.toUpperCase() + " - " + tipo.toUpperCase();
    }

	@Override
	public String ubicacion() {
		// TODO Auto-generated method stub
		return null;
	}

	}
