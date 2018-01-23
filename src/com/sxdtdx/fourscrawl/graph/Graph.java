package com.sxdtdx.fourscrawl.graph;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
/**
 * ͼ�� ������
 * @author dq
 *
 */
public abstract class Graph {
	float left = 0, top = 0, right = 0, bottom = 0;
	Paint paint = null;
	Path path = new Path(); // ��ͼ ·��
	int i = 10;
	int sAngle=0;//��ʼ�Ƕ�
	float cx = 0, cy = 0;
	int cAngle=0;// �任�Ƕ�

    /**
     * �� ��ͼ�η���
     */
	public abstract void draw(Canvas canvas, Bitmap cachebBitmap,
			Bitmap sdbitmap, Paint penPaint, float sa, float sb, float movex,
			float movey);
	
	public int getsAngle() {
		return sAngle;
	}
	public void setsAngle(int sAngle) {
		this.sAngle = sAngle;
	}
	public int getcAngle() {
		return cAngle;
	}
	public void setcAngle(int cAngle) {
		this.cAngle = cAngle;
	}
	/**
	 * ���������Bitmapͼ��
	 * 
	 * @param canvas
	 * @param bitmap
	 */
	public void drawCacheBitmap(Canvas canvas, Bitmap bitmap) {
		canvas.drawBitmap(bitmap, 0, 0, null);

	}
	/**
	 * ��ָ�뿪��Ļʱ ����ʾ��ͼ�񻭵� ����Bitmap��
	 * 
	 * @param cachecanvas
	 * @param paint
	 */
	public void drawCacheCanvasGraph(Canvas cachecanvas, Paint paint) {
		cachecanvas.drawPath(path, paint);
	}
	protected int computeCurrentAngle(float x, float y,float cx,float cy) {
		// ����Բ���������Ƕ�
		float distance = (float) Math.sqrt(((x - cx) * (x - cx) + (y - cy)
				* (y - cy)));
		int degree = (int) (Math.acos((x - cx) / distance) * 180 / Math.PI);
		if (y < cy) {
			degree = -degree;
		}
		if (degree < 0) {
			degree += 360;
		}
		return degree;
	}
	public abstract void move(Canvas canvas, Bitmap cachebBitmap,
			Bitmap sdbitmap, float x, float y);

	public abstract void rotate(Canvas canvas, Bitmap cachebBitmap,
			Bitmap sdbitmap,float x,float y);

	public abstract void zoom(Canvas canvas, Bitmap cachebBitmap,
			Bitmap sdbitmap, float x, float y, float sa, float sb, float width,
			float height);

	public abstract void copy();

	public abstract void paste(Canvas canvas, Bitmap cachebBitmap,
			Bitmap sdbitmap, Paint p);

	public abstract void delete();
	
  
  
}
