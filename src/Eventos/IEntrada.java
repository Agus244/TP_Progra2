package Eventos;

public interface IEntrada {

    /**
     * Calcula y devuelve el precio de la entrada.
     *
     * @return El precio de la entrada.
     */
    double precio();

    /**
     * Retorna la ubicación de la entrada formateada.
     * Si la entrada es de tipo "CAMPO", la ubicación será "CAMPO".
     * Si es para un sector con asientos, el formato será "{SECTOR} - Asientos: {NRO ASIENTO}, {NRO ASIENTO}, ..."
     *
     * @return Una cadena de texto que representa la ubicación de la entrada.
     */
    String ubicacion();

    /**
     * Retorna una representación en cadena de la entrada, siguiendo el formato:
     * "- {FECHA DE LA FUNCION} - {NOMBRE DEL ESPECTACULO} - {NOMBRE DE LA SEDE} - {UBICACION}"
     * La ubicación se formatea según las reglas de 'ubicacion()'.
     *
     * @return Una cadena de texto formateada de la entrada.
     */
    String toString();

    /**
     * Retorna el objeto Funcion al que pertenece esta entrada.
     *
     * @return El objeto Funcion asociado a la entrada.
     */
    Funcion getFuncion();

    /**
     * Retorna el correo electrónico del comprador de la entrada.
     *
     * @return El email del comprador.
     */
    String getEmail();

    /**
     * Retorna el nombre del sector de la entrada.
     *
     * @return El sector de la entrada.
     */
    String getSector();

    /**
     * Retorna un array de enteros con los números de asiento de la entrada.
     *
     * @return Un array de enteros con los asientos.
     */
    int[] getAsientos();

    /**
     * Retorna el tipo de la entrada (ej. "CAMPO", "PLATEA").
     *
     * @return El tipo de la entrada.
     */
    String getTipo();

    /**
     * Verifica si la entrada ha sido anulada.
     *
     * @return true si la entrada está anulada, false en caso contrario.
     */
    boolean estaAnulada();

    /**
     * Verifica si la entrada ya ha sido utilizada.
     *
     * @return true si la entrada fue usada, false en caso contrario.
     */
    boolean fueUsada();

    /**
     * Establece el estado de anulación de la entrada.
     *
     * @param anulada true para anular la entrada, false para desanularla.
     */
    void setAnulada(boolean anulada);

    /**
     * Establece el estado de uso de la entrada.
     *
     * @param fueUsada true para marcar la entrada como usada, false para desmarcarla.
     */
    void setFueUsada(boolean fueUsada);
}