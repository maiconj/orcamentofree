package com.orcamentofree.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.util.Log;

public class SDCardUtils {

	private static final String TAG = SDCardUtils.class.getName();
	private static final String LOG = "DESENV";

	public static File getSdCardFile(String dirName, String fileName){
		File sdcard = android.os.Environment.getExternalStorageDirectory();
		File dir = new File(sdcard, dirName);
		if(!dir.exists()) {
			dir.mkdir();
		}
		File file = new File(dir,fileName);
		return file;
	}
	
	public static File writeToSdCard(File f, byte[] bytes) {
		try {
			if(f != null) {
				FileOutputStream fos = new FileOutputStream(f);
				fos.write(bytes);
				fos.close(); 
			}
		} catch (FileNotFoundException e) {
			Log.e(LOG, TAG+"--"+e.getMessage(), e);
		} catch (IOException e) {
			Log.e(LOG, TAG+"--"+e.getMessage(), e);
		}
		return f;
	}
}
