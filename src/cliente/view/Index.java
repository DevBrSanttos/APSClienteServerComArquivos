package cliente.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import cliente.Cliente;

public class Index {

	private JFrame frame;
	private JTextField txtMessage;
	private JTabbedPane tabbedPane;
	private JLabel lblNome;
	private JTextArea chat;

	private Cliente cliente;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Index window = new Index();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Index() {
		initialize();
		cliente = new Cliente(this);
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 586, 448);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 34, 425, 323);
		frame.getContentPane().add(tabbedPane);

		txtMessage = new JTextField();
		txtMessage.setBounds(10, 368, 306, 32);
		frame.getContentPane().add(txtMessage);
		txtMessage.setColumns(10);

		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String message = txtMessage.getText();
				sendMessage(message);
				txtMessage.setText("");
			}
		});
		btnEnviar.setBounds(326, 368, 109, 32);
		frame.getContentPane().add(btnEnviar);

		JButton btnEnviarArquivo = new JButton("Arquivo");
		btnEnviarArquivo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				sendFile();
			}
		});

		btnEnviarArquivo.setBounds(445, 368, 109, 32);
		frame.getContentPane().add(btnEnviarArquivo);

		lblNome = new JLabel("New label");
		lblNome.setBounds(10, 11, 144, 20);
		frame.getContentPane().add(lblNome);
		newPane("Geral");
	}

	private void newPane(String titulo) {
		JPanel newPanel = new JPanel();
		newPanel.setLayout(null);
		tabbedPane.addTab(titulo, newPanel);

		chat = new JTextArea();
		chat.setEditable(false);
		chat.setForeground(Color.black);

		JScrollPane scroll = new JScrollPane(chat);
		scroll.setBounds(0, 0, 425, 323);
		newPanel.add(scroll);
	}

	public void updatedPanelMessage(String message) {
		this.chat.setText(this.chat.getText() + message + "\n");
	}

	private void sendMessage(String message) {
		try {
			this.cliente.sendMessageText(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendFile() {
		try {
			this.cliente.sendFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setLabel(String nome) {
		this.lblNome.setText("Bem vindo " + nome);
	}

}
