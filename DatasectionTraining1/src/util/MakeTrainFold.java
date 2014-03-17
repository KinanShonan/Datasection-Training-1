package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.kohsuke.args4j.*;

public class MakeTrainFold {
	
	public static void main(String [] args){
		MakeTrainFoldOption option = new MakeTrainFoldOption();
		CmdLineParser parser = new CmdLineParser(option);
		
		try {
			parser.parseArgument(args);			
			
			for (int k = 0; k < option.numberOfFolds; ++k){
				option.trnFoldNumber = k;
				String trainFoldName = option.directory + File.separator + "train" + option.trnFoldNumber + ".tagged";
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(trainFoldName), "UTF-8"));
				
				//merge all test-fold files except for the leave-out one
				for (int i = 0; i < option.numberOfFolds; ++i){
					if (i == option.trnFoldNumber) continue; //skip the leave-out one			
					
					String currentTestFold = option.directory + File.separator  + option.testFoldPrefix + i + ".tagged";				
					BufferedReader reader = new BufferedReader(new InputStreamReader(
							new FileInputStream(currentTestFold), "UTF-8"));
					
					System.out.println("Reading " + currentTestFold);
					String line;
					while((line = reader.readLine()) != null){					
						writer.write(line + "\n");
					}
					
					reader.close();
				}
				
				writer.close();
			}
		}
		catch (CmdLineException ce){
			System.err.println("MakeTrainFold [options...] [arguments...]");
			parser.printUsage(System.err);
			return;
		}
		catch (IOException ioe){
			System.out.println(ioe.getMessage());
			ioe.printStackTrace();
			return;
		}
	}
	
}

class MakeTrainFoldOption{
	//@Option(name="-no", usage="specify train fold number to make")
	public int trnFoldNumber;
	
	@Option(name="-num", usage="specify number of folds")
	public int numberOfFolds;
	
	@Option(name="-pre", usage="specify the prefix of test fold names")
	public String testFoldPrefix;
	
	@Option(name="-dir", usage = "specify data directory")
	public String directory;
}