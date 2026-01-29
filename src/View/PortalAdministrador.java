package View;

import Controller.AdministradorController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PortalAdministrador extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);
    private final String nomeAdministrador;

    private final JTable tabelaAlunos = new JTable();
    private final JTable tabelaProfessores = new JTable();
    private final JTable tabelaMatriculas = new JTable();
    private final JTable tabelaCursos = new JTable();
    private final JTable tabelaTurmas = new JTable();
    private final JTable tabelaDisciplinas = new JTable();
    private final JTable tabelaAulas = new JTable();
    private final JTable tabelaNotas = new JTable();
    private final JTable tabelaPublicacoes = new JTable();

    private final AdministradorController controller;

    public PortalAdministrador(int idAdministrador, String nomeAdministrador) {
        this.nomeAdministrador = nomeAdministrador;
        this.controller = new AdministradorController(contentPanel);

        setTitle("Portal do Administrador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
    }
    private class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setForeground(Color.WHITE);
            setBackground(new Color(40, 40, 40));
            setFont(new Font("SansSerif", Font.BOLD, 13));
            setPreferredSize(new Dimension(160, 40));
            setMaximumSize(new Dimension(180, 40));

            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    setBackground(new Color(70, 70, 70));
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    setBackground(new Color(40, 40, 40));
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
        topPanel.setBackground(new Color(30, 30, 30));
        topPanel.setPreferredSize(new Dimension(1000, 50));
        JLabel titleLabel = new JLabel("  Portal do Administrador - " + nomeAdministrador);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        topPanel.add(titleLabel, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setPreferredSize(new Dimension(220, 0));
        menuPanel.setBackground(new Color(20, 20, 20));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        String[] opcoes = {"Alunos", "Professores", "Matrículas", "Cursos", "Turmas", "Disciplinas", "Aulas", "Notas", "Publicações"};

        for (String opcao : opcoes) {
            RoundedButton botao = new RoundedButton(opcao);
            botao.setAlignmentX(Component.CENTER_ALIGNMENT);
            botao.addActionListener(e -> {
                cardLayout.show(contentPanel, opcao);
                controller.carregarDados(opcao, getTabelaCorrespondente(opcao));
            });
            menuPanel.add(botao);
            menuPanel.add(Box.createVerticalStrut(15));
        }

        menuPanel.add(Box.createVerticalGlue());

        RoundedButton logoutButton = new RoundedButton("Logout");
        logoutButton.setFont(new Font("SansSerif", Font.PLAIN, 13));
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.addActionListener((ActionEvent e) -> {
            UIManager.put("OptionPane.yesButtonText", "Sim");
            UIManager.put("OptionPane.noButtonText", "Não");

            int escolha = JOptionPane.showConfirmDialog(this, "Deseja realmente sair?", "Logout", JOptionPane.YES_NO_OPTION);
            if (escolha == JOptionPane.YES_OPTION) {
                dispose();
                SwingUtilities.invokeLater(() -> new TelaLoginAdm().mostrar());
            }
        });

        menuPanel.add(logoutButton);
        add(menuPanel, BorderLayout.WEST);

        contentPanel.add(criarPainelComTabela(tabelaAlunos, "Alunos"), "Alunos");
        contentPanel.add(criarPainelComTabela(tabelaProfessores, "Professores"), "Professores");
        contentPanel.add(criarPainelComTabela(tabelaMatriculas, "Matrículas"), "Matrículas");
        contentPanel.add(criarPainelComTabela(tabelaCursos, "Cursos"), "Cursos");
        contentPanel.add(criarPainelComTabela(tabelaTurmas, "Turmas"), "Turmas");
        contentPanel.add(criarPainelComTabela(tabelaDisciplinas, "Disciplinas"), "Disciplinas");
        contentPanel.add(criarPainelComTabela(tabelaAulas, "Aulas"), "Aulas");
        contentPanel.add(criarPainelComTabela(tabelaNotas, "Notas"), "Notas");
        contentPanel.add(criarPainelComTabela(tabelaPublicacoes, "Publicações"), "Publicações");

        add(contentPanel, BorderLayout.CENTER);

        cardLayout.show(contentPanel, "Alunos");
        controller.carregarDados("Alunos", tabelaAlunos);
    }

    private JPanel criarPainelComTabela(JTable tabela, String titulo) {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(50, 50, 50));

        String[] colunas = switch (titulo) {
            case "Alunos" -> new String[]{"Nome", "CPF", "ID Curso", "ID Turma"};
            case "Professores" -> new String[]{"ID Professor", "Nome", "Email", "CPF", "Senha"};
            case "Matrículas" -> new String[]{"ID Aluno", "ID Curso", "Data Matrícula", "RA"};
            case "Cursos" -> new String[]{"ID Curso", "Nome", "Tipo", "Duração"};
            case "Turmas" -> new String[]{"ID Turma", "Turno", "Semestre", "ID Curso"};
            case "Disciplinas" -> new String[]{"ID Disciplina", "Nome", "ID Curso", "ID Turma", "ID Professor"};
            case "Aulas" -> new String[]{"ID Aula", "Carga Horária", "Disciplina", "ID Professor"};
            case "Notas" -> new String[]{"ID Aluno", "ID Disciplina", "Nota NP1", "Nota NP2", "Média Final", "Exame Final", "Situação"};
            case "Publicações" -> new String[]{"ID Publicação", "Título", "Ano", "ID Aluno", "ID Professor"};
            default -> new String[]{"Coluna 1", "Coluna 2"};
        };

        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela.setModel(model);
        tabela.setFillsViewportHeight(true);
        tabela.setRowHeight(28);
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tabela.setBackground(Color.WHITE);
        tabela.setForeground(Color.BLACK);
        tabela.setGridColor(Color.GRAY);

        JTableHeader cabecalho = tabela.getTableHeader();
        cabecalho.setFont(new Font("SansSerif", Font.BOLD, 14));
        cabecalho.setBackground(new Color(30, 30, 30));
        cabecalho.setForeground(Color.WHITE);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tabela.setRowSorter(sorter);

        JLabel labelPesquisa = new JLabel("🔍 Pesquisar: ");
        labelPesquisa.setForeground(Color.WHITE);
        labelPesquisa.setFont(new Font("SansSerif", Font.BOLD, 16));

        JTextField campoBusca = new JTextField();
        campoBusca.setPreferredSize(new Dimension(250, 35));
        campoBusca.setFont(new Font("SansSerif", Font.PLAIN, 16));
        campoBusca.setBackground(new Color(50, 50, 50));
        campoBusca.setForeground(Color.WHITE);
        campoBusca.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));

        campoBusca.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String filtro = campoBusca.getText().toLowerCase();
                TableRowSorter<TableModel> sorter = new TableRowSorter<>(tabela.getModel());
                tabela.setRowSorter(sorter);
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + filtro, 0));
            }
        });

        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelPesquisa.setBackground(new Color(35, 35, 35));
        painelPesquisa.add(labelPesquisa);
        painelPesquisa.add(campoBusca);

        painel.add(painelPesquisa, BorderLayout.NORTH);

        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return painel;
    }

    private JTable getTabelaCorrespondente(String aba) {
        return switch (aba) {
            case "Alunos" -> tabelaAlunos;
            case "Professores" -> tabelaProfessores;
            case "Matrículas" -> tabelaMatriculas;
            case "Cursos" -> tabelaCursos;
            case "Turmas" -> tabelaTurmas;
            case "Disciplinas" -> tabelaDisciplinas;
            case "Aulas" -> tabelaAulas;
            case "Notas" -> tabelaNotas;
            case "Publicações" -> tabelaPublicacoes;
            default -> null;
        };
    }
}

