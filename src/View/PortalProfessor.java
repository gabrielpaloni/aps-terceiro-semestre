package View;

import Controller.ProfessorController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableModel;

public class PortalProfessor extends JFrame {
    private final ProfessorController controller;
    private final String nomeProfessor;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);

    public PortalProfessor(int idProfessor, String nomeProfessor) {
        this.nomeProfessor = nomeProfessor;
        controller = new ProfessorController(this, idProfessor);

        setTitle("Portal do Professor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
        cardLayout.show(contentPanel, "Minhas Turmas");
    }

    private void initComponents() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(30, 30, 30));
        topPanel.setPreferredSize(new Dimension(1000, 50));
        JLabel titleLabel = new JLabel("  Portal do Professor - " + nomeProfessor);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        topPanel.add(titleLabel, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setPreferredSize(new Dimension(220, 0));
        menuPanel.setBackground(new Color(45, 45, 45));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        String[] opcoes = {"Minhas Turmas", "Relatórios"};
        for (String opcao : opcoes) {
            RoundedButton botao = new RoundedButton(opcao);
            botao.setAlignmentX(Component.CENTER_ALIGNMENT);
            botao.setMaximumSize(new Dimension(180, 48));
            botao.setPreferredSize(new Dimension(180, 48));
            botao.setFont(new Font("SansSerif", Font.BOLD, 13));
            botao.addActionListener(e -> trocarPainel(opcao));
            menuPanel.add(botao);
            menuPanel.add(Box.createVerticalStrut(18));
        }

        menuPanel.add(Box.createVerticalGlue());

        RoundedButton logoutButton = new RoundedButton("Logout");
        logoutButton.setFont(new Font("SansSerif", Font.PLAIN, 13));
        logoutButton.setPreferredSize(new Dimension(180, 48));
        logoutButton.setMaximumSize(new Dimension(180, 48));
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoutButton.addActionListener(e -> {
            Object[] botoes = {"Sim", "Não"};
            int escolha = JOptionPane.showOptionDialog(
                    null,
                    "Deseja realmente sair?",
                    "Confirmação de Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    botoes,
                    botoes[1]
            );

            if (escolha == JOptionPane.YES_OPTION) {
                dispose();
                SwingUtilities.invokeLater(() -> new TelaEscolhaLogin().mostrar(tipo -> {
                    switch (tipo) {
                        case "Professor" -> new TelaLoginProfessor().mostrar();
                        case "Aluno" -> new TelaLoginAluno().mostrar("", "");
                        case "Administrador" -> new TelaLoginAdm().mostrar();
                        default -> JOptionPane.showMessageDialog(null, "Tipo de login não reconhecido.");
                    }
                }));
            }
        });

        menuPanel.add(logoutButton);
        add(menuPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        contentPanel.add(criarPainelDisciplinas(), "Minhas Turmas");
        contentPanel.add(criarPainelRelatorios(), "Relatórios");
    }

    private JPanel criarPainelDisciplinas() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(35, 35, 35));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        List<Object[]> alunos = controller.buscarTodosAlunosDoProfessor();
        String[] colunas = {"Nome", "Nota NP1", "Nota NP2", "Média", "Exame Final", "Situação", "Disciplina"};

        for (Object[] aluno : alunos) {
            if (aluno[4] instanceof Double && (Double) aluno[4] == 0.0) {
                aluno[4] = "Não necessário";
            } else {
                aluno[4] = String.valueOf(aluno[4]);
            }
        }

        Object[][] dados = alunos.toArray(new Object[0][]);
        JTable tabela = new JTable(dados, colunas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela.setFont(new Font("SansSerif", Font.BOLD, 16));
        tabela.setRowHeight(32);

        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 18));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setPreferredSize(new Dimension(800, 400));

        JTextField campoPesquisa = new JTextField();
        campoPesquisa.setPreferredSize(new Dimension(250, 35));
        campoPesquisa.setFont(new Font("SansSerif", Font.PLAIN, 16));
        campoPesquisa.setBackground(new Color(50, 50, 50));
        campoPesquisa.setForeground(Color.WHITE);
        campoPesquisa.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));

        JLabel labelPesquisa = new JLabel("🔍 Pesquisar: ");
        labelPesquisa.setForeground(Color.WHITE);
        labelPesquisa.setFont(new Font("SansSerif", Font.BOLD, 16));

        campoPesquisa.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String filtro = campoPesquisa.getText().toLowerCase();
                TableRowSorter<TableModel> sorter = new TableRowSorter<>(tabela.getModel());
                tabela.setRowSorter(sorter);
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + filtro, 0));
            }
        });

        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelPesquisa.setBackground(new Color(35, 35, 35));
        painelPesquisa.add(labelPesquisa);
        painelPesquisa.add(campoPesquisa);

        painel.add(painelPesquisa, BorderLayout.NORTH);
        painel.add(scrollPane, BorderLayout.CENTER);

        return painel;
    }


    private JPanel criarPainelRelatorios() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(35, 35, 35));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        List<Object[]> publicacoes = controller.buscarPublicacoesDoProfessor();

        System.out.println("Total de publicações encontradas: " + publicacoes.size());

        if (publicacoes.isEmpty()) {
            JLabel mensagem = new JLabel("Nenhuma publicação encontrada.");
            mensagem.setFont(new Font("SansSerif", Font.BOLD, 18));
            mensagem.setForeground(Color.WHITE);
            mensagem.setHorizontalAlignment(SwingConstants.CENTER);
            painel.add(mensagem, BorderLayout.CENTER);
            return painel;
        }

        String[] colunas = {"Título da Publicação", "Ano", "Aluno"};
        Object[][] dados = publicacoes.toArray(new Object[0][]);
        JTable tabela = new JTable(dados, colunas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela.setFont(new Font("SansSerif", Font.BOLD, 16));
        tabela.setRowHeight(30);

        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 18));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setPreferredSize(new Dimension(800, 400));

        painel.add(scrollPane, BorderLayout.CENTER);
        return painel;
    }

    private void trocarPainel(String opcao) {
        cardLayout.show(contentPanel, opcao);
    }

    public void mostrar() {
        setVisible(true);
    }
    private static class RoundedButton extends JButton {
        private final Color normalColor = new Color(60, 60, 60);
        private final Color hoverColor = new Color(90, 90, 90);

        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setForeground(Color.WHITE);
            setBackground(normalColor);
            setBorderPainted(false);
            setOpaque(false);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(hoverColor);
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(normalColor);
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
            super.paintComponent(g);
        }
    }
}