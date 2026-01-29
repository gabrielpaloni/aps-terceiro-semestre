package Model;

public class Aluno {
    private int id_aluno;
    private String nome_aluno;
    private String cpf_aluno;

    public Aluno(int id_aluno, String nome_aluno, String cpf_aluno) {
        this.id_aluno = id_aluno;
        this.nome_aluno = nome_aluno;
        this.cpf_aluno = cpf_aluno;
    }

    public int getId_aluno() {
        return id_aluno;
    }

    public String getNome_aluno() {
        return nome_aluno;
    }

    public String getCpf_aluno() {
        return cpf_aluno;
    }
}
