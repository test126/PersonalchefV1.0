package com.goodfriends.personalchef.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.goodfriends.personalchef.common.CommonFun;

public class FileCache {

	
	private String externalCacheDir;// SD卡缓存目录
	public FileCache(String externalCacheDir) {
		super();
		this.externalCacheDir = externalCacheDir;
	}

	
	/**
	 * 对本地缓存的查找
	 */
	public Bitmap getBitmapFromSD(String url) {
		// SD卡不可用
		if (externalCacheDir == null) {
			return null;
		}
		// 截取文件名
		int begin = url.lastIndexOf("/");
		String bitmapName = url.substring(begin + 1);
		Log.i("share", "externalCacheDir "+externalCacheDir);
		File cacheDir = new File(externalCacheDir);
		File[] cacheFiles = cacheDir.listFiles();
		if (cacheFiles == null) {
			return null;
		}

		// 查找本地文件
		for (int i = 0; i < cacheFiles.length; i++) {
			String fileName = cacheFiles[i].getName();
			if (bitmapName.equals(fileName)) {
				String fileUrl = externalCacheDir +"/"+ bitmapName;
				Log.i("share", "fileUrl "+fileUrl);
				return BitmapFactory.decodeFile(fileUrl);
			}
		}
		return null;

	}
	
	
	/**
	 * 保存文件到本地缓存
	 */
	public void putBitmapToSD(String url, Bitmap bitmap) {

		// SD卡不可用
		if (externalCacheDir == null) {
			return;
		}
		if (url.equals(CommonFun.advurl)) {
			externalCacheDir += "/"+CommonFun.advCacheDir;
		}

		// 截取文件名
		int begin = url.lastIndexOf("/");
		String bitmapName = url.substring(begin + 1);

		String fileName = externalCacheDir + "/" + bitmapName;
		File bitmapFile = new File(fileName);

		// 保存
		try {
			if (!bitmapFile.exists()) {
				bitmapFile.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(bitmapFile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
