package cliente.models;

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String nome;
	private Arquivo arquivo;
	private String msg;
	
	public Message(String nome, Arquivo arquivo) {
		this.nome = nome;
		this.setArquivo(arquivo);
	}
	
	public Message(String nome, String msg) {
		this.nome = nome;
		this.msg = msg;
	}
	
	public Message(String cliente) {
		nome = cliente;
	}
	
	public Message() {}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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
