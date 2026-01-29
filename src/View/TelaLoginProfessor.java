 package View;

import Model.Conexao;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TelaLoginProfessor {
    public void mostrar() {
        JFrame frame = new JFrame("Login Professor");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(350, 250);
        frame.setLayout(null);

        JLabel labelUsuario = new JLabel("Email:");
        labelUsuario.setBounds(20, 50, 100, 30);
        JTextField campoUsuario = new JTextField();
        campoUsuario.setBounds(90, 50, 235, 30);

        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setBounds(20, 100, 100, 30);

        JPasswordField campoSenha = new JPasswordField();
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
                char[] senhaInput = campoSenha.getPassword();

                try (Connection conn = Conexao.conectar();
                     PreparedStatement stmt = conn.prepareStatement(
                             "SELECT id_professor, nome_professor FROM professor WHERE email_professor = ? AND senha_prof = ?")) {

                    stmt.setString(1, usuarioInput);
                    stmt.setString(2, String.valueOf(senhaInput));

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            int idProfessor = rs.getInt("id_professor");
                            String nomeProfessor = rs.getString("nome_professor");
                            JOptionPane.showMessageDialog(frame, "Bem-vindo(a), " + nomeProfessor + "!");
                            frame.dispose();
                            new PortalProfessor(idProfessor, nomeProfessor).mostrar();
                        } else {
                            JOptionPane.showMessageDialog(frame, "Email ou senha inválidos.");
                            campoUsuario.setText("");
                            campoSenha.setText("");
                            campoUsuario.requestFocusInWindow();
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Erro ao conectar ao banco de dados.");
                } finally {
                    java.util.Arrays.fill(senhaInput, '\0');
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