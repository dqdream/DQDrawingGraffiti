package com.sxdtdx.fourscrawl.view;

import com.sxdtdx.fourscrawl.activity.ScrawlActivity;
import com.sxdtdx.fourscrawl.graph.DrawCut;
import com.sxdtdx.fourscrawl.graph.DrawFoldLine;
import com.sxdtdx.fourscrawl.graph.DrawFreeLine;
import com.sxdtdx.fourscrawl.graph.DrawLine;
import com.sxdtdx.fourscrawl.graph.DrawOval;
import com.sxdtdx.fourscrawl.graph.DrawPol;
import com.sxdtdx.fourscrawl.graph.DrawRect;
import com.sxdtdx.fourscrawl.graph.DrawBezier;
import com.sxdtdx.fourscrawl.graph.DrawText;
import com.sxdtdx.fourscrawl.graph.Graph;
import com.sxdtdx.fourscrawl.tool.CacheStack;
import com.sxdtdx.fourscrawl.tool.FloodFillUtil;
import com.sxdtdx.fourscrawl.tool.SharePrefenceUtil;
import com.sxdtdx.fourscrawl.tool.ToastUtil;

import dalvik.system.VMRuntime;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Bitmap.Config;
import android.graphics.PathEffect;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * 主画图类
 * 
 * @author dq
 * 
 */
public class MainDraw extends SurfaceView implements Callback {
	private Paint canvasPaint = new Paint();// 画布画笔
	private Paint penPaint = new Paint();// 默认画笔
	private Paint penPaintFill = new Paint();
	private Canvas canvas;// 默认画布
	private float canvasleft = 0;// 画布的边界 左
	private float canvastop = 0;// 上
	private float canvasright = 0;// 右
	private float canvasbottom = 0;// 下
	private float clickX = 0, clickY = 0;
	private float startX = 0, startY = 0;
	private float x = 0, y = 0; // 移动差距
	private float a = 0, b = 0;// 点击屏幕的初始坐标
	private float sa = 0, sb = 0;// 点击屏幕的方位/ 起始点的位置
	private Bitmap cachebBitmap;// 缓存bitmap
	private Canvas cacheCanvas;// 缓存画布
	private Paint eraserpaint = new Paint();
	private boolean IsMoveCanvas = false;// 画布移动
	private boolean IsZoomCanvas = false;// 画布的 缩放
	private float px = 0, py = 0;// 移动画布画布后的偏移量
	private boolean isMove = true;// 画笔是否移动
	private int canvascolor = Color.GRAY; // 底色
	ColorPickerDialog dialog;// 颜色选择器
	Context activity = new ScrawlActivity();
	int drawstate = 0;// 0 默认画笔 1-6为对应的图形 7为文字
	// 图形类
	DrawRect drawRect = null;
	DrawOval drawOval = null;
	DrawLine drawLine = null;
	DrawBezier drawbezier = null;
	DrawPol drawPol = null;
	DrawFoldLine drawFoldLine = null;
	DrawFreeLine drawFreeLine = null;
	DrawText drawText = null;
	DrawCut drawCut = null;
	Graph graph = null;
	CacheStack cacheStack = new CacheStack(getContext());// 撤销 恢复 栈
	Bitmap sdbitmap;// 底层背景
	Canvas sdc;// 底层背景画布
	int lastgraph = 0, graphflag = 0;
	boolean ismovegraph = false;
	MaskFilter maskFilter; // 画笔筛选
	private Matrix matrix;// 画布矩阵 滚动 缩放时使用

	public MainDraw(Context context, AttributeSet attrs) {
		super(context, attrs);
		activity = context;
		this.canvasright = SharePrefenceUtil.getCanvasWidth(context);
		this.canvasbottom = SharePrefenceUtil.getCanvasHight(context);
		init();
	}

	public MainDraw(Context context) {
		super(context);
		activity = context;
		this.canvasright = SharePrefenceUtil.getCanvasWidth(context);
		this.canvasbottom = SharePrefenceUtil.getCanvasHight(context);
		init();
	}

	// 初始化数据
	public void init() {
		matrix = new Matrix();
		drawFreeLine = new DrawFreeLine();
		initPenPaint();
		getHolder().addCallback(this);
		try {
			getInitCacheBitmap();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 初始化画笔
	private void initPenPaint() {
		canvasPaint
				.setColor(SharePrefenceUtil.getBackgroundColor(getContext()));

		penPaint.setAntiAlias(true);
		penPaint.setDither(true);
		penPaint.setColor(SharePrefenceUtil.getPaintColor(getContext()));
		penPaint.setStyle(Paint.Style.STROKE);
		penPaint.setStrokeWidth(SharePrefenceUtil.getPenWidth(getContext()));
		penPaint.setStrokeJoin(Paint.Join.ROUND);

		penPaintFill.setAntiAlias(true);
		penPaintFill.setDither(true);
		penPaintFill.setColor(SharePrefenceUtil.getPaintColor(getContext()));
		penPaintFill.setStyle(Paint.Style.STROKE);
		penPaintFill
				.setStrokeWidth(SharePrefenceUtil.getPenWidth(getContext()));
		penPaintFill.setStrokeJoin(Paint.Join.ROUND);

		/**
		 * 橡皮属性
		 */
		eraserpaint.setAntiAlias(true);
		eraserpaint.setDither(true);
		eraserpaint.setStrokeWidth(SharePrefenceUtil.getPenWidth(getContext()));
		eraserpaint.setStyle(Paint.Style.STROKE);
		eraserpaint.setStrokeJoin(Paint.Join.ROUND);
		eraserpaint
				.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
		if (SharePrefenceUtil.getPenPoint(getContext()) == 0) {
			penPaint.setStrokeCap(Paint.Cap.ROUND);
			penPaintFill.setStrokeCap(Paint.Cap.ROUND);
			eraserpaint.setStrokeCap(Paint.Cap.ROUND);
		} else {
			penPaint.setStrokeCap(Paint.Cap.SQUARE);
			penPaintFill.setStrokeCap(Paint.Cap.SQUARE);
			eraserpaint.setStrokeCap(Paint.Cap.SQUARE);
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
			penPaint.setAlpha(50);
			penPaintFill.setAlpha(50);
			break;
		}
		penPaint.setMaskFilter(maskFilter);
		penPaintFill.setMaskFilter(maskFilter);
	}

	// 初始化 画布背景
	public void getInitCacheBitmap() throws Exception {
		if (!TextUtils.isEmpty(SharePrefenceUtil.getSDPath(getContext()))) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = SharePrefenceUtil
					.getImageIsSize(getContext()); // 图片宽高都为原来的二分之一，即图片为原来的四分之一?
			options.inTempStorage = new byte[1024 * 1024 * 5]; // 5MB的临时存储空间
			options.inPurgeable = true;
			options.inInputShareable = true;
			Bitmap b = null;
			if (SharePrefenceUtil.getImageIsSize(getContext()) != 1) {
				ToastUtil.ShortMake(getContext(), "图片过大，自动缩放！");
			}
			b = BitmapFactory.decodeStream(
					getContext().getContentResolver()
							.openInputStream(
									Uri.parse(SharePrefenceUtil
											.getSDPath(getContext()))), null,
					options);
			sdbitmap = Bitmap.createBitmap(b.getWidth(), b.getHeight(),
					Config.RGB_565);
			sdc = new Canvas(sdbitmap);
			sdc.drawBitmap(b, 0, 0, null);
			if (!b.isRecycled()) {
				b.recycle();
			}
			cachebBitmap = Bitmap.createBitmap(b.getWidth(), b.getHeight(),
					Config.ARGB_4444);
		} else {
			sdbitmap = Bitmap.createBitmap((int) canvasright,
					(int) canvasbottom, Config.RGB_565);
			sdc = new Canvas(sdbitmap);
			sdc.drawRect(canvasleft, canvastop, canvasright, canvasbottom,
					canvasPaint);
			cachebBitmap = Bitmap.createBitmap((int) canvasright,
					(int) canvasbottom, Config.ARGB_4444);
		}
		cacheCanvas = new Canvas(cachebBitmap);
		cacheStack.push(cachebBitmap);
	}

	/**
	 * 画图方法 以一个缓存Bitmap 形式 载入
	 */
	public void drawCanvas() {
		lockSetCanvas();
		canvas.drawBitmap(sdbitmap, 0, 0, null);
		canvas.drawBitmap(BitmapWriting(cachebBitmap), 0, 0, null);
		getHolder().unlockCanvasAndPost(canvas);
	}

	public Bitmap BitmapWriting(Bitmap bitmap) {
		cacheCanvas = new Canvas(bitmap);
		if (isMove) {
			if (drawstate == -1) {// 橡皮
				cacheCanvas.drawLine(startX - px, startY - py, clickX - px,
						clickY - py, eraserpaint);
			}
		}
		startX = clickX;
		startY = clickY;
		return bitmap;
	}

	/**
	 * 获得锁定画布
	 * 
	 * @return
	 */
	public Canvas lockSetCanvas() {
		canvas = getHolder().lockCanvas(
				new Rect((int) canvasleft, (int) canvastop, (int) canvasright,
						(int) canvasbottom));
		canvas.drawColor(canvascolor);
		return canvas;
	}

	// 锁定画布
	public Canvas lockSetCanvasRect(float x, float y) {
		canvas = getHolder()
				.lockCanvas(
						new Rect((int) x - 75, (int) y - 75, (int) x + 75,
								(int) y + 75));
		canvas.drawColor(canvascolor);
		return canvas;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		drawCanvas();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	boolean isLastFill = false;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		clickX = event.getX();
		clickY = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if ((ismovegraph || (lastgraph == 3 && (drawbezier.getI() == 2 || drawbezier
					.getI() == 3)))
					&& !IsMoveCanvas
					&& !IsZoomCanvas
					&& graphflag != 5) {
				cachebBitmap = cacheStack.undo(cachebBitmap, cacheCanvas);
				if (graphflag == 3) {
					graph.setsAngle(0);
					graph.setcAngle(0);
				}
				if (isLastFill) {
					if (lastgraph != 8) {
						cachebBitmap = cacheStack.undo(cachebBitmap,
								cacheCanvas);
					}
					isLastFill = false;
				}
			}
			if (ismovegraph && !IsMoveCanvas && !IsZoomCanvas && graphflag == 5) {
				fillAlgorithm.startFill((int) clickX, (int) clickY);
				isLastFill = true;
			}
			if (((ScrawlActivity) activity).mtoolswitch) {
				handler.sendEmptyMessageDelayed(1, 300);
			}
			if (getDrawstate() > 0) {
				penPaint.setStrokeWidth(5);
				penPaintFill.setStrokeWidth(5);
				PathEffect effect = new DashPathEffect(new float[] { 10, 10,
						10, 10 }, 5);
				penPaint.setPathEffect(effect);
				penPaintFill.setPathEffect(effect);
			}
			isMove = false;
			drawCanvas();
			a = event.getRawX();
			b = event.getRawY();
			sa = a;
			sb = b;
			break;
		case MotionEvent.ACTION_MOVE:
			x = event.getRawX() - a;
			y = event.getRawY() - b;
			if (IsMoveCanvas) {// 画布移动
				px = px + x;
				py = py + y;
				matrix.postTranslate(x, y);
				canvas.setMatrix(matrix);
				drawCanvas();
			} else if (IsZoomCanvas) {// 画布缩放
				float ix = 1.0f;
				float iy = 1.0f;
				if (sa < canvasright / 2) {
					ix = ix - (x / canvasright);
				} else {
					ix = ix + (x / canvasright);
				}
				if (sb < canvasbottom / 2) {
					iy = iy - (y / canvasbottom);
				} else {
					iy = iy + (y / canvasbottom);
				}
				matrix.postScale(ix, iy, canvasright / 2, canvasbottom / 2);
				canvas.setMatrix(matrix);
				drawCanvas();
			}
			if (!ismovegraph) {
				moveDraw(event);
			} else if (ismovegraph && !IsMoveCanvas && !IsZoomCanvas
					&& graphflag != 5) {
				switch (graphflag) {
				case 1:
					graph.move(lockSetCanvas(), cachebBitmap, sdbitmap, x, y);
					getHolder().unlockCanvasAndPost(canvas);
					break;
				case 2:
					graph.zoom(lockSetCanvas(), cachebBitmap, sdbitmap, x, y,
							sa, sb, canvasright, canvasbottom);
					getHolder().unlockCanvasAndPost(canvas);
					break;
				case 3:
					graph.rotate(lockSetCanvas(), cachebBitmap, sdbitmap,
							event.getRawX(), event.getRawY());
					getHolder().unlockCanvasAndPost(canvas);
					break;
				}
			}
			a = event.getRawX();
			b = event.getRawY();
			break;
		case MotionEvent.ACTION_UP:
			System.out.println(graphflag);
			if (!IsMoveCanvas && !IsZoomCanvas && getDrawstate() >= 0
					&& graphflag != 5) {
				penPaint.setPathEffect(null);
				penPaintFill.setPathEffect(null);
				penPaint.setStrokeWidth(SharePrefenceUtil
						.getPenWidth(getContext()));
				penPaintFill.setStrokeWidth(SharePrefenceUtil
						.getPenWidth(getContext()));
				switch (getDrawstate()) {
				case 0:
					lastgraph = 0;
					graph = drawFreeLine;
					graph.drawCacheCanvasGraph(cacheCanvas, penPaint);
					drawFreeLine.initpath();
					break;
				case 1:
					lastgraph = 1;
					graph = drawLine;
					graph.drawCacheCanvasGraph(cacheCanvas, penPaint);
					break;
				case 2:
					lastgraph = 2;
					graph = drawFoldLine;
					if (drawFoldLine.drawNextPoint(event.getRawX() - px,
							event.getRawY() - py)
							&& !ismovegraph) {
						cachebBitmap = cacheStack.undo(cachebBitmap,
								cacheCanvas);
						drawCanvas();
					}
					drawFoldLine
							.drawCacheCanvasGraph(cacheCanvas, penPaintFill);
					break;
				case 3:
					lastgraph = 3;
					graph = drawbezier;
					drawbezier.EvenUp();
					graph.drawCacheCanvasGraph(cacheCanvas, penPaint);
					break;
				case 4:
					lastgraph = 4;
					graph = drawRect;
					graph.drawCacheCanvasGraph(cacheCanvas, penPaintFill);
					break;
				case 5:
					lastgraph = 5;
					graph = drawPol;
					if (drawPol.drawNextPoint(event.getRawX() - px,
							event.getRawY() - py)
							&& !ismovegraph) {
						cachebBitmap = cacheStack.undo(cachebBitmap,
								cacheCanvas);
						drawCanvas();
					}
					drawPol.drawCacheCanvasGraph(cacheCanvas, penPaintFill);
					break;
				case 6:
					lastgraph = 6;
					graph = drawOval;
					graph.drawCacheCanvasGraph(cacheCanvas, penPaintFill);
					break;
				case 7:
					lastgraph = 7;
					graph = drawText;
					if (!ismovegraph) {
						if (getX(event.getRawX()) - getX(sa) > 0
								&& (int) getY(event.getRawY()) - getY(sb) > 0) {
							drawText.drawGraphText(lockSetCanvas(),
									cachebBitmap, sdbitmap, penPaint, getX(sa),
									getY(sb), getX(event.getRawX()),
									getY(event.getRawY()));
							getHolder().unlockCanvasAndPost(canvas);
							handler.sendEmptyMessage(3);
						}
					} else {
						drawText.saveCacheText(cacheCanvas);
					}
					break;
				case 8:
					lastgraph = 8;
					graph = drawCut;
					if (!ismovegraph) {
						if (getX(event.getRawX()) - getX(sa) > 0
								&& (int) getY(event.getRawY()) - getY(sb) > 0) {
							drawCut.cut(cacheCanvas, cachebBitmap, getContext());
							drawCanvas();
							try {
								cacheStack.push(cachebBitmap);
							} catch (Exception e) {
								e.printStackTrace();
							}
							drawCut.drawCut(lockSetCanvas(), cachebBitmap,
									sdbitmap);
							getHolder().unlockCanvasAndPost(canvas);
							ismovegraph = true;
							graphflag = 1;
						}
					} else {
						drawCut.save(cacheCanvas);
					}
					break;
				}
				if (getDrawstate() != 7 && getDrawstate() != 8) {
					drawCanvas();
				}

				if (!((ScrawlActivity) activity).mGraphTool) {
					handler.sendEmptyMessage(2);
				}

			}
			try {
				cacheStack.push(cachebBitmap);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		return true;
	}

	boolean isclick = false;

	// 恢复 比例
	public void restore() {
		matrix = new Matrix();
		matrix.setTranslate(px, py);
		canvas.setMatrix(matrix);
		drawCanvas();
	}

	public void CanvasZoom(float sx) {
		matrix = new Matrix();
		matrix.setScale(sx, sx, canvasright / 2, canvasbottom / 2);
		matrix.postTranslate(px, py);
		canvas.setMatrix(matrix);
		drawCanvas();
	}

	// 画图形
	private void moveDraw(MotionEvent event) {
		if (!IsMoveCanvas && !IsZoomCanvas) {
			switch (getDrawstate()) {
			case -1:
				isMove = true;
				drawCanvas();
				break;
			case 0:
				drawFreeLine.drawGraphFreeLine(
						lockSetCanvasRect(event.getRawX(), event.getRawY()),
						cachebBitmap, sdbitmap, penPaint, getX(sa), getY(sb),
						getX(event.getRawX()), getY(event.getRawY()));
				getHolder().unlockCanvasAndPost(canvas);
				break;
			case 1:
				drawLine.drawGraphLine(lockSetCanvas(), cachebBitmap, sdbitmap,
						penPaint, sa - px, sb - py, event.getRawX() - px,
						event.getRawY() - py);
				getHolder().unlockCanvasAndPost(canvas);
				break;
			case 2:
				drawFoldLine.drawGraphFoldLine(lockSetCanvas(), cachebBitmap,
						sdbitmap, penPaintFill, getX(sa), getY(sb),
						getX(event.getRawX()), getY(event.getRawY()));
				getHolder().unlockCanvasAndPost(canvas);
				break;
			case 3:
				drawbezier.drawGraphBezier(lockSetCanvas(), cachebBitmap,
						sdbitmap, penPaint, getX(sa), getY(sb),
						getX(event.getRawX()), getY(event.getRawY()));
				getHolder().unlockCanvasAndPost(canvas);
				break;
			case 4:
				drawRect.drawGraphRect(lockSetCanvas(), cachebBitmap, sdbitmap,
						penPaintFill, getX(sa), getY(sb),
						getX(event.getRawX()), getY(event.getRawY()));
				getHolder().unlockCanvasAndPost(canvas);
				break;
			case 5:
				drawPol.drawGraphPolLine(lockSetCanvas(), cachebBitmap,
						sdbitmap, penPaintFill, getX(sa), getY(sb),
						getX(event.getRawX()), getY(event.getRawY()));
				getHolder().unlockCanvasAndPost(canvas);
				break;
			case 6:
				drawOval.drawGraphOval(lockSetCanvas(), cachebBitmap, sdbitmap,
						penPaintFill, getX(sa), getY(sb),
						getX(event.getRawX()), getY(event.getRawY()));
				getHolder().unlockCanvasAndPost(canvas);
				break;
			case 7:
				drawText.drawGraphText(lockSetCanvas(), cachebBitmap, sdbitmap,
						penPaint, getX(sa), getY(sb), getX(event.getRawX()),
						getY(event.getRawY()));
				getHolder().unlockCanvasAndPost(canvas);
				break;
			case 8:
				drawCut.GraphCut(lockSetCanvas(), cachebBitmap, sdbitmap,
						penPaint, getX(sa), getY(sb), getX(event.getRawX()),
						getY(event.getRawY()));
				getHolder().unlockCanvasAndPost(canvas);
				break;
			}
		}
	}

	/*
	 * 图元移动
	 */
	public void move() {
		if (!IsMoveCanvas && !IsZoomCanvas) {
			if (getDrawstate() >= 0 && graph != null) {
				ismovegraph = true;
				graphflag = 1;
			}
		}
	}

	/*
	 * 图元缩放
	 */
	public void zoom() {
		if (!IsMoveCanvas && !IsZoomCanvas) {
			if (getDrawstate() >= 0 && graph != null) {
				ismovegraph = true;
				graphflag = 2;
			}
		}
	}

	/**
	 * 旋转
	 */
	public void rotate() {
		if (!IsMoveCanvas && !IsZoomCanvas) {
			if (getDrawstate() >= 0 && graph != null) {
				ismovegraph = true;
				graphflag = 3;
			}
		}
	}

	/*
	 * 图元 粘贴 粘贴最后一次画的图元
	 */
	public void paste() {
		if (!IsMoveCanvas && !IsZoomCanvas) {
			if (getDrawstate() >= 0 && graph != null && graphflag != 5) {
				graph.paste(lockSetCanvas(), cachebBitmap, sdbitmap, penPaint);
				getHolder().unlockCanvasAndPost(canvas);
				switch (getDrawstate()) {
				case 7:
					drawText.saveCacheText(cacheCanvas);
					break;
				case 8:
					drawCut.save(cacheCanvas);
					break;
				default:
					if (getDrawstate() == 2 || getDrawstate() == 4
							|| getDrawstate() == 5 || getDrawstate() == 6) {
						graph.drawCacheCanvasGraph(cacheCanvas, penPaintFill);
					} else
						graph.drawCacheCanvasGraph(cacheCanvas, penPaint);
					break;
				}
				drawCanvas();
				try {
					cacheStack.push(cachebBitmap);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				ToastUtil.ShortMake(getContext(), "复制失败，最后一次操作没有进行图元绘制");
			}
		}
	}

	/*
	 * 图元删除
	 */
	public void graphdelete() {
		if (!IsMoveCanvas && !IsZoomCanvas) {
			initGraph();
			undo();
		}
	}

	FloodFillUtil fillAlgorithm;

	// 填充
	public void graphfill() {
		if (!IsMoveCanvas && !IsZoomCanvas) {
			dialog = new ColorPickerDialog(getContext(), Color.BLACK, "填充颜色",
					new ColorPickerDialog.OnColorChangedListener() {
						@Override
						public void colorChanged(int color) {
							fillAlgorithm = new FloodFillUtil(cachebBitmap,
									color);
							ismovegraph = true;
							graphflag = 5;
						}
					});
			dialog.show();
		}
	}

	/**
	 * 撤销
	 */
	public void undo() {
		initGraph();
		cachebBitmap = cacheStack.undo(cachebBitmap, cacheCanvas);
		drawCanvas();
	}

	public void drawtext(String text) {
		drawText.drawtext(text, cacheCanvas, penPaint, getContext());
		drawCanvas();
		try {
			cacheStack.push(cachebBitmap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 清空画布
	public void clearCanvas() {
		initGraph();
		cacheStack.clear();
		try {
			if (!cachebBitmap.isRecycled()) {
				cachebBitmap.recycle();
			}
			getInitCacheBitmap();
		} catch (Exception e) {
			e.printStackTrace();
		}
		drawCanvas();
	}

	/**
	 * 恢复
	 */
	public void redo() {
		initGraph();
		cachebBitmap = cacheStack.redo(cachebBitmap, cacheCanvas);
		drawCanvas();
	}

	/**
	 * 是否让画布移动
	 * 
	 * @return
	 */
	public boolean isIsMoveCanvas() {
		return IsMoveCanvas;
	}

	public void setIsMoveCanvas(boolean isMoveCanvas) {
		IsMoveCanvas = isMoveCanvas;
	}

	public boolean isIsZoomCanvas() {
		return IsZoomCanvas;
	}

	public void setIsZoomCanvas(boolean isZoomCanvas) {
		IsZoomCanvas = isZoomCanvas;
	}

	public Bitmap getCachebBitmap() {
		sdc.drawBitmap(cachebBitmap, 0, 0, null);
		return sdbitmap;
	}

	public void initSet() throws Exception {
		initPenPaint();
		cacheStack.setMax(SharePrefenceUtil.getCacheNum(getContext()));
		if (TextUtils.isEmpty(SharePrefenceUtil.getSDPath(getContext()))) {
			sdbitmap = Bitmap.createBitmap((int) canvasright,
					(int) canvasbottom, Config.RGB_565);
			sdc = new Canvas(sdbitmap);
			sdc.drawRect(canvasleft, canvastop, canvasright, canvasbottom,
					canvasPaint);
			cacheCanvas = new Canvas(cachebBitmap);
			cacheStack.push(cachebBitmap);
		}
		drawCanvas();
	}

	public int getDrawstate() {
		return drawstate;
	}

	public void setDrawstate(int drawstate) {
		this.drawstate = drawstate;
		initGraph();
	}

	/**
	 * 偏移后X y
	 * 
	 * @param x
	 *            y
	 * @return
	 */
	public float getX(float x) {
		return x - px;
	}

	public float getY(float y) {
		return y - py;
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				((ScrawlActivity) activity).ToolUp();
				((ScrawlActivity) activity).mtoolswitch = false;
				break;
			case 2:
				((ScrawlActivity) activity).showGraphTool();
				((ScrawlActivity) activity).mGraphTool = true;
				break;
			case 3:
				((ScrawlActivity) activity).ShowTextDialog();
				break;
			case 10:

				break;
			}
		};
	};

	public void initGraph() {
		setIsmavegraph();
		graph = null;
		switch (getDrawstate()) {
		case 0:
			drawFreeLine = new DrawFreeLine();
			break;
		case 1:
			drawLine = new DrawLine();
			break;
		case 2:
			drawFoldLine = new DrawFoldLine();
			break;
		case 3:
			drawbezier = new DrawBezier();
			break;
		case 4:
			drawRect = new DrawRect();
			break;
		case 5:
			drawPol = new DrawPol();
			break;
		case 6:
			drawOval = new DrawOval();
			break;
		case 7:
			drawText = new DrawText();
			break;
		case 8:
			drawCut = new DrawCut();
			break;
		}
	}

	public void setIsmavegraph() {
		graphflag = 0;
		if (ismovegraph) {
			ismovegraph = false;
		}
	}

}
