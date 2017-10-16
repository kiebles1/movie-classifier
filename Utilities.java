

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Scanner;

public class Utilities {
	
	public static Hashtable<String, Integer> ReadFilesFromTo(Hashtable<String, Integer> pVocabDictionary, 
			File[] pFileList, int pFrom, int pTo)
	{
		
		for(int i = pFrom; i < pTo; i++) {
			
			try {
				Scanner lScr = new Scanner(pFileList[i]);
				CountWords(pVocabDictionary, lScr);
				lScr.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		}
				
		return pVocabDictionary;
		
	}
	
	public static Hashtable<String, Integer> CountUniqueWords(File[] pFileList, int pFrom, int pTo)
	{
		Hashtable<String, Integer> lUniqueWordsMap = 
				ReadFilesFromTo(new Hashtable<String, Integer>(), pFileList, pFrom, pTo);
		
		return lUniqueWordsMap;
	}
	
	public static int GetTrainingSetSize(Hashtable<String, Integer> pTrainingSet)
	{
		int lTotalNumberOfWords = 0;
		
		for(Integer lNumberOfOccurences : pTrainingSet.values()) {
			lTotalNumberOfWords += lNumberOfOccurences;
		}
		
		return lTotalNumberOfWords; 
	}
	
	public static int GetLastFoldAddOn(int pFoldNumber, int pFileSetSize)
	{
		int lFoldAddOn = 0;
		
		int lRemainder = pFileSetSize % pFoldNumber;
		
		if(pFoldNumber == 3) {
			lFoldAddOn = lRemainder;
		}
		
		return lFoldAddOn;
	}
	
/*-------------------------------------------------------*/
	
	private static void CountWords(Hashtable<String, Integer> pVocabDictionary, Scanner pScr)
	{
				
		String lCurrentWord = new String();
		
		while(pScr.hasNext()) {
			
			lCurrentWord = pScr.next();
			
			if(!pVocabDictionary.containsKey(lCurrentWord)) {
				pVocabDictionary.put(lCurrentWord, 1);
			}
			else {
				Integer lCurrentWordCount = pVocabDictionary.get(lCurrentWord);
				pVocabDictionary.replace(lCurrentWord, ++lCurrentWordCount);
			}
		}			
	}	
}
