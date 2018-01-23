package com.sxdtdx.fourscrawl.activity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sxdtdx.fourscrawl.R;
import com.sxdtdx.fourscrawl.tool.SaveToFile;
import com.sxdtdx.fourscrawl.tool.ToastUtil;
import com.sxdtdx.fourscrawl.view.MainDraw;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * 画图页面
 * 
 * @author dq
 * 
 */
public class ScrawlActivity extends Activity implements OnClickListener {

	private ImageView imgScrawToolSwitch, imgScrawUndo, imgScrawRedo;
	private LinearLayout linScrawTool, linScrawGraph;
	private Button rbtnScrawMove, rbtnScrawZoom, rbtnScrawClear, rbtnScrawSave,
			rbtnScrawFill, rbtnScrawSet, rbtnScrawPen, rbtnScrawPel,
			rbtnScrawPelText, rbtnScrawEraser;
	public boolean mtoolswitch = false, mIsMoveCanvas = false,
			mIsZoomCanvas = false, mGraphTool = false;
	private MainDraw mainDraw;// 主画图类
	private Button btnScrawGraphMove, btnScrawGrapRotato, btnScrawGraphZoom,
			btnScrawGraphPaste, btnScrawGraphCancle, btnScrawGraphDelete,
			btnScrawGraphReset, btnScrawGraphCut;
	private View textview;// 文本视图
	private EditText textDialog;
	private Button btnDialog;
	private Typeface ty;
	private SeekBar seekbarPenZoom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initDraw();
		setContentView(R.layout.scrawl_layout);
		findID();
		setOnClick();
	}

	/**
	 * 退出程序 清除 缓存文件
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		SaveToFile.delCacheFile();
	}

	private void initDraw() {
		mainDraw = new MainDraw(this);
		ty = Typeface.createFromAsset(this.getAssets(), "fonts/a3.ttf");
	}

	private void findID() {
		mainDraw = (MainDraw) findViewById(R.id.scrawMainDraw);
		imgScrawToolSwitch = (ImageView) findViewById(R.id.imgScrawToolSwitch);
		imgScrawUndo = (ImageView) findViewById(R.id.imgScrawUndo);
		imgScrawRedo = (ImageView) findViewById(R.id.imgScrawRedo);
		linScrawTool = (LinearLayout) findViewById(R.id.linScrawTool);
		linScrawGraph = (LinearLayout) findViewById(R.id.linScrawGraph);
		rbtnScrawMove = (Button) findViewById(R.id.rbtnScrawMove);
		rbtnScrawZoom = (Button) findViewById(R.id.rbtnScrawZoom);
		rbtnScrawClear = (Button) findViewById(R.id.rbtnScrawClear);
		rbtnScrawSave = (Button) findViewById(R.id.rbtnScrawSave);
		rbtnScrawFill = (Button) findViewById(R.id.rbtnScrawFill);
		rbtnScrawSet = (Button) findViewById(R.id.rbtnScrawSet);
		rbtnScrawPen = (Button) findViewById(R.id.rbtnScrawPen);
		rbtnScrawPel = (Button) findViewById(R.id.rbtnScrawPel);
		rbtnScrawPelText = (Button) findViewById(R.id.rbtnScrawPelText);
		rbtnScrawEraser = (Button) findViewById(R.id.rbtnScrawEraser);
		btnScrawGraphMove = (Button) findViewById(R.id.btnScrawGraphMove);
		btnScrawGrapRotato = (Button) findViewById(R.id.btnScrawGrapRotato);
		btnScrawGraphZoom = (Button) findViewById(R.id.btnScrawGraphZoom);
		btnScrawGraphPaste = (Button) findViewById(R.id.btnScrawGraphPaste);
		btnScrawGraphCancle = (Button) findViewById(R.id.btnScrawGraphCancle);
		btnScrawGraphDelete = (Button) findViewById(R.id.btnScrawGraphDelete);
		btnScrawGraphReset = (Button) findViewById(R.id.btnScrawGraphReset);
		btnScrawGraphCut = (Button) findViewById(R.id.btnScrawGraphCut);
		seekbarPenZoom = (SeekBar) findViewById(R.id.seekbarPenZoom);
		seekbarPenZoom
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					@Override
					public void onStopTrackingTouch(SeekBar arg0) {

					}

					@Override
					public void onStartTrackingTouch(SeekBar arg0) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onProgressChanged(SeekBar arg0, int arg1,
							boolean arg2) {
						if (mIsZoomCanvas) {
							mainDraw.CanvasZoom((arg1) * 0.01f);
						}

					}
				});
		setButtonType();
	}

	/**
	 * 设置字体风格
	 */
	public void setButtonType() {
		int sysVersion = Integer.parseInt(VERSION.SDK);
		if (sysVersion >= 9) {
			rbtnScrawMove.setTypeface(ty);
			rbtnScrawZoom.setTypeface(ty);
			rbtnScrawClear.setTypeface(ty);
			rbtnScrawSave.setTypeface(ty);
			rbtnScrawFill.setTypeface(ty);
			rbtnScrawSet.setTypeface(ty);
			rbtnScrawPen.setTypeface(ty);
			rbtnScrawPel.setTypeface(ty);
			rbtnScrawPelText.setTypeface(ty);
			rbtnScrawEraser.setTypeface(ty);
			btnScrawGraphMove.setTypeface(ty);
			btnScrawGrapRotato.setTypeface(ty);
			btnScrawGraphZoom.setTypeface(ty);
			btnScrawGraphPaste.setTypeface(ty);
			btnScrawGraphCancle.setTypeface(ty);
			btnScrawGraphDelete.setTypeface(ty);
			btnScrawGraphReset.setTypeface(ty);
			btnScrawGraphCut.setTypeface(ty);
		}

	}

	private void setOnClick() {
		imgScrawToolSwitch.setOnClickListener(this);
		imgScrawUndo.setOnClickListener(this);
		imgScrawRedo.setOnClickListener(this);
		rbtnScrawMove.setOnClickListener(this);
		rbtnScrawZoom.setOnClickListener(this);
		rbtnScrawClear.setOnClickListener(this);
		rbtnScrawSave.setOnClickListener(this);
		rbtnScrawFill.setOnClickListener(this);
		rbtnScrawSet.setOnClickListener(this);
		rbtnScrawPen.setOnClickListener(this);
		rbtnScrawPel.setOnClickListener(this);
		rbtnScrawPelText.setOnClickListener(this);
		rbtnScrawEraser.setOnClickListener(this);
		btnScrawGraphMove.setOnClickListener(this);
		btnScrawGrapRotato.setOnClickListener(this);
		btnScrawGraphZoom.setOnClickListener(this);
		btnScrawGraphPaste.setOnClickListener(this);
		btnScrawGraphCancle.setOnClickListener(this);
		btnScrawGraphDelete.setOnClickListener(this);
		btnScrawGraphReset.setOnClickListener(this);
		btnScrawGraphCut.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgScrawToolSwitch:
			if (!mtoolswitch) {
				mtoolswitch = true;
				ToolDown();
			} else {
				mtoolswitch = false;
				ToolUp();
			}
			break;
		case R.id.rbtnScrawMove:
			mtoolswitch = false;
			ToolUp();
			if (mIsMoveCanvas) {
				ToastUtil.ShortMake(this, "停止无极滚动");
				mIsMoveCanvas = false;
			} else {
				ToastUtil.ShortMake(this, "开始无极滚动，再次点击关闭滚动");
				mainDraw.initGraph();
				mIsMoveCanvas = true;
			}
			if (mainDraw.isIsZoomCanvas()) {
				mIsZoomCanvas = false;
				mainDraw.setIsZoomCanvas(mIsZoomCanvas);
				seekbarPenZoom.setVisibility(View.GONE);
				mainDraw.restore();
			}
			mainDraw.setIsMoveCanvas(mIsMoveCanvas);
			hideGraphTool();
			mGraphTool = false;
			break;
		case R.id.rbtnScrawZoom:
			mtoolswitch = false;
			ToolUp();
			if (mIsZoomCanvas) {
				ToastUtil.ShortMake(this, "停止缩放浏览");
				seekbarPenZoom.setVisibility(View.GONE);
				mIsZoomCanvas = false;
				mainDraw.restore();
			} else {
				ToastUtil.ShortMake(this, "开始缩放浏览，再次点击关闭浏览");
				seekbarPenZoom.setProgress(100);
				seekbarPenZoom.setVisibility(View.VISIBLE);
				mainDraw.initGraph();
				mIsZoomCanvas = true;
			}
			if (mainDraw.isIsMoveCanvas()) {
				mIsMoveCanvas = false;
				mainDraw.setIsMoveCanvas(false);
			}
			mainDraw.setIsZoomCanvas(mIsZoomCanvas);
			hideGraphTool();
			mGraphTool = false;
			break;
		case R.id.rbtnScrawClear:
			mtoolswitch = false;
			ToolUp();
			mainDraw.clearCanvas();
			break;
		case R.id.rbtnScrawSave:
			try {
				ToastUtil.ShortMake(
						this,
						"保存成功"
								+ SaveToFile.saveToFile(mainDraw
										.getCachebBitmap()));
			} catch (IOException e) {
				ToastUtil.ShortMake(this, "保存失败！\n" + e);
			}
			break;
		case R.id.rbtnScrawFill:
			mainDraw.graphfill();
			break;
		case R.id.rbtnScrawSet:
			mainDraw.initGraph();
			Intent i = new Intent(this, PaintStyleActivity.class);
			startActivityForResult(i, 1);
			break;
		case R.id.rbtnScrawPen:
			mtoolswitch = false;
			ToolUp();
			mainDraw.setDrawstate(0);
			break;
		case R.id.rbtnScrawPel:
			mtoolswitch = false;
			ToolUp();
			ShowGraphDialog();
			break;
		case R.id.rbtnScrawPelText:
			mtoolswitch = false;
			ToolUp();
			mainDraw.setDrawstate(7);
			break;
		case R.id.rbtnScrawEraser:
			mtoolswitch = false;
			ToolUp();
			mainDraw.setDrawstate(-1);
			break;
		case R.id.imgScrawUndo:
			mainDraw.undo();
			break;
		case R.id.imgScrawRedo:
			mainDraw.redo();
			break;
		case R.id.btnScrawGraphReset:
			mainDraw.setDrawstate(0);
			break;
		case R.id.btnScrawGraphMove:
			mainDraw.move();
			break;
		case R.id.btnScrawGrapRotato:
			mainDraw.rotate();
			break;
		case R.id.btnScrawGraphZoom:
			mainDraw.zoom();
			break;
		case R.id.btnScrawGraphPaste:
			mainDraw.paste();
			break;
		case R.id.btnScrawGraphDelete:
			mainDraw.graphdelete();
			break;
		case R.id.btnScrawGraphCut:
			mainDraw.setDrawstate(8);
			break;
		case R.id.btnScrawGraphCancle:
			hideGraphTool();
			mGraphTool = false;
			break;
		}

	}

	/**
	 * 隐藏工具栏
	 */
	public void ToolUp() {
		Animation anup = AnimationUtils.loadAnimation(this,
				R.anim.scrawl_tool_up);
		linScrawTool.startAnimation(anup);
		anup.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				linScrawTool.setVisibility(View.GONE);
				imgScrawUndo.setVisibility(View.GONE);
				imgScrawRedo.setVisibility(View.GONE);
			}
		});
	}

	/**
	 * 显示工具栏
	 */
	public void ToolDown() {
		Animation andown = AnimationUtils.loadAnimation(this,
				R.anim.scrawl_tool_down);
		imgScrawUndo.setVisibility(View.VISIBLE);
		imgScrawRedo.setVisibility(View.VISIBLE);
		linScrawTool.setVisibility(View.VISIBLE);
		linScrawTool.setAnimation(andown);
	}

	public void showGraphTool() {
		linScrawGraph.setVisibility(View.VISIBLE);
	}

	public void hideGraphTool() {
		linScrawGraph.setVisibility(View.GONE);
	}

	private int ri = 0;

	/**
	 * 选择图形
	 */
	public void ShowGraphDialog() {
		new AlertDialog.Builder(this)
				.setTitle("选择图形")
				.setItems(
						new String[] { "涂鸦", "直线", "折线", "3次贝塞尔曲线", "矩形",
								"多边形", "椭圆" },
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								ri = arg1;
								mainDraw.setDrawstate(ri);
							}
						})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						ToolDown();
						mtoolswitch = true;
					}
				}).create().show();
	}

	public void ShowTextDialog() {
		textview = LayoutInflater.from(this)
				.inflate(R.layout.text_dialog, null);
		textDialog = (EditText) textview.findViewById(R.id.textDialog);
		btnDialog = (Button) textview.findViewById(R.id.btnDialog);
		btnDialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				textDialog.setText("");
			}
		});
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		textDialog.setText(str);
		new AlertDialog.Builder(this).setTitle("输入文字").setView(textview)
				.setNeutralButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						if (!TextUtils.isEmpty(textDialog.getText().toString())) {
							mainDraw.drawtext(textDialog.getText().toString());
						} else {
							ToastUtil.ShortMake(ScrawlActivity.this, "请输入文字");
						}
					}
				}).setNegativeButton("取消", null).create().show();
	}

	/**
	 * 返回键监听
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this)
					.setTitle("系统提示")
					.setItems(new String[] { "取消", "返回上层页面", "保存后退出", "退出程序" },
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									if (arg1 == 0) {
										return;
									} else if (arg1 == 1) {
										finish();
										return;
									} else if (arg1 == 2) {
										try {
											ToastUtil.ShortMake(
													ScrawlActivity.this,
													"保存成功"
															+ SaveToFile
																	.saveToFile(mainDraw
																			.getCachebBitmap()));
										} catch (IOException e) {
											ToastUtil.ShortMake(
													ScrawlActivity.this,
													"保存失败！\n" + e);
										}
									}
									Intent intent = new Intent(
											Intent.ACTION_MAIN);
									intent.addCategory(Intent.CATEGORY_HOME);
									intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(intent);
									finish();
								}
							}).create().show();

		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		try {
			mainDraw.setIsmavegraph();
			mainDraw.initSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
