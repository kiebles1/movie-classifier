public class FileManager {

	private String mFileName;
	private String mFilePath;
	
	public FileManager(String pFileName, String pFilePath) {
		mFileName = pFileName;
		mFilePath = pFilePath;
	}
	
	public String GetFileName() {
		return mFileName;
	}
	
	public String GetFilePath() {
		return mFilePath;
	}
}
