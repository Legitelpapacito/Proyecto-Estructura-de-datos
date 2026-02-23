package clases.java;

public class ResultadoEjecucion {

    private final boolean valido;
    private final String mensaje;

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

    @Override
    public String toString() {
        return "ResultadoEjecucion{valido=" + valido + ", mensaje='" + mensaje + "'}";
    }
}
