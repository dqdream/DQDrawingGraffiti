package com.sxdtdx.fourscrawl.graph;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * 3´Î±´Èû¶ûÇúÏß
 * 
 * @author dq
 * 
 */
public class DrawBezier extends Graph {

	int flag = 1;
	Paint penpaint = null;
	public int getI() {
		return flag;
	}

	float endx = 0, endy = 0, startx = 0, starty = 0, x1 = 0, y1 = 0, x2 = 0,
			y2 = 0;

	public void drawCanvasBezier(Canvas canvas, Paint paint, float sa,
			float sb, float x, float y) {
		if (startx == 0 || starty == 0) {
			startx = x;
			starty = y;
		}
		path.reset();
		path.moveTo(startx, starty);
		System.out.println(flag);
		switch (flag) {
		case 1:
			endx = x;
			endy = y;
			path.cubicTo(startx, starty, startx, starty, endx, endy);
			break;
		case 2:
			x1 = x;
			y1 = y;
			path.cubicTo(x1, y1, x1, y1, endx, endy);
			break;
		case 3:
			x2 = x;
			y2 = y;
			path.cubicTo(x1, y1, x2, y2, endx, endy);
			break;
		}
		canvas.drawPath(path, paint);
	}

	public void drawGraphBezier(Canvas canvas, Bitmap cachebBitmap,
			Bitmap sdbitmap, Paint penPaint, float sa, float sb, float movex,
			float movey) {
		drawCacheBitmap(canvas, sdbitmap);
		drawCacheBitmap(canvas, cachebBitmap);
		penpaint = penPaint;
		drawCanvasBezier(canvas, penPaint, sa, sb, movex, movey);
	}

	public int EvenUp() {
		if (mflag) {
			cx = (startx + endx) / 2;
			cy = (starty + endy) / 2;
			flag++;
			if (flag == 4) {
				initdata();
			}
		}
		return flag;
	}

	private void initdata() {
		flag = 1;
		startx = 0;
		starty = 0;
	}

	boolean mflag = true;

	@Override
	public void move(Canvas canvas, Bitmap cachebBitmap, Bitmap sdbitmap,
			float x, float y) {
		mflag=false;
		cx += x;
		cy += y;
		Matrix matrix = new Matrix();
		matrix.preTranslate(x, y);
		path.transform(matrix);
		drawCacheBitmap(canvas, sdbitmap);
		drawCacheBitmap(canvas, cachebBitmap);
		canvas.drawPath(path, penpaint);
	}

	@Override
	public void rotate(Canvas canvas, Bitmap cachebBitmap, Bitmap sdbitmap,
			float x, float y) {
		mflag=false;
		if (sAngle == 0) {
			sAngle = computeCurrentAngle(x, y, cx, cy);
		}
		cAngle = computeCurrentAngle(x, y, cx, cy);
		Matrix matrix = new Matrix();
		matrix.preRotate(cAngle - sAngle, cx, cy);
		path.transform(matrix);
		drawCacheBitmap(canvas, sdbitmap);
		drawCacheBitmap(canvas, cachebBitmap);
		canvas.drawPath(path, penpaint);
		sAngle = cAngle;
	}

	@Override
	public void zoom(Canvas canvas, Bitmap cachebBitmap, Bitmap sdbitmap,
			float x, float y, float sa, float sb, float width, float height) {
		mflag=false;
		Matrix matrix = new Matrix();
		float ix = 1.0f;
		float iy = 1.0f;
		if (sa < width / 2) {
			ix = ix - (x / width);
		} else {
			ix = ix + (x / width);
		}
		if (sb < height / 2) {
			iy = iy - (y / height);
		} else {
			iy = iy + (y / height);
		}
		matrix.postScale(ix, iy, cx, cy);
		path.transform(matrix);
		drawCacheBitmap(canvas, sdbitmap);
		drawCacheBitmap(canvas, cachebBitmap);
		canvas.drawPath(path, penpaint);
	}

	@Override
	public void copy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void paste(Canvas canvas, Bitmap cachebBitmap, Bitmap sdbitmap,
			Paint paint) {
		cx += 10;
		cy += 10;
		initdata();
		Matrix matrix = new Matrix();
		matrix.preTranslate(10,10);
		path.transform(matrix);
		drawCacheBitmap(canvas, sdbitmap);
		drawCacheBitmap(canvas, cachebBitmap);
		canvas.drawPath(path, paint);
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Canvas canvas, Bitmap cachebBitmap, Bitmap sdbitmap,
			Paint penPaint, float sa, float sb, float movex, float movey) {
		drawCacheBitmap(canvas, sdbitmap);
		drawCacheBitmap(canvas, cachebBitmap);
		penpaint = penPaint;
		drawCanvasBezier(canvas, penPaint, sa, sb, movex, movey);
	}

}
