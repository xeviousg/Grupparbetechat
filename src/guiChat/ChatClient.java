package guiChat;



import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.SystemColor;
import java.awt.Color;

@SuppressWarnings("serial")
public class ChatClient extends JFrame {
	ReadProp r;


	JTextField textToSend;
	JButton button;
	JButton btnDisconnect;
	Font font;
	Socket socket;
	PrintWriter writer;
	String ip;
	int port;

	JTextArea receivedText;
	BufferedReader reader;
	private String nameToSend;

	public ChatClient(String name) throws FileNotFoundException {

		super("Chat: " + name);
		r = new ReadProp();
		
		try {
			ip = r.propIP();
			port = r.propPort();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		nameToSend = name;

		font = new Font("Serif", Font.PLAIN, 26); // changing the texts font
		textToSend = new JTextField();
		textToSend.setForeground(new Color(0, 0, 0));
		textToSend.setBackground(new Color(255, 255, 255));
		textToSend.setFont(font);
		button = new JButton("Send");
		button.addActionListener(new SendListener()); 
		btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(new Disconnect ());

		name = textToSend.getText();

		button.setFont(font);
		btnDisconnect.setFont(font);
		Container send = new JPanel();
		send.setLayout(new BorderLayout());
		send.add(BorderLayout.CENTER, textToSend);
		send.add(BorderLayout.EAST, button);
		send.add(BorderLayout.WEST, btnDisconnect);


		font = new Font("Serif", Font.PLAIN, 18);
		receivedText = new JTextArea();
		receivedText.setForeground(new Color(0, 0, 0));
		receivedText.setEditable(false);
		receivedText.setBackground(SystemColor.inactiveCaption);
		receivedText.setFont(font);
		JScrollPane scroll = new JScrollPane(receivedText);


		getContentPane().add(BorderLayout.CENTER, scroll);
		getContentPane().add(BorderLayout.SOUTH, send);

		configNetWork(nameToSend);		


		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(550,650);
		setVisible(true);


	}




	private class RunServer implements Runnable{
		@Override
		public void run() {
			writer.println(nameToSend);
			writer.flush(); 

			try{
				String text;
				while((text = reader.readLine()) != null){
					receivedText.append(text + "\n"); 
				}
			} catch(Exception x){
				x.printStackTrace();
			}

		}		
	}


	private void configNetWork(String name){



		try{			

			socket = new Socket(ip, port); 
			writer = new PrintWriter(socket.getOutputStream()); 
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
			writer.println(name);
			new Thread(new RunServer()).start(); 

		} catch (Exception e){
			e.printStackTrace();
		}
	}


	public class SendListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {


			writer.println(textToSend.getText());
			writer.flush(); 
			textToSend.setText(""); 
			textToSend.requestFocus(); 

		}
	}	




	public void Disconnect() {

		try {
			writer.println(" is Disconnected ");
			writer.flush();
			socket.close();
		} catch(IOException ex) {
			receivedText.append("Failed to disconnect. \n");
		}

		textToSend.setEditable(true);


	}

	public class Disconnect implements ActionListener{
		public void actionPerformed(ActionEvent e ) {
			Disconnect();
		}

	}	
	public static void main(String[] args) throws FileNotFoundException {


		new ChatClient(JOptionPane.showInputDialog("Type your name: " ));



	}

}
