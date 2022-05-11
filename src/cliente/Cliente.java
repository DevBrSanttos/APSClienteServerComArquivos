package cliente;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import cliente.models.Arquivo;
import cliente.models.Message;
import cliente.view.Index;

public class Cliente {

	private static final String SERVER_ADDRESS = "127.0.0.1";
	public static final int PORT = 3000;
	private Socket socket;
	private Index index;
	private Message message;
	private BufferedOutputStream bos;

	public Cliente(Index index) throws IOException {
		this.index = index;
		this.socket = new Socket(SERVER_ADDRESS, PORT);
		
		new Thread(new ListenerSocket(socket, index)).start();
		init();
	}

	public void init() throws IOException {
		String nome = "";
		while (nome.isEmpty())
			nome = JOptionPane.showInputDialog("Digite seu nome");

		this.index.setLabel(nome);
		message = new Message(nome);
		this.sendMessage(message);
	}

	public void sendMessageText(String txtMessage) throws IOException {
		this.sendMessage(new Message(this.message.getCliente(), txtMessage));
		this.index.updatedPanelMessage(this.message.getCliente() + ": " + txtMessage);
	}
	
	public void sendMessage(Message message) {
		try {

			bos = new BufferedOutputStream(this.socket.getOutputStream());
			byte[] bytea = this.serializarMessage(message);
			bos.write(bytea);
			bos.flush();
			
		} catch (Exception e) {
			System.out.println("Error ao enviar a mensagem: " + e);
		}
		
	}

	public void sendFile() throws FileNotFoundException {
		JFileChooser fileChooser = new JFileChooser();
		int opt = fileChooser.showOpenDialog(null);
		FileInputStream fis = null;
		if (opt == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			byte[] bFile = new byte[(int) file.length()];

			try {
				fis = new FileInputStream(file);
				fis.read(bFile);
				fis.close();
				
				Arquivo arquivo = new Arquivo();
				long kbSize = file.length() / 1024;
				arquivo.setNome(file.getName());
				arquivo.setConteudo(bFile);
				arquivo.setTamanhoKB(kbSize);
				
				Message msg = new Message(message.getCliente(), arquivo);
				this.index.updatedPanelMessage("Arquivo enviado: " + msg.getArquivo().getNome());
				sendMessage(msg);

			} catch (IOException e) {
				System.out.println("Erro enviar arquivo: " + e);
			}
		}
	}

	private byte[] serializarMessage(Message message) {
		try {
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			ObjectOutputStream ous;
			ous = new ObjectOutputStream(bao);
			ous.writeObject(message);
			return bao.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private class ListenerSocket implements Runnable {

		private Socket socket;
		private Index index;

		public ListenerSocket(Socket clienteSocket, Index index) {
			this.socket = clienteSocket;
			this.index = index;
		}

		@Override
		public void run() {
			BufferedInputStream bf = null;
			Message message = null;
			
			try {
				while((bf = new BufferedInputStream(socket.getInputStream())) != null) {
					byte[] objectAsByte = new byte[this.socket.getReceiveBufferSize()];
					bf.read(objectAsByte);
					message = (Message) getObjectFromByte(objectAsByte);
					
					if(message.getMsg() != null) {
						this.index.updatedPanelMessage(message.getCliente() + ": " + message.getMsg());
					}
					if(message.getArquivo() != null) {
						this.salvarArquivo(message);
						this.index.updatedPanelMessage(message.getCliente() + " enviou um arquivo: " + message.getArquivo().getNome());
					}
				}
				
				
			} catch (Exception e) {
				System.out.println("Error ao receber msg: " + e);
			}
		}

		private Object getObjectFromByte(byte[] objectAsByte) {
			Object obj = null;
			ByteArrayInputStream bis = null;
			ObjectInputStream ois = null;
			try {
				bis = new ByteArrayInputStream(objectAsByte);
				ois = new ObjectInputStream(bis);
				obj = ois.readObject();

				bis.close();
				ois.close();

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			return obj;

		}
		
		private void salvarArquivo(Message message) {			
			try {
				String caminho = javax.swing.filechooser.FileSystemView.getFileSystemView().getDefaultDirectory().getPath()+ "\\" + message.getArquivo().getNome();
				FileOutputStream fos = new FileOutputStream(caminho);
				fos.write(message.getArquivo().getConteudo());
				fos.close();
			} catch (IOException e) {
				System.out.println("Error ao salvar o arquivo: " + e);
			}
		}
	}
}
