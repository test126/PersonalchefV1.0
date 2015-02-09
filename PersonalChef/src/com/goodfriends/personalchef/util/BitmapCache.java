package com.goodfriends.personalchef.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class BitmapCache implements ImageCache {

	private LruCache<String, Bitmap> mMemoryCache;
	private String sdCardRoot = Environment.getExternalStorageDirectory()
			.getAbsolutePath()+"pcCache";

	
	public BitmapCache() {
		// 获取到可用内存的最大值，使用内存超出这个值会引起OutOfMemory异常。
		// LruCache通过构造函数传入缓存值，以KB为单位。
		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		// 使用最大可用内存值的1/8作为缓存的大小。
		int cacheSize = maxMemory / 8;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@SuppressLint("NewApi")
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// 重写此方法来衡量每张图片的大小，默认返回图片数量。
				return bitmap.getByteCount() / 1024;
			}
		};
	}

	@Override
	public Bitmap getBitmap(String url) {
		Bitmap mBitmap = mMemoryCache.get(url);
		if (mBitmap != null) {
			System.out.println("从缓存中读的图片");
		}
		return mMemoryCache.get(url);
		// return getBitmapFromSD(url);

	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		mMemoryCache.put(url, bitmap);
		// putBitmapToSD(url, bitmap);
	}

	public Bitmap getBitmapFromSD(String url) {
		String sdState = Environment.getExternalStorageState();
		if (!sdState.equals(Environment.MEDIA_MOUNTED)) {
			return null;
		}

		String bitmapName = url.substring(url.lastIndexOf("/") + 1);
		File cacheDir = new File(sdCardRoot);
		File[] cacheFiles = cacheDir.listFiles();
		if (cacheFiles == null) {
			return null;
		}
		for (int i = 0; i < cacheFiles.length; i++) {
			String fileName = cacheFiles[i].getName();
			String fileUrl = sdCardRoot + bitmapName;
			if (bitmapName.equals(fileName)) {
				return BitmapFactory.decodeFile(fileUrl);
			}
		}
		return null;

	}

	public void putBitmapToSD(String url, Bitmap bitmap) {

		String sdState = Environment.getExternalStorageState();
		if (!sdState.equals(Environment.MEDIA_MOUNTED)) {
			return;
		}

		File dir = new File(sdCardRoot);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String bitmapName = url.substring(url.lastIndexOf("/") + 1);
		File bitmapFile = new File(sdCardRoot + bitmapName);
		if (!bitmapFile.exists()) {
			try {
				bitmapFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(bitmapFile);
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
