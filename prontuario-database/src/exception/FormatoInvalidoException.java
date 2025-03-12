package exception;

public class FormatoInvalidoException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    private String nomeCampo;
    private String formatoEsperado;
    
    public FormatoInvalidoException(String nomeCampo, String formatoEsperado) {
        super("O campo '" + nomeCampo + "' não está no formato esperado: " + formatoEsperado);
        this.nomeCampo = nomeCampo;
        this.formatoEsperado = formatoEsperado;
    }
    
    public String getNomeCampo() {
        return nomeCampo;
    }
    
    public String getFormatoEsperado() {
        return formatoEsperado;
    }
}