package com.sxdtdx.fourscrawl.tool;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;

/**
 * Undo Redo ˫��ѭ��ջ
 * 
 * @author dq
 * 
 */
public class CacheStack {
	List<String> bitmapsUndo = new ArrayList<String>();
	List<String> bitmapsRedo = new ArrayList<String>();
	int max = 0;// ջ�� �������

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public CacheStack(Context context) {
		max = SharePrefenceUtil.getCacheNum(context);
	}

	// ����ջ
	public void push(Bitmap cachebBitmap) throws Exception {
		bitmapsUndo.add(SaveToFile.saveCacheFile(cachebBitmap));
		if (bitmapsUndo.size() > max) {// Ĭ�ϳ��� 5��
			bitmapsUndo.remove(0);
		}
	}

	// ����
	public Bitmap undo(Bitmap cachebBitmap, Canvas cachecanvas) {
		if (bitmapsUndo.size() > 1) {
			bitmapsRedo.add(bitmapsUndo.remove(bitmapsUndo.size() - 1));
			Bitmap b = BitmapFactory.decodeFile(bitmapsUndo.get(bitmapsUndo
					.size() - 1));
			if (!cachebBitmap.isRecycled()) {
				cachebBitmap.recycle();
			}
			cachebBitmap = Bitmap.createBitmap(b.getWidth(), b.getHeight(),
					Config.ARGB_4444);
			cachecanvas = new Canvas(cachebBitmap);
			cachecanvas.drawBitmap(b, 0, 0, null);
		}
		return cachebBitmap;
	}

	// �ָ�
	public Bitmap redo(Bitmap cachebBitmap, Canvas cachecanvas) {
		if (bitmapsRedo.size() != 0) {
			Bitmap b = BitmapFactory.decodeFile(bitmapsRedo.get(bitmapsRedo
					.size() - 1));
			if (!cachebBitmap.isRecycled()) {
				cachebBitmap.recycle();
			}
			cachebBitmap = Bitmap.createBitmap(b.getWidth(), b.getHeight(),
					Config.ARGB_4444);
			cachecanvas = new Canvas(cachebBitmap);
			cachecanvas.drawBitmap(b, 0, 0, null);
			bitmapsUndo.add(bitmapsRedo.remove(bitmapsRedo.size() - 1));
		}
		return cachebBitmap;
	}

	// ���ջ
	public void clear() {
		if (bitmapsUndo.size() > 1) {
			bitmapsRedo.add(bitmapsUndo.remove(bitmapsUndo.size() - 1));
			bitmapsUndo.clear();
		}
	}
}
