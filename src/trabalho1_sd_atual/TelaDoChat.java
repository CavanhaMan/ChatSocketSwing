package trabalho1_sd_atual;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import trabalho1_sd_atual.Mensagem.Acao;
import trabalho1_sd_atual.Service.ClienteService;

public class TelaDoChat extends javax.swing.JFrame {

    private Socket socket;
    private Mensagem mensagem;
    private ClienteService cliente;

    public TelaDoChat() {
        initComponents();
    }

    private class ListenerSocket implements Runnable {

        private ObjectInputStream entrada;

        public ListenerSocket(Socket socket) {
            try {
                this.entrada = new ObjectInputStream(socket.getInputStream());
            } catch (IOException ex) {
                System.out.println(ex.getMessage() + " Local: trabalho1_sd_atual.TelaDoChat.ListenerSocket.<init>()");
            }
        }

        @Override
        public void run() {
            Mensagem mensagem = null;
            try {
                while ((mensagem = (Mensagem) entrada.readObject()) != null) {
                    Mensagem.Acao acao = mensagem.getAcaoDoCliente();

                    switch (acao) {
                        case CONECTAR:
                            conectar(mensagem);
                            break;
                        case DESCONECTAR:
                            desconectar();
                            socket.close();
                            break;
                        case ENVIAR_PARA_UM:
                            recebendo_mensagem(mensagem);
                            break;
                        case USUARIOS_ONLINE:
                            usuarios_online(mensagem);
                            break;
                    }
                }
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println(ex.getMessage() + " Local: trabalho1_sd_atual.TelaDoChat.ListenerSocket.run()");
            }
        }

    }

    private void conectar(Mensagem msg) {
        if (msg.getTextoDaMensagem().equals("NO")) {
            this.entraNome.setText("");
            JOptionPane.showMessageDialog(this, "Já existe um usuário conectado com este nome!\nTente novamente com outro nome.");
            return;
        }

        this.mensagem = msg;
        JOptionPane.showMessageDialog(this, "Olá " + msg.getNomeDoCliente() + "!\nVocê está conectado(a).");
        this.botaoConectar.setEnabled(false);
        this.entraNome.setEnabled(false);
        this.botaoDesconectar.setEnabled(true);
        this.usuariosConectados.setEnabled(true);
        this.campoDoChat.setEnabled(true);
        this.campoEntrarMensagens.setEnabled(true);
        this.botaoEnviar.setEnabled(true);
        this.botaoLimpar.setEnabled(true);
        this.campoDoChat.setEnabled(true);
    }

    private void desconectar() {
        this.entraNome.setText("");
        this.botaoConectar.setEnabled(true);
        this.entraNome.setEnabled(true);
        this.botaoDesconectar.setEnabled(false);
        this.usuariosConectados.setEnabled(false);
        this.campoDoChat.setEnabled(false);
        this.campoEntrarMensagens.setEnabled(false);
        this.campoEntrarMensagens.setText("");
        this.botaoEnviar.setEnabled(false);
        this.botaoLimpar.setEnabled(false);
        this.campoDoChat.setEnabled(false);
        this.campoDoChat.setText("");
        this.campoEntrarMensagens.setText("");
        JOptionPane.showMessageDialog(this, "Desconectado!\nAté a próxima!!!");
    }

    private void recebendo_mensagem(Mensagem msg) {
        String teste = this.mensagem.getNomeDoCliente();
        if (teste.equals(msg.getNomeDoCliente())) {
            this.campoDoChat.append("Você disse => " + msg.getTextoDaMensagem() + "\n");
        } else {
            this.campoDoChat.append(msg.getNomeDoCliente() + " => " + msg.getTextoDaMensagem() + "\n");
        }
    }

    private void usuarios_online(Mensagem msg) {
        Set<String> nomes = msg.getUsuariosConectados();
        nomes.remove(msg.getNomeDoCliente());
        DefaultComboBoxModel listaComboBox = (DefaultComboBoxModel) usuariosConectados.getModel();

        for (String aux : nomes) {
            if (nomes.isEmpty()) {
                this.usuariosConectados.addItem(aux);
            } else {
                if (listaComboBox.getIndexOf(aux) == -1) {
                    this.usuariosConectados.addItem(aux);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        entraNome = new javax.swing.JTextField();
        botaoConectar = new javax.swing.JButton();
        botaoDesconectar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        usuariosConectados = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        campoDoChat = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        campoEntrarMensagens = new javax.swing.JTextArea();
        botaoEnviar = new javax.swing.JButton();
        botaoLimpar = new javax.swing.JButton();

        jCheckBox1.setText("jCheckBox1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/trabalho1_sd_atual/img/logoIFTM_P.png"))); // NOI18N

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel2.setFont(new java.awt.Font("Sitka Text", 0, 24)); // NOI18N
        jLabel2.setText("Nome ou Apelido:");

        entraNome.setFont(new java.awt.Font("Sitka Text", 0, 20)); // NOI18N
        entraNome.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        botaoConectar.setFont(new java.awt.Font("Sitka Text", 1, 18)); // NOI18N
        botaoConectar.setText("Conectar");
        botaoConectar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        botaoConectar.setBorderPainted(false);
        botaoConectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoConectarActionPerformed(evt);
            }
        });

        botaoDesconectar.setFont(new java.awt.Font("Sitka Text", 1, 18)); // NOI18N
        botaoDesconectar.setText("Sair");
        botaoDesconectar.setBorderPainted(false);
        botaoDesconectar.setEnabled(false);
        botaoDesconectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoDesconectarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(entraNome, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(botaoConectar, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(botaoDesconectar, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(botaoConectar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botaoDesconectar, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(entraNome)
                        .addComponent(jLabel2)))
                .addGap(12, 12, 12))
        );

        jLabel3.setFont(new java.awt.Font("Sitka Text", 0, 24)); // NOI18N
        jLabel3.setText("Usuários conectados:");

        usuariosConectados.setFont(new java.awt.Font("Sitka Text", 0, 24)); // NOI18N
        usuariosConectados.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {"Selecione"}));
        usuariosConectados.setEnabled(false);
        usuariosConectados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usuariosConectadosActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        campoDoChat.setEditable(false);
        campoDoChat.setColumns(20);
        campoDoChat.setFont(new java.awt.Font("Adobe Fan Heiti Std B", 0, 18)); // NOI18N
        campoDoChat.setRows(5);
        campoDoChat.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        campoDoChat.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        campoDoChat.setEnabled(false);
        campoDoChat.setName(""); // NOI18N
        campoDoChat.setSelectionColor(new java.awt.Color(0, 0, 0));
        jScrollPane1.setViewportView(campoDoChat);

        campoEntrarMensagens.setColumns(20);
        campoEntrarMensagens.setFont(new java.awt.Font("Adobe Fan Heiti Std B", 0, 18)); // NOI18N
        campoEntrarMensagens.setRows(5);
        campoEntrarMensagens.setEnabled(false);
        jScrollPane2.setViewportView(campoEntrarMensagens);

        botaoEnviar.setFont(new java.awt.Font("Sitka Text", 1, 18)); // NOI18N
        botaoEnviar.setText("Enviar");
        botaoEnviar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        botaoEnviar.setBorderPainted(false);
        botaoEnviar.setEnabled(false);
        botaoEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoEnviarActionPerformed(evt);
            }
        });

        botaoLimpar.setFont(new java.awt.Font("Sitka Text", 1, 18)); // NOI18N
        botaoLimpar.setText("Limpar");
        botaoLimpar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        botaoLimpar.setBorderPainted(false);
        botaoLimpar.setEnabled(false);
        botaoLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoLimparActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(botaoEnviar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(botaoLimpar, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(botaoEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(botaoLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(9, 9, 9))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(150, 150, 150)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(usuariosConectados, javax.swing.GroupLayout.PREFERRED_SIZE, 567, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel1)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(usuariosConectados, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botaoConectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoConectarActionPerformed
        String nome = entraNome.getText();

        if (!nome.isEmpty()) {
            this.mensagem = new Mensagem();
            this.mensagem.setAcaoDoCliente(Mensagem.Acao.CONECTAR);
            this.mensagem.setNomeDoCliente(nome);

            this.cliente = new ClienteService();
            this.socket = this.cliente.clienteConectar();

            new Thread(new ListenerSocket(this.socket)).start();

            this.cliente.clienteEnviar(mensagem);
        }
    }//GEN-LAST:event_botaoConectarActionPerformed

    private void botaoDesconectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoDesconectarActionPerformed
        Mensagem  mensagem = new Mensagem();
        mensagem.setNomeDoCliente(this.mensagem.getNomeDoCliente());
        this.mensagem.setAcaoDoCliente(Mensagem.Acao.DESCONECTAR);
        this.cliente.clienteEnviar(this.mensagem);
        desconectar();
    }//GEN-LAST:event_botaoDesconectarActionPerformed

    private void botaoEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoEnviarActionPerformed
        String texto = this.campoEntrarMensagens.getText();
        String nome = this.mensagem.getNomeDoCliente();
        this.mensagem = new Mensagem();

        String testeSelecionado = (String) usuariosConectados.getSelectedItem();
        if (testeSelecionado.equals("Selecione")) {
            this.mensagem.setAcaoDoCliente(Acao.ENVIAR_PARA_TODOS);
        } else {
            this.mensagem.setNomeUsuarioprivado(testeSelecionado);
            this.mensagem.setAcaoDoCliente(Acao.ENVIAR_PARA_UM);
        }

        if (!texto.isEmpty()) {
            this.mensagem.setNomeDoCliente(nome);
            this.mensagem.setTextoDaMensagem(texto);
            this.cliente.clienteEnviar(this.mensagem);
        }

        this.campoEntrarMensagens.setText("");
    }//GEN-LAST:event_botaoEnviarActionPerformed

    private void botaoLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoLimparActionPerformed
        this.campoEntrarMensagens.setText("");
    }//GEN-LAST:event_botaoLimparActionPerformed

    private void usuariosConectadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usuariosConectadosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_usuariosConectadosActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botaoConectar;
    private javax.swing.JButton botaoDesconectar;
    private javax.swing.JButton botaoEnviar;
    private javax.swing.JButton botaoLimpar;
    private javax.swing.JTextArea campoDoChat;
    private javax.swing.JTextArea campoEntrarMensagens;
    private javax.swing.JTextField entraNome;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox<String> usuariosConectados;
    // End of variables declaration//GEN-END:variables
}
