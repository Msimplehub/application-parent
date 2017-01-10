package org.wostore.customer.business.file.fetch;

public class SiteInfoBean {
	
	private String sSiteURL; // Site's URL
	private String sFilePath; // Saved File's Path
	private String sFileName; // Saved File's Name
	private int nSplitter; // Count of Splited Downloading File

	public SiteInfoBean() {
		// default value of nSplitter is 5
		this("", "", "", 5);
	}

	public SiteInfoBean(String sURL, String sPath, String sName, int nSpiltter) {
		sSiteURL = sURL;
		sFilePath = sPath;
		sFileName = sName;
		this.nSplitter = nSpiltter;
	}

	public String getSSiteURL() {
		return sSiteURL;
	}

	public void setSSiteURL(String value) {
		sSiteURL = value;
	}

	public String getSFilePath() {
		return sFilePath;
	}

	public void setSFilePath(String value) {
		sFilePath = value;
	}

	public String getSFileName() {
		return sFileName;
	}

	public void setSFileName(String value) {
		sFileName = value;
	}

	public int getNSplitter() {
		return nSplitter;
	}

	public void setNSplitter(int nCount) {
		nSplitter = nCount;
	}
}

class Utility {
	public Utility() {
	}

	public static void sleep(int nSecond) {
		try {
			Thread.sleep(nSecond);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void log(String sMsg) {
		System.err.println(sMsg);
	}

	public static void log(int sMsg) {
		System.err.println(sMsg);
	}
}