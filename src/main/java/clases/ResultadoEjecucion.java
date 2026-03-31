package clases;

/**
 * Clase que representa el resultado de ejecutar un script.
 * Indica si fue válido y un mensaje asociado.
 */
public class ResultadoEjecucion {

    /** Indica si el script es válido */
    private final boolean valido;

    /** Mensaje con información del resultado */
    private final String mensaje;

    /**
     * Constructor del resultado.
     *
     * @param valido indica si el script es válido
     * @param mensaje mensaje descriptivo
     */
    public ResultadoEjecucion(boolean valido, String mensaje) {
        this.valido = valido;
        this.mensaje = mensaje;
    }

    /**
     * Indica si el resultado es válido.
     *
     * @return true si es válido, false en caso contrario
     */
    public boolean esValido() {
        return valido;
    }

    /**
     * Obtiene el mensaje del resultado.
     *
     * @return mensaje asociado
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Representación en texto del resultado.
     *
     * @return cadena con el contenido del objeto
     */
    @Override
    public String toString() {
        return "ResultadoEjecucion{valido=" + valido + ", mensaje='" + mensaje + "'}";
    }
}