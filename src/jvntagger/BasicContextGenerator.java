/*
    Copyright (C) 2009, Cam-Tu Nguyen, Xuan-Hieu Phan
    
    Email:	ncamtu@gmail.com; pxhieu@gmail.com
    URL:	http://www.hori.ecei.tohoku.ac.jp/~hieuxuan
    
    Graduate School of Information Sciences,
    Tohoku University
*/

package jvntagger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jvntagger.data.ContextGenerator;
import jvntagger.data.Sentence;
import jvntagger.data.VnSyllParser;

public class BasicContextGenerator extends ContextGenerator {

	private static final String DEFAULT_E_DICT = "jvntagger.ComputerDict.txt";
	public BasicContextGenerator() {
		readDict();		
	}
	
	
	Map word2dictags = new HashMap<String, List>();
	
	public boolean readDict(){
		try {
			URL url = BasicContextGenerator.class.getClassLoader().getResource(DEFAULT_E_DICT);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					url.openStream(), "UTF-8"));
			word2dictags.clear();
						
			String line, temp = null;
			while ((line = reader.readLine()) != null ){
				String [] tokens = line.split("\t");
		
				String word, tag;
				if (tokens == null)
					continue;
				
				if (tokens.length != 2){
					continue;					
				}
				else if (tokens.length == 2){
					if (tokens[0].equals("")){
						if (temp == null)
							continue;
						else {
							//System.out.println(temp);
							word = temp;
							tag = tokens[1];
						}
					}
					else{ 
						word = tokens[0].trim().toLowerCase();
						tag = tokens[1].trim();
						temp = word;
					}
				}
				else continue;
				
				word = word.replace(" ","_");
				List dictags = (List) word2dictags.get(word);
				if (dictags == null){
					dictags = new ArrayList<String>();
				}
				dictags.add(tag);
				word2dictags.put(word, dictags);
			}
			
			reader.close();
			return true;
		}
		
		catch (Exception e){
			return false;
		}
	}
	
	@Override
	public String[] getContext(Sentence sent, int pos) {
		// TODO Auto-generated method stub
				
		if (pos < 0 || pos >= sent.size())
			return null;
		
		List<String> cps = new ArrayList<String>();

		//single word
		for (int i = -2; i <=2; ++i){
			String cp = "1:";
			
			if (pos + i == sent.size()){
				cp += Integer.toString(i) + ":" + "ES";
			}
			else if (pos + i == -1){
				cp += Integer.toString(i) + ":" + "BS";
			}
			else if (0 <= pos + i && pos + i < sent.size()){
				cp += Integer.toString(i) + ":" + sent.getWordAt(pos + i);
			}
			else {
				cp = "";
			}
			
			if (!cp.equals("")) cps.add(cp);
		}
		
		//prefix : check if the first syllable of a word is in a predefined list such as 'su'
		// simpler: if the word has more than 3 syllables, take the first one as prefix
		//suffix: 
				
		//2 consecutive words
		String nextnext = "", prevprev = "";
		if (pos - 2 >= 0){
			prevprev = sent.getWordAt(pos - 2) + ":" + sent.getWordAt(pos - 1);
			cps.add("2:pp:" + prevprev);
		}
		
		if (pos + 2 < sent.size()){
			nextnext = sent.getWordAt(pos + 1) + ":" + sent.getWordAt(pos + 2);
			cps.add("3:nn:" + nextnext);
		}
		
		if (pos - 1 >= 0){
			String prevcur = sent.getWordAt(pos - 1) + ":" + sent.getWordAt(pos);
			cps.add("4:pc:" + prevcur);
		}
		
		if (pos + 1 < sent.size()){
			String curnext = sent.getWordAt(pos) + ":" + sent.getWordAt(pos + 1);
			cps.add("5:cn:" + curnext);
		}
		
		//has number, all number
		String curWord = sent.getWordAt(pos);
		//System.out.println("[" + curWord + "]");
		boolean isAllNumber = true, hasNumber = false;
		
		for (int i = 0; i < curWord.length(); ++i){
			char c = curWord.charAt(i);
			if (c == '_') continue;
			
			if (Character.isDigit(c)){
				if (!hasNumber) hasNumber = true;
			}
			else {
				if (isAllNumber) isAllNumber = false;
			}
		}
		
		if (isAllNumber)
			cps.add("6:an");
		
		if (!isAllNumber && hasNumber){
			cps.add("7:hn");
		}
		
		//has hyphen
		if (curWord.contains("-")){
			cps.add("8:hyph");
		}
		
		//has slash
		if (curWord.contains("/"))
			cps.add("9:slash");
		
		//has comma
		if (curWord.contains(":"))
			cps.add("10:com");
		
		
		//all cap and initial cap
		boolean isAllCap = true;
		
		for (int i = 0 ; i < curWord.length(); ++i){
			if (curWord.charAt(i) == '_' || curWord.charAt(i) == '.') continue;
			
			if (!Character.isUpperCase(curWord.charAt(i))){
				isAllCap = false;
				break;
			}
		}
		
		if (!isAllCap && Character.isUpperCase(curWord.charAt(0))){
			cps.add("11:ic");
		}
		
		if (isAllCap)
			cps.add("12:ac");
		
		//is mark context
		String [] marks = {".", "...", "?", "!", "(", ")", ":", "-", "/", "\"", ","};
		boolean isMark = false;
		
		for (int i = 0; i < marks.length; ++i){
			if (marks[i].equalsIgnoreCase(curWord)){
				isMark = true;
				break;
			}
		}
		
		if (isMark)
			cps.add("13:mk");		
		
		// + DICTIONARY CONTEXT --> TEST2
		if (word2dictags.containsKey(curWord)){
			List tags = (List) word2dictags.get(curWord);
			
			for (int i = 0; i < tags.size(); ++i){
				cps.add("14:dict-" + tags.get(i));
			}
		}
		
		//++ REPRETATIVE CONTEXT --> TEST3
			String [] sylls = curWord.split("_");
		if (sylls.length == 2){ //consider 2-syllable words
			VnSyllParser parser1 = new VnSyllParser(sylls[0]);
			VnSyllParser parser2 = new VnSyllParser(sylls[1]);
			
			if (parser1.isValidVnSyllable() && parser2.isValidVnSyllable()){
				if (parser1.getNonToneSyll().equalsIgnoreCase(parser2.getNonToneSyll())){
					cps.add("15:fr");
				}
				else if (parser1.getRhyme().equalsIgnoreCase(parser2.getRhyme())){
					cps.add("16:rr");
				}
			}
		}
		
		//+++ POS sequence features --> TEST 4
//		String Lfea = "";
//		String Rfea = "";
		/*if (pos - 2 >= 0){
			String pprevprev = sent.getTWordAt(pos - 2).getTag() + ":" + sent.getTagAt(pos - 1);
			cps.add("17:ppp:" + pprevprev);
		}
		if (pos - 1 >= 0){
			String pprevcur = sent.getTagAt(pos - 1) + ":" + sent.getWordAt(pos);
			cps.add("18:ppc:" + pprevcur);
		}*/
		
		String [] ret = new String[cps.size()];		
		return cps.toArray(ret);
	}

}
