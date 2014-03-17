/*
    Copyright (C) 2009, Cam-Tu Nguyen, Xuan-Hieu Phan
    
    Email:	ncamtu@gmail.com; pxhieu@gmail.com
    URL:	http://www.hori.ecei.tohoku.ac.jp/~hieuxuan
    
    Graduate School of Information Sciences,
    Tohoku University
*/

package jvntagger.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Vector;
import jvntagger.POSTagger;

public class Session extends Thread {
	
	//------------------------
	// Data
	//------------------------
	POSTagger tagger;
	private Socket incoming;
	
	//-----------------------
	// Methods
	//-----------------------
	public Session(POSTagger tagger){		
		this.tagger = tagger;
	}
		 	    
	public synchronized void setSocket(Socket s){
		this.incoming = s;
		notify();
	}
	
	public synchronized void run(){	
		while (true){
			try {
				if (incoming == null) {
		            wait();	            
		        }
				
				System.out.println("Socket opening ...");
				BufferedReader in = new BufferedReader(new InputStreamReader(
						incoming.getInputStream(), "UTF-8"));				
				//PrintStream out = (PrintStream) incoming.getOutputStream();
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
						incoming.getOutputStream(), "UTF-8"));
				
				String content = "";
				
				while (true){				
					int ch = in.read();
					if (ch == 0) //end of string
						break;
					
					content += (char) ch;
				}
				
				//System.out.println(content);
				String tagged = tagger.tagging(content);
				//Thread.sleep(4000);
				
				out.write(tagged.trim());
				out.write((char)0);
				out.flush();
			}
			catch (InterruptedIOException e){
				System.out.println("The conection is interrupted");	
			}
			catch (Exception e){
				System.out.println(e);
				e.printStackTrace();
			}
			
			//update pool
			//go back in wait queue if there is fewer than max
			this.setSocket(null);
			Vector<Session> pool = TaggingService.pool;
			synchronized (pool) {
				if (pool.size() >= TaggingService.maxNSession){
					/* too many threads, exit this one*/
					return;
				}
				else {				
					pool.addElement(this);
				}
			}
		}
	}
}
