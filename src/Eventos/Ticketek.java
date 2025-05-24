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


	public void registrarSede(String nombre, String direccion, int capacidadMaxima) {	
        if (nombre == null || nombre.isEmpty()) throw new IllegalArgumentException("Nombre de sede inválido");
        if (direccion == null || direccion.isEmpty()) throw new IllegalArgumentException("Dirección inválida");
        if (capacidadMaxima <= 0) throw new IllegalArgumentException("Capacidad inválida");
        if (sedes.containsKey(nombre)) throw new RuntimeException("Sede ya registrada");
        sedes.put(nombre, new Estadio(nombre, direccion, capacidadMaxima));

	}

	@Override
	public void registrarSede(String nombre, String direccion, int capacidadMaxima, int asientosPorFila,
			String[] sectores, int[] capacidad, int[] porcentajeAdicional) {
        if (nombre == null || nombre.isEmpty() || direccion == null || direccion.isEmpty() || capacidadMaxima <= 0 || asientosPorFila <= 0)throw new IllegalArgumentException("Datos de sede inválidos");
        if (sectores == null || capacidad == null || porcentajeAdicional == null ||
            sectores.length != capacidad.length || sectores.length != porcentajeAdicional.length)throw new IllegalArgumentException("Datos de sectores inválidos");
        if (sedes.containsKey(nombre)) throw new RuntimeException("Sede ya registrada");
        sedes.put(nombre, new Teatro(nombre, direccion, capacidadMaxima, asientosPorFila, sectores, capacidad, porcentajeAdicional));
	}

	@Override
	public void registrarSede(String nombre, String direccion, int capacidadMaxima, int asientosPorFila,
			int cantidadPuestos, double precioConsumicion, String[] sectores, int[] capacidad,
			int[] porcentajeAdicional) {
        /*if (nombre == null || nombre.isEmpty() || direccion == null || direccion.isEmpty() || capacidadMaxima <= 0 || asientosPorFila <= 0 || cantidadFilas <= 0) {
            throw new IllegalArgumentException("Datos de miniestadio inválidos");
        }
        if (sectores == null || capacidad == null || porcentajeAdicional == null ||
            sectores.length != capacidad.length || sectores.length != porcentajeAdicional.length) {
            throw new IllegalArgumentException("Datos de sectores inválidos");
        }
        if (costoAdicional < 0) throw new IllegalArgumentException("Costo adicional inválido");
        if (sedes.containsKey(nombre)) throw new RuntimeException("Sede ya registrada");
        sedes.put(nombre, new Miniestadio(nombre, direccion, capacidadMaxima, asientosPorFila, cantidadFilas, costoAdicional, sectores, capacidad, porcentajeAdicional));
*/
	    if (nombre == null || nombre.isEmpty() || direccion == null || direccion.isEmpty() || capacidadMaxima <= 0 || asientosPorFila <= 0 || cantidadPuestos <= 0)throw new IllegalArgumentException("Datos de sede inválidos");
	    if (sectores == null || capacidad == null || porcentajeAdicional == null || sectores.length != capacidad.length || sectores.length != porcentajeAdicional.length)throw new IllegalArgumentException("Datos de sectores inválidos");
	    if (precioConsumicion < 0) throw new IllegalArgumentException("Precio de consumición inválido");
	    if (sedes.containsKey(nombre)) throw new RuntimeException("Sede ya registrada");
	    sedes.put(nombre, new Miniestadio(nombre, direccion, capacidadMaxima, asientosPorFila, cantidadPuestos, precioConsumicion, sectores, capacidad, porcentajeAdicional));
	}

	@Override
	public void registrarUsuario(String email, String nombre, String apellido, String contrasenia) {
	    if (email == null || email.isEmpty() || !email.contains("@")) throw new IllegalArgumentException("Email inválido");
	    if (nombre == null || nombre.isEmpty()) throw new IllegalArgumentException("Nombre inválido");
	    if (apellido == null || apellido.isEmpty()) throw new IllegalArgumentException("Apellido inválido");
	    if (contrasenia == null || contrasenia.isEmpty()) throw new IllegalArgumentException("Contraseña inválida");
	    if (usuarios.containsKey(email)) throw new RuntimeException("Usuario ya registrado con ese email");

	    usuarios.put(email, new Usuario(email, nombre, apellido, contrasenia));
	}

	@Override
	public void registrarEspectaculo(String nombre) {
		 if (espectaculos.containsKey(nombre)) throw new RuntimeException("Espectáculo ya registrado");
	        espectaculos.put(nombre, new Espectaculo(nombre));
	}

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

	@Override
	public String listarFunciones(String nombreEspectaculo) {
		
		if (nombreEspectaculo == null || nombreEspectaculo.isEmpty()) throw new IllegalArgumentException("Nombre de espectáculo inválido");
		
        if (!espectaculos.containsKey(nombreEspectaculo)) throw new RuntimeException("Espectáculo no encontrado");
        
        return espectaculos.get(nombreEspectaculo).toString();
	}

	@Override
	public List<IEntrada> listarEntradasEspectaculo(String nombreEspectaculo) {
			if (nombreEspectaculo == null || nombreEspectaculo.isEmpty()) throw new IllegalArgumentException("Nombre de espectáculo inválido");
	        if (!entradasPorEspectaculo.containsKey(nombreEspectaculo)) return new ArrayList<>();
	        return new ArrayList<>(entradasPorEspectaculo.get(nombreEspectaculo));
	}

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

	@Override
	public List<IEntrada> listarTodasLasEntradasDelUsuario(String email, String contrasenia) {
		if (email == null || contrasenia == null || email.isEmpty() || contrasenia.isEmpty())throw new IllegalArgumentException("Datos inválidos");
        Usuario usuario = usuarios.get(email);
        if (usuario == null || !usuario.validarContrasena(contrasenia))throw new RuntimeException("Usuario o contraseña inválidos");
        return new ArrayList<>(usuario.entradas);
	}

	@Override
	public boolean anularEntrada(IEntrada entrada, String contrasenia) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IEntrada cambiarEntrada(IEntrada entrada, String contrasenia, String fecha, String sector, int asiento) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IEntrada cambiarEntrada(IEntrada entrada, String contrasenia, String fecha) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double costoEntrada(String nombreEspectaculo, String fecha) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double costoEntrada(String nombreEspectaculo, String fecha, String sector) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double totalRecaudado(String nombreEspectaculo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double totalRecaudadoPorSede(String nombreEspectaculo, String nombreSede) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<IEntrada> venderEntrada(String nombreEspectaculo, String fecha, String email, String contrasenia,
			String sector, int[] asientos) {
		// TODO Auto-generated method stub
		return null;
	}

}
