package Eventos;

import java.time.format.DateTimeFormatter; // Para formatear la fecha
import java.util.Objects; // Para equals() y hashCode()
import java.util.UUID;    // Para generar un ID único para cada entrada

public class Entrada implements IEntrada {
    private String idEntrada; // Identificador único de la entrada
    private Funcion funcion;
    private Usuario usuario;
    private String sector;
    private int[] asientos; // Puede ser un array de 1 para asiento único o vacío para campo general
    private String tipo;    // "CAMPO" o "ASIENTO"
    private boolean anulada;
    private boolean usada;

    /**
     * Constructor para una entrada con asiento específico (ej. Teatro, Miniestadio).
     *
     * @param funcion   La función a la que pertenece la entrada.
     * @param usuario   El usuario que compró la entrada.
     * @param sector    El sector de la entrada (ej. "Platea", "Palco").
     * @param asiento   El número de asiento.
     * @throws IllegalArgumentException Si algún parámetro es nulo o inválido.
     */
    public Entrada(Funcion funcion, Usuario usuario, String sector, int asiento) {
        if (funcion == null) throw new IllegalArgumentException("La función no puede ser nula.");
        if (usuario == null) throw new IllegalArgumentException("El usuario no puede ser nulo.");
        if (sector == null || sector.trim().isEmpty()) throw new IllegalArgumentException("El sector no puede ser nulo o vacío.");
        if (asiento <= 0) throw new IllegalArgumentException("El número de asiento debe ser positivo.");

        this.idEntrada = UUID.randomUUID().toString(); // Genera un ID único
        this.funcion = funcion;
        this.usuario = usuario;
        this.sector = sector;
        this.asientos = new int[]{asiento}; // Un solo asiento
        this.tipo = "ASIENTO";
        this.anulada = false;
        this.usada = false;
    }

    /**
     * Constructor para una entrada de tipo "CAMPO" (ej. Estadio).
     * En este caso, no hay asientos específicos, solo una "unidad" de capacidad.
     *
     * @param funcion   La función a la que pertenece la entrada.
     * @param usuario   El usuario que compró la entrada.
     * @param sector    El sector (debería ser "CAMPO" para este constructor).
     * @throws IllegalArgumentException Si algún parámetro es nulo o inválido.
     */
    public Entrada(Funcion funcion, Usuario usuario, String sector) {
        if (funcion == null) throw new IllegalArgumentException("La función no puede ser nula.");
        if (usuario == null) throw new IllegalArgumentException("El usuario no puede ser nulo.");
        if (sector == null || sector.trim().isEmpty()) throw new IllegalArgumentException("El sector no puede ser nulo o vacío.");
        
        this.idEntrada = UUID.randomUUID().toString(); // Genera un ID único
        this.funcion = funcion;
        this.usuario = usuario;
        this.sector = sector;
        this.asientos = new int[]{}; // No hay asientos específicos
        this.tipo = "CAMPO";
        this.anulada = false;
        this.usada = false;
    }

    // --- Métodos de la interfaz IEntrada ---

    @Override
    public double precio() {
        // Delega el cálculo del precio a la sede, que considera el precio base de la función
        // y los recargos por sector específicos de la sede (y miniestadio).
        return funcion.getSede().obtenerPrecioBase(funcion, sector);
    }

    @Override
    public String ubicacion() {
        if (this.tipo.equalsIgnoreCase("CAMPO")) {
            return "CAMPO";
        } else if (this.tipo.equalsIgnoreCase("ASIENTO") && asientos != null && asientos.length > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(sector).append(" - Asiento: ");
            for (int i = 0; i < asientos.length; i++) {
                sb.append(asientos[i]);
                if (i < asientos.length - 1) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }
        return "Ubicación Desconocida"; // Fallback
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("- %s - %s - %s - %s",
                funcion.getFecha().format(formatter),
                funcion.getEspectaculo().getNombre(),
                funcion.getSede().getNombre(),
                ubicacion());
    }

    @Override
    public Funcion getFuncion() {
        return funcion;
    }

    @Override
    public String getEmail() {
        return usuario.getEmail();
    }

    @Override
    public String getSector() {
        return sector;
    }

    @Override
    public int[] getAsientos() {
        // Devuelve una copia defensiva del array para evitar modificaciones externas.
        return asientos != null ? asientos.clone() : new int[0];
    }

    @Override
    public String getTipo() {
        return tipo;
    }

    @Override
    public boolean estaAnulada() {
        return anulada;
    }

    @Override
    public boolean fueUsada() {
        return usada;
    }

    @Override
    public void setAnulada(boolean anulada) {
        this.anulada = anulada;
    }

    @Override
    public void setFueUsada(boolean fueUsada) {
        this.usada = fueUsada;
    }
    
    // --- Métodos Adicionales (no en la interfaz, pero útiles) ---

    // Este getter es necesario para la clase Ticketek en anularEntrada/cambiarEntrada
    public String getIdEntrada() {
        return idEntrada;
    }
    
    // Este getter es necesario para la clase Ticketek en anularEntrada/cambiarEntrada
    public Usuario getUsuario() {
        return usuario;
    }

    // --- Sobreescritura de equals() y hashCode() ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entrada entrada = (Entrada) o;
        // La igualdad se basa únicamente en el ID único de la entrada
        return Objects.equals(idEntrada, entrada.idEntrada);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEntrada);
    }
}