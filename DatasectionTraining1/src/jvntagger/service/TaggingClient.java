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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;

public class TaggingClient {
	//-----------------------
	// Data
	//-----------------------
	String host;
	int port;
	
	private BufferedReader in;
	private BufferedWriter out;
	private Socket sock;
	
	//-----------------------
	// Methods
	//-----------------------
	public TaggingClient(String host, int port){
		this.host = host;
		this.port = port;
	}
	
	public boolean connect(){
		try {
			sock = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(
					sock.getInputStream(), "UTF-8"));
			out = new BufferedWriter(new OutputStreamWriter(
					sock.getOutputStream(), "UTF-8"));
			return true;
		}
		catch (Exception e){
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	public String process(String data){
		try {
			out.write(data);
			out.write((char)0);
			out.flush();
			
			//Get data from server
			String tagged = "";
			while (true){				
				int ch = in.read();
				
				if (ch == 0) break;
				tagged += (char) ch;			
			}
			return tagged;
		}
		catch (Exception e){
			System.out.println(e.getMessage());
			return "";
		}
		
	}
	
	public void close(){
		try {
			this.sock.close();
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	//----------------------------------
	// main method, testing this client
	//---------------------------------
	public static void main(String [] args){
		if (args.length != 2){
			System.out.println("TaggingClient [inputfile] [outputfile]");
			return;
		}
		
		try {
			// Create a tagging client, open connection
			TaggingClient client = new TaggingClient("localhost", 2929);			
			
			// read data from file
			// process data, save into another file			
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(args[0]), "UTF-8"));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(args[1]), "UTF-8"));
			
			client.connect();
			String line;
			String input = "";			
			while ((line = reader.readLine()) != null){
				input += line + "\n";					
			}
			
			String tagged = client.process(input);
			writer.write(tagged + "\n");
			
			client.close();
			reader.close();
			writer.close();
			
		}
		catch (Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
}
