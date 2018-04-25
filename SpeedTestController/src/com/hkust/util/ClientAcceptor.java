package com.hkust.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import com.hkust.UI.MainWindow;

public class ClientAcceptor  implements Runnable{
	public MessageSenter senter=null;
	private ServerSocket server;
	private Vector<Socket> clients;
	private MainWindow w;
	public Thread t;
	public ClientAcceptor(ServerSocket server,Vector<Socket> clients, MainWindow mainWindow){
		this.server=server;
		this.clients=clients;
		this.w=mainWindow;
		t=new Thread(this);
		t.start();
	}
	public void acceptTo(ServerSocket server,Vector<Socket> clients){
		while(true){
			try {
			Socket s=null;
			
			s = server.accept();
			
			clients.add(s);
			
			w.showCLients();
			} catch (IOException e) {
				try {
					server.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
				
			}
		}
	}
	

	@Override
	public void run() {
		acceptTo(server,clients);	
	}
	public void startTest(){
		senter=new MessageSenter(this.clients,w);
	}
}
