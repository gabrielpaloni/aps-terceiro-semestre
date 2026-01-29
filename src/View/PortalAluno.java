package View;

import Controller.AlunoController;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class PortalAluno extends JFrame {
    private final AlunoController controller;
    private String nomeAluno;
    private JLabel titleLabel;
    private RoundedButton logoutButton;
    private JPanel menuPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    private Map<String, JTable> tabelasPorAba = new HashMap<>();
    private int idAluno;

    private JTable infoTable;

    public PortalAluno(int idAluno, String nomeAluno) {
        this.idAluno = idAluno;
        this.nomeAluno = nomeAluno;
        controller = new AlunoController();
        setTitle("Portal do Aluno");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();

        carregarNotasDoBanco(idAluno);
        carregarDadosAluno(idAluno);
        carregarMediasExameFinal(idAluno);
        carregarPublicacoes(idAluno);
    }

    public class RoundedButton extends JButton {
        public RoundedButton(String label) {
            super(label);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setBackground(new Color(70, 130, 180));
            setFont(new Font("SansSerif", Font.BOLD, 11));
            setPreferredSize(new Dimension(160, 40));
            setMaximumSize(new Dimension(160, 40));

            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    setBackground(new Color(100, 160, 220));
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    setBackground(new Color(70, 130, 180));
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(getBackground());
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            super.paintComponent(g);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {}
    }

    private void initComponents() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(40, 95, 185));
        topPanel.setPreferredSize(new Dimension(1000, 50));
        titleLabel = new JLabel("  Portal do Aluno - " + nomeAluno);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        topPanel.add(titleLabel, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setPreferredSize(new Dimension(200, 0));
        menuPanel.setBackground(Color.LIGHT_GRAY);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        String[] menuItems = {
                "Notas",
                "Dados do Aluno",
                "Médias / Exame Final",
                "Publicações",
                "Dados do Curso"
        };

        for (String item : menuItems) {
            RoundedButton button = new RoundedButton(item);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            menuPanel.add(button);
            menuPanel.add(Box.createVerticalStrut(15));

            JPanel panel;

            if (item.equals("Dados do Curso")) {
                panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setBackground(Color.WHITE);

                String[] infoColumns = {"Curso", "Duração"};
                Object[][] infoData = {{"-", "-"}};
                infoTable = new JTable(infoData, infoColumns);
                infoTable.setTableHeader(null);
                infoTable.setRowHeight(28);
                infoTable.setFont(new Font("SansSerif", Font.BOLD, 16));
                infoTable.setEnabled(false);
                infoTable.setBackground(Color.WHITE);
                infoTable.setShowGrid(false);

                DefaultTableModel modelo = new DefaultTableModel(new Object[][]{}, new String[]{"Disciplinas", "Carga Horária"}) {
                    public boolean isCellEditable(int row, int column) { return false; }
                };
                JTable table = new JTable(modelo);
                table.setRowHeight(28);
                table.setFont(new Font("SansSerif", Font.PLAIN, 13));
                table.setGridColor(new Color(220, 220, 220));
                table.setShowVerticalLines(false);
                table.setShowHorizontalLines(true);

                JTableHeader header = table.getTableHeader();
                header.setBackground(new Color(70, 130, 180));
                header.setForeground(Color.WHITE);
                header.setFont(new Font("SansSerif", Font.BOLD, 14));
                table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

                JScrollPane scrollPane = new JScrollPane(table);
                scrollPane.setPreferredSize(new Dimension(800, 400));

                panel.add(infoTable);
                panel.add(scrollPane);

                tabelasPorAba.put(item, table);

                button.addActionListener((ActionEvent e) -> {
                    cardLayout.show(contentPanel, item);
                    carregarDadosCurso(idAluno);
                });
            } else {
                DefaultTableModel modelo = new DefaultTableModel(new Object[][]{}, gerarCabecalhos(item)) {
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                JTable table = new JTable(modelo);

                table.setRowHeight(28);
                table.setFont(new Font("SansSerif", Font.PLAIN, 13));
                table.setGridColor(new Color(220, 220, 220));
                table.setShowVerticalLines(false);
                table.setShowHorizontalLines(true);

                JTableHeader header = table.getTableHeader();
                header.setReorderingAllowed(false);
                header.setBackground(new Color(70, 130, 180));
                header.setForeground(Color.WHITE);
                header.setFont(new Font("SansSerif", Font.BOLD, 14));

                table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

                JScrollPane scrollPane = new JScrollPane(table);
                scrollPane.setPreferredSize(new Dimension(800, 400));

                panel = new JPanel(new BorderLayout());
                panel.add(scrollPane, BorderLayout.CENTER);
                tabelasPorAba.put(item, table);
                button.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, item));
            }

            contentPanel.add(panel, item);
        }

        menuPanel.add(Box.createVerticalGlue());

        logoutButton = new RoundedButton("Logout");
        logoutButton.setFont(new Font("SansSerif", Font.PLAIN, 11));
        logoutButton.setPreferredSize(new Dimension(160, 40));
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(logoutButton);

        add(menuPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        logoutButton.addActionListener(e -> {
            String[] opcoes = {"Sim", "Não"};
            JOptionPane pane = new JOptionPane(
                    "Deseja realmente sair?",
                    JOptionPane.PLAIN_MESSAGE,
                    JOptionPane.YES_NO_OPTION,
                    null,
                    opcoes,
                    null
            );
            JDialog dialog = pane.createDialog(this, "Confirmação de Logout");
            dialog.setVisible(true);

            Object selectedValue = pane.getValue();
            if ("Sim".equals(selectedValue)) {
                this.dispose();
                new TelaEscolhaLogin().mostrar(tipo -> {
                    switch (tipo) {
                        case "Administrador" -> new TelaLoginAdm().mostrar();
                        case "Aluno" -> new TelaLoginAluno().mostrar("", "");
                        case "Professor" -> new TelaLoginProfessor().mostrar();
                    }
                });
            }
        });
    }

    private String[] gerarCabecalhos(String item) {
        return switch (item) {
            case "Notas" -> new String[]{"Disciplina", "Nota - NP1", "Nota - NP2", "Média"};
            case "Dados do Aluno" -> new String[]{"Nome", "CPF", "Curso", "Matrícula"};
            case "Médias / Exame Final" -> new String[]{"Disciplina", "Média Final", "Nota do Exame", "Situação"};
            case "Publicações" -> new String[]{"Assunto", "Professor", "Ano da Publicação"};
            case "Dados do Curso" -> new String[]{"Disciplinas", "Carga Horária"};
            default -> new String[]{" ", " "};
        };
    }

    private void carregarNotasDoBanco(int idAluno) {
        JTable tabelaNotas = tabelasPorAba.get("Notas");
        if (tabelaNotas == null) {
            System.err.println("Tabela Notas não encontrada!");
            return;
        }

        DefaultTableModel modelo = (DefaultTableModel) tabelaNotas.getModel();
        modelo.setRowCount(0);

        try {
            for (Object[] linha : controller.buscarNotasAluno(idAluno)) {
                modelo.addRow(linha);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar notas: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarDadosAluno(int idAluno) {
        JTable tabelaDados = tabelasPorAba.get("Dados do Aluno");
        if (tabelaDados == null) return;
        DefaultTableModel modelo = (DefaultTableModel) tabelaDados.getModel();
        modelo.setRowCount(0);
        Object[] dados = controller.buscarDadosAluno(idAluno);
        if (dados[0] != null) {
            modelo.addRow(dados);
        }
    }

    private void carregarMediasExameFinal(int idAluno) {
        JTable tabela = tabelasPorAba.get("Médias / Exame Final");
        if (tabela == null) return;
        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        modelo.setRowCount(0);
        for (Object[] linha : controller.buscarMediasExameFinal(idAluno)) {
            modelo.addRow(linha);
        }
    }

    private void carregarPublicacoes(int idAluno) {
        JTable tabela = tabelasPorAba.get("Publicações");
        if (tabela == null) return;
        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        modelo.setRowCount(0);
        for (Object[] linha : controller.buscarPublicacoesAluno(idAluno)) {
            modelo.addRow(linha);
        }
    }

    private void carregarDadosCurso(int idAluno) {
        JTable tabela = tabelasPorAba.get("Dados do Curso");
        if (tabela == null) return;
        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        modelo.setRowCount(0);

        Object[] dadosCurso = controller.buscarDadosCurso(idAluno);
        if (infoTable != null) {
            String curso = dadosCurso[1] != null ? "Curso: " + dadosCurso[1] : "Curso: -";
            String duracao = dadosCurso[2] != null ? "Duração: " + dadosCurso[2] : "Duração: -";
            infoTable.setValueAt(curso, 0, 0);
            infoTable.setValueAt(duracao, 0, 1);
        }

        for (Object[] linha : controller.buscarDisciplinasECargaHoraria(idAluno)) {
            modelo.addRow(linha);
        }
    }
}