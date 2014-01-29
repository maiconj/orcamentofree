package com.orcamentofree.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.util.Log;

public class SDCardUtils {

	private static final String TAG = SDCardUtils.class.getName();
	private static final String LOG = "DESENV";
	
	public static File getSdCardFile(String dirName, String fileName) {
		File file = null;
		try {
			File sdcard = android.os.Environment.getExternalStorageDirectory();
			File dir = new File(sdcard, dirName);
			if (!dir.exists()) {
				dir.mkdir();
			}
			file = new File(dir, fileName);
		} catch (Exception e) {
			Log.e(LOG, TAG + "-" + e.getMessage());
		}
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
			Log.e(LOG, TAG+"-"+e.getMessage());
		} catch (IOException e) {
			Log.e(LOG, TAG+"-"+e.getMessage());
		}catch (Exception e) {
			Log.e(LOG, TAG+"-"+e.getMessage());
		}
		return f;
	}
	
	/**
	 * Renomeia o arquivo absolutePathOld : AbsolutePath antigo; absolutePathNew
	 * : AbsolutePath novo;
	 * */
	public static boolean remaneCardFile(String absolutePathOld, String absolutePathNew) {
		boolean success= false;
		try {
			File file = new File(absolutePathOld);
			File file2 = new File(absolutePathNew);
			success = file.renameTo(file2);
		} catch (Exception e) {
			Log.e(LOG, TAG+"-"+e.getMessage());
		}		
		return success;
	}
	
	public static boolean deleteCardFile(String absolutePath) {
		boolean success= false;
		try {
			File file = new File(absolutePath);
			success=file.delete();
		} catch (Exception e) {
			Log.e(LOG, TAG+"-"+e.getMessage());
		}		
		return success;
	}
}

