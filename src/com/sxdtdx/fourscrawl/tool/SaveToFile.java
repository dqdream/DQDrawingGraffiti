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
 * ���� �ļ�������
 * @author dq
 *
 */
public class SaveToFile {
	/**
	 * �Ե�ǰ�����ڱ���ͼƬ
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
			throw new RuntimeException("�ļ���" + filename + " �Ѵ��ڣ�");
		FileOutputStream fos = new FileOutputStream(new File(filename));
		// �� bitmap ѹ����������ʽ��ͼƬ����
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
	 * ���滭ͼ���� ������·��
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
		// �� bitmap ѹ����������ʽ��ͼƬ����
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
