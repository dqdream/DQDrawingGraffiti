package com.sxdtdx.fourscrawl.view;

import com.sxdtdx.fourscrawl.tool.SharePrefenceUtil;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class DrawLineShow extends SurfaceView implements Callback {
private	Paint paint = new Paint();
private	MaskFilter maskFilter;
private	float width = 0;
private	float height = 0;
private   Paint pentext=new Paint();
private   Typeface ty;
	public DrawLineShow(Context context, AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
		width = (float) this.getWidth();
		height = (float) this.getHeight() / 2;
		initPen();
	}

	public DrawLineShow(Context context) {
		super(context);
		getHolder().addCallback(this);
		width = (float) this.getWidth();
		height = (float) this.getHeight() / 2;
		initPen();
	}

	Canvas canvas;

	void initPen() {
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setColor(SharePrefenceUtil.getPaintColor(getContext()));
		paint.setStrokeWidth(SharePrefenceUtil.getPenWidth(getContext()));
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		
		pentext.setAntiAlias(true);
		pentext.setDither(true);
		pentext.setColor(SharePrefenceUtil.getPaintColor(getContext()));
		pentext.setStrokeWidth(0);
		pentext.setStyle(Paint.Style.STROKE);
		pentext.setStrokeJoin(Paint.Join.ROUND);
		pentext.setTextSize(SharePrefenceUtil.getPenText(getContext()));
		switch (SharePrefenceUtil.getTextStyle(getContext())) {
		case 0:
			ty=null;
			break;
		case 1:
			ty=Typeface.createFromAsset(getContext().getAssets(),"fonts/a1.ttf"); 
			break;
		case 2:
			ty=Typeface.createFromAsset(getContext().getAssets(),"fonts/a2.ttf"); 
			break;
		case 3:
			ty=Typeface.createFromAsset(getContext().getAssets(),"fonts/a3.ttf"); 
			break;
		}
		pentext.setTypeface(ty);
		if (SharePrefenceUtil.getPenPoint(getContext())==0) {
			paint.setStrokeCap(Paint.Cap.ROUND);
			pentext.setStrokeCap(Paint.Cap.ROUND);
		}else {
			paint.setStrokeCap(Paint.Cap.SQUARE);
			pentext.setStrokeCap(Paint.Cap.SQUARE);

		}
		switch (SharePrefenceUtil.getPenStyle(getContext())) {
		case 0:
			maskFilter = null;
			break;
		case 1:
			maskFilter = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
			break;
		case 2:
			maskFilter = new EmbossMaskFilter(new float[] { 1, 1, 1 }, 0.4f, 6,
					3.5f);
			break;
		case 3:
			maskFilter = null;
			paint.setAlpha(50);
			pentext.setAlpha(50);
			break;
		}
		paint.setMaskFilter(maskFilter);
		pentext.setMaskFilter(maskFilter);
	}

	public void onPaintChanged() {
		initPen();
		draw();
	}

	public void draw() {
		canvas = getHolder().lockCanvas();
		canvas.drawColor(SharePrefenceUtil.getBackgroundColor(getContext()));
		width = (float) this.getWidth();
		height = (float) this.getHeight() / 3;
		canvas.drawLine(50, height, width - 50, height, paint);
		canvas.drawText("Ϳѻ   tuya 1 2", width/2-130, height*2, pentext);
		getHolder().unlockCanvasAndPost(canvas);
	}

	public void setPaint(Paint p) {
		paint = p;
	}

	public Paint getPaint() {
		return paint;
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		draw();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub

	}
}
