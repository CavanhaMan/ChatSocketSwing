package trabalho1_sd_atual;

import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;

public class Mensagem implements Serializable{
    
    private String nomeDoCliente;
    private String textoDaMensagem;
    private String nomeUsuarioprivado;
    private Set<String> usuariosConectados =  new HashSet<>();
    private Acao acaoDoCliente;
    
    public enum Acao{
        CONECTAR, DESCONECTAR, ENVIAR_PARA_UM, ENVIAR_PARA_TODOS, USUARIOS_ONLINE
    }

    public String getNomeDoCliente() {return nomeDoCliente;}
    public void setNomeDoCliente(String nomeDoCliente) {this.nomeDoCliente = nomeDoCliente;}
    public String getTextoDaMensagem() {return textoDaMensagem;}
    public void setTextoDaMensagem(String textoDaMensagem) {this.textoDaMensagem = textoDaMensagem;}
    public String getNomeUsuarioprivado() {return nomeUsuarioprivado;}
    public void setNomeUsuarioprivado(String nomeUsuarioprivado) {this.nomeUsuarioprivado = nomeUsuarioprivado;}
    public Set<String> getUsuariosConectados() {return usuariosConectados;}
    public void setUsuariosConectados(Set<String> usuariosConectados) {this.usuariosConectados = usuariosConectados;}
    public Acao getAcaoDoCliente() {return acaoDoCliente;}
    public void setAcaoDoCliente(Acao acaoDoCliente) {this.acaoDoCliente = acaoDoCliente;}    
}
