package cliente.models;

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String Cliente;
	private Arquivo arquivo;
	private String msg;
	
	public Message(String cliente, Arquivo arquivo) {
		Cliente = cliente;
		this.setArquivo(arquivo);
	}
	
	public Message(String cliente, String msg) {
		Cliente = cliente;
		this.msg = msg;
	}
	
	public Message(String cliente) {
		Cliente = cliente;
	}
	
	public Message() {}

	public String getCliente() {
		return Cliente;
	}

	public void setCliente(String cliente) {
		Cliente = cliente;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Arquivo getArquivo() {
		return arquivo;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}	
}
