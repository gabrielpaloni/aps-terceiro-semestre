package Controller;

import View.PortalProfessor;
import java.sql.*;
import java.util.*;
import Model.Conexao;

public class ProfessorController {
    private final PortalProfessor portal;
    private final int idProfessor;

    public ProfessorController(PortalProfessor portal, int idProfessor) {
        this.portal = portal;
        this.idProfessor = idProfessor;
    }

    public List<Object[]> buscarTodosAlunosDoProfessor() {
        List<Object[]> alunos = new ArrayList<>();

        String sql = """
        SELECT a.nome_aluno, 
               n.nota1, n.nota2, n.media_final, n.exame_final, n.situacao, 
               d.nome_disciplina
        FROM notas_completas n
        JOIN aluno a ON n.id_aluno = a.id_aluno
        JOIN disciplina d ON n.id_disciplina = d.id_disciplina
        WHERE d.id_professor = ? 
        ORDER BY d.nome_disciplina, a.nome_aluno;
    """;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProfessor);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                alunos.add(new Object[]{
                        rs.getString("nome_aluno"),
                        rs.getDouble("nota1"),
                        rs.getDouble("nota2"),
                        rs.getDouble("media_final"),
                        (rs.getDouble("exame_final") == 0.0) ? "Não necessário" : rs.getDouble("exame_final"),
                        rs.getString("situacao"),
                        rs.getString("nome_disciplina")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alunos;
    }

    public List<Object[]> buscarPublicacoesDoProfessor() {
        List<Object[]> publicacoes = new ArrayList<>();

        String sql = """
        SELECT p.titulo, p.ano_publicacao, a.nome_aluno
        FROM publicacao p
        JOIN aluno a ON p.id_aluno = a.id_aluno
        WHERE p.id_professor = ? 
        ORDER BY p.ano_publicacao DESC, p.titulo;
    """;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProfessor);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] dadosPublicacao = new Object[]{
                        rs.getString("titulo"),
                        rs.getInt("ano_publicacao"),
                        rs.getString("nome_aluno")
                };

                publicacoes.add(dadosPublicacao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return publicacoes;
    }


    public void mostrarPainel(String nomePainel) {
        portal.mostrar();
    }
}