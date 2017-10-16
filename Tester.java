import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;

public class Tester {
	
	private int mTestingFoldNumber;
	
	private Hashtable<String, Integer> mPositiveTrainingTable;
	private Hashtable<String, Integer> mNegativeTrainingTable;
	
	private int mPositiveTrainingSetSize;
	private int mNegativeTrainingSetSize;
	
	private int mNumberOfPositiveFiles;
	private int mNumberOfNegativeFiles;
		
	public Tester(int pTestingFoldNumber, String pTrainingFileNames[])
	{
		
		mTestingFoldNumber = pTestingFoldNumber;
		
		mPositiveTrainingTable = new Hashtable<String, Integer>();
		mNegativeTrainingTable = new Hashtable<String, Integer>();
		
		for(String lStr : pTrainingFileNames) {
			
			ReadTrainingFiles(lStr);
		}
		
		mPositiveTrainingSetSize = Utilities.GetTrainingSetSize(mPositiveTrainingTable);
		mNegativeTrainingSetSize = Utilities.GetTrainingSetSize(mNegativeTrainingTable);
		
		File lPositivePath = new File("../SentimentData/tokens/pos");
		mNumberOfPositiveFiles = lPositivePath.listFiles().length;
		File lNegativePath = new File("../SentimentData/tokens/neg");
		mNumberOfNegativeFiles = lNegativePath.listFiles().length;
		
		int lPositiveFoldSize = mNumberOfPositiveFiles / 3;
		int lNegativeFoldSize = mNumberOfNegativeFiles / 3;
		
		int lStartingFoldIndex = lPositiveFoldSize * (mTestingFoldNumber - 1);
		int lEndingFoldIndex = lPositiveFoldSize * mTestingFoldNumber;
		Hashtable<String, Integer> lUniquePositiveTestWords = 
				Utilities.CountUniqueWords(lPositivePath.listFiles(), lStartingFoldIndex, lEndingFoldIndex);
		
		lStartingFoldIndex = lNegativeFoldSize * (mTestingFoldNumber - 1);
		lEndingFoldIndex = lNegativeFoldSize * mTestingFoldNumber;
		Hashtable<String, Integer>lUniqueNegativeTestWords = 
				Utilities.CountUniqueWords(lNegativePath.listFiles(), lStartingFoldIndex, lEndingFoldIndex);
		
		lUniquePositiveTestWords.putAll(lUniqueNegativeTestWords);

		// Add the words that were first encountered in testing to the tables that they're
		// missing from with a "number of occurences" value of 0.
		for(Map.Entry<String, Integer> lEntry : lUniquePositiveTestWords.entrySet()) {
			if(!mPositiveTrainingTable.containsKey(lEntry.getKey())) {
				mPositiveTrainingTable.put(lEntry.getKey(), 0);
			}
			if(!mNegativeTrainingTable.containsKey(lEntry.getKey())) {
				mNegativeTrainingTable.put(lEntry.getKey(), 0);
			}
		}		
		
		mPositiveTrainingSetSize += 0.1 * mPositiveTrainingTable.size();
		mNegativeTrainingSetSize += 0.1 * mNegativeTrainingTable.size();
				
	}
	
	public double[] PredictSentiments(String pSentimentFolderName)
	{
		
		File lPath = new File("../SentimentData/tokens/" + pSentimentFolderName);
		File[] lFilesList = lPath.listFiles();
		
		int lFoldSize = lFilesList.length / 3;
		
		boolean lPredictionStatus;
		
		int lSuccessfulPredictions = 0;
		int lTotalPredictions = 0;
		
		int lStartingFoldIndex = lFoldSize * (mTestingFoldNumber - 1);
		int lEndingFoldIndex = lFoldSize * mTestingFoldNumber;
		
		for(int i = lStartingFoldIndex; i < lEndingFoldIndex; i++) {
				lPredictionStatus = PredictFileSentiment(lFilesList[i], pSentimentFolderName);		
				if(lPredictionStatus) {
					lSuccessfulPredictions++;
				}
				lTotalPredictions++;
		}
		
		double[] lSuccessesAndTotal = {(double)lSuccessfulPredictions, (double)lTotalPredictions};

		return lSuccessesAndTotal;
		
	}
	
	
/*--------------------------------------------------------------------------------*/
	private boolean PredictFileSentiment(File pFile, String pSentimentFolderName)
	{
		
		double lPositiveProbability = Math.log(((double)mNumberOfPositiveFiles) / (mNumberOfPositiveFiles + mNumberOfNegativeFiles)); 
		double lNegativeProbability = Math.log(((double)mNumberOfNegativeFiles) / (mNumberOfPositiveFiles + mNumberOfNegativeFiles));
		
		try {
			Scanner lScr = new Scanner(pFile);
			while(lScr.hasNext()) {
				String lCurrentWord = lScr.next();

				if(mPositiveTrainingTable.containsKey(lCurrentWord)) {
					int lNumberOfOccurences = mPositiveTrainingTable.get(lCurrentWord);
					lPositiveProbability += Math.log(((double)lNumberOfOccurences + 0.1) / (double)mPositiveTrainingSetSize);
				}
				
				if(mNegativeTrainingTable.containsKey(lCurrentWord)) {
					int lNumberOfOccurences = mNegativeTrainingTable.get(lCurrentWord);
					lNegativeProbability += Math.log(((double)lNumberOfOccurences + 0.1) / (double)mNegativeTrainingSetSize);
				}					
			}

			lScr.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return DetermineClassificationSuccess(lPositiveProbability, lNegativeProbability, pSentimentFolderName);
	}
	
	private boolean DetermineClassificationSuccess(double pPositiveProbability, double pNegativeProbability, String pSentimentFolderName)
	{
		boolean lSuccess = true;
		
		if(pPositiveProbability > pNegativeProbability) {
			
			if(pSentimentFolderName.compareTo("neg") == 0) {
				lSuccess = false;
			}
			
		}
		else {
			
			if(pSentimentFolderName.compareTo("pos") == 0) {
				lSuccess = false;
			}
			
		}
		
		return lSuccess;
	}
	
	private void ReadTrainingFiles(String pTrainingFileName) {
		
		File lFile = new File(pTrainingFileName);
		try {
			Scanner lScr = new Scanner(lFile);
			String lCurrentLine[] = new String[2];
			while(lScr.hasNextLine()) {
				
				lCurrentLine = lScr.nextLine().split(" ");
				
				if(pTrainingFileName.contains("PosTrainingData.txt")) {
					if(lCurrentLine.length > 1) {
						mPositiveTrainingTable.put(lCurrentLine[0], Integer.parseInt(lCurrentLine[1]));
					}
				}
				else {
					if(lCurrentLine.length > 1) {
						mNegativeTrainingTable.put(lCurrentLine[0], Integer.parseInt(lCurrentLine[1]));
					}
				}
				
			}
			
			lScr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
				
	}

}
