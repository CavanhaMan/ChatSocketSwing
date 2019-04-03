package trabalho1_sd_atual.Service;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import trabalho1_sd_atual.Mensagem;

public class ClienteService {
    private Socket conexao;
    private ObjectOutputStream saida;
    
    public Socket clienteConectar(){
        try {
            this.conexao = new Socket("localhost", 2000);
            this.saida = new ObjectOutputStream(conexao.getOutputStream());
        } catch (IOException ex) {
            System.out.println(ex.getMessage() + " Local: trabalho1_sd_atual.Service.ClienteService.clienteConectar()");
        }
        return conexao;
    }
    
    public void clienteEnviar(Mensagem msg){
        try {
            saida.writeObject(msg);
        } catch (IOException ex) {
            System.out.println(ex.getMessage() + " Local: trabalho1_sd_atual.Service.ClienteService.clienteEnviar()");
        }
    }
}
