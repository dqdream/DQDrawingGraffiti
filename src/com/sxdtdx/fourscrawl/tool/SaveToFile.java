package com.sxdtdx.fourscrawl.tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
/**
 * 保存 文件处理类
 * @author dq
 *
 */
public class SaveToFile {
	/**
	 * 以当前的日期保存图片
	 * 
	 * @param bitmap
	 * @throws FileNotFoundException
	 */
	public static String saveToFile(Bitmap bitmap) throws FileNotFoundException {
		File f = Environment.getExternalStorageDirectory();
		String filepath = f.getPath() + "/FourDraw/draw";
		File file = new File(filepath);
		if (!file.exists()) {
			file.mkdirs();
		}
		String filename = getFilename(file);
		File ff = new File(filename);
		if (ff.exists())
			throw new RuntimeException("文件：" + filename + " 已存在！");
		FileOutputStream fos = new FileOutputStream(new File(filename));
		// 将 bitmap 压缩成其他格式的图片数据
		bitmap.compress(CompressFormat.PNG, 50, fos);
		try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filename;
	}

	private static String getFilename(File f) {
		Calendar c = Calendar.getInstance();
		String name = c.get(Calendar.YEAR) + c.get(Calendar.MONTH)
				+ c.get(Calendar.DAY_OF_MONTH) + c.get(Calendar.HOUR_OF_DAY)
				+ c.get(Calendar.MINUTE) + c.get(Calendar.SECOND) + ".png";
		System.out.println(name);
		String filename = f.getPath() + "/" + name;
		return filename;
	}

	/**
	 * 保存画图缓存 并返回路径
	 * 
	 * @param bitmap
	 * @throws IOException
	 */
	public static String saveCacheFile(Bitmap bitmap) throws IOException {
		File f = Environment.getExternalStorageDirectory();
		String filepath = f.getPath() + "/FourDraw/cache";
		File file = new File(filepath);
		if (!file.exists()) {
			file.mkdirs();
		}
		String filename = getFilename(file);
		FileOutputStream fos = new FileOutputStream(new File(filename));
		// 将 bitmap 压缩成其他格式的图片数据
		bitmap.compress(CompressFormat.PNG, 50, fos);
		fos.close();
		return filename;
	}

	public static void delCacheFile() {
		File f = Environment.getExternalStorageDirectory();
		String filepath = f.getPath() + "/FourDraw/cache";
		File file = new File(filepath);
		if (!file.exists()) {
			file.mkdirs();
		}
		File fs[] = file.listFiles();
		for (int i = 0; i < fs.length; i++) {
			fs[i].delete();
		}
		System.out.println(file.listFiles().length);
	}

}
