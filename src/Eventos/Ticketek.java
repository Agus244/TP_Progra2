package Eventos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set; // Importar Set para el toString de Sede

public class Ticketek implements ITicketek {

    private Map<String, Usuario> usuarios;
    private Map<String, Sede> sedes;
    private Map<String, Espectaculo> espectaculos;

    // Mapas para optimización en O(1)
    private Map<String, Entrada> entradasPorId; // Clave: idEntrada (String), Valor: Entrada
    private Map<String, Map<String, Double>> recaudacionPorSedeYEspectaculo; // Clave: nombreSede -> nombreEspectaculo -> Double

    // Formateador para las fechas dd/MM/YY
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yy");

    public Ticketek() {
        this.usuarios = new HashMap<>();
        this.sedes = new HashMap<>();
        this.espectaculos = new HashMap<>();
        this.entradasPorId = new HashMap<>();
        this.recaudacionPorSedeYEspectaculo = new HashMap<>();
    }

    @Override
    public void registrarSede(String nombre, String direccion, int capacidadMaxima) {
        if (nombre == null || nombre.isEmpty()) throw new IllegalArgumentException("Nombre de sede inválido.");
        if (direccion == null || direccion.isEmpty()) throw new IllegalArgumentException("Dirección inválida.");
        if (capacidadMaxima <= 0) throw new IllegalArgumentException("Capacidad máxima inválida.");
        if (sedes.containsKey(nombre)) throw new RuntimeException("Sede ya registrada con ese nombre.");

        sedes.put(nombre, new Estadio(nombre, direccion, capacidadMaxima));
    }

    @Override
    public void registrarSede(String nombre, String direccion, int capacidadMaxima, int asientosPorFila,
                              String[] sectores, int[] capacidad, int[] porcentajeAdicional) {
        if (nombre == null || nombre.isEmpty() || direccion == null || direccion.isEmpty() || capacidadMaxima <= 0)
            throw new IllegalArgumentException("Datos de sede inválidos: nombre, dirección o capacidad.");
        if (asientosPorFila <= 0)
            throw new IllegalArgumentException("Datos de sede inválidos: asientos por fila.");
        if (sectores == null || capacidad == null || porcentajeAdicional == null ||
            sectores.length == 0 || sectores.length != capacidad.length || sectores.length != porcentajeAdicional.length)
            throw new IllegalArgumentException("Datos de sectores inválidos: arrays nulos o de longitud inconsistente.");
        if (sedes.containsKey(nombre)) throw new RuntimeException("Sede ya registrada con ese nombre.");

        sedes.put(nombre, new Teatro(nombre, direccion, capacidadMaxima, asientosPorFila, sectores, capacidad, porcentajeAdicional));
    }

    @Override
    public void registrarSede(String nombre, String direccion, int capacidadMaxima, int asientosPorFila,
                              int cantidadPuestos, double precioConsumicion, String[] sectores, int[] capacidad,
                              int[] porcentajeAdicional) {
       
        
        if (nombre == null || nombre.isEmpty() || direccion == null || direccion.isEmpty() || capacidadMaxima <= 0)
            throw new IllegalArgumentException("Datos de miniestadio inválidos: nombre, dirección o capacidad.");
        if (asientosPorFila <= 0)
            throw new IllegalArgumentException("Datos de miniestadio inválidos: asientos por fila.");
        if (precioConsumicion < 0) throw new IllegalArgumentException("Precio de consumición (costo adicional) inválido.");
        if (sectores == null || capacidad == null || porcentajeAdicional == null ||
            sectores.length == 0 || sectores.length != capacidad.length || sectores.length != porcentajeAdicional.length)
            throw new IllegalArgumentException("Datos de sectores inválidos: arrays nulos o de longitud inconsistente.");
        if (sedes.containsKey(nombre)) throw new RuntimeException("Sede ya registrada con ese nombre.");

        sedes.put(nombre, new Miniestadio(nombre, direccion, capacidadMaxima, asientosPorFila, sectores, capacidad, porcentajeAdicional, precioConsumicion));
    }

    @Override
    public void registrarUsuario(String email, String nombre, String apellido, String contrasenia) {
        if (email == null || email.isEmpty() || !email.contains("@")) throw new IllegalArgumentException("Email inválido.");
        if (nombre == null || nombre.isEmpty()) throw new IllegalArgumentException("Nombre inválido.");
        if (apellido == null || apellido.isEmpty()) throw new IllegalArgumentException("Apellido inválido.");
        if (contrasenia == null || contrasenia.isEmpty()) throw new IllegalArgumentException("Contraseña inválida.");
        if (usuarios.containsKey(email)) throw new RuntimeException("Usuario ya registrado con ese email.");

        // Usamos el constructor de Usuario que toma nombre y apellido
        usuarios.put(email, new Usuario(email, nombre, apellido, contrasenia));
    }

    @Override
    public void registrarEspectaculo(String nombre) {
        if (nombre == null || nombre.isEmpty()) throw new IllegalArgumentException("Nombre de espectáculo inválido.");
        if (espectaculos.containsKey(nombre)) throw new RuntimeException("Espectáculo ya registrado con ese nombre.");
        espectaculos.put(nombre, new Espectaculo(nombre));
    }

    @Override
    public void agregarFuncion(String nombreEspectaculo, String fechaStr, String nombreSede, double precioBase) {
        if (nombreEspectaculo == null || nombreEspectaculo.isEmpty()) throw new IllegalArgumentException("Nombre de espectáculo inválido.");
        if (fechaStr == null || fechaStr.isEmpty()) throw new IllegalArgumentException("Fecha inválida.");
        if (nombreSede == null || nombreSede.isEmpty()) throw new IllegalArgumentException("Nombre de sede inválido.");
        if (precioBase <= 0) throw new IllegalArgumentException("Precio base inválido.");

        Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
        if (espectaculo == null) throw new RuntimeException("Espectáculo no encontrado.");
        Sede sede = sedes.get(nombreSede);
        if (sede == null) throw new RuntimeException("Sede no encontrada.");

        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaStr, DATE_FORMATTER); // Usar el formateador definido
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use dd/mm/YY.");
        }

        Funcion funcion = new Funcion(espectaculo, sede, fecha, precioBase);
        espectaculo.agregarFuncion(funcion); // Esto lanza excepción si ya hay una función para esa fecha
    }

    @Override
    public List<IEntrada> venderEntrada(String nombreEspectaculo, String fechaStr, String email, String contrasenia, int cantidadEntradas) {
        if (nombreEspectaculo == null || nombreEspectaculo.isEmpty()) throw new IllegalArgumentException("Nombre de espectáculo inválido.");
        if (fechaStr == null || fechaStr.isEmpty()) throw new IllegalArgumentException("Fecha inválida.");
        if (email == null || email.isEmpty() || contrasenia == null || contrasenia.isEmpty()) throw new IllegalArgumentException("Datos de usuario inválidos.");
        if (cantidadEntradas <= 0) throw new IllegalArgumentException("Cantidad de entradas inválida.");

        Usuario usuario = usuarios.get(email);
        if (usuario == null || !usuario.autenticar(contrasenia)) throw new RuntimeException("Usuario o contraseña incorrectos.");

        Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
        if (espectaculo == null) throw new RuntimeException("Espectáculo no encontrado.");

        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use dd/mm/YY.");
        }

        Funcion funcion = espectaculo.getFuncion(fecha);
        if (funcion == null) throw new RuntimeException("Función no encontrada para la fecha especificada.");

        Sede sede = funcion.getSede();
        if (sede == null) throw new RuntimeException("Sede asociada a la función no encontrada.");

        // Validar si la sede es numerada (Estadio) o no
        if (!(sede instanceof Estadio)) {
            throw new RuntimeException("Esta sede no soporta la venta por cantidad de entradas sin especificar sector/asiento (no es un Estadio).");
        }
        Estadio estadio = (Estadio) sede;

        List<IEntrada> nuevasEntradas = new ArrayList<>();
        try {
            for (int i = 0; i < cantidadEntradas; i++) {
                // Estadio vende entradas de tipo "CAMPO"
                Entrada entrada = estadio.venderEntrada(funcion, usuario, "CAMPO", 1); // 1 unidad de capacidad
                
                	nuevasEntradas.add(entrada);
                    // Registrar la entrada en los mapas de Ticketek
                    entradasPorId.put(entrada.getIdEntrada(), entrada);
                    usuario.agregarEntrada(entrada);
                    funcion.agregarEntrada(entrada); // Agregar entrada a la función

                    // Actualizar la recaudación
                    recaudacionPorSedeYEspectaculo
                        .computeIfAbsent(sede.getNombre(), k -> new HashMap<>())
                        .merge(nombreEspectaculo, entrada.precio(), Double::sum);
                
            }
            return nuevasEntradas;
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al vender entradas: " + e.getMessage());
        }
    }

    @Override
    public List<IEntrada> venderEntrada(String nombreEspectaculo, String fechaStr, String email, String contrasenia, String sector, int[] asientos) {
        if (nombreEspectaculo == null || nombreEspectaculo.isEmpty()) throw new IllegalArgumentException("Nombre de espectáculo inválido.");
        if (fechaStr == null || fechaStr.isEmpty()) throw new IllegalArgumentException("Fecha inválida.");
        if (email == null || email.isEmpty() || contrasenia == null || contrasenia.isEmpty()) throw new IllegalArgumentException("Credenciales de usuario inválidas.");
        if (sector == null || sector.isEmpty()) throw new IllegalArgumentException("Sector inválido.");
        if (asientos == null || asientos.length == 0) throw new IllegalArgumentException("Asientos inválidos.");

        Usuario usuario = usuarios.get(email);
        if (usuario == null || !usuario.autenticar(contrasenia)) throw new RuntimeException("Usuario o contraseña incorrectos.");

        Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
        if (espectaculo == null) throw new RuntimeException("Espectáculo no encontrado.");

        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use dd/mm/YY.");
        }

        Funcion funcion = espectaculo.getFuncion(fecha);
        if (funcion == null) throw new RuntimeException("Función no encontrada para la fecha especificada.");

        Sede sede = funcion.getSede();
        if (sede == null) throw new RuntimeException("Sede asociada a la función no encontrada.");

        // Validar si la sede es numerada (Teatro o Miniestadio)
        if (!(sede instanceof Teatro)) { // Miniestadio hereda de Teatro
            throw new RuntimeException("Esta sede no soporta la venta por sector y asiento (no es un Teatro o Miniestadio).");
        }
        Teatro teatro = (Teatro) sede;

        List<IEntrada> nuevasEntradas = new ArrayList<>();
        try {
            for (int asiento : asientos) {
                // Teatro o Miniestadio venden entradas de tipo "ASIENTO"
                Entrada entrada = teatro.venderEntrada(funcion, usuario, sector, asiento);
                nuevasEntradas.add(entrada);
                
                // Registrar la entrada en los mapas de Ticketek
                entradasPorId.put(entrada.getIdEntrada(), entrada);
                usuario.agregarEntrada(entrada);
                funcion.agregarEntrada(entrada); // Agregar entrada a la función

                // Actualizar la recaudación
                recaudacionPorSedeYEspectaculo.computeIfAbsent(sede.getNombre(), k -> new HashMap<>())
                                              .merge(nombreEspectaculo, entrada.precio(), Double::sum);
            }
            return nuevasEntradas;
        } catch (RuntimeException e) {
            throw new RuntimeException("Error al vender entradas por sector y asiento: " + e.getMessage());
        }
    }

    @Override
    public String listarFunciones(String nombreEspectaculo) {
        if (nombreEspectaculo == null || nombreEspectaculo.isEmpty()) throw new IllegalArgumentException("Nombre de espectáculo inválido.");
        
        Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
        if (espectaculo == null) throw new RuntimeException("Espectáculo no encontrado.");

        StringBuilder sb = new StringBuilder();
        Map<LocalDate, Funcion> funcionesDelEspectaculo = espectaculo.getFunciones();

        if (funcionesDelEspectaculo.isEmpty()) {
            sb.append("El espectáculo '").append(nombreEspectaculo).append("' no tiene funciones programadas.\n");
            return sb.toString();
        }

        
        funcionesDelEspectaculo.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> {
                Funcion funcion = entry.getValue();
                Sede sede = funcion.getSede();
                
                sb.append(" - (").append(funcion.getFecha().format(DATE_FORMATTER)).append(") ");
                sb.append(sede.getNombre()).append(" - ");

                if (sede instanceof Estadio) {
                   
                    int entradasVendidas = sede.getAsientosOcupadosEnSector(funcion, "CAMPO"); 
                    int capacidadTotal = sede.getCapacidadTotal();
                    sb.append(entradasVendidas).append("/").append(capacidadTotal);
                } else if (sede instanceof Teatro) { 
                    Teatro teatro = (Teatro) sede;
                    String[] sectores = teatro.obtenerSectores();
                    for (int i = 0; i < sectores.length; i++) {
                        String sectorNombre = sectores[i];
                        int entradasVendidasSector = sede.getAsientosOcupadosEnSector(funcion, sectorNombre);
                        int capacidadSector = teatro.obtenerCapacidadPorSector(sectorNombre);
                        sb.append(sectorNombre).append(": ").append(entradasVendidasSector).append("/").append(capacidadSector);
                        if (i < sectores.length - 1) {
                            sb.append(" | ");
                        }
                    }
                }
                sb.append("\n");
            });
        return sb.toString();
    }

    @Override
    public List<IEntrada> listarEntradasEspectaculo(String nombreEspectaculo) {
        if (nombreEspectaculo == null || nombreEspectaculo.isEmpty()) throw new IllegalArgumentException("Nombre de espectáculo inválido.");
        
        Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
        if (espectaculo == null) {
            throw new RuntimeException("Espectáculo no encontrado.");
        }
       
        return espectaculo.listarEntradasVendidas();
    }

    @Override
    public List<IEntrada> listarEntradasFuturas(String email, String contrasenia) {
        if (email == null || email.isEmpty() || contrasenia == null || contrasenia.isEmpty()) throw new IllegalArgumentException("Datos de usuario inválidos.");
        
        Usuario usuario = usuarios.get(email);
        if (usuario == null || !usuario.autenticar(contrasenia)) throw new RuntimeException("Usuario o contraseña inválidos.");
        
        List<IEntrada> futuras = new ArrayList<>();
        LocalDate hoy = LocalDate.now();
        
        for (IEntrada iEntrada : usuario.getMisEntradas()) {
            Entrada entrada = (Entrada) iEntrada; // Castear a Entrada para acceder a campos específicos
            if (!entrada.estaAnulada() && entrada.getFuncion().getFecha().isAfter(hoy)) {
                futuras.add(entrada);
            }
        }
        return futuras;
    }

    @Override
    public List<IEntrada> listarTodasLasEntradasDelUsuario(String email, String contrasenia) {
        if (email == null || email.isEmpty() || contrasenia == null || contrasenia.isEmpty()) throw new IllegalArgumentException("Datos de usuario inválidos.");
        
        Usuario usuario = usuarios.get(email);
        if (usuario == null || !usuario.autenticar(contrasenia)) throw new RuntimeException("Usuario o contraseña inválidos.");
        
        return usuario.getMisEntradas(); 
    }


    @Override
    public boolean anularEntrada(IEntrada entrada, String contrasenia) {
        if (entrada == null) throw new IllegalArgumentException("Entrada inválida (nula).");
        if (contrasenia == null || contrasenia.isEmpty()) throw new IllegalArgumentException("Contraseña inválida.");

        Entrada entAux = (Entrada) entrada;
        Entrada entParaAnular = entradasPorId.get(entAux.getIdEntrada());

       
        if (entParaAnular == null) {
            throw new RuntimeException("La entrada no existe."); 
        }
        if (entParaAnular.estaAnulada()) {
            throw new RuntimeException("La entrada ya ha sido anulada."); 
        }
      

       
        if (entParaAnular.getFuncion().getFecha().isBefore(LocalDate.now())) {
            return false;
        }

        Usuario usuario = usuarios.get(entParaAnular.getEmail()); 
        if (usuario == null || !usuario.autenticar(contrasenia)) {
            throw new RuntimeException("Contraseña incorrecta para el usuario asociado a esta entrada.");
        }

       
        entParaAnular.setAnulada(true);

      
        Sede sede = entParaAnular.getFuncion().getSede();
        Funcion funcion = entParaAnular.getFuncion();

        if (entParaAnular.getTipo().equalsIgnoreCase("ASIENTO")) {
            sede.desmarcarAsientoOcupado(funcion, entParaAnular.getSector(), entParaAnular.getAsientos()[0]);
        } else if (entParaAnular.getTipo().equalsIgnoreCase("CAMPO")) {
            sede.desmarcarAsientoOcupado(funcion, "CAMPO", 1);
        }

       
        funcion.eliminarEntrada(entParaAnular); 

        
        double costoEntradaAnulada = entParaAnular.precio();
        String nombreEspectaculo = entParaAnular.getFuncion().getEspectaculo().getNombre();
        String nombreSede = sede.getNombre();

        recaudacionPorSedeYEspectaculo.computeIfPresent(nombreSede, (s, espectaculosRec) -> {
            espectaculosRec.computeIfPresent(nombreEspectaculo, (e, recaudado) ->
                    recaudado - costoEntradaAnulada);
            return espectaculosRec;
        });

        return true;
    }

    @Override
    public IEntrada cambiarEntrada(IEntrada entradaExistente, String contrasenia, String nuevaFechaStr, String nuevoSector, int nuevoAsiento) {
        if (entradaExistente == null) throw new IllegalArgumentException("Entrada inválida (nula).");
        if (contrasenia == null || contrasenia.isEmpty()) throw new IllegalArgumentException("Contraseña inválida.");
        if (nuevaFechaStr == null || nuevaFechaStr.isEmpty()) throw new IllegalArgumentException("Nueva fecha inválida.");
        if (nuevoSector == null || nuevoSector.isEmpty()) throw new IllegalArgumentException("Nuevo sector inválido.");
        if (nuevoAsiento <= 0) throw new IllegalArgumentException("Nuevo asiento inválido.");
        
        Entrada entAux = (Entrada) entradaExistente;
        Entrada entActual = entradasPorId.get(entAux.getIdEntrada());
        if (entActual == null || entActual.estaAnulada()) throw new RuntimeException("Entrada no encontrada o ya anulada.");
        
        
        if (entActual.getFuncion().getFecha().isBefore(LocalDate.now())) {
            throw new RuntimeException("No se puede cambiar una entrada cuya función ya ha pasado.");
        }

        Usuario usuario = usuarios.get(entActual.getEmail());
        if (usuario == null || !usuario.autenticar(contrasenia)) throw new RuntimeException("Contraseña incorrecta para el usuario de la entrada.");

        Espectaculo espectaculo = entActual.getFuncion().getEspectaculo();
        LocalDate nuevaFecha;
        try {
            nuevaFecha = LocalDate.parse(nuevaFechaStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de nueva fecha inválido. Use dd/mm/YY.");
        }
        
        Funcion nuevaFuncion = espectaculo.getFuncion(nuevaFecha);
        if (nuevaFuncion == null) throw new RuntimeException("Nueva función no encontrada para la fecha " + nuevaFechaStr + " del mismo espectáculo.");
        
      
        if (!nuevaFuncion.getSede().getNombre().equals(entActual.getFuncion().getSede().getNombre())) {
            throw new RuntimeException("El cambio de función no permite cambio de sede.");
        }
        
       
        Sede sedeOriginal = entActual.getFuncion().getSede();
        if (!(sedeOriginal instanceof Teatro)) {
            throw new RuntimeException("El tipo de sede de la entrada original no soporta cambio de sector y asiento.");
        }
        Teatro teatroOriginal = (Teatro) sedeOriginal;

        try {
           
            boolean anulado = anularEntrada(entradaExistente, contrasenia);
            if (!anulado) {
               
                throw new RuntimeException("Fallo al anular la entrada original para el cambio.");
            }

           
            Entrada nuevaEntrada = teatroOriginal.venderEntrada(nuevaFuncion, usuario, nuevoSector, nuevoAsiento);

          
            entradasPorId.put(nuevaEntrada.getIdEntrada(), nuevaEntrada);
            usuario.agregarEntrada(nuevaEntrada);
            nuevaFuncion.agregarEntrada(nuevaEntrada); 

           
            recaudacionPorSedeYEspectaculo.computeIfAbsent(sedeOriginal.getNombre(), k -> new HashMap<>())
                                          .merge(espectaculo.getNombre(), nuevaEntrada.precio(), Double::sum);

            return nuevaEntrada;

        } catch (RuntimeException e) {
            
            throw new RuntimeException("Error al cambiar la entrada de fecha, sector y asiento: " + e.getMessage());
        }
    }

    @Override
    public IEntrada cambiarEntrada(IEntrada entradaExistente, String contrasenia, String nuevaFechaStr) {
        if (entradaExistente == null) throw new IllegalArgumentException("Entrada inválida (nula).");
        if (contrasenia == null || contrasenia.isEmpty()) throw new IllegalArgumentException("Contraseña inválida.");
        if (nuevaFechaStr == null || nuevaFechaStr.isEmpty()) throw new IllegalArgumentException("Nueva fecha inválida.");
        
        Entrada entAux = (Entrada) entradaExistente;
        Entrada entActual = entradasPorId.get(entAux.getIdEntrada());
        if (entActual == null || entActual.estaAnulada()) throw new RuntimeException("Entrada no encontrada o ya anulada.");
        
      
        if (entActual.getFuncion().getFecha().isBefore(LocalDate.now())) {
            throw new RuntimeException("No se puede cambiar una entrada cuya función ya ha pasado.");
        }

        Usuario usuario = usuarios.get(entActual.getEmail());
        if (usuario == null || !usuario.autenticar(contrasenia)) throw new RuntimeException("Contraseña incorrecta para el usuario de la entrada.");

        Espectaculo espectaculo = entActual.getFuncion().getEspectaculo();
        LocalDate nuevaFecha;
        try {
            nuevaFecha = LocalDate.parse(nuevaFechaStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de nueva fecha inválido. Use dd/mm/YY.");
        }
        
        Funcion nuevaFuncion = espectaculo.getFuncion(nuevaFecha);
        if (nuevaFuncion == null) throw new RuntimeException("Nueva función no encontrada para la fecha " + nuevaFechaStr + " del mismo espectáculo.");
        
        
        if (!nuevaFuncion.getSede().getNombre().equals(entActual.getFuncion().getSede().getNombre())) {
            throw new RuntimeException("El cambio de función a otra fecha no permite cambio de sede.");
        }

        Sede sedeOriginal = entActual.getFuncion().getSede();
        String tipoEntrada = entActual.getTipo();
        String sectorOriginal = entActual.getSector();
        int[] asientosOriginales = entActual.getAsientos();

        try {
           
            boolean anulado = anularEntrada(entradaExistente, contrasenia);
            if (!anulado) {
                throw new RuntimeException("Fallo al anular la entrada original para el cambio.");
            }

           
            Entrada nuevaEntrada;
            if (tipoEntrada.equalsIgnoreCase("CAMPO")) {
                if (!(sedeOriginal instanceof Estadio)) throw new RuntimeException("Inconsistencia: entrada de CAMPO en sede no Estadio.");
                nuevaEntrada = ((Estadio) sedeOriginal).venderEntrada(nuevaFuncion, usuario, sectorOriginal, 1);
            } else if (tipoEntrada.equalsIgnoreCase("ASIENTO")) {
                if (!(sedeOriginal instanceof Teatro)) throw new RuntimeException("Inconsistencia: entrada de ASIENTO en sede no Teatro/Miniestadio.");
                if (asientosOriginales == null || asientosOriginales.length == 0) throw new RuntimeException("Asiento original inválido para cambio de fecha.");
                nuevaEntrada = ((Teatro) sedeOriginal).venderEntrada(nuevaFuncion, usuario, sectorOriginal, asientosOriginales[0]);
            } else {
                throw new RuntimeException("Tipo de entrada desconocido: " + tipoEntrada);
            }

           
            entradasPorId.put(nuevaEntrada.getIdEntrada(), nuevaEntrada);
            usuario.agregarEntrada(nuevaEntrada);
            nuevaFuncion.agregarEntrada(nuevaEntrada); 
            
          
            recaudacionPorSedeYEspectaculo.computeIfAbsent(sedeOriginal.getNombre(), k -> new HashMap<>())
                                          .merge(espectaculo.getNombre(), nuevaEntrada.precio(), Double::sum);

            return nuevaEntrada;

        } catch (RuntimeException e) {
            throw new RuntimeException("Error al cambiar la entrada de fecha: " + e.getMessage());
        }
    }

    @Override
    public double costoEntrada(String nombreEspectaculo, String fechaStr) {
        if (nombreEspectaculo == null || nombreEspectaculo.isEmpty()) throw new IllegalArgumentException("Nombre de espectáculo inválido.");
        if (fechaStr == null || fechaStr.isEmpty()) throw new IllegalArgumentException("Fecha inválida.");

        Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
        if (espectaculo == null) throw new RuntimeException("Espectáculo no encontrado.");

        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use dd/mm/YY.");
        }

        Funcion funcion = espectaculo.getFuncion(fecha);
        if (funcion == null) throw new RuntimeException("Función no encontrada para la fecha especificada.");

        Sede sede = funcion.getSede();
        if (!(sede instanceof Estadio)) {
             throw new RuntimeException("Este método es solo para estadios. Para sedes con sectores, use el método con 'sector'.");
        }

       
        return funcion.getPrecioBase();
    }

    @Override
    public double costoEntrada(String nombreEspectaculo, String fechaStr, String sector) {
        if (nombreEspectaculo == null || nombreEspectaculo.isEmpty()) throw new IllegalArgumentException("Nombre de espectáculo inválido.");
        if (fechaStr == null || fechaStr.isEmpty()) throw new IllegalArgumentException("Fecha inválida.");
        if (sector == null || sector.isEmpty()) throw new IllegalArgumentException("Sector inválido.");

        Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
        if (espectaculo == null) throw new RuntimeException("Espectáculo no encontrado.");

        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use dd/mm/YY.");
        }

        Funcion funcion = espectaculo.getFuncion(fecha);
        if (funcion == null) throw new RuntimeException("Función no encontrada para la fecha especificada.");

        Sede sede = funcion.getSede();
        if (!(sede instanceof Teatro)) {
            throw new RuntimeException("Este método es solo para sedes con asientos y sectores (Teatros o Miniestadios).");
        }
        
       
        return sede.obtenerPrecioBase(funcion, sector);
    }
    
    @Override
    public double totalRecaudado(String nombreEspectaculo) {
        if (nombreEspectaculo == null || nombreEspectaculo.isEmpty()) throw new IllegalArgumentException("Nombre de espectáculo inválido.");
        
        Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
        if (espectaculo == null) throw new RuntimeException("Espectáculo no encontrado.");
        
        
        return espectaculo.calcularRecaudacionTotal();
    }
    
    @Override
    public double totalRecaudadoPorSede(String nombreEspectaculo, String nombreSede) {
        if (nombreEspectaculo == null || nombreEspectaculo.isEmpty()) throw new IllegalArgumentException("Nombre de espectáculo inválido.");
        if (nombreSede == null || nombreSede.isEmpty()) throw new IllegalArgumentException("Nombre de sede inválido.");

        Map<String, Double> recaudacionEspectaculosEnSede = recaudacionPorSedeYEspectaculo.get(nombreSede);
        if (recaudacionEspectaculosEnSede == null) {
            return 0.0; 
        }

     
        return recaudacionEspectaculosEnSede.getOrDefault(nombreEspectaculo, 0.0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Sistema Ticketek ---\n");

        sb.append("## Usuarios Registrados (").append(usuarios.size()).append("):\n");
        if (usuarios.isEmpty()) {
            sb.append("  Ninguno.\n");
        } else {
            for (Usuario usuario : usuarios.values()) {
                sb.append("  ").append(usuario.toString().replace("\n", "\n  ")).append("\n");
            }
        }
        sb.append("---\n");

        sb.append("## Sedes Registradas (").append(sedes.size()).append("):\n");
        if (sedes.isEmpty()) {
            sb.append("  Ninguna.\n");
        } else {
            for (Sede sede : sedes.values()) {
                sb.append("  ").append(sede.toString()).append("\n");
            }
        }
        sb.append("---\n");

        sb.append("## Espectáculos Registrados (").append(espectaculos.size()).append("):\n");
        if (espectaculos.isEmpty()) {
            sb.append("  Ninguno.\n");
        } else {
            for (Espectaculo espectaculo : espectaculos.values()) {
                sb.append("  ").append(espectaculo.toString().replace("\n", "\n  ")).append("\n");
            }
        }
        sb.append("------------------------\n");
        return sb.toString();
    }
}