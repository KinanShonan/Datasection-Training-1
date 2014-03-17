/*
    Copyright (C) 2009, Cam-Tu Nguyen, Xuan-Hieu Phan
    
    Email:	ncamtu@gmail.com; pxhieu@gmail.com
    URL:	http://www.hori.ecei.tohoku.ac.jp/~hieuxuan
    
    Graduate School of Information Sciences,
    Tohoku University
*/

package jvntagger.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import jvntagger.CRFTagger;
import jvntagger.MaxentTagger;
import jvntagger.POSTagger;

public class TaggingService extends Thread {
	
	//---------------------------
	// Data
	//---------------------------
	private int port = 2929;
	private ServerSocket socket;
	public static final int maxNSession = 5;
	public static Vector<Session> pool;
	
	private POSTagger tagger = null;
	private String modelDir;
	
	//---------------------------
	// Constructor
	//---------------------------
	public TaggingService(int p, String modelDir){
		this.port = p;
		this.modelDir = modelDir;		
		
	}
	
	public TaggingService(String modelDir){
		this.modelDir = modelDir;
	}
	
	private void init(){
		try {
			if (modelDir.contains("maxent")){
				tagger = new MaxentTagger(modelDir);
			}
			else if (modelDir.contains("crfs")){
				tagger = new CRFTagger(modelDir);
			}
			
			/* start session threads*/
			pool = new Vector<Session>();
			for (int i = 0; i < maxNSession; ++i){
				Session w = new Session(tagger);
				w.start(); //start a pool of session threads at start-up time rather than on demand for efficiency 
				pool.add(w);
			}
		}
		catch (Exception e){
			System.out.println("Error while initilizing service:" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	//------------------------
	// main methods
	//------------------------
	public void run(){
		System.out.println("Starting tagging service!");
		try {
			this.socket = new ServerSocket(this.port);
		}
		catch (IOException ioe){
			System.out.println(ioe);
			System.exit(1);
		}
		
		init();
		System.out.println("Tagging service is started successfully");
		while (true){
			Socket incoming = null;
			try{
				incoming = this.socket.accept();
				Session w = null;
				synchronized (pool) {
					if (pool.isEmpty()){
						w = new Session(tagger);
						w.setSocket(incoming); //additional sessions
						w.start();						
					} else{
						w = pool.elementAt(0);
						pool.removeElementAt(0);						
						w.setSocket(incoming);
					}
				}
			}
			catch (IOException e){
				System.out.println(e);
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String [] args){
		if (args.length != 1){
			System.out.println("TaggingService [model dir]");
			return;
		}
		new TaggingService(args[0]).run();
	}
}
