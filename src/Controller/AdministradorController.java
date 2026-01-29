package Controller;

import Model.Conexao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class AdministradorController {
    private final JPanel painelPrincipal;

    public AdministradorController(JPanel painelPrincipal) {
        this.painelPrincipal = painelPrincipal;
    }

    public void mostrarPainel(String opcao) {
        painelPrincipal.removeAll();
        painelPrincipal.setLayout(new BorderLayout());

        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Seção: " + opcao);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        conteudo.add(titulo);
        conteudo.add(Box.createVerticalStrut(20));

        String[] campos = switch (opcao) {
            case "Alunos" -> new String[]{"Nome", "CPF", "ID Curso", "ID Turma"};
            case "Professores" -> new String[]{"ID Professor", "Nome", "Email", "CPF", "Senha"};
            case "Matrículas" -> new String[]{"ID Aluno", "ID Curso", "Data Matrícula", "RA"};
            case "Cursos" -> new String[]{"ID Curso", "Nome", "Tipo", "Duração"};
            case "Disciplinas" -> new String[]{"ID Disciplina", "Nome", "ID Curso", "ID Turma", "ID Professor"};
            case "Aulas" -> new String[]{"ID Aula", "Carga Horária", "Nome", "ID Professor"};
            case "Notas" -> new String[]{"ID Aluno", "ID Disciplina", "Situação"};
            case "Publicações" -> new String[]{"ID Publicação", "Título", "Ano", "ID Aluno", "ID Professor"};
            default -> new String[]{"Opção inválida"};
        };

        for (String campo : campos) {
            JLabel label = new JLabel("- " + campo);
            label.setFont(new Font("Arial", Font.PLAIN, 16));
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            conteudo.add(label);
            conteudo.add(Box.createVerticalStrut(10));
        }

        painelPrincipal.add(conteudo, BorderLayout.NORTH);
        painelPrincipal.revalidate();
        painelPrincipal.repaint();
    }

    public void carregarDados(String aba, JTable tabela) {
        String query = switch (aba) {
            case "Alunos" -> "SELECT nome_aluno, cpf_aluno, id_curso, id_turma FROM aluno";
            case "Professores" -> "SELECT id_professor, nome_professor, email_professor, cpf_professor, senha_prof FROM professor";
            case "Matrículas" -> "SELECT id_aluno, id_curso, data_matricula, id_matricula FROM matricula ORDER BY id_aluno ASC";
            case "Cursos" -> "SELECT id_curso, nome_curso, tipo_curso, duracao FROM curso";
            case "Turmas" -> "SELECT id_turma, turno, semestre, id_curso FROM turma";
            case "Disciplinas" -> "SELECT id_disciplina, nome_disciplina, id_curso, id_turma, id_professor FROM disciplina";
            case "Aulas" -> "SELECT a.id_aula, CONCAT(a.carga_horaria, ' horas'), d.nome_disciplina, a.id_professor FROM aula a JOIN disciplina d ON a.id_disciplina = d.id_disciplina";
            case "Notas" -> "SELECT id_aluno, id_disciplina, nota1, nota2, media_final, exame_final, situacao FROM notas_completas";
            case "Publicações" -> "SELECT id_publicacao, titulo, ano_publicacao, id_aluno, id_professor FROM publicacao";
            default -> null;
        };

        if (query == null) return;

        DefaultTableModel model = (DefaultTableModel) tabela.getModel();
        model.setRowCount(0);

        try (Connection con = Conexao.conectar();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                Vector<Object> linha = new Vector<>();
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    linha.add(rs.getObject(i));
                }
                model.addRow(linha);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(painelPrincipal, "Erro ao carregar dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
