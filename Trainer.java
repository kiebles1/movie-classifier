import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Map;

public class Trainer {
	
	private static final String POSITIVE_PATH = "../SentimentData/tokens/pos/";
	private static final String NEGATIVE_PATH = "../SentimentData/tokens/neg/";
	
	private int mFirstTrainingFoldNumber;
	private int mSecondTrainingFoldNumber;
	
	private Hashtable<String, Integer> mPositiveTable;
	private Hashtable<String, Integer> mNegativeTable;
	
	public Trainer(int pTrainingFoldNumbers[]) 
	{
		
		mFirstTrainingFoldNumber = pTrainingFoldNumbers[0];
		mSecondTrainingFoldNumber = pTrainingFoldNumbers[1];
		
		mPositiveTable = new Hashtable<String, Integer>();
		mNegativeTable = new Hashtable<String, Integer>();
		
	}
	
	public void Train() 
	{
				
		File lPositivePath = new File(POSITIVE_PATH);
		File lNegativePath = new File(NEGATIVE_PATH);
		
		File[] lPositiveFiles = lPositivePath.listFiles();
		File[] lNegativeFiles = lNegativePath.listFiles();
				
		int lPosFoldSize = lPositiveFiles.length / 3;
		int lFirstPositiveFoldAddOn = Utilities.GetLastFoldAddOn(mFirstTrainingFoldNumber, lPositiveFiles.length);
		int lSecondPositiveFoldAddOn = Utilities.GetLastFoldAddOn(mSecondTrainingFoldNumber, lPositiveFiles.length);
		
		int lNegFoldSize = lNegativeFiles.length / 3;
		int lFirstNegativeFoldAddOn = Utilities.GetLastFoldAddOn(mFirstTrainingFoldNumber, lNegativeFiles.length);
		int lSecondNegativeFoldAddOn = Utilities.GetLastFoldAddOn(mSecondTrainingFoldNumber, lNegativeFiles.length);
		
		mPositiveTable = Utilities.ReadFilesFromTo(mPositiveTable, lPositiveFiles, (mFirstTrainingFoldNumber - 1) *
				lPosFoldSize, mFirstTrainingFoldNumber * lPosFoldSize + lFirstPositiveFoldAddOn);
		
		mNegativeTable = Utilities.ReadFilesFromTo(mNegativeTable, lNegativeFiles, (mFirstTrainingFoldNumber - 1) *
				lNegFoldSize, mFirstTrainingFoldNumber * lNegFoldSize + lFirstNegativeFoldAddOn);
		
		mPositiveTable = Utilities.ReadFilesFromTo(mPositiveTable, lPositiveFiles, (mSecondTrainingFoldNumber - 1) *
				lPosFoldSize, mSecondTrainingFoldNumber * lPosFoldSize + lSecondPositiveFoldAddOn);
		
		mNegativeTable = Utilities.ReadFilesFromTo(mNegativeTable, lNegativeFiles, (mSecondTrainingFoldNumber - 1) *
				lNegFoldSize, mSecondTrainingFoldNumber * lNegFoldSize + lSecondNegativeFoldAddOn);
					
	}
	
	public void SaveTrainingData()
	{
	
		PrintWriter lWriter = null;
		
		try {
			lWriter = new PrintWriter("../SentimentData/PosTrainingData.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for(Map.Entry<String, Integer> lEntry : mPositiveTable.entrySet()) {
			
			lWriter.println(lEntry.getKey() + " " + lEntry.getValue());
		}
		
		lWriter.close();
				
		try {
			lWriter = new PrintWriter("../SentimentData/NegTrainingData.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for(Map.Entry<String, Integer> lEntry : mNegativeTable.entrySet()) {
			
			lWriter.println(lEntry.getKey() + " " + lEntry.getValue());
		}
		
		lWriter.close();
		
	}
	
}










