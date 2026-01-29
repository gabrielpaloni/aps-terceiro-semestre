package View;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class TelaEscolhaLogin {
    public void mostrar(Consumer<String> onLoginSelecionado) {
        Consumer<String> finalOnLoginSelecionado = onLoginSelecionado != null ? onLoginSelecionado : tipo -> {
            System.out.println("Nenhuma ação definida para OnLoginSelecionado.");
        };

        JPanel escolhaPanel = new JPanel();
        escolhaPanel.setLayout(new BoxLayout(escolhaPanel, BoxLayout.Y_AXIS));

        JLabel labelTitulo = new JLabel("Escolha como deseja logar!");
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        labelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JPanel botoesPanel = new JPanel();
        botoesPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnAdm = new JButton("Administrador");
        JButton btnAluno = new JButton("Aluno");
        JButton btnProfessor = new JButton("Professor");

        btnAdm.setFocusPainted(false);
        btnAluno.setFocusPainted(false);
        btnProfessor.setFocusPainted(false);

        Dimension buttonSize = new Dimension(150, 40);
        btnAdm.setPreferredSize(buttonSize);
        btnAluno.setPreferredSize(buttonSize);
        btnProfessor.setPreferredSize(buttonSize);

        botoesPanel.add(btnAdm);
        botoesPanel.add(btnAluno);
        botoesPanel.add(btnProfessor);

        escolhaPanel.add(labelTitulo);
        escolhaPanel.add(botoesPanel);

        JDialog dialogo = new JDialog();
        dialogo.setTitle("\uD83C\uDF32");
        dialogo.setModal(true);
        dialogo.getContentPane().add(escolhaPanel);
        dialogo.pack();
        dialogo.setLocationRelativeTo(null);

        btnAdm.addActionListener(e -> {
            dialogo.dispose();
            finalOnLoginSelecionado.accept("Administrador");
        });

        btnAluno.addActionListener(e -> {
            dialogo.dispose();
            finalOnLoginSelecionado.accept("Aluno");
        });

        btnProfessor.addActionListener(e -> {
            dialogo.dispose();
            finalOnLoginSelecionado.accept("Professor");
        });

        dialogo.setVisible(true);
    }
}