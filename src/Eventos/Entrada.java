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
    
    @Override
    public double precio() {
        return funcion.sede.obtenerPrecioBase(funcion, sector);
    }

	@Override
	public String ubicacion() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("- ")
	      .append(funcion.fecha)
	      .append(" - ")
	      .append(funcion.espectaculo.nombre)
	      .append(" - ")
	      .append(funcion.sede.nombre)
	      .append(" - ");

	    if (tipo.equalsIgnoreCase("CAMPO")) {
	        sb.append("CAMPO");
	    } else {
	        sb.append(sector.toUpperCase());
	        if (asientos != null && asientos.length > 0) {
	            sb.append(" - Asientos: ");
	            for (int i = 0; i < asientos.length; i++) {
	                sb.append(asientos[i]);
	                if (i < asientos.length - 1) sb.append(", ");
	            }
	        }
	    }

	    return sb.toString();
	}

	}
