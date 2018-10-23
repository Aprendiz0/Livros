package livro.model;

public class Autor {

    private int id;
    private String nome;
    private long cpf;

    public Autor() {
    }

    public Autor(int id, String nome, long cpf) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public long getCpf() {
        return cpf;
    }

    public void setCpf(long cpf) {
        this.cpf = cpf;
    }

    @Override
    public String toString() {
        return "Id: " + getId()
                + "\nNome: " + getNome()
                + "\nCPF: "
                + String.valueOf(getCpf()).replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }
}
