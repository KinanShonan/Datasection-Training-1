package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;


import jvntagger.BasicContextGenerator;
import jvntagger.POSContextGenerator;
import jvntagger.POSDataReader;
import jvntagger.data.Sentence;
import jvntagger.data.TaggingData;


public class POSTaggingData extends TaggingData{

	List<String> marks = new ArrayList<String>();
	boolean isGeneral = true;
	
	public POSTaggingData(String modeldir){		
		super.addContextGenerator(new POSContextGenerator(modeldir + File.separator + "featuretemplate.xml"));
		//super.addContextGenerator(new BasicContextGenerator());
	}
	
	public void genData(List<Sentence> data, Writer out){
		try{
			//genenerate data in the form that each sequence are separated by one white line
			//sequence = list of observation
			//observation = context + tag
			
			for (int i = 0; i < data.size(); ++i){
				Sentence sent = data.get(i);
				
				//System.out.print(".");
				for (int j = 0; j < sent.size(); ++j){
					String [] context = super.getContext(sent,j);
					
					//write context
					for (int k = 0; k < context.length; ++k){
						out.write(context[k]);
						
						if (k != context.length - 1) out.write(" ");
					}
					
					String tag = sent.getTagAt(j);
					if (isGeneral)
					{						
						char c = tag.charAt(0);
						
						if (tag.equalsIgnoreCase("Cc") || tag.equalsIgnoreCase("Cm") ||
								tag.equalsIgnoreCase("Nn"))
							out.write(" " + tag + "\n");
						else if (Character.isLetter(c))
							out.write(" " + tag.charAt(0) + "\n");
						else {
							if (!marks.contains(sent.getTagAt(j)))
								marks.add(tag);
							
							out.write(" " + tag + "\n");
						}
					}
					else {
						out.write(" " + tag + "\n");
					}
				}
				
				if (i != data.size() - 1) out.write("\n");
			}
			
		//	System.out.println(Character.isUpperCase('L'));
			
			for (int i = 0; i < marks.size(); ++i){
				System.out.println(marks.get(i));
			}
			
			System.out.println(marks.size());
			System.out.println("Generating training data for " + data.size() + "sentences");
		}
		catch (Exception e){
			System.out.println("error:" + e.getMessage());
			e.printStackTrace();
		}		
	}
	
	public static void main(String [] args){
		if (args.length != 3){
			System.out.println("Usage: POSTaggingData [inputdir] [fileprefix] [nofold]");
			return;
		}
		
		int nofold = Integer.parseInt(args[2]);
		
		for (int i = 0; i < nofold; ++i){
			QTagDataReader reader = new QTagDataReader();
			List<Sentence> data = reader.readFile(args[0] + File.separator + args[1] + i + ".txt");
			System.out.println("read " + data.size() + " completed");
			POSTaggingData crfDGen = new POSTaggingData(args[0]);
			
			try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(args[0] + File.separator + args[1] + i + ".tagged"), "UTF-8"));
			
			//if (args[2].equalsIgnoreCase("spec"))
			crfDGen.isGeneral = false;
			crfDGen.genData(data, writer);
			
			writer.close();
			}
			catch (Exception e){
				System.out.println("error:" + e.getMessage());
				e.printStackTrace();
				return;
			}
		}
	}
}
