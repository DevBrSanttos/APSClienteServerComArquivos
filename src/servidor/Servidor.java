package servidor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import cliente.models.Message;

public class Servidor {

	public static final int PORT = 3000;
	private ServerSocket serverSocket;
	private Socket socket = new Socket();
	private Map<String, OutputStream> clientesSocket = new HashMap<>();

	public Servidor() {
		try {
			this.serverSocket = new ServerSocket(PORT);
			System.out.println("Servidor inicializado");

			while (true) {
				this.socket = this.serverSocket.accept();
				new Thread(new ListenerSocket(this.socket)).start();
			}
		} catch (IOException e) {
			System.out.println("Error na conexão do cliente " + e);

		}
	}

	private class ListenerSocket implements Runnable {
		private Socket socket;

		public ListenerSocket(Socket clienteSocket) throws IOException {
			this.socket = clienteSocket;
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
					clientesSocket.put(message.getCliente(), this.socket.getOutputStream());
					
					if(message.getMsg() != null || message.getArquivo() != null) {
						for(Map.Entry<String, OutputStream> kv: clientesSocket.entrySet()) {
							if(!message.getCliente().equals(kv.getKey())) {
								sendMessage(message, kv.getValue());
							}
						}
					}
					
				}
			}catch(Exception e) {
				clientesSocket.remove(message.getCliente());
				System.out.println(message.getCliente() + " desconectou!");
			}		
		}
		
		private void sendMessage(Message message, OutputStream outputStream) {
			try {
				BufferedOutputStream bos = new BufferedOutputStream(outputStream);
				byte[] bytea = this.serializarMessage(message);
				bos.write(bytea);
				bos.flush();
				
			} catch (Exception e) {
				System.out.println("Error ao enviar a mensagem: " + e);
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

	}

	public static void main(String[] args) {
		new Servidor();
	}

}
