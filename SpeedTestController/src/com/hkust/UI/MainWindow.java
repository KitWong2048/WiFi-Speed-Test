package com.hkust.UI;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.hkust.util.ClientAcceptor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class MainWindow extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static long startTime[];
	public static long lastTime[];
	public static long currentTime[];
	public static long ReadBit[];
	public static long LastReadBit[];
	public static void main(String args[]){
		new MainWindow();
	}
	private JPanel clientlist,data,control;
	private JButton listen,begin,show,stop;
	private JScrollPane listS,dataS;
	private JTextArea listA,dataA;
	private JLabel connected;
	private JSplitPane splite;
	private JTextField port; 
	private ServerSocket server;
	private Vector<Socket> clients;
	private ClientAcceptor acceptor;
	MainWindow(){
		clients=new Vector<Socket>();	
		clientlist=new JPanel();
		data=new JPanel();
		control=new JPanel();
		
		port=new JTextField(7);
		port.setText("6666");
		
		listen=new JButton("Begin Service");
		begin=new JButton("Start test");
		show=new JButton("Show corrent data");
		stop=new JButton("Stop");
		
		begin.setEnabled(false);
		show.setEnabled(false);
		stop.setEnabled(false);
		
		listA=new JTextArea();
		dataA=new JTextArea();
		listA.setEditable(false);
		dataA.setEditable(false);
		listA.setFont(new Font("TimesRoman", Font.BOLD, 15));
		dataA.setFont(new Font("TimesRoman", Font.BOLD, 15));
		
		connected=new JLabel("Clients List (0)",JLabel.CENTER);
		connected.setFont(new Font("TimesRoman", Font.BOLD, 15));
		
		listS=new JScrollPane(listA);
		dataS=new JScrollPane(dataA); 
		
		splite=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,clientlist,data);
		
		clientlist.setLayout(new BorderLayout());
		data.setLayout(new BorderLayout());
		
		listen.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					server.close();
				} catch (Exception e) {
					
				}
				try {
					server=new ServerSocket(Integer.parseInt(port.getText()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				MainWindow.this.dataA.setText("");
				MainWindow.this.listA.setText("");
				try{
					showCLients();
				}catch(Exception e){}
				acceptor=new ClientAcceptor(MainWindow.this.server,MainWindow.this.clients,MainWindow.this);	
				begin.setEnabled(true);
				listen.setEnabled(false);
			}
			
		});
		begin.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				acceptor.startTest();
				show.setEnabled(true);
				stop.setEnabled(true);
				begin.setEnabled(false);
			}
			
		});
		show.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				acceptor.senter.recordAll();
				MainWindow.this.writeReport();
				
			}
			
		});
		stop.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				acceptor.senter.stopAll();
				acceptor.t.stop();
				
				MainWindow.this.clients=new Vector<Socket>();
				MainWindow.this.writeReport();
				begin.setEnabled(false);
				show.setEnabled(false);
				stop.setEnabled(false);
				listen.setEnabled(true);
				
			}
			
		});
		
		
		
		
		clientlist.add(connected,BorderLayout.NORTH);
		clientlist.add(listS);
		clientlist.add(port,BorderLayout.SOUTH);
		data.add(dataS);
		control.add(listen);control.add(begin);control.add(show);control.add(stop);
		
		this.add(splite);
		this.add(control,BorderLayout.SOUTH);
		
		this.setSize(1200, 600);
		this.setLocation(200,100);
		this.setVisible(true);
		this.setTitle("Speed Test Controller");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	private void writeReport(){
		String s="Client Number\tStart Time\t\tCurrent Time\t\tLast Time(s)\t\tAlready Sent\t\tAverage Speed\t\tSpeed Since last Record\n";
		for(int i=0;i<startTime.length;i++){
			s+=("client "+i+"\t"+startTime[i]+"\t\t"+currentTime[i]+"\t\t"+(currentTime[i]-startTime[i])/1000.0+"\t\t"+ReadBit[i]+"\t\t"+8.0*1000.0*ReadBit[i]/(currentTime[i]-startTime[i])/1024.0/1024+"Mb/s")+"\t\t"+8.0*1000.0*(ReadBit[i]-LastReadBit[i])/(currentTime[i]-lastTime[i])/1024.0/1024+"Mb/s\n";
			//s+=("client "+i+"\t"+startTime[i]+"\t\t"+currentTime[i]+"\t\t"+(currentTime[i]-startTime[i])/1000.0+"\t\t"+ReadBit[i]+"\t\t"+1000.0*ReadBit[i]/(currentTime[i]-startTime[i])/1024.0/1024+"Mb/s")+"\t\t"+(currentTime[i]-lastTime[i])+"\n";
		}
		s+="---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n";
		dataA.setText(dataA.getText()+s);
	}
	public void showCLients(){
		String s="";
		for(int i=0;i<this.clients.size();i++){
			s+=("Client "+i+"\n");
		}
		this.listA.setText(s);
		this.connected.setText(this.connected.getText().substring(0,this.connected.getText().indexOf("(")+1)+this.clients.size()+")");
	}
}
