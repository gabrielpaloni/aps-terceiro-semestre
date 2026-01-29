package Controller;

import javax.swing.*;
import java.awt.Color;
import Model.Aluno;
import java.sql.*;
import Model.Conexao;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class AlunoController {

    public Aluno autenticarAluno(String id_matricula, String cpf_aluno) {
        Aluno aluno = null;
        try (Connection conn = Conexao.conectar()) {
            String sql = "SELECT a.id_aluno, a.nome_aluno FROM aluno a JOIN matricula m ON a.id_aluno = m.id_aluno WHERE m.id_matricula = ? AND a.cpf_aluno = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id_matricula);
            stmt.setString(2, cpf_aluno);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int idAluno = rs.getInt("id_aluno");
                String nome = rs.getString("nome_aluno");
                aluno = new Aluno(idAluno, nome, cpf_aluno);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aluno;
    }

    public String buscarNomeAluno(String id_matricula) {
        String nomeAluno = null;
        String sql = "SELECT a.nome_aluno FROM aluno a JOIN matricula m ON a.id_aluno = m.id_aluno WHERE m.id_matricula = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id_matricula);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                nomeAluno = rs.getString("nome_aluno");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nomeAluno;
    }

    public List<Object[]> buscarNotasAluno(int idAluno) {
        List<Object[]> notas = new ArrayList<>();
        Set<String> disciplinasAdicionadas = new HashSet<>();

        String sql = """
        SELECT d.nome_disciplina, nc.nota1, nc.nota2, nc.media_final
        FROM notas_completas nc
        JOIN disciplina d ON nc.id_disciplina = d.id_disciplina
        WHERE nc.id_aluno = ?
    """;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAluno);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String disciplina = rs.getString("nome_disciplina");
                if (disciplinasAdicionadas.contains(disciplina)) continue;
                disciplinasAdicionadas.add(disciplina);

                Double nota1 = rs.getObject("nota1") != null ? rs.getDouble("nota1") : null;
                Double nota2 = rs.getObject("nota2") != null ? rs.getDouble("nota2") : null;
                Double media = rs.getObject("media_final") != null ? rs.getDouble("media_final") : null;

                notas.add(new Object[]{disciplina, nota1, nota2, media});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notas;
    }

    public Object[] buscarDadosAluno(int idAluno) {
        Object[] dados = new Object[4];
        String sql = "SELECT a.nome_aluno, a.cpf_aluno, c.nome_curso, m.id_matricula " +
                "FROM aluno a " +
                "LEFT JOIN matricula m ON a.id_aluno = m.id_aluno " +
                "LEFT JOIN curso c ON m.id_curso = c.id_curso " +
                "WHERE a.id_aluno = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAluno);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                dados[0] = rs.getString("nome_aluno");
                dados[1] = rs.getString("cpf_aluno");
                dados[2] = rs.getString("nome_curso");
                dados[3] = rs.getObject("id_matricula");
            }
            System.out.println(java.util.Arrays.toString(dados));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dados;
    }

    public List<Object[]> buscarMediasExameFinal(int idAluno) {
        List<Object[]> lista = new ArrayList<>();
        Set<String> disciplinasAdicionadas = new HashSet<>();

        String sql = """
        SELECT d.nome_disciplina, nc.media_final, nc.exame_final, nc.situacao
        FROM notas_completas nc
        JOIN disciplina d ON nc.id_disciplina = d.id_disciplina
        WHERE nc.id_aluno = ?
    """;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAluno);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String disciplina = rs.getString("nome_disciplina");
                if (disciplinasAdicionadas.contains(disciplina)) continue;
                disciplinasAdicionadas.add(disciplina);

                Double mediaFinal = rs.getObject("media_final") != null ? rs.getDouble("media_final") : null;
                Double exameFinal = rs.getObject("exame_final") != null ? rs.getDouble("exame_final") : null;
                String situacao = rs.getString("situacao");

                String notaExame;
                if (mediaFinal != null && mediaFinal >= 7.0) {
                    notaExame = "Não necessário";
                } else if (exameFinal != null) {
                    notaExame = String.valueOf(exameFinal);
                } else {
                    notaExame = "-";
                }

                lista.add(new Object[]{disciplina, mediaFinal, notaExame, situacao});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Object[]> buscarPublicacoesAluno(int idAluno) { /* ... */
        List<Object[]> lista = new ArrayList<>();
        String sql = """
            SELECT p.titulo, pr.nome_professor, p.ano_publicacao
            FROM publicacao p
            JOIN aluno a ON p.id_aluno = a.id_aluno
            LEFT JOIN professor pr ON p.id_professor = pr.id_professor
            WHERE a.id_aluno = ?
        """;
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAluno);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String titulo = rs.getString("titulo");
                String nomeProfessor = rs.getString("nome_professor");
                int ano = rs.getInt("ano_publicacao");
                String anoStr = String.valueOf(ano);
                lista.add(new Object[]{titulo, nomeProfessor, anoStr});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    public Object[] buscarDadosCurso(int idAluno) { /* ... */
        Object[] dadosCurso = new Object[3];
        String sql = """
        SELECT c.id_curso, c.nome_curso, c.duracao
        FROM curso c
        JOIN matricula m ON c.id_curso = m.id_curso
        WHERE m.id_aluno = ?
    """;
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAluno);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                dadosCurso[0] = rs.getInt("id_curso");
                dadosCurso[1] = rs.getString("nome_curso");
                dadosCurso[2] = rs.getString("duracao");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dadosCurso;
    }
    public List<Object[]> buscarDisciplinasECargaHoraria(int idAluno) {
        List<Object[]> lista = new ArrayList<>();
        Set<String> disciplinasAdicionadas = new HashSet<>();

        String sql = """
        SELECT d.nome_disciplina, a.carga_horaria
        FROM disciplina d
        JOIN aula a ON d.id_disciplina = a.id_disciplina
        JOIN matricula m ON m.id_curso = d.id_curso
        WHERE m.id_aluno = ?
    """;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAluno);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String disciplina = rs.getString("nome_disciplina");
                if (disciplinasAdicionadas.contains(disciplina)) continue;
                disciplinasAdicionadas.add(disciplina);

                String cargaHoraria = rs.getString("carga_horaria") + " horas";

                lista.add(new Object[]{disciplina, cargaHoraria});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void alterarSenha(JFrame frame) {
        JOptionPane.showMessageDialog(frame, "Alteração de senha em andamento.");
    }

    public void alterarEmail(JFrame frame) {
        JOptionPane.showMessageDialog(frame, "Alteração de e-mail em andamento.");
    }

    public void alterarTema(JFrame frame, boolean isDarkMode) {
        UIManager.put("Panel.background", isDarkMode ? Color.DARK_GRAY : Color.LIGHT_GRAY);
        UIManager.put("Button.background", isDarkMode ? Color.GRAY : new Color(70, 130, 180));
        SwingUtilities.updateComponentTreeUI(frame);
    }

    public void atualizarDadosContato(JFrame frame) {
        JOptionPane.showMessageDialog(frame, "Atualize seus dados de contato.");
    }
}