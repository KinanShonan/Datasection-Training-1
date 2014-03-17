package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import jvntagger.data.DataReader;
import jvntagger.data.Sentence;

public class QTagDataReader extends DataReader {
	
	@Override
	public List<Sentence> readFile(String datafile){
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(datafile), "UTF-8"));
			
			String line = null;
			List<Sentence> data = new ArrayList<Sentence>();
			Sentence sentence = new Sentence();
			
			while ((line = reader.readLine()) != null){
				StringTokenizer tk = new StringTokenizer(line, "\t");
				if (tk.countTokens() != 2) {
					//error
					data.add(sentence);
					sentence = new Sentence();					
					continue;
				}
				
				String word = tk.nextToken();
				String tag = tk.nextToken();
				
				/*for (int i = 0; i < tags.length; ++i){
					if (tag.equalsIgnoreCase(tags[i])){
						tag = tags[i];
						
						break;
					}
				}	*/
				
				sentence.addTWord(word, tag);
			}
			
			reader.close();
			return data;
		}
		catch (Exception e){
			System.out.println("Error while reading data!");
			return null;
		}	
	}
	
	public List<Sentence> readString(String dataStr){
		String [] lines = dataStr.split("\r\n");
		List<Sentence> data = new ArrayList<Sentence>();
		Sentence sentence = new Sentence();
		
		for (String line : lines){
			StringTokenizer tk = new StringTokenizer(line, "\t");
			if (tk.countTokens() != 2) {
				//error
				sentence = new Sentence();
				continue;
			}
			
			String word = tk.nextToken();
			String tag = tk.nextToken();
			sentence.addTWord(word, tag);
			
			if (word.equals(".") || word.equals("...")
					|| word.equals("!") || word.equals("?")){
				data.add(sentence);
				sentence = new Sentence();
			}
		}
		
		return data;
	}
	
	public static void main(String [] args){
		if (args.length != 2){
			System.out.println("Usage: QTagDataReader -inputfile [inputfile]");
			return;
		}
		
		QTagDataReader reader = new QTagDataReader();
		List<Sentence> data = reader.readFile(args[1]);
		
		System.out.println(data.size());
//		for (int i = 2; i < data.size(); ++i){
//			data.get(i).print();
//		}
	}
}
