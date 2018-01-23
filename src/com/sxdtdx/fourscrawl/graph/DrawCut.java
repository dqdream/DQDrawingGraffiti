package com.sxdtdx.fourscrawl.graph;

import com.sxdtdx.fourscrawl.tool.SharePrefenceUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path.Direction;

/**
 * ºÙ«–
 * 
 * @author dq
 * 
 */
public class DrawCut extends Graph {

	public void drawCanvasCut(Canvas canvas, Paint paint, float sa, float sb,
			float x, float y) {
		path.reset();
		path.addRect(left, top, right, bottom, Direction.CCW);
		canvas.drawPath(path, paint);
	}

	public void GraphCut(Canvas canvas, Bitmap cachebBitmap, Bitmap sdbitmap,
			Paint penPaint, float sa, float sb, float movex, float movey) {
		drawCacheBitmap(canvas, sdbitmap);
		drawCacheBitmap(canvas, cachebBitmap);
		paint = penPaint;
		left = sa;
		top = sb;
		right = movex;
		bottom = movey;
		drawCanvasCut(canvas, penPaint, sa, sb, movex, movey);
	}

	Matrix matrix;

	public void cut(Canvas cachecanvas, Bitmap cachebBitmap, Context context) {
		matrix = new Matrix();
		clipbitmap = Bitmap.createBitmap(cachebBitmap, (int) left, (int) top,
				Math.abs((int) (right - left)), Math.abs((int) (bottom - top)));
		cachecanvas.clipPath(path);
		cachecanvas.drawColor(SharePrefenceUtil.getBackgroundColor(context));
	}

	Bitmap clipbitmap;

	public void drawCut(Canvas canvas, Bitmap cachebBitmap, Bitmap sdbitmap) {
		matrix = new Matrix();
		matrix.setTranslate(left, top);
		drawCacheBitmap(canvas, sdbitmap);
		drawCacheBitmap(canvas, cachebBitmap);
		canvas.drawBitmap(clipbitmap, matrix, null);
	}

	public void save(Canvas cachecanvas) {
		if (matrix != null && clipbitmap != null) {
			cachecanvas.drawBitmap(clipbitmap, matrix, null);
		}
	}

	
	@Override
	public void draw(Canvas canvas, Bitmap cachebBitmap, Bitmap sdbitmap,
			Paint penPaint, float sa, float sb, float movex, float movey) {

	}

	@Override
	public void move(Canvas canvas, Bitmap cachebBitmap, Bitmap sdbitmap,
			float x, float y) {
		left += x;
		right += x;
		top += y;
		bottom += y;
		drawCacheBitmap(canvas, sdbitmap);
		drawCacheBitmap(canvas, cachebBitmap);
		if (matrix != null && clipbitmap != null) {
			matrix.setTranslate(left, top);
			canvas.drawBitmap(clipbitmap, matrix, null);
		}
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
		drawCacheBitmap(canvas, sdbitmap);
		drawCacheBitmap(canvas, cachebBitmap);
		if (matrix != null && clipbitmap != null) {
			matrix.preRotate(cAngle - sAngle, clipbitmap.getWidth() / 2,
					clipbitmap.getHeight() / 2);
			canvas.drawBitmap(clipbitmap, matrix, null);
		}
		sAngle = cAngle;

	}

	@Override
	public void zoom(Canvas canvas, Bitmap cachebBitmap, Bitmap sdbitmap,
			float x, float y, float sa, float sb, float width, float height) {
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
		drawCacheBitmap(canvas, sdbitmap);
		drawCacheBitmap(canvas, cachebBitmap);
		if (matrix != null && clipbitmap != null) {
			matrix.postScale(ix, iy, (right - left) / 2 + left, (bottom - top)
					/ 2 + top);
			canvas.drawBitmap(clipbitmap, matrix, null);
		}
	}

	@Override
	public void copy() {
	}

	@Override
	public void paste(Canvas canvas, Bitmap cachebBitmap, Bitmap sdbitmap,
			Paint p) {
		left += 10;
		right += 10;
		top += 10;
		bottom += 10;
		drawCacheBitmap(canvas, sdbitmap);
		drawCacheBitmap(canvas, cachebBitmap);
		if (matrix != null && clipbitmap != null) {
			matrix.preTranslate(i += 10, i += 10);
			canvas.drawBitmap(clipbitmap, matrix, null);
		}
	}

	@Override
	public void delete() {
	}

}
