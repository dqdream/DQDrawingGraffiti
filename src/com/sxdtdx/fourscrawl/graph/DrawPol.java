package com.sxdtdx.fourscrawl.graph;

import java.util.ArrayList;
import java.util.List;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
/**
 * ¶à±ßÐÎ
 * @author dq
 *
 */
public class DrawPol extends Graph {

	float startx = 0, starty = 0;

	public void drawCanvasPolLine(Canvas canvas, Paint paint, float sa,
			float sb, float x, float y) {
		if (left == 0 || top == 0) {
			xs.add(sa);
			ys.add(sb);
			startx = sa;
			starty = sb;
			left = sa;
			top = sb;
			ps.moveTo(left, top);
		}
		path.reset();
		path.moveTo(left, top);
		path.lineTo(x, y);
		canvas.drawPath(path, paint);
	}

	public void drawGraphPolLine(Canvas canvas, Bitmap cachebBitmap,
			Bitmap sdbitmap, Paint penPaint, float sa, float sb, float movex,
			float movey) {
		drawCacheBitmap(canvas, sdbitmap);
		drawCacheBitmap(canvas, cachebBitmap);
		paint = penPaint;
		right = movex;
		bottom = movey;
		drawCanvasPolLine(canvas, penPaint, sa, sb, movex, movey);
	}
  
	Path ps = new Path();
	int m = 0;
	boolean mflag = true;
	final int p = 30;
	List<Float> xs = new ArrayList<Float>();
	List<Float> ys = new ArrayList<Float>();
	public boolean drawNextPoint(float x, float y) {
		m = m + 1;
		if (mflag) {
			if (x > startx - p && x < startx + p && y > starty - p
					&& y < starty + p && m > 1) {
				path.reset();
				path.moveTo(left, top);
				path.lineTo(startx, starty);
				ps.lineTo(startx, starty);
				getcxy();
				initdata();
			} else {
				xs.add(x);
				ys.add(y);
				ps.lineTo(right, bottom);
				left = x;
				top = y;
				getcxy();
			}
		} else {
			mflag = true;
		}
		if (m > 1) {
			return true;
		}
		return false;
	}

	public void getcxy() {
		float countx = 0;
		float county = 0;
		for (int i = 0; i < xs.size(); i++) {
			countx = countx + xs.get(i);
			county = county + ys.get(i);
		}
		cx = countx / xs.size();
		cy = county / ys.size();
	}

	private void initdata() {
		startx = 0;
		starty = 0;
		left = 0;
		top = 0;
		right = 0;
		bottom = 0;
		path = new Path();
		xs = new ArrayList<Float>();
		ys = new ArrayList<Float>();
	}

	public void drawCacheCanvasGraph(Canvas cachecanvas, Paint paint) {
		cachecanvas.drawPath(ps, paint);
	}

	@Override
	public void move(Canvas canvas, Bitmap cachebBitmap, Bitmap sdbitmap,
			float x, float y) {
		mflag = false;
		cx += x;
		cy += y;
		Matrix matrix = new Matrix();
		matrix.preTranslate(x, y);
		ps.transform(matrix);
		drawCacheBitmap(canvas, sdbitmap);
		drawCacheBitmap(canvas, cachebBitmap);
		canvas.drawPath(ps, paint);
	}

	@Override
	public void rotate(Canvas canvas, Bitmap cachebBitmap, Bitmap sdbitmap,
			float x, float y) {
		mflag = false;
		if (sAngle == 0) {
			sAngle = computeCurrentAngle(x, y, cx, cy);
		}
		cAngle = computeCurrentAngle(x, y, cx, cy);
		Matrix matrix = new Matrix();
		matrix.preRotate(cAngle - sAngle, cx, cy);
		ps.transform(matrix);
		drawCacheBitmap(canvas, sdbitmap);
		drawCacheBitmap(canvas, cachebBitmap);
		canvas.drawPath(ps, paint);
		sAngle = cAngle;
	}

	@Override
	public void zoom(Canvas canvas, Bitmap cachebBitmap, Bitmap sdbitmap,
			float x, float y, float sa, float sb, float width, float height) {
		mflag = false;
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
		ps.transform(matrix);
		drawCacheBitmap(canvas, sdbitmap);
		drawCacheBitmap(canvas, cachebBitmap);
		canvas.drawPath(ps, paint);
	}

	@Override
	public void copy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void paste(Canvas canvas, Bitmap cachebBitmap, Bitmap sdbitmap,
			Paint p) {
		cx += 10;
		cy += 10;
		if (left != 0) {
			left += 10;
			top += 10;
		}
		Matrix matrix = new Matrix();
		matrix.preTranslate(10, 10);
		ps.transform(matrix);
		drawCacheBitmap(canvas, sdbitmap);
		drawCacheBitmap(canvas, cachebBitmap);
		canvas.drawPath(ps, paint);
	}

	@Override
	public void delete() {

	}

	@Override
	public void draw(Canvas canvas, Bitmap cachebBitmap, Bitmap sdbitmap,
			Paint penPaint, float sa, float sb, float movex, float movey) {
		drawCacheBitmap(canvas, sdbitmap);
		drawCacheBitmap(canvas, cachebBitmap);
		paint = penPaint;
		right = movex;
		bottom = movey;
		drawCanvasPolLine(canvas, penPaint, sa, sb, movex, movey);
	}

}
