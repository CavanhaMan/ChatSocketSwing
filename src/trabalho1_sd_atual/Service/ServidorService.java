package trabalho1_sd_atual.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import trabalho1_sd_atual.Mensagem;
import trabalho1_sd_atual.Mensagem.Acao;

public class ServidorService {

    private ServerSocket serverSocket;
    private Socket conexao;
    private Map<String, ObjectOutputStream> mapaUsuariosOnlines = new HashMap<String, ObjectOutputStream>();
    InetAddress ipMaquina, nomeMaquina;
    int portaRemota;
    FileWriter arquivo;
    PrintWriter gravarArq;
    String arq = "";

    public ServidorService() {
        try {

            serverSocket = new ServerSocket(2000);
            arquivo = new FileWriter("D:\\Cavanhaman\\ChatSocketSwing_TrabalhoSD1-master\\historico.txt");
            gravarArq = new PrintWriter(arquivo);

            while (true) {
                System.out.println("Aguardando conexão...");
                conexao = serverSocket.accept();
                System.out.println("Cliente conectou!");

                ipMaquina = conexao.getInetAddress();
                nomeMaquina = conexao.getLocalAddress();
                portaRemota = conexao.getPort();

                new Thread(new ListenerSocket(conexao)).start();
            }
        } catch (IOException ex) {System.out.println(ex.getMessage());}
    }

    private class ListenerSocket implements Runnable {

        private ObjectOutputStream saida;
        private ObjectInputStream entrada;

        public ListenerSocket(Socket s) {
            try {
                this.saida = new ObjectOutputStream(s.getOutputStream());
                this.entrada = new ObjectInputStream(s.getInputStream());
            } catch (IOException ex) {System.out.println(ex.getMessage());}
        }

        @Override
        public void run() {
            Mensagem mensagem = null;

            try {

                while ((mensagem = (Mensagem) entrada.readObject()) != null) {
                    Acao acao = mensagem.getAcaoDoCliente();

                    arq += "Nome da máquina: " + nomeMaquina + " / IP da máquina: " + ipMaquina + " / Porta usada: " + portaRemota + " / Mensagem: " + mensagem.getTextoDaMensagem() + "\n";
                    //gravarArq.println("Nome da máquina: " + nomeMaquina + " / IP da máquina: " + ipMaquina + " / Porta usada: " + portaRemota + " / Mensagem: " + mensagem.getTextoDaMensagem());
                    System.out.println(mensagem.getTextoDaMensagem());

                    switch (acao) {
                        case CONECTAR:
                            boolean estaConectado = conectar(mensagem, saida);
                            if (estaConectado) {
                                mapaUsuariosOnlines.put(mensagem.getNomeDoCliente(), saida);
                                enviarListaUsuarios();
                            }
                            break;
                        case DESCONECTAR:
                            desconectar(mensagem, saida);
                            enviarListaUsuarios();
                            return;
                        case ENVIAR_PARA_UM:
                            enviar_para_um(mensagem);
                            break;
                        case ENVIAR_PARA_TODOS:
                            enviar_para_todos(mensagem);
                            break;
                    }

                }
            } catch (IOException | ClassNotFoundException ex) {
                Mensagem m = new Mensagem();
                m.setNomeDoCliente(mensagem.getNomeDoCliente());
                desconectar(m, saida);
                enviarListaUsuarios();
                System.out.println(mensagem.getNomeDoCliente() + " deixou o chat.");
            } finally {
                try {
                    arquivo.write(arq);
                    if (mapaUsuariosOnlines.isEmpty()) {
                        arquivo.close();
                    }
                } catch (IOException ex) {Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);}
            }
        }
    }

    private boolean conectar(Mensagem msg, ObjectOutputStream s) {
        if (mapaUsuariosOnlines.size() == 0) {
            msg.setTextoDaMensagem("YES");
            enviar(msg, s);
            return true;
        }

        for (Map.Entry<String, ObjectOutputStream> kv : mapaUsuariosOnlines.entrySet()) {
            if (kv.getKey().equals(msg.getNomeDoCliente())) {
                msg.setTextoDaMensagem("NO");
                enviar(msg, s);
                return false;
            } else {
                msg.setTextoDaMensagem("YES");
                enviar(msg, s);
                return true;
            }
        }

        return false;
    }

    private void desconectar(Mensagem msg, ObjectOutputStream s) {
        mapaUsuariosOnlines.remove(msg.getNomeDoCliente());
        msg.setTextoDaMensagem(" saiu do chat.");
        msg.setAcaoDoCliente(Acao.ENVIAR_PARA_UM);

        enviar_para_todos(msg);
        System.out.println("Usuário " + msg.getNomeDoCliente() + " saiu da sala.");
    }

    private void enviar(Mensagem msg, ObjectOutputStream s) {
        try {
            s.writeObject(msg);
        } catch (IOException ex) {System.out.println(ex.getMessage() + " Local: trabalho1_sd_atual.Service.ServidorService.enviar_para_um()");}
    }

    private void enviar_para_um(Mensagem msg) {
        for (Map.Entry<String, ObjectOutputStream> aux : mapaUsuariosOnlines.entrySet()) {
            if (aux.getKey().equals(msg.getNomeUsuarioprivado()) || aux.getKey().equals(msg.getNomeDoCliente())) {
                try {
                    aux.getValue().writeObject(msg);
                } catch (IOException ex) {System.out.println(ex.getMessage() + " Local: trabalho1_sd_atual.Service.ServidorService.enviar_para_um()");}
            }
        }
    }

    private void enviar_para_todos(Mensagem msg) {
        for (Map.Entry<String, ObjectOutputStream> aux : mapaUsuariosOnlines.entrySet()) {
            msg.setAcaoDoCliente(Acao.ENVIAR_PARA_UM);
            try {
                aux.getValue().writeObject(msg);
            } catch (IOException ex) {System.out.println(ex.getMessage() + " Local: trabalho1_sd_atual.Service.ServidorService.enviar_para_todos()");}
        }
    }

    private void enviarListaUsuarios() {
        Set<String> listaOnlines = new HashSet<String>();
        for (Map.Entry<String, ObjectOutputStream> aux : mapaUsuariosOnlines.entrySet()) {
            listaOnlines.add(aux.getKey());
        }

        Mensagem msg = new Mensagem();
        msg.setAcaoDoCliente(Acao.USUARIOS_ONLINE);
        msg.setUsuariosConectados(listaOnlines);

        for (Map.Entry<String, ObjectOutputStream> aux : mapaUsuariosOnlines.entrySet()) {
            msg.setNomeDoCliente(aux.getKey());
            try {
                aux.getValue().writeObject(msg);
            } catch (IOException ex) {System.out.println(ex.getMessage() + " Local: trabalho1_sd_atual.Service.ServidorService.enviar_para_todos()");}
        }
    }
}
