package com.sxdtdx.fourscrawl.graph;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
/**
 * Ϳѻ
 * @author dq
 *
 */
public class DrawFreeLine extends Graph {

	float startx = 0, starty = 0;
	Paint penpaint = null;
	public void drawCanvasFreeLine(Canvas canvas, Paint paint, float sa,
			float sb, float x, float y) {
		if (left == 0 && top == 0) {
			path.reset();
			startx = sa;
			starty = sb;
			left = sa;
			top = sb;
			path.moveTo(left, top);
		}
		path.lineTo(x, y);
		canvas.drawPath(path, paint);
		left = x;
		top = y;
	}

	public void initpath() {
		left = 0;
		top = 0;
	}

	public void drawGraphFreeLine(Canvas canvas, Bitmap cachebBitmap,
			Bitmap sdbitmap, Paint penPaint, float sa, float sb, float movex,
			float movey) {
		drawCacheBitmap(canvas, sdbitmap);
		drawCacheBitmap(canvas, cachebBitmap);
		penpaint=penPaint;
		right = movex;
		bottom = movey;
		drawCanvasFreeLine(canvas, penPaint, sa, sb, movex, movey);
	}

	@Override
	public void move(Canvas canvas, Bitmap cachebBitmap, Bitmap sdbitmap,
			float x, float y) {
		startx += x;
		right += x;
		starty += y;
		bottom += y;
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
		cx = (right - startx) / 2 + startx;
		cy = (bottom - starty) / 2 + starty;
		if (sAngle == 0) {
			sAngle = computeCurrentAngle(x, y, cx, cy);
		}
		cAngle = computeCurrentAngle(x, y, cx, cy);
		Matrix matrix = new Matrix();
		matrix.preRotate(cAngle-sAngle,cx,cy);
		path.transform(matrix);
		drawCacheBitmap(canvas, sdbitmap);
		drawCacheBitmap(canvas, cachebBitmap);
		canvas.drawPath(path, penpaint);
		sAngle=cAngle;
	}

	@Override
	public void zoom(Canvas canvas, Bitmap cachebBitmap, Bitmap sdbitmap,
			float x, float y, float sa, float sb, float width, float height) {
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
		matrix.postScale(ix, iy, (right - startx) / 2 + left, (bottom - starty) / 2
				+ starty);
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
		startx += 10;
		right += 10;
		starty += 10;
		bottom += 10;
		initpath();
		Matrix matrix = new Matrix();
		matrix.preTranslate(i+=10,i+=10);
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
		right = movex;
		bottom = movey;
		drawCanvasFreeLine(canvas, penPaint, sa, sb, movex, movey);
	}
}
