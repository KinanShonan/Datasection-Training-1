/*
    Copyright (C) 2009, Cam-Tu Nguyen, Xuan-Hieu Phan
    
    Email:	ncamtu@gmail.com; pxhieu@gmail.com
    URL:	http://www.hori.ecei.tohoku.ac.jp/~hieuxuan
    
    Graduate School of Information Sciences,
    Tohoku University
*/

package jvntagger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


import jvntagger.data.DataReader;
import jvntagger.data.Sentence;
import jvntagger.data.StringUtils;

public class POSDataReader extends DataReader{
	protected String [] tags = {"N", "Np", "Nc", "Nu", "V", "A", "P", "L", "M", "R",
			"E", "C", "I", "T", "B", "Y", "X", "Ny", "Nb", "Vb", "Mrk"};

	protected boolean isTrainReading = false;
	//-------------------------------------
	// Constructor
	//-------------------------------------
	public POSDataReader(){
		// Do nothing
	}
	
	public  POSDataReader(boolean isTrainReading){
		this.isTrainReading = isTrainReading;
	}
	
	
	//-------------------------------------
	// Override methods
	//-------------------------------------
	@Override
	public List<Sentence> readFile(String datafile){
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(datafile), "UTF-8"));
			
			String line = null;
			List<Sentence> data = new ArrayList<Sentence>();
			while ((line = reader.readLine()) != null){
				Sentence sentence = new Sentence();
				boolean error = false;
				//System.out.println(line);
				
				if (line.startsWith("#"))
					continue;
				
				StringTokenizer tk = new StringTokenizer(line, " ");				
				
				while (tk.hasMoreTokens()){
					String word = "", tag = null;
					String token = tk.nextToken();
					
					if (isTrainReading){
						if (token == "/"){
							word = "/";
							tag = "Mrk";
						}
						else if (token == "///"){
							word = "/";
							tag = "Mrk";
						}
						else {
							
							String [] fields = token.split("/");						
							if (fields.length == 1){						
								error = true;
								break;
							}
							else if (fields.length == 2){
								word = fields[0];
								tag = fields[1];
							}			
							else if (fields.length > 2){//token = 20/9/08
								tag = fields[fields.length - 1];
								for (int i = 0; i < fields.length - 2; ++i)
									word += fields[i] + "/";
								word += fields[fields.length - 2];
							}
							
							if (tag != null){
								if (StringUtils.isSign(tag))
									sentence.addTWord(word, "Mrk");
								else {									
									boolean found = false;
									for (int i = 0; i < tags.length; ++i){
										if (tag.equalsIgnoreCase(tags[i])){
											//sentence.addTWord(word, tags[i]);
											tag = tags[i];
											found = true;
											break;
										}
									}	
									
									if (!found) {error = true;
									System.out.println("error");
									System.out.println(tag);
									}
									sentence.addTWord(word, tag);
								}
							}
							else {
								//sentence.addTWord(word, tag);								
								error = true; //uncomment this when reading data for training
								break;
							}
						}
					}
					else {
						word = token;
						tag = null;
						sentence.addTWord(word, tag);
					}
				}
				
				if (!error)
					data.add(sentence);
			}
			
			reader.close();
			return data;
		}
		catch (Exception e){
			System.out.println("Error while reading data!");
			e.printStackTrace();
			return null;
		}	
	}
	
	@Override
	public List<Sentence> readString(String dataStr){
		String [] lines = dataStr.split("\n");
		
		List<Sentence> data = new ArrayList<Sentence>();
		for (String line : lines){
			Sentence sentence = new Sentence();
			StringTokenizer tk = new StringTokenizer(line, " ");				
			
			while (tk.hasMoreTokens()){
				if (isTrainReading){
					String token = tk.nextToken();
					String [] fields = token.split("/");
					
					if (fields.length > 0){
						String word = fields[0];
						String tag = null;
						if (fields.length == 2)
							tag = fields[1];
						
						sentence.addTWord(word, tag);
					}
				}
				else {
					String token = tk.nextToken();
					sentence.addTWord(token, null);
				}
			}			
			data.add(sentence);
		}
		
		return data;
	}
}