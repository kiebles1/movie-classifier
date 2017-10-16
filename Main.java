public class Main {
		
	public static void main(String[] args) 
	{

		int[] lTrainingFoldNumbers = new int[2];
		int lTestingFoldNumber = 0;

		switch (args[0]) {

		case "train":
			for(int i = 1; i < 3; i++) {
				
				if(args[i].compareTo("fold1") == 0) {
					lTrainingFoldNumbers[i-1] = 1;
				}
				else if(args[i].compareTo("fold2") == 0) {
					lTrainingFoldNumbers[i-1] = 2;
				}
				else if(args[i].compareTo("fold3") == 0) {
					lTrainingFoldNumbers[i-1] = 3;
				}
				else {
					System.err.println("Invalid command line argument.");
				}
												
			}
			
			System.out.println("Training folds: " + lTrainingFoldNumbers[0] + ", " + lTrainingFoldNumbers[1] + "\n");
			
			Trainer lTrainer = new Trainer(lTrainingFoldNumbers);
			lTrainer.Train();
			lTrainer.SaveTrainingData();

			break;

		case "test":
			if(args[1].compareTo("fold1") == 0) {
				lTestingFoldNumber = 1;
			}
			else if(args[1].compareTo("fold2") == 0) {
				lTestingFoldNumber = 2;
			}
			else if(args[1].compareTo("fold3") == 0) {
				lTestingFoldNumber = 3;
			}
			else {
				System.err.println("Invalid command line argument.");
			}
			
			System.out.println("Testing fold: " + lTestingFoldNumber);

			String[] lTrainingDataFileNames = {"../SentimentData/PosTrainingData.txt", "../SentimentData/NegTrainingData.txt"};
			
			Tester lTester = new Tester(lTestingFoldNumber, lTrainingDataFileNames);
			double[] lPositiveSuccessesAndTotal = lTester.PredictSentiments("pos");
			double[] lNegativeSuccessesAndTotal = lTester.PredictSentiments("neg");
			
			double lRate = (lPositiveSuccessesAndTotal[0] + lNegativeSuccessesAndTotal[0])/(lPositiveSuccessesAndTotal[1] + lNegativeSuccessesAndTotal[1]);
			
			System.out.println("The rate of success is " + lRate);
			
			break;

		default:
			System.err.println("Invalid command line argument.");
			break;
		}		
	}
}
