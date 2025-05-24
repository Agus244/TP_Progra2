package Eventos;

class Teatro extends Sede {
    String[] sectores;
    int[] capacidadPorSector;
    int[] porcentajeIncremento;

    Teatro(String nombre, String direccion, int capacidadTotal, int asientosPorFila, String[] sectores, int[] capacidadPorSector, int[] porcentajeIncremento) {
        super(nombre, direccion, capacidadTotal);
        this.asientosPorFila = asientosPorFila;
        this.sectores = sectores;
        this.capacidadPorSector = capacidadPorSector;
        this.porcentajeIncremento = porcentajeIncremento;
    }

    double obtenerPrecioBase(Funcion funcion, String sector) {
        for (int i = 0; i < sectores.length; i++) {
            if (sectores[i].equals(sector)) {
                return funcion.precioBase * (1 + porcentajeIncremento[i] / 100.0);
            }
        }
        throw new RuntimeException("Sector no encontrado");
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Teatro: ").append(nombre)
          .append(" | Dirección: ").append(direccion)
          .append(" | Capacidad: ").append(capacidadTotal)
          .append(" | Asientos por fila: ").append(asientosPorFila)
          .append(" | Sectores: ");
        for (int i = 0; i < sectores.length; i++) {
            sb.append(sectores[i]);
            if (i < sectores.length - 1) sb.append(", ");
        }
        return sb.toString();
    }

	@Override
	protected String getNombre() {
		return nombre;
	}

	@Override
	protected int getCapacidadTotal() {
		return capacidadTotal;
	}

	@Override
	protected int getAsientosPorFila() {
		return asientosPorFila;
	}

	@Override
	protected String getDireccion() {
		return direccion;
	}

	@Override
	protected Entrada venderEntrada(Funcion funcion, Usuario usuario, String sector, int asiento) {
		/**
	     * Vende una entrada para una función, usuario, sector y asiento específicos.
	     *
	     * @param funcion La función para la que se vende la entrada.
	     * @param usuario El usuario que compra la entrada.
	     * @param sector El nombre del sector donde se ubica el asiento.
	     * @param asiento El número de asiento dentro del sector.
	     * @return La entrada vendida.
	     * @throws IllegalArgumentException Si los datos de entrada son inválidos.
	     * @throws RuntimeException Si el sector no existe, el asiento ya está ocupado o se excede la capacidad.
	     */
			if (funcion == null) throw new IllegalArgumentException("Función no puede ser nula.");
	        if (usuario == null) throw new IllegalArgumentException("Usuario no puede ser nulo.");
	        if (sector == null || sector.isEmpty()) throw new IllegalArgumentException("Sector inválido.");
	        if (asiento <= 0) throw new IllegalArgumentException("Número de asiento inválido.");

	        // Obtener o inicializar el mapa de asientos ocupados para esta función
	        Map<String, Set<Integer>> ocupadosPorSector = asientosOcupadosPorFuncion.computeIfAbsent(funcion, k -> new HashMap<>());
	        
	        // Obtener o inicializar el conjunto de asientos ocupados para este sector
	        Set<Integer> asientosOcupadosEnSector = ocupadosPorSector.computeIfAbsent(sector, k -> new HashSet<>());

	        // Verificar si el sector existe y obtener su capacidad
	        int indiceSector = -1;
	        int capacidadMaximaSector = 0;
	        for (int i = 0; i < sectores.length; i++) {
	            if (sectores[i].equals(sector)) {
	                indiceSector = i;
	                capacidadMaximaSector = capacidadPorSector[i];
	                break;
	            }
	        }

	        if (indiceSector == -1) {
	            throw new RuntimeException("El sector '" + sector + "' no existe en este teatro.");
	        }

	        // Verificar si el asiento ya está ocupado
	        if (asientosOcupadosEnSector.contains(asiento)) {
	            throw new RuntimeException("El asiento " + asiento + " en el sector " + sector + " ya está ocupado para esta función.");
	        }
	        
	        // Verificar si se excede la capacidad del sector
	        if (asientosOcupadosEnSector.size() >= capacidadMaximaSector) {
	            throw new RuntimeException("El sector " + sector + " ha alcanzado su capacidad máxima.");
	        }

	        // Marcar el asiento como ocupado
	        asientosOcupadosEnSector.add(asiento);

	        // Calcular el costo total de la entrada
	        double costoTotal = obtenerPrecioBase(funcion, sector);

	        // Crear y retornar la entrada
	        return new Entrada(funcion, usuario, costoTotal, sector, asiento);
		

	}

}
