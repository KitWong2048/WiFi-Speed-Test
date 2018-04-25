package com.hkust.speedclient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class BeginTest implements Runnable{
	public Thread t;
	private Socket s;
	private byte[] buffer=new byte[1000];
	private String reci="";
	private TextView hint;
	public BeginTest(String ip,int port, TextView hint) throws UnknownHostException, IOException{
		t=new Thread(this);
		s=new Socket(ip,port);
		this.hint=hint;
		t.start();
	}
	@Override
	public void run() {
		BufferedInputStream is=null;
		BufferedOutputStream os=null;
		DataOutputStream ods=null;
		try {
			is=new BufferedInputStream(s.getInputStream());		
			os=new BufferedOutputStream(s.getOutputStream());
			ods=new DataOutputStream(os);
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
	
		while(true){
			try{
				
				is.read(buffer);
				reci=new String(buffer);	
				//hint.setText(reci);
				ods.writeUTF(reci.length()+"");
			}catch(Exception e){
				try {
					s.close();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					os.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					is.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				break;
			}
		}
		
	}

}
