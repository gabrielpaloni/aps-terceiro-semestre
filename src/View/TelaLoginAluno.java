package View;

import Model.Conexao;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TelaLoginAluno {
    public void mostrar(String usuarioPadrao, String senhaPadrao) {
        JFrame frame = new JFrame("Login Aluno");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(350, 250);
        frame.setLayout(null);

        JLabel labelUsuario = new JLabel("RA:");
        labelUsuario.setBounds(20, 50, 100, 30);
        JTextField campoUsuario = new JTextField(usuarioPadrao);
        campoUsuario.setBounds(90, 50, 235, 30);

        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setBounds(20, 100, 100, 30);

        JPasswordField campoSenha = new JPasswordField(senhaPadrao);
        campoSenha.setBounds(90, 100, 150, 30);

        JButton btnOlho = new JButton("Mostrar");
        btnOlho.setBounds(243, 100, 80, 30);
        btnOlho.addActionListener(e -> {
            if (campoSenha.getEchoChar() == '\u2022') {
                campoSenha.setEchoChar((char) 0);
                btnOlho.setText("Ocultar");
            } else {
                campoSenha.setEchoChar('\u2022');
                btnOlho.setText("Mostrar");
            }
        });

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(20, 160, 100, 40);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBounds(225, 160, 100, 40);
        btnVoltar.addActionListener(e -> {
            frame.setVisible(false);
            new TelaEscolhaLogin().mostrar(tipo -> {
                frame.dispose();
                switch (tipo) {
                    case "Administrador":
                        new TelaLoginAdm().mostrar();
                        break;
                    case "Aluno":
                        new TelaLoginAluno().mostrar("", "");
                        break;
                    case "Professor":
                        new TelaLoginProfessor().mostrar();
                        break;
                    default:
                        frame.setVisible(true);
                        break;
                }
            });
        });

        frame.add(labelUsuario);
        frame.add(campoUsuario);
        frame.add(labelSenha);
        frame.add(campoSenha);
        frame.add(btnOlho);
        frame.add(btnLogin);
        frame.add(btnVoltar);

        campoUsuario.addActionListener(e -> campoSenha.requestFocusInWindow());

        ActionListener tentarLogin = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuarioInput = campoUsuario.getText();
                String senhaInput = new String(campoSenha.getPassword());

                String sql = "SELECT a.id_aluno, a.nome_aluno FROM aluno a JOIN matricula m ON a.id_aluno = m.id_aluno WHERE m.id_matricula = ? AND a.cpf_aluno = ?";

                try (Connection conn = Conexao.conectar();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {

                    stmt.setString(1, usuarioInput);
                    stmt.setString(2, senhaInput);

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            int idAluno = rs.getInt("id_aluno");
                            String nomeAluno = rs.getString("nome_aluno");
                            JOptionPane.showMessageDialog(frame, "Bem-vindo(a), " + nomeAluno + "!");
                            PortalAluno portalAluno = new PortalAluno(idAluno, nomeAluno);
                            portalAluno.setVisible(true);
                            frame.dispose();
                        } else {
                            JOptionPane.showMessageDialog(frame, "RA ou CPF inválidos.");
                            campoUsuario.setText("");
                            campoSenha.setText("");
                            campoUsuario.requestFocusInWindow();
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Erro ao conectar ao banco de dados:\n" + ex.getMessage());
                }
            }
        };

        campoSenha.addActionListener(tentarLogin);
        btnLogin.addActionListener(tentarLogin);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        SwingUtilities.invokeLater(campoUsuario::requestFocusInWindow);
    }
}