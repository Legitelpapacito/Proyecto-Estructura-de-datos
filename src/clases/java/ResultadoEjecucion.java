package clases.java;
public class ResultadoEjecucion {

    private boolean valido;
    private String mensaje;

    public ResultadoEjecucion(boolean valido, String mensaje) {
        this.valido = valido;
        this.mensaje = mensaje;
    }

    public boolean esValido() {
        return valido;
    }

    public String getMensaje() {
        return mensaje;
    }
}