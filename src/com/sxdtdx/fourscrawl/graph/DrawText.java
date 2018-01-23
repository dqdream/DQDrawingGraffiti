package com.sxdtdx.fourscrawl.graph;

import com.sxdtdx.fourscrawl.tool.SharePrefenceUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path.Direction;
import android.graphics.Typeface;

/**
 * ÎÄ±¾Àà
 * 
 * @author dq
 * 
 */
public class DrawText extends Graph {

	private Bitmap textbitmap;
	private Canvas textcanvas;
	private Paint pat;
	Typeface ty;

	public void drawCanvasText(Canvas canvas, Paint paint, float sa, float sb,
			float x, float y) {
		path.reset();
		path.addRect(left, top, right, bottom, Direction.CCW);
		canvas.drawPath(path, paint);
	}

	public void drawGraphText(Canvas canvas, Bitmap cachebBitmap,
			Bitmap sdbitmap, Paint penPaint, float sa, float sb, float movex,
			float movey) {
		drawCacheBitmap(canvas, sdbitmap);
		drawCacheBitmap(canvas, cachebBitmap);
		paint = penPaint;
		left = sa;
		top = sb;
		right = movex;
		bottom = movey;
		drawCanvasText(canvas, penPaint, sa, sb, movex, movey);
	}

	public void drawtext(String text, Canvas cachecanvas, Paint p,
			Context context) {
		switch (SharePrefenceUtil.getTextStyle(context)) {
		case 0:
			ty = null;
			break;
		case 1:
			ty = Typeface.createFromAsset(context.getAssets(), "fonts/a1.ttf");
			break;
		case 2:
			ty = Typeface.createFromAsset(context.getAssets(), "fonts/a2.ttf");
			break;
		case 3:
			ty = Typeface.createFromAsset(context.getAssets(), "fonts/a3.ttf");
			break;
		}
		textbitmap = Bitmap.createBitmap(Math.abs((int) (right - left)),
				Math.abs((int) (bottom - top)), Config.ARGB_4444);
		textcanvas = new Canvas(textbitmap);
		pat = new Paint(p);
		pat.setStrokeWidth(0);
		pat.setTextSize(SharePrefenceUtil.getPenText(context));
		pat.setTypeface(ty);
		textcanvas.drawText(text, 0, (bottom - top) / 2, pat);
		matrix = new Matrix();
		matrix.preTranslate(left, top);
		cachecanvas.drawBitmap(textbitmap, matrix, null);
	}

	public void saveCacheText(Canvas cachecanvas) {
		if (matrix != null && textbitmap != null) {
			cachecanvas.drawBitmap(textbitmap, matrix, null);
		}
	}

	Matrix matrix;

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
		if (matrix != null && textbitmap != null) {
			matrix.setTranslate(left, top);
			canvas.drawBitmap(textbitmap, matrix, null);
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
		if (matrix != null && textbitmap != null) {
			matrix.preRotate(cAngle - sAngle, textbitmap.getWidth() / 2,
					textbitmap.getHeight() / 2);
			canvas.drawBitmap(textbitmap, matrix, null);
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
		if (matrix != null && textbitmap != null) {
			matrix.postScale(ix, iy, (right - left) / 2 + left, (bottom - top)
					/ 2 + top);
			canvas.drawBitmap(textbitmap, matrix, null);
		}
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
		drawCacheBitmap(canvas, sdbitmap);
		drawCacheBitmap(canvas, cachebBitmap);
		if (matrix != null && textbitmap != null) {
			matrix.preTranslate(i += 10, i += 10);
			canvas.drawBitmap(textbitmap, matrix, null);
		}
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

}
