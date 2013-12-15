package guiChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
	
	List<PrintWriter> writers = new ArrayList<>(); 
	

	public ChatServer() {		
		ServerSocket server;
		
		try {
			server = new ServerSocket(50000);
		while(true){
			Socket socket = server.accept();
			new Thread(new ListenClient(socket)).start();
			PrintWriter p = new PrintWriter(socket.getOutputStream());
			writers.add(p);
	} 
		} catch (IOException e) {			
			e.printStackTrace();
	}
		
}
	
	private void sendForAll(String text){
		for (PrintWriter w : writers) {
			try{
			w.println(text);
			w.flush();
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	
	private class ListenClient implements Runnable{
		
		BufferedReader reader;
		String name;
		
		public ListenClient(Socket socket) {
			try {
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
			} catch (Exception e) {				
				e.printStackTrace();
			}			
		}		
	@Override
	public void run() {
		
		while(name==null){
			try {
				name  = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		sendForAll(name+" is online");
		
		
		try {
			String text;
			while((text = reader.readLine()) != null){
				sendForAll(name+": "+text);				
			}			
		} catch (Exception x ){
			x.printStackTrace();
		}			
	}
	
}
	
	public static void main(String[] args) {
		new ChatServer();
		

	}

}
