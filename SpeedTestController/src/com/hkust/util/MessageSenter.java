package com.hkust.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JFrame;

import com.hkust.UI.MainWindow;
//Once the object of MessageSenter is created, The server begin to sent string to all
//of its current clients and record the begin time of each client. Call stopAll() method 
//to stop sent and receive, call recordAll to record the current information
public class MessageSenter{
	private BufferedOutputStream bs[];
	private DataInputStream is[];
	private Thread[] thread;
	private Thread[] Hearthread;
	private int sentLength[];
	private byte[] sent=new byte[1000];
	private JFrame j=null;
	//private String S="";
	public MessageSenter(Vector<Socket> clients,JFrame j){
		this.j=j;
		this.bs=new BufferedOutputStream[clients.size()];
		this.is=new DataInputStream[clients.size()];
		sentLength=new int[clients.size()];
		for(int i=0;i<sent.length;i++) sent[i]=(byte)i;
		
		MainWindow.startTime=new long[clients.size()];
		MainWindow.currentTime=new long[clients.size()];
		MainWindow.lastTime=new long[clients.size()];
		MainWindow.ReadBit=new long[clients.size()];
		MainWindow.LastReadBit=new long[clients.size()];
		Hearthread=new Thread[bs.length];
		thread=new Thread[bs.length];
		for(int i=0;i<bs.length;i++){
			try{
				bs[i]=new BufferedOutputStream(clients.get(i).getOutputStream());
			}catch(Exception e){	
			}
			try{
				is[i]=new DataInputStream(new BufferedInputStream(clients.get(i).getInputStream()));
			}catch(Exception e){
				
			}
		}
		for(int i=0;i<bs.length;i++){
			sentTo(i);	
			hearFrom(i);
		}
	}
	private void sentTo(final int i){
		thread[i]=new Thread(){
			public void run(){
				MainWindow.startTime[i]=System.currentTimeMillis();
				MainWindow.currentTime[i]=MainWindow.startTime[i];
				MainWindow.lastTime[i]=MainWindow.startTime[i];
				while(true){
					try {
						bs[i].write(sent);
						//bs[i].flush();
					} catch (IOException e) {
						try {
							bs[i].close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						break;
					}
				}
			}
		};
		thread[i].start();
	}
	private void hearFrom(final int i){
		Hearthread[i]=new Thread(){
			public void run(){
				while(true){
					try {
						String ns=is[i].readUTF();
						//j.setTitle(ns);
						sentLength[i]+=Integer.parseInt(ns);
						
					} catch (IOException e) {
						try {
							is[i].close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						break;
						
					}
				}
			}
		};
		Hearthread[i].start();
	}
	public void stopAll(){
		for(int i=0;i<thread.length;i++){
			thread[i].stop();
			Hearthread[i].stop();
			recordAll();
			try {
				bs[i].close();
			} catch (IOException e) {				
			}		
			try {
				is[i].close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
	public void recordAll(){
		System.arraycopy(MainWindow.currentTime, 0, MainWindow.lastTime, 0, MainWindow.lastTime.length);
		System.arraycopy(MainWindow.ReadBit, 0, MainWindow.LastReadBit, 0, MainWindow.LastReadBit.length);
		for(int i=0;i<thread.length;i++){		
			MainWindow.currentTime[i]=System.currentTimeMillis();
			MainWindow.ReadBit[i]=sentLength[i];
		}
	}
}
