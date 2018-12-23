package gui;

import arquivo.ArquivoPascal;
import analises.AnaliseSintatica;
import analises.AnaliseLexica;
import analises.AnaliseSemantica;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import tabela.AnaliseTokens;
import tabela.Expressoes;
import tabela.Token;
import translaters.PortugolToken;
import translaters.PortugolTranslater;

public class NitroPascal extends javax.swing.JFrame {

    private UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
    private static final long serialVersionUID = 1L;
    public static ArrayList<Token> saidaLexica = null;
    private String codigo = "";
    public static int indexToken = 0;
    private static int linha = 0;
    private SimpleAttributeSet READ_WRITE = new SimpleAttributeSet();
    private SimpleAttributeSet STRING = new SimpleAttributeSet();
    private SimpleAttributeSet FOR_TO_DO = new SimpleAttributeSet();
    private SimpleAttributeSet IF = new SimpleAttributeSet();
    private SimpleAttributeSet OPERADOR_LOGICO = new SimpleAttributeSet();
    private SimpleAttributeSet TIPO = new SimpleAttributeSet();
    private SimpleAttributeSet NUMERO = new SimpleAttributeSet();
    private SimpleAttributeSet IDENTIFICADOR = new SimpleAttributeSet();
    private SimpleAttributeSet PALAVRA_CHAVE = new SimpleAttributeSet();

    private int tempo = 1000;

    public NitroPascal() {
        initComponents();
        this.setTitle("NitroPascal");
        this.setExtendedState(NitroPascal.MAXIMIZED_BOTH);

        look1.setText(looks[0].getName());
        look2.setText(looks[1].getName());

        try {
            if (System.getProperties().toString().contains("WINDOWS")) {
                look3.setText(looks[2].getName());
                look4.setText(looks[3].getName());
                UIManager.setLookAndFeel(looks[0].getClassName());
                mudarLAF(2);
            } else {
                UIManager.setLookAndFeel(looks[0].getClassName());
                look3.setEnabled(false);
                look4.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        StyleConstants.setForeground(PALAVRA_CHAVE, Color.BLUE);
        StyleConstants.setBold(PALAVRA_CHAVE, true);

        StyleConstants.setForeground(IDENTIFICADOR, Color.GREEN);
        StyleConstants.setBold(IDENTIFICADOR, true);

        StyleConstants.setBold(NUMERO, true);
        StyleConstants.setForeground(NUMERO, Color.BLACK);

        StyleConstants.setForeground(TIPO, Color.CYAN);
        StyleConstants.setBold(TIPO, true);

        StyleConstants.setForeground(OPERADOR_LOGICO, Color.RED);
        StyleConstants.setBold(OPERADOR_LOGICO, true);

        StyleConstants.setForeground(IF, Color.BLUE);
        StyleConstants.setBold(IF, true);

        StyleConstants.setForeground(FOR_TO_DO, Color.BLUE);
        StyleConstants.setBold(FOR_TO_DO, true);

        StyleConstants.setForeground(STRING, Color.ORANGE);
        StyleConstants.setBold(STRING, true);

        StyleConstants.setForeground(READ_WRITE, Color.GRAY);
        StyleConstants.setBold(READ_WRITE, true);

        compilar.setEnabled(false);

        textAreaCodigo.setVisible(false);
    }

    protected static void append(String s, AttributeSet attributes, JTextPane tp) {
        Document d = tp.getDocument();
        try {
            d.insertString(d.getLength(), s, attributes);
        } catch (BadLocationException ble) {
            JOptionPane.showMessageDialog(null, ble.getMessage());
        }
    }

    public void adicionarResultadoSintatica(Token t, boolean teste) {
        if (teste) {
            if (linha < t.getLinha()) {
                append("\n", null, areaCodigo);
            }
        }

        if (t.getTipo().equals(Expressoes.PALAVRA_RESERVADA) || t.getTipo().equals(Expressoes.TRUE_FALSE)) {
            if (t.getConteudo().equalsIgnoreCase("begin")) {
                append(t.getConteudo() + " ", PALAVRA_CHAVE, areaCodigo);
            } else {
                append(t.getConteudo() + " ", PALAVRA_CHAVE, areaCodigo);
            }
        } else {
            if (t.getTipo().equals(Expressoes.NUMERO)) {
                append(t.getConteudo() + " ", NUMERO, areaCodigo);
            } else {
                if ((t.getTipo().equals(Expressoes.IF)) || (t.getTipo().equals(Expressoes.THEN))) {
                    append(t.getConteudo() + " ", IF, areaCodigo);
                } else {
                    if (t.getTipo().equals(Expressoes.OPERADORES_LOGICOS)) {
                        append(t.getConteudo() + " ", OPERADOR_LOGICO, areaCodigo);
                    } else {
                        if (t.getTipo().equals(Expressoes.STRING)) {
                            append(t.getConteudo() + " ", STRING, areaCodigo);
                        } else {
                            if ((t.getTipo().equals(Expressoes.READ)) || (t.getTipo().equals(Expressoes.WRITE)) || (t.getTipo().equals(Expressoes.FECHA_PARENTESES))) {
                                append(t.getConteudo() + " ", READ_WRITE, areaCodigo);
                            } else {
                                if (t.getTipo().equals(Expressoes.TIPO)) {
                                    append(t.getConteudo() + " ", TIPO, areaCodigo);
                                } else {
                                    if ((t.getTipo().equals(Expressoes.FOR)) || (t.getTipo().equals(Expressoes.TO)) || (t.getTipo().equals(Expressoes.DO)) || (t.getTipo().equals(Expressoes.WHILE))) {
                                        append(t.getConteudo() + " ", FOR_TO_DO, areaCodigo);
                                    } else {
                                        if (t.getTipo().equals(Expressoes.IDENTIFICADOR)) {
                                            append(t.getConteudo() + " ", IDENTIFICADOR, areaCodigo);
                                        } else {
                                            if (t.getTipo().equals(Expressoes.PONTO_E_VIRGULA)) {
                                                append(t.getConteudo() + " ", OPERADOR_LOGICO, areaCodigo);
                                            } else {
                                                if ((t.getTipo().equals(Expressoes.OPERADORES_MATEMATICOS) ||
                                                        (t.getTipo().equals(Expressoes.PONTO) || (t.getTipo().equals(Expressoes.DOIS_PONTOS) || (t.getTipo().equals(Expressoes.ATRIBUICAO)))))) {
                                                    append(t.getConteudo() + " ", OPERADOR_LOGICO, areaCodigo);
                                                } else {
                                                    append(t.getConteudo() + " ", null, areaCodigo);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        linha = t.getLinha();
    }

    public void mudarLAF(int tipo) {
        try {
            UIManager.setLookAndFeel(looks[tipo].getClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NitroPascal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(NitroPascal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(NitroPascal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(NitroPascal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        abaCodigo = new javax.swing.JTabbedPane();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        panelCodigo = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        textAreaLinhas = new javax.swing.JTextArea();
        jPanel16 = new javax.swing.JPanel();
        areaCodigo = new javax.swing.JTextPane();
        textAreaCodigo = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        areaLexica = new javax.swing.JTextPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        areaSintatica = new javax.swing.JTextPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        areaSemantica = new javax.swing.JTextPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        areaPortugol = new javax.swing.JTextPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        abrir = new javax.swing.JMenuItem();
        salvar = new javax.swing.JMenuItem();
        sair = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        compilar = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        look1 = new javax.swing.JMenuItem();
        look2 = new javax.swing.JMenuItem();
        look3 = new javax.swing.JMenuItem();
        look4 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        info = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelCodigo.setLayout(new java.awt.BorderLayout());

        textAreaLinhas.setBackground(new java.awt.Color(204, 204, 204));
        textAreaLinhas.setColumns(5);
        textAreaLinhas.setEditable(false);
        textAreaLinhas.setFont(new java.awt.Font("Verdana", 0, 12));
        textAreaLinhas.setRows(5);
        textAreaLinhas.setFocusable(false);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(textAreaLinhas, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(textAreaLinhas, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
        );

        panelCodigo.add(jPanel15, java.awt.BorderLayout.LINE_START);

        areaCodigo.setFont(new java.awt.Font("Verdana", 0, 12));
        areaCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                areaCodigoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                areaCodigoKeyTyped(evt);
            }
        });

        textAreaCodigo.setColumns(20);
        textAreaCodigo.setRows(5);
        textAreaCodigo.setFocusable(false);
        textAreaCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textAreaCodigoKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(textAreaCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(693, Short.MAX_VALUE))
            .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(areaCodigo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 802, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(textAreaCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(areaCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE))
        );

        panelCodigo.add(jPanel16, java.awt.BorderLayout.CENTER);

        jScrollPane6.setViewportView(panelCodigo);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE)
        );

        abaCodigo.addTab("Arquivo", jPanel13);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Saída da Análise Léxica"));

        areaLexica.setEditable(false);
        areaLexica.setFont(new java.awt.Font("Verdana", 0, 12));
        jScrollPane2.setViewportView(areaLexica);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 863, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("L\u00e9xica", jPanel3);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Saída da Análise Sintática"));

        areaSintatica.setEditable(false);
        areaSintatica.setFont(new java.awt.Font("Verdana", 0, 12));
        jScrollPane3.setViewportView(areaSintatica);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 863, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Sint\u00e1tica", jPanel4);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Saída da Análise Semântica"));

        areaSemantica.setEditable(false);
        areaSemantica.setFont(new java.awt.Font("Verdana", 0, 12));
        jScrollPane4.setViewportView(areaSemantica);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 863, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Sem\u00e2ntica", jPanel5);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE)
        );

        abaCodigo.addTab("An\u00e1lises", jPanel2);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Codigo em Portugol"));

        areaPortugol.setEditable(false);
        areaPortugol.setFont(new java.awt.Font("Verdana", 0, 12));
        jScrollPane5.setViewportView(areaPortugol);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 868, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 884, Short.MAX_VALUE)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 502, Short.MAX_VALUE)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        abaCodigo.addTab("Portugol", jPanel1);

        jMenu1.setText("Arquivo"); // NOI18N

        abrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        abrir.setText("Abrir"); // NOI18N
        abrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirActionPerformed(evt);
            }
        });
        jMenu1.add(abrir);

        salvar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        salvar.setText("Salvar"); // NOI18N
        salvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salvarActionPerformed(evt);
            }
        });
        jMenu1.add(salvar);

        sair.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F12, 0));
        sair.setText("Sair"); // NOI18N
        sair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sairActionPerformed(evt);
            }
        });
        jMenu1.add(sair);

        jMenuBar1.add(jMenu1);

        jMenu4.setText("Compilador"); // NOI18N

        compilar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, 0));
        compilar.setText("Compilar"); // NOI18N
        compilar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compilarActionPerformed(evt);
            }
        });
        jMenu4.add(compilar);

        jMenuBar1.add(jMenu4);

        jMenu5.setText("Look"); // NOI18N

        look1.setText("Item"); // NOI18N
        look1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                look1ActionPerformed(evt);
            }
        });
        jMenu5.add(look1);

        look2.setText("Item"); // NOI18N
        look2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                look2ActionPerformed(evt);
            }
        });
        jMenu5.add(look2);

        look3.setText("Item"); // NOI18N
        look3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                look3ActionPerformed(evt);
            }
        });
        jMenu5.add(look3);

        look4.setText("Item"); // NOI18N
        look4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                look4ActionPerformed(evt);
            }
        });
        jMenu5.add(look4);

        jMenuBar1.add(jMenu5);

        jMenu2.setText("Ajuda"); // NOI18N

        info.setText("Informações"); // NOI18N
        info.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                infoActionPerformed(evt);
            }
        });
        jMenu2.add(info);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(abaCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, 889, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(abaCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void abrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abrirActionPerformed
        this.textAreaCodigo.setText("");
        this.textAreaLinhas.setText("");
        this.areaCodigo.setText("");
        this.areaLexica.setText("");
        this.areaSintatica.setText("");
        this.areaSemantica.setText("");
        this.areaPortugol.setText("");

        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        String cod = ArquivoPascal.lerArquivo(".");
        this.codigo = cod;
        areaCodigo.setText(cod.replace("\t", "    "));
        abaCodigo.setTitleAt(0, ArquivoPascal.getNomeArquivo());

        textAreaCodigo.setText(cod.replace("\t", "    "));
        textAreaLinhas.setText("");
        for (int i = 1; i < textAreaCodigo.getLineCount(); i++) {
            textAreaLinhas.append(i + "\n");
        }

        abaCodigo.setTitleAt(0, ArquivoPascal.getNomeArquivo());

        compilar.setEnabled(true);
        this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_abrirActionPerformed

    private void sairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sairActionPerformed
        System.exit(0);
}//GEN-LAST:event_sairActionPerformed
    private void compilarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compilarActionPerformed
        try {
            new Thread("splash") {

                @Override
                @SuppressWarnings("static-access")
                public void run() {
                    TelaInformacoes jf = new TelaInformacoes();

                    //aparece a msg por um tempo
                    jf.setText("Análise Léxica...");
                    try {
                        Thread.sleep(tempo);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(NitroPascal.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    ArrayList<Token> tokensLexica = new ArrayList<Token>();
                    saidaLexica = new ArrayList<Token>();

                    tokensLexica = AnaliseTokens.lerSequencialmente(areaCodigo.getText());
                    saidaLexica = AnaliseLexica.getAnalizadorLexico(tokensLexica).getTokens();
                    areaCodigo.setText("");

                    for (Token tok : saidaLexica) {
                        append("TOKEN: ", PALAVRA_CHAVE, areaLexica);
                        if (tok.getTipo().equalsIgnoreCase(Expressoes.NAO_RECONHECIDO)) {
                            append(String.format("%-15s", tok.getConteudo()), OPERADOR_LOGICO, areaLexica);
                        } else {
                            append(String.format("%-15s", tok.getConteudo()), NUMERO, areaLexica);
                        }
                        append(" TIPO: ", PALAVRA_CHAVE, areaLexica);
                        if (tok.getTipo().equalsIgnoreCase(Expressoes.NAO_RECONHECIDO)) {
                            append(tok.getTipo() + "\n", OPERADOR_LOGICO, areaLexica);
                        } else {
                            append(tok.getTipo() + "\n", NUMERO, areaLexica);
                        }
                        append("INICIO: ", PALAVRA_CHAVE, areaLexica);
                        if (tok.getTipo().equalsIgnoreCase(Expressoes.NAO_RECONHECIDO)) {
                            append(String.format("%4s", tok.getInicio() + ""), OPERADOR_LOGICO, areaLexica);
                        } else {
                            append(String.format("%4s", tok.getInicio() + ""), NUMERO, areaLexica);
                        }
                        append(" FIM: ", PALAVRA_CHAVE, areaLexica);
                        if (tok.getTipo().equalsIgnoreCase(Expressoes.NAO_RECONHECIDO)) {
                            append(String.format("%4s", tok.getFim() + ""), OPERADOR_LOGICO, areaLexica);
                        } else {
                            append(String.format("%4s", tok.getFim() + ""), NUMERO, areaLexica);
                        }
                        append(" LINHA: ", PALAVRA_CHAVE, areaLexica);
                        if (tok.getTipo().equalsIgnoreCase(Expressoes.NAO_RECONHECIDO)) {
                            append(String.format("%4s", tok.getLinha() + "") + "\n\n", OPERADOR_LOGICO, areaLexica);
                        } else {
                            append(String.format("%4s", tok.getLinha() + "") + "\n\n", NUMERO, areaLexica);
                        }

                        if (saidaLexica.indexOf(tok) == 0) {
                            adicionarResultadoSintatica(tok, false);
                        } else {
                            adicionarResultadoSintatica(tok, true);
                        }

                    }

                    for (Token token : AnaliseLexica.getErroLexico()) {
                        append("Token " + token.getConteudo() + " não reconhecido na linha " + token.getLinha() + ". \n", OPERADOR_LOGICO, areaLexica);
                    }

                    //aparece a msg por um tempo
                    jf.setText("Análise Sintática...");
                    try {
                        Thread.sleep(tempo);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(NitroPascal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    AnaliseSintatica as = new AnaliseSintatica(saidaLexica);

                    //aparece a msg por um tempo
                    jf.setText("Iniciando Descida...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(NitroPascal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    as.iniciarDescidaRecursiva();

                    //aparece a msg por um tempo
                    jf.setText("Fim Descida...");
                    try {
                        Thread.sleep(tempo);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(NitroPascal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ArrayList<String> lidosSintatica = as.getLidosSintatica();

                    for (String lidos : lidosSintatica) {
                        append("LEU: ", PALAVRA_CHAVE, areaSintatica);
                        append(lidos, NUMERO, areaSintatica);
                        append("\n", NUMERO, areaSintatica);
                    }

                    //aparece a msg por um tempo
                    jf.setText("Análise Semântica...");
                    try {
                        Thread.sleep(tempo);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(NitroPascal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    AnaliseSemantica asem = new AnaliseSemantica(saidaLexica);
                    ArrayList<Token> saidaSem = asem.adicionarTiposPascal();

                    areaSemantica.setText("");
                    for (String s : asem.getMsgErro()) {
                        if (!s.startsWith("ERRO")) {
                            append(s + "\n", PALAVRA_CHAVE, areaSemantica);
                        } else {
                            append(s + "\n", OPERADOR_LOGICO, areaSemantica);
                        }
                    }

                    //aparece a msg por um tempo
                    jf.setText("Gerando código Portugol...");
                    try {
                        Thread.sleep(tempo);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(NitroPascal.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    PortugolTranslater.setInstanciaNull(); //reseta a instancia de portugoltranslater
                    ArrayList<PortugolToken> saidaPortugol = PortugolTranslater.getTranslater().translate(saidaSem);
                    areaPortugol.setText("");
                    for (PortugolToken portugolToken : saidaPortugol) {
                        append(" " + portugolToken.getConteudo() + " ", PALAVRA_CHAVE, areaPortugol);
                    }

                    //coloca os cursores nas posições iniciais
                    areaLexica.setCaretPosition(0);
                    areaSintatica.setCaretPosition(0);
                    areaSemantica.setCaretPosition(0);
                    areaPortugol.setCaretPosition(0);

                    //esvazia os arrays
                    saidaLexica.clear();
                    saidaPortugol.clear();
                    tokensLexica.clear();
                    saidaSem.clear();

                    //seta a aba ativa como sendo a aba de código
                    abaCodigo.setSelectedIndex(0);

                    jf.dispose();
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
}//GEN-LAST:event_compilarActionPerformed

    private void salvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salvarActionPerformed
        ArquivoPascal.salvarArquivo(areaCodigo.getText());
    }//GEN-LAST:event_salvarActionPerformed

    private void look1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_look1ActionPerformed
        mudarLAF(0);
    }//GEN-LAST:event_look1ActionPerformed

    private void look2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_look2ActionPerformed
        mudarLAF(1);
    }//GEN-LAST:event_look2ActionPerformed

    private void look3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_look3ActionPerformed
        mudarLAF(2);
    }//GEN-LAST:event_look3ActionPerformed

    private void look4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_look4ActionPerformed
        mudarLAF(3);
    }//GEN-LAST:event_look4ActionPerformed

    private void infoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_infoActionPerformed
        JOptionPane.showMessageDialog(this, "Componentes: \nDiego Henrique\nWaldney Andrade\nBosco Moreira", "Desenvolvimento do Nitro Pascal", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_infoActionPerformed

    private void textAreaCodigoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textAreaCodigoKeyTyped
        textAreaCodigo.setText(areaCodigo.getText());
        jPanel4.setPreferredSize(new Dimension(0, textAreaCodigo.getLineCount() * 16));

        textAreaLinhas.setText("");
        for (int i = 1; i <= textAreaCodigo.getLineCount(); i++) {
            textAreaLinhas.append(i + "\n");
        }
}//GEN-LAST:event_textAreaCodigoKeyTyped

    private void areaCodigoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_areaCodigoKeyPressed
    }//GEN-LAST:event_areaCodigoKeyPressed

    private void areaCodigoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_areaCodigoKeyTyped
        textAreaCodigoKeyTyped(evt);
    }//GEN-LAST:event_areaCodigoKeyTyped

//    public static void main(String args[]) {
//        java.awt.EventQueue.invokeLater(new Runnable() {
//
//            public void run() {
//                new NitroPascal().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane abaCodigo;
    private javax.swing.JMenuItem abrir;
    private javax.swing.JTextPane areaCodigo;
    private javax.swing.JTextPane areaLexica;
    private javax.swing.JTextPane areaPortugol;
    private javax.swing.JTextPane areaSemantica;
    private javax.swing.JTextPane areaSintatica;
    private javax.swing.JMenuItem compilar;
    private javax.swing.JMenuItem info;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JMenuItem look1;
    private javax.swing.JMenuItem look2;
    private javax.swing.JMenuItem look3;
    private javax.swing.JMenuItem look4;
    private javax.swing.JPanel panelCodigo;
    private javax.swing.JMenuItem sair;
    private javax.swing.JMenuItem salvar;
    private javax.swing.JTextArea textAreaCodigo;
    private javax.swing.JTextArea textAreaLinhas;
    // End of variables declaration//GEN-END:variables
}
