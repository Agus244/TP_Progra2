package Eventos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ticketek implements ITicketek {
	
	private Map<String, Usuario> usuarios = new HashMap<>();
    private Map<String, Sede> sedes = new HashMap<>();
    private Map<String, Espectaculo> espectaculos = new HashMap<>();

    // Acceso rápido para operaciones en O(1)
    private Map<IEntrada, Entrada> entradasPorInstancia = new HashMap<>();
    private Map<String, List<Entrada>> entradasPorEspectaculo = new HashMap<>();
    private Map<String, Map<String, List<Entrada>>> entradasPorEspectaculoYFecha = new HashMap<>();
    private Map<String, Map<String, Double>> recaudacionPorSedeYEspectaculo = new HashMap<>();

    //------------------------------------------------------------------COMPLETA-----------------------------------------
	public void registrarSede(String nombre, String direccion, int capacidadMaxima) {	
        if (nombre == null || nombre.isEmpty()) throw new IllegalArgumentException("Nombre de sede inválido");
        if (direccion == null || direccion.isEmpty()) throw new IllegalArgumentException("Dirección inválida");
        if (capacidadMaxima <= 0) throw new IllegalArgumentException("Capacidad inválida");
        if (sedes.containsKey(nombre)) throw new RuntimeException("Sede ya registrada");
        sedes.put(nombre, new Estadio(nombre, direccion, capacidadMaxima));

	}
	
	//------------------------------------------------------------------COMPLETA-----------------------------------------
	@Override
	public void registrarSede(String nombre, String direccion, int capacidadMaxima, int asientosPorFila,
			String[] sectores, int[] capacidad, int[] porcentajeAdicional) {
        if (nombre == null || nombre.isEmpty() || direccion == null || direccion.isEmpty() || capacidadMaxima <= 0 || asientosPorFila <= 0)throw new IllegalArgumentException("Datos de sede inválidos");
        if (sectores == null || capacidad == null || porcentajeAdicional == null ||
            sectores.length != capacidad.length || sectores.length != porcentajeAdicional.length)throw new IllegalArgumentException("Datos de sectores inválidos");
        if (sedes.containsKey(nombre)) throw new RuntimeException("Sede ya registrada");
        sedes.put(nombre, new Teatro(nombre, direccion, capacidadMaxima, asientosPorFila, sectores, capacidad, porcentajeAdicional));
	}
	
	//------------------------------------------------------------------COMPLETA-----------------------------------------
	@Override
	public void registrarSede(String nombre, String direccion, int capacidadMaxima, int asientosPorFila,
			int cantidadPuestos, double precioConsumicion, String[] sectores, int[] capacidad,
			int[] porcentajeAdicional) {
	    if (nombre == null || nombre.isEmpty() || direccion == null || direccion.isEmpty() || capacidadMaxima <= 0 || asientosPorFila <= 0 || cantidadPuestos <= 0)throw new IllegalArgumentException("Datos de sede inválidos");
	    if (sectores == null || capacidad == null || porcentajeAdicional == null || sectores.length != capacidad.length || sectores.length != porcentajeAdicional.length)throw new IllegalArgumentException("Datos de sectores inválidos");
	    if (precioConsumicion < 0) throw new IllegalArgumentException("Precio de consumición inválido");
	    if (sedes.containsKey(nombre)) throw new RuntimeException("Sede ya registrada");
	    sedes.put(nombre, new Miniestadio(nombre, direccion, capacidadMaxima, asientosPorFila, cantidadPuestos, precioConsumicion, sectores, capacidad, porcentajeAdicional));
	}
	
	//------------------------------------------------------------------COMPLETA-----------------------------------------
	@Override
	public void registrarUsuario(String email, String nombre, String apellido, String contrasenia) {
	    if (email == null || email.isEmpty() || !email.contains("@")) throw new IllegalArgumentException("Email inválido");
	    if (nombre == null || nombre.isEmpty()) throw new IllegalArgumentException("Nombre inválido");
	    if (apellido == null || apellido.isEmpty()) throw new IllegalArgumentException("Apellido inválido");
	    if (contrasenia == null || contrasenia.isEmpty()) throw new IllegalArgumentException("Contraseña inválida");
	    if (usuarios.containsKey(email)) throw new RuntimeException("Usuario ya registrado con ese email");

	    usuarios.put(email, new Usuario(email, nombre, apellido, contrasenia));
	}
	
	//------------------------------------------------------------------COMPLETA-----------------------------------------
	@Override
	public void registrarEspectaculo(String nombre) {
		 if (espectaculos.containsKey(nombre)) throw new RuntimeException("Espectáculo ya registrado");
	        espectaculos.put(nombre, new Espectaculo(nombre));
	}
	
	//------------------------------------------------------------------COMPLETA-----------------------------------------
	@Override
	public void agregarFuncion(String nombreEspectaculo, String fecha, String sede, double precioBase) {
		
		if (nombreEspectaculo == null || nombreEspectaculo.isEmpty()) throw new IllegalArgumentException("Nombre de espectáculo inválido");
        if (fecha == null || fecha.isEmpty()) throw new IllegalArgumentException("Fecha inválida");
        if (sede == null || sede.isEmpty()) throw new IllegalArgumentException("Nombre de sede inválido");
        if (precioBase < 0) throw new IllegalArgumentException("Precio base inválido");
        
		Espectaculo e = espectaculos.get(nombreEspectaculo);
        if (e == null) throw new RuntimeException("Espectáculo no encontrado");
        Sede s = sedes.get(sede);
        if (s == null) throw new RuntimeException("Sede no encontrada");

        Funcion f = new Funcion(fecha, precioBase, s, e);
        e.agregarFuncion(f);

	}

	@Override
	public List<IEntrada> venderEntrada(String nombreEspectaculo, String fecha, String email, String contrasenia, int cantidadEntradas) {
		
		//COMPROBAR DATOS
		    if (nombreEspectaculo == null || nombreEspectaculo.isEmpty()) throw new IllegalArgumentException("Nombre de espectáculo inválido");
		    if (fecha == null || fecha.isEmpty()) throw new IllegalArgumentException("Fecha inválida");
		    if (email == null || email.isEmpty() || contrasenia == null || contrasenia.isEmpty()) throw new IllegalArgumentException("Datos de usuario inválidos");
		    if (cantidadEntradas <= 0) throw new IllegalArgumentException("Cantidad de entradas inválida");
	
		    Usuario usuario = usuarios.get(email);
		    if (usuario == null || !usuario.validarContrasena(contrasenia)) throw new RuntimeException("Usuario o contraseña incorrectos");
	
		    Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
		    if (espectaculo == null) throw new RuntimeException("Espectáculo no encontrado");
	
		    Funcion funcion = espectaculo.getFuncion(fecha);
		    if (funcion == null) throw new RuntimeException("Función no encontrada para la fecha especificada");
	
		    Sede sede = funcion.getSede();
		    if (sede == null) throw new RuntimeException("Sede asociada a la función no encontrada");

	    List<IEntrada> nuevasEntradas = new ArrayList<>();
	    try {
	        for (int i = 0; i < cantidadEntradas; i++) {
	            // Asume que la sede tiene un método para vender entradas por cantidad,
	            // si la sede soporta diferentes tipos de entradas (ej: con o sin sector),
	            // esta lógica debería ser más sofisticada para elegir el tipo de entrada correcto.
	            // Para simplificar, asumiremos que se vende una entrada "general" o por la capacidad total.
	            Entrada entrada = sede.venderEntradaGeneral(funcion, usuario);
	            nuevasEntradas.add(entrada);
	            entradasPorInstancia.put(entrada, entrada); // Almacena por instancia
	            entradasPorEspectaculo.computeIfAbsent(nombreEspectaculo, k -> new ArrayList<>()).add(entrada);
	            entradasPorEspectaculoYFecha.computeIfAbsent(nombreEspectaculo, k -> new HashMap<>())
	                                         .computeIfAbsent(fecha, k -> new ArrayList<>())
	                                         .add(entrada);
	            usuario.agregarEntrada(entrada);
	            
	            // Actualizar la recaudación
	            double costoEntrada = entrada.precio();
	            recaudacionPorSedeYEspectaculo.computeIfAbsent(sede.getNombre(), k -> new HashMap<>())
	                                          .merge(nombreEspectaculo, costoEntrada, Double::sum);
	        }
	        return nuevasEntradas;
	    } catch (RuntimeException e) {
	        // En caso de error (ej: capacidad agotada), se pueden revertir las ventas parciales si es necesario.
	        // Por simplicidad, aquí solo relanzamos la excepción.
	        throw new RuntimeException("Error al vender entradas: " + e.getMessage());
	    }
	}
	
	
	@Override
	public List<IEntrada> venderEntrada(String nombreEspectaculo, String fecha, String email, String contrasenia, int cantidadEntradas) {
	    if (nombreEspectaculo == null || nombreEspectaculo.isEmpty()) throw new IllegalArgumentException("Nombre de espectáculo inválido");
	    if (fecha == null || fecha.isEmpty()) throw new IllegalArgumentException("Fecha inválida");
	    if (email == null || email.isEmpty() || contrasenia == null || contrasenia.isEmpty()) throw new IllegalArgumentException("Credenciales de usuario inválidas");
	    if (cantidadEntradas <= 0) throw new IllegalArgumentException("Cantidad de entradas inválida");

	    Usuario usuario = usuarios.get(email);
	    if (usuario == null || !usuario.validarContrasena(contrasenia)) throw new RuntimeException("Usuario o contraseña incorrectos");

	    Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
	    if (espectaculo == null) throw new RuntimeException("Espectáculo no encontrado");

	    Funcion funcion = espectaculo.getFuncion(fecha);
	    if (funcion == null) throw new RuntimeException("Función no encontrada para la fecha especificada");

	    Sede sede = funcion.getSede();
	    if (sede == null) throw new RuntimeException("Sede asociada a la función no encontrada");

	    List<IEntrada> nuevasEntradas = new ArrayList<>();
	    try {
	        for (int i = 0; i < cantidadEntradas; i++) {
	            // Asume que la sede tiene un método para vender entradas por cantidad,
	            // si la sede soporta diferentes tipos de entradas (ej: con o sin sector),
	            // esta lógica debería ser más sofisticada para elegir el tipo de entrada correcto.
	            // Para simplificar, asumiremos que se vende una entrada "general" o por la capacidad total.
	            Entrada entrada = sede.venderEntradaGeneral(funcion, usuario);
	            nuevasEntradas.add(entrada);
	            entradasPorInstancia.put(entrada, entrada); // Almacena por instancia
	            entradasPorEspectaculo.computeIfAbsent(nombreEspectaculo, k -> new ArrayList<>()).add(entrada);
	            entradasPorEspectaculoYFecha.computeIfAbsent(nombreEspectaculo, k -> new HashMap<>())
	                                         .computeIfAbsent(fecha, k -> new ArrayList<>())
	                                         .add(entrada);
	            usuario.agregarEntrada(entrada);
	            
	            // Actualizar la recaudación
	            double costoEntrada = entrada.getCostoTotal(); 
	            recaudacionPorSedeYEspectaculo.computeIfAbsent(sede.getNombre(), k -> new HashMap<>())
	                                          .merge(nombreEspectaculo, costoEntrada, Double::sum);
	        }
	        return nuevasEntradas;
	    } catch (RuntimeException e) {
	        // En caso de error (ej: capacidad agotada), se pueden revertir las ventas parciales si es necesario.
	        // Por simplicidad, aquí solo relanzamos la excepción.
	        throw new RuntimeException("Error al vender entradas: " + e.getMessage());
	    }
	}
	
	//------------------------------------------------------------------COMPLETA-----------------------------------------
	@Override
	public String listarFunciones(String nombreEspectaculo) {
		
		if (nombreEspectaculo == null || nombreEspectaculo.isEmpty()) throw new IllegalArgumentException("Nombre de espectáculo inválido");
		
        if (!espectaculos.containsKey(nombreEspectaculo)) throw new RuntimeException("Espectáculo no encontrado");
        
        return espectaculos.get(nombreEspectaculo).toString();
	}
	
	//------------------------------------------------------------------COMPLETA-----------------------------------------
	@Override
	public List<IEntrada> listarEntradasEspectaculo(String nombreEspectaculo) {
			if (nombreEspectaculo == null || nombreEspectaculo.isEmpty()) throw new IllegalArgumentException("Nombre de espectáculo inválido");
	        if (!entradasPorEspectaculo.containsKey(nombreEspectaculo)) return new ArrayList<>();
	        return new ArrayList<>(entradasPorEspectaculo.get(nombreEspectaculo));
	}
	
	//------------------------------------------------------------------INCOMPLETA-----------------------------------------
	@Override
	public List<IEntrada> listarEntradasFuturas(String email, String contrasenia) {
        if (email == null || contrasenia == null || email.isEmpty() || contrasenia.isEmpty())throw new IllegalArgumentException("Datos inválidos");
        Usuario usuario = usuarios.get(email);
        if (usuario == null || !usuario.validarContrasena(contrasenia))throw new RuntimeException("Usuario o contraseña inválidos");
        List<IEntrada> futuras = new ArrayList<>();
        for (IEntrada e : usuario.entradas) {
            Entrada ent = (Entrada) e;
            if (!ent.anulada) {//No se si esta bien esta poronga <-----------------------
                futuras.add(e);
            }
        }
        return futuras;
	}

	//------------------------------------------------------------------COMPLETA-----------------------------------------
	@Override
	public List<IEntrada> listarTodasLasEntradasDelUsuario(String email, String contrasenia) {
		if (email == null || contrasenia == null || email.isEmpty() || contrasenia.isEmpty())throw new IllegalArgumentException("Datos inválidos");
        Usuario usuario = usuarios.get(email);
        if (usuario == null || !usuario.validarContrasena(contrasenia))throw new RuntimeException("Usuario o contraseña inválidos");
        return new ArrayList<>(usuario.entradas);
	}

	@Override
	public boolean anularEntrada(IEntrada entrada, String contrasenia) {
	    if (entrada == null) throw new IllegalArgumentException("Entrada inválida");
	    if (contrasenia == null || contrasenia.isEmpty()) throw new IllegalArgumentException("Contraseña inválida");

	    Entrada ent = entradasPorInstancia.get(entrada);
	    if (ent == null || ent.anulada) return false; // La entrada no existe o ya está anulada

	    Usuario usuario = usuarios.get(ent.getUsuario().getEmail());
	    if (usuario == null || !usuario.validarContrasena(contrasenia)) throw new RuntimeException("Contraseña incorrecta para el usuario de la entrada");

	    // Lógica para anular la entrada en la sede
	    Sede sede = ent.getFuncion().getSede();
	    boolean anuladaEnSede = sede.anularEntrada(ent); // Asume que la sede maneja la disponibilidad de asientos
	    
	    if (anuladaEnSede) {
	        ent.anular(); // Marca la entrada como anulada
	        // Opcional: remover de las listas de acceso rápido si la lógica de negocio lo requiere,
	        // pero generalmente se mantienen con el flag 'anulada' para historial.
	        // Por ejemplo, para las listas de entradas futuras, se filtraría por 'anulada == false'.
	        
	        // Descontar de la recaudación
	        double costoEntrada = ent.getCostoTotal();
	        String nombreEspectaculo = ent.getFuncion().getEspectaculo().getNombre();
	        String nombreSede = sede.getNombre();

	        recaudacionPorSedeYEspectaculo.computeIfPresent(nombreSede, (s, espectaculosRec) -> {
	            espectaculosRec.computeIfPresent(nombreEspectaculo, (e, recaudado) -> recaudado - costoEntrada);
	            return espectaculosRec;
	        });

	        return true;
	    }
	    return false;
	}

	@Override
	public IEntrada cambiarEntrada(IEntrada entrada, String contrasenia, String fecha) {
	    if (entrada == null) throw new IllegalArgumentException("Entrada inválida");
	    if (contrasenia == null || contrasenia.isEmpty()) throw new IllegalArgumentException("Contraseña inválida");
	    if (fecha == null || fecha.isEmpty()) throw new IllegalArgumentException("Nueva fecha inválida");

	    Entrada entActual = entradasPorInstancia.get(entrada);
	    if (entActual == null || entActual.anulada) throw new RuntimeException("Entrada no encontrada o ya anulada");

	    Usuario usuario = usuarios.get(entActual.getEmail());
	    if (usuario == null || !usuario.validarContrasena(contrasenia)) throw new RuntimeException("Contraseña incorrecta para el usuario de la entrada");

	    Espectaculo espectaculo = entActual.getFuncion().getEspectaculo();
	    Funcion nuevaFuncion = espectaculo.getFuncion(fecha);
	    if (nuevaFuncion == null) throw new RuntimeException("Nueva función no encontrada para la fecha " + fecha);
	    if (!nuevaFuncion.getSede().getNombre().equals(entActual.getFuncion().getSede().getNombre())) {
	        throw new RuntimeException("El cambio de fecha no permite cambio de sede.");
	    }
	    
	    try {
	        // Asume que la sede tiene un método para cambiar una entrada general
	        IEntrada nuevaEntrada = entActual.getFuncion().getSede().cambiarEntradaGeneral(entActual, nuevaFuncion);

	        // Anular la entrada vieja (lógica similar a anularEntrada pero sin quitarla del usuario)
	        entActual.anular(); // Marcamos la vieja como anulada
	        usuario.removerEntrada(entActual); // La quitamos de las entradas activas del usuario si tu lista es filtrada
	        
	        // Agregar la nueva entrada
	        usuario.agregarEntrada((Entrada) nuevaEntrada);
	        entradasPorInstancia.put(nuevaEntrada, (Entrada) nuevaEntrada);
	        entradasPorEspectaculo.computeIfAbsent(espectaculo.getNombre(), k -> new ArrayList<>()).add((Entrada) nuevaEntrada);
	        entradasPorEspectaculoYFecha.computeIfAbsent(espectaculo.getNombre(), k -> new HashMap<>())
	                                     .computeIfAbsent(fecha, k -> new ArrayList<>())
	                                     .add((Entrada) nuevaEntrada);

	        // Ajustar recaudación: descontar la antigua y sumar la nueva
	        double costoVieja = entActual.getCostoTotal();
	        double costoNueva = nuevaEntrada.getCostoTotal();
	        String nombreSede = entActual.getFuncion().getSede().getNombre();
	        String nombreEspectaculo = espectaculo.getNombre();

	        recaudacionPorSedeYEspectaculo.computeIfPresent(nombreSede, (s, espectaculosRec) -> {
	            espectaculosRec.computeIfPresent(nombreEspectaculo, (e, recaudado) -> recaudado - costoVieja + costoNueva);
	            return espectaculosRec;
	        });

	        return nuevaEntrada;

	    } catch (RuntimeException e) {
	        throw new RuntimeException("Error al cambiar la entrada de fecha: " + e.getMessage());
	    }
	}


	@Override
	public IEntrada cambiarEntrada(IEntrada entrada, String contrasenia, String fecha, String sector, int asiento) {
	    if (entrada == null) throw new IllegalArgumentException("Entrada inválida");
	    if (contrasenia == null || contrasenia.isEmpty()) throw new IllegalArgumentException("Contraseña inválida");
	    if (fecha == null || fecha.isEmpty()) throw new IllegalArgumentException("Nueva fecha inválida");
	    if (sector == null || sector.isEmpty()) throw new IllegalArgumentException("Nuevo sector inválido");
	    if (asiento <= 0) throw new IllegalArgumentException("Nuevo asiento inválido");

	    Entrada entActual = entradasPorInstancia.get(entrada);
	    if (entActual == null || entActual.anulada) throw new RuntimeException("Entrada no encontrada o ya anulada");

	    Usuario usuario = usuarios.get(entActual.getUsuario().getEmail());
	    if (usuario == null || !usuario.validarContrasena(contrasenia)) throw new RuntimeException("Contraseña incorrecta para el usuario de la entrada");

	    Espectaculo espectaculo = entActual.getFuncion().getEspectaculo();
	    Funcion nuevaFuncion = espectaculo.getFuncion(fecha);
	    if (nuevaFuncion == null) throw new RuntimeException("Nueva función no encontrada para la fecha " + fecha);
	    if (!nuevaFuncion.getSede().getNombre().equals(entActual.getFuncion().getSede().getNombre())) {
	        throw new RuntimeException("El cambio de fecha, sector y asiento no permite cambio de sede.");
	    }

	    try {
	        // Asume que la sede tiene un método para cambiar una entrada específica
	        IEntrada nuevaEntrada = entActual.getFuncion().getSede().cambiarEntrada(entActual, nuevaFuncion, sector, asiento);

	        // Anular la entrada vieja (lógica similar a anularEntrada pero sin quitarla del usuario)
	        entActual.anular(); // Marcamos la vieja como anulada
	        usuario.removerEntrada(entActual); 
	        
	        // Agregar la nueva entrada
	        usuario.agregarEntrada((Entrada) nuevaEntrada);
	        entradasPorInstancia.put(nuevaEntrada, (Entrada) nuevaEntrada);
	        entradasPorEspectaculo.computeIfAbsent(espectaculo.getNombre(), k -> new ArrayList<>()).add((Entrada) nuevaEntrada);
	        entradasPorEspectaculoYFecha.computeIfAbsent(espectaculo.getNombre(), k -> new HashMap<>())
	                                     .computeIfAbsent(fecha, k -> new ArrayList<>())
	                                     .add((Entrada) nuevaEntrada);
	        
	        // Ajustar recaudación: descontar la antigua y sumar la nueva
	        double costoVieja = entActual.getCostoTotal();
	        double costoNueva = nuevaEntrada.getCostoTotal();
	        String nombreSede = entActual.getFuncion().getSede().getNombre();
	        String nombreEspectaculo = espectaculo.getNombre();

	        recaudacionPorSedeYEspectaculo.computeIfPresent(nombreSede, (s, espectaculosRec) -> {
	            espectaculosRec.computeIfPresent(nombreEspectaculo, (e, recaudado) -> recaudado - costoVieja + costoNueva);
	            return espectaculosRec;
	        });

	        return nuevaEntrada;

	    } catch (RuntimeException e) {
	        throw new RuntimeException("Error al cambiar la entrada de fecha, sector y asiento: " + e.getMessage());
	    }
	}

	@Override
	public double costoEntrada(String nombreEspectaculo, String fecha) {
	    if (nombreEspectaculo == null || nombreEspectaculo.isEmpty()) throw new IllegalArgumentException("Nombre de espectáculo inválido");
	    if (fecha == null || fecha.isEmpty()) throw new IllegalArgumentException("Fecha inválida");

	    Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
	    if (espectaculo == null) throw new RuntimeException("Espectáculo no encontrado");

	    Funcion funcion = espectaculo.getFuncion(fecha);
	    if (funcion == null) throw new RuntimeException("Función no encontrada para la fecha especificada");

	    return funcion.getPrecioBase();
	}

	@Override
	public double costoEntrada(String nombreEspectaculo, String fecha, String sector) {
	    if (nombreEspectaculo == null || nombreEspectaculo.isEmpty()) throw new IllegalArgumentException("Nombre de espectáculo inválido");
	    if (fecha == null || fecha.isEmpty()) throw new IllegalArgumentException("Fecha inválida");
	    if (sector == null || sector.isEmpty()) throw new IllegalArgumentException("Sector inválido");

	    Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
	    if (espectaculo == null) throw new RuntimeException("Espectáculo no encontrado");

	    Funcion funcion = espectaculo.getFuncion(fecha);
	    if (funcion == null) throw new RuntimeException("Función no encontrada para la fecha especificada");

	    Sede sede = funcion.getSede();
	    // Asume que la Sede tiene un método para calcular el costo total de una entrada dado un sector
	    // Esto es crucial para que los diferentes tipos de sedes (Estadio, Teatro, Miniestadio) apliquen sus lógicas de precios.
	    return sede.calcularCostoEntradaPorSector(funcion.getPrecioBase(), sector);
	}
	@Override
	public double totalRecaudado(String nombreEspectaculo) {
	    if (nombreEspectaculo == null || nombreEspectaculo.isEmpty()) throw new IllegalArgumentException("Nombre de espectáculo inválido");
	    
	    double total = 0;
	    for (Map<String, Double> recaudacionSede : recaudacionPorSedeYEspectaculo.values()) {
	        total += recaudacionSede.getOrDefault(nombreEspectaculo, 0.0);
	    }
	    return total;
	}
	
	@Override
	public double totalRecaudadoPorSede(String nombreEspectaculo, String nombreSede) {
	    if (nombreEspectaculo == null || nombreEspectaculo.isEmpty()) throw new IllegalArgumentException("Nombre de espectáculo inválido");
	    if (nombreSede == null || nombreSede.isEmpty()) throw new IllegalArgumentException("Nombre de sede inválido");

	    Map<String, Double> recaudacionEspectaculos = recaudacionPorSedeYEspectaculo.get(nombreSede);
	    if (recaudacionEspectaculos == null) return 0.0; 

	    return recaudacionEspectaculos.getOrDefault(nombreEspectaculo, 0.0);
	}

	@Override
	public List<IEntrada> venderEntrada(String nombreEspectaculo, String fecha, String email, String contrasenia, String sector, int[] asientos) {
	    if (nombreEspectaculo == null || nombreEspectaculo.isEmpty()) throw new IllegalArgumentException("Nombre de espectáculo inválido");
	    if (fecha == null || fecha.isEmpty()) throw new IllegalArgumentException("Fecha inválida");
	    if (email == null || email.isEmpty() || contrasenia == null || contrasenia.isEmpty()) throw new IllegalArgumentException("Credenciales de usuario inválidas");
	    if (sector == null || sector.isEmpty()) throw new IllegalArgumentException("Sector inválido");
	    if (asientos == null || asientos.length == 0) throw new IllegalArgumentException("Asientos inválidos");

	    Usuario usuario = usuarios.get(email);
	    if (usuario == null || !usuario.validarContrasena(contrasenia)) throw new RuntimeException("Usuario o contraseña incorrectos");

	    Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
	    if (espectaculo == null) throw new RuntimeException("Espectáculo no encontrado");

	    Funcion funcion = espectaculo.getFuncion(fecha);
	    if (funcion == null) throw new RuntimeException("Función no encontrada para la fecha especificada");

	    Sede sede = funcion.getSede();
	    if (sede == null) throw new RuntimeException("Sede asociada a la función no encontrada");

	    List<IEntrada> nuevasEntradas = new ArrayList<>();
	    try {
	        for (int asiento : asientos) {
	            Entrada entrada = sede.venderEntrada(funcion, usuario, sector, asiento); // Asume que la sede tiene este método
	            nuevasEntradas.add(entrada);
	            entradasPorInstancia.put(entrada, entrada);
	            entradasPorEspectaculo.computeIfAbsent(nombreEspectaculo, k -> new ArrayList<>()).add(entrada);
	            entradasPorEspectaculoYFecha.computeIfAbsent(nombreEspectaculo, k -> new HashMap<>())
	                                         .computeIfAbsent(fecha, k -> new ArrayList<>())
	                                         .add(entrada);
	            usuario.agregarEntrada(entrada);
	            
	            // Actualizar la recaudación
	            double costoEntrada = entrada.precio();
	            recaudacionPorSedeYEspectaculo.computeIfAbsent(sede.getNombre(), k -> new HashMap<>())
	                                          .merge(nombreEspectaculo, costoEntrada, Double::sum);
	        }
	        return nuevasEntradas;
	    } catch (RuntimeException e) {
	        throw new RuntimeException("Error al vender entradas por sector y asiento: " + e.getMessage());
	    }
	}

}
