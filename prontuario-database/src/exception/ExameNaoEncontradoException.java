package exception;

public class ExameNaoEncontradoException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    private Long id;
    
    public ExameNaoEncontradoException(Long id) {
        super("Exame com ID '" + id + "' não foi encontrado.");
        this.id = id;
    }
    
    public Long getId() {
        return id;
    }
}