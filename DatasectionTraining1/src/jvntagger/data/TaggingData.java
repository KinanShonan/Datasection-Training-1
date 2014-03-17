/*
    Copyright (C) 2009, Cam-Tu Nguyen, Xuan-Hieu Phan
    
    Email:	ncamtu@gmail.com; pxhieu@gmail.com
    URL:	http://www.hori.ecei.tohoku.ac.jp/~hieuxuan
    
    Graduate School of Information Sciences,
    Tohoku University
*/

package jvntagger.data;

import java.util.ArrayList;

public class TaggingData {
	protected ArrayList<ContextGenerator> cntxGenVector = null;
	
	public TaggingData(){		
		cntxGenVector = new ArrayList<ContextGenerator>();		
	}

	public TaggingData(ArrayList<ContextGenerator> _cntxGenVector){		
		cntxGenVector = _cntxGenVector;
	}
	
	public TaggingData(ContextGenerator cntxGen){		
		cntxGenVector = new ArrayList<ContextGenerator>();
		cntxGenVector.add(cntxGen);
	}
	
	public void addContextGenerator(ContextGenerator cntxGen){
		cntxGenVector.add(cntxGen);
	}
	
	public String [] getContext (Sentence sent, int wordIdx){
		ArrayList<String> tempCps = new ArrayList<String>();
		
		for (int i = 0; i < cntxGenVector.size(); ++i){
			String [] context = cntxGenVector.get(i).getContext(sent, wordIdx);
			if (context != null){
				for (int j = 0; j < context.length; ++j){
					if (context[j].trim().equals("")) continue;
					tempCps.add(context[j]);
				}
			}
		}
		
		String [] tempCpsArray = new String[tempCps.size()];
		return tempCps.toArray(tempCpsArray);
	}
}
