package exception;

public class CampoObrigatorioException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    private String nomeCampo;
    
    public CampoObrigatorioException(String nomeCampo) {
        super("O campo '" + nomeCampo + "' é obrigatório.");
        this.nomeCampo = nomeCampo;
    }
    
    public String getNomeCampo() {
        return nomeCampo;
    }
}