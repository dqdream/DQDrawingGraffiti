package com.sxdtdx.fourscrawl.graph;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
/**
 * Ö±Ïß
 * @author dq
 *
 */
public class DrawLine extends Graph {
	Paint penpaint = null;
	public void drawCanvasLine(Canvas canvas, Paint paint, float sa, float sb,
			float x, float y) {
		path.reset();
		path.moveTo(sa, sb);
		path.lineTo(x, y);
		canvas.drawPath(path, paint);
	}

	public void drawGraphLine(Canvas canvas, Bitmap cachebBitmap,
			Bitmap sdbitmap, Paint penPaint, float sa, float sb, float movex,
			float movey) {
		drawCacheBitmap(canvas, sdbitmap);
		drawCacheBitmap(canvas, cachebBitmap);
		penpaint = penPaint;
		left = sa;
		top = sb;
		right = movex;
		bottom = movey;
		drawCanvasLine(canvas, penPaint, sa, sb, movex, movey);
	}

	@Override
	public void move(Canvas canvas, Bitmap cachebBitmap, Bitmap sdbitmap,
			float x, float y) {
		left += x;
		right += x;
		top += y;
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
		cx = (right - left) / 2 + left;
		cy = (bottom - top) / 2 + top;
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
		matrix.postScale(ix, iy, (right - left) / 2 + left, (bottom - top) / 2
				+ top);
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
		left += 10;
		right += 10;
		top += 10;
		bottom += 10;
		Matrix matrix = new Matrix();
		matrix.preTranslate(i += 10, i += 10);
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
		left = sa;
		top = sb;
		right = movex;
		bottom = movey;
		drawCanvasLine(canvas, penPaint, sa, sb, movex, movey);
		
	}

}
