package com.hkust.speedclient;
import java.io.IOException;

import java.net.UnknownHostException;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
	private EditText ip,port;
	public TextView hint;
	private Button begin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ip=(EditText)findViewById(R.id.Ip);
        port=(EditText)findViewById(R.id.Port);
        hint=(TextView)findViewById(R.id.Hint);
        begin=(Button)findViewById(R.id.Begin);
        begin.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(ip.getText().toString().equals("")) ip.setText("10.0.2.2");
				if(port.getText().toString().equals("")) port.setText("6666");
				try {
					new Thread(runnable).start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					hint.setText(e.toString());
					
				}
				
			}
        	
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    Runnable runnable=new Runnable(){
    	 @Override
    	 public void run(){
    		 try {
				new BeginTest(ip.getText().toString(),Integer.parseInt(port.getText().toString()),MainActivity.this.hint);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				hint.setText(e.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				hint.setText(e.toString());
			}
    	 }
    };
}
