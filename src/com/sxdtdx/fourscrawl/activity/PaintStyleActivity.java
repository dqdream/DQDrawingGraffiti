package com.sxdtdx.fourscrawl.activity;

import com.sxdtdx.fourscrawl.R;
import com.sxdtdx.fourscrawl.tool.SharePrefenceUtil;
import com.sxdtdx.fourscrawl.view.ColorPickerDialog;
import com.sxdtdx.fourscrawl.view.DrawLineShow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * 设置  画笔 文本 页面
 * @author dq
 *
 */
public class PaintStyleActivity extends Activity implements OnClickListener,
		OnSeekBarChangeListener {
	private Button btnComplete, btnpaintstyle, btnUndoCacheNum,btntextstyle;
	private TextView textPaintColor, textBGColor, textPenWidth,textPenText;
	private RadioGroup radioPaintGroup;
	private EditText editUndoCacheNum;
	private ColorPickerDialog dialog;
	private SeekBar seekbarPenWidth,seekbarPenText;
	private Intent i;
	private DrawLineShow drawLineShow;
	String style[];
	String style2[];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		drawLineShow = new DrawLineShow(this);
		setContentView(R.layout.set_paint);
		findID();
		setOnClick();
		initData();
	}

	private void findID() {
		textPaintColor = (TextView) findViewById(R.id.textPaintColor);
		textBGColor = (TextView) findViewById(R.id.textBGColor);
		btnComplete = (Button) findViewById(R.id.btnComplete);
		seekbarPenWidth = (SeekBar) findViewById(R.id.seekbarPenWidth);
		textPenWidth = (TextView) findViewById(R.id.textPenWidth);
		drawLineShow = (DrawLineShow) findViewById(R.id.lineShow);
		btnpaintstyle = (Button) findViewById(R.id.btnpaintstyle);
		radioPaintGroup = (RadioGroup) findViewById(R.id.radioPaintGroup);
		btnUndoCacheNum = (Button) findViewById(R.id.btnUndoCacheNum);
		editUndoCacheNum = (EditText) findViewById(R.id.editUndoCacheNum);
		textPenText=(TextView) findViewById(R.id.textPenText);
		seekbarPenText=(SeekBar) findViewById(R.id.seekbarPenText);
		btntextstyle=(Button) findViewById(R.id.btntextstyle);
	}

	private void setOnClick() {
		textPaintColor.setOnClickListener(this);
		textBGColor.setOnClickListener(this);
		btnComplete.setOnClickListener(this);
		seekbarPenWidth.setProgress(SharePrefenceUtil.getPenWidth(this));
		seekbarPenWidth.setOnSeekBarChangeListener(this);
		seekbarPenText.setProgress(SharePrefenceUtil.getPenText(this));
		seekbarPenText.setOnSeekBarChangeListener(this);
		btnpaintstyle.setOnClickListener(this);
		btnUndoCacheNum.setOnClickListener(this);
		btntextstyle.setOnClickListener(this);
		if (SharePrefenceUtil.getPenPoint(this) == 0) {
			radioPaintGroup.check(R.id.rbtnPaintCircle);
		} else {
			radioPaintGroup.check(R.id.rbtnPaintSquare);
		}
		radioPaintGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						switch (arg1) {
						case R.id.rbtnPaintCircle:
							SharePrefenceUtil.savePenPoint(
									PaintStyleActivity.this, 0);
							drawLineShow.onPaintChanged();
							break;
						case R.id.rbtnPaintSquare:
							SharePrefenceUtil.savePenPoint(
									PaintStyleActivity.this, 1);
							drawLineShow.onPaintChanged();
							break;
						}
					}
				});
	}
    /**
     * 初始化数据
     */
	private void initData() {
		i = new Intent();
		style = new String[] {"默认画笔", "铅笔模糊", "毛笔浮雕", "透明水彩" };
		style2 = new String[] {"默认字体","涂鸦字体1(不影响汉字)", "涂鸦字体2(不影响汉字)","涂鸦字体3(2.3以上系统支持)"};
		textPenWidth.setText("画笔宽度:"
				+ SharePrefenceUtil.getPenWidth(PaintStyleActivity.this));
		textPenText.setText("文本大小:"
				+ SharePrefenceUtil.getPenText(PaintStyleActivity.this));
		textPaintColor.setBackgroundColor(SharePrefenceUtil
				.getPaintColor(PaintStyleActivity.this));
		textBGColor.setBackgroundColor(SharePrefenceUtil
				.getBackgroundColor(PaintStyleActivity.this));
		btnpaintstyle.setText(style[SharePrefenceUtil
				.getPenStyle(PaintStyleActivity.this)]);
		btntextstyle.setText(style2[SharePrefenceUtil.getTextStyle(this)]);
		btnUndoCacheNum.setText(SharePrefenceUtil.getCacheNum(this)-1+"步");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textPaintColor:
			dialog = new ColorPickerDialog(this, textBGColor.getTextColors()
					.getDefaultColor(), "字体颜色",
					new ColorPickerDialog.OnColorChangedListener() {
						@Override
						public void colorChanged(int color) {
							SharePrefenceUtil.savePaintColor(
									PaintStyleActivity.this, color);
							textPaintColor.setBackgroundColor(SharePrefenceUtil
									.getPaintColor(PaintStyleActivity.this));
							drawLineShow.onPaintChanged();
						}
					});
			dialog.show();
			break;
		case R.id.textBGColor:
			dialog = new ColorPickerDialog(this, textBGColor.getTextColors()
					.getDefaultColor(), "背景颜色",
					new ColorPickerDialog.OnColorChangedListener() {
						@Override
						public void colorChanged(int color) {
							SharePrefenceUtil.saveBackgroundColor(
									PaintStyleActivity.this, color);
							textBGColor.setBackgroundColor(SharePrefenceUtil
									.getBackgroundColor(PaintStyleActivity.this));
							drawLineShow.onPaintChanged();
						}
					});
			dialog.show();
			break;
		case R.id.btnComplete:
			// 准备数据的方法
			setResult(1, i);
			finish();
			break;
		case R.id.btnpaintstyle:
			ShowGraphDialog(style,R.id.btnpaintstyle);
			break;
		case R.id.btnUndoCacheNum:
			if (!btnUndoCacheNum.getText().equals("确定")) {
				btnUndoCacheNum.setText("确定");
				editUndoCacheNum.setVisibility(View.VISIBLE);
			} else {
				if (!TextUtils.isEmpty(editUndoCacheNum.getText().toString())) {
					editUndoCacheNum.setVisibility(View.GONE);
					btnUndoCacheNum.setText(editUndoCacheNum.getText()
							.toString() + "步");
					SharePrefenceUtil.saveCacheNum(PaintStyleActivity.this,
							Integer.parseInt(editUndoCacheNum.getText()
									.toString())+1);
				}
			}
			break;
		case R.id.btntextstyle:
			ShowGraphDialog(style2,R.id.btntextstyle);
			break;
		}
	}
    /**
     * seekbar 监听
     */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		switch (seekBar.getId()) {
		case R.id.seekbarPenWidth:
			textPenWidth.setText("画笔宽度:" + progress);
			SharePrefenceUtil.savePenWidth(this, progress);
			drawLineShow.onPaintChanged();
			break;
		case R.id.seekbarPenText:
			textPenText.setText("文本大小:"+progress);
			SharePrefenceUtil.savePenText(this, progress);
			drawLineShow.onPaintChanged();
			break;
		}
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}
    /**
     * 图形选择 dialog
     * @param s
     * @param id
     */
	public void ShowGraphDialog(final String [] s,final int id) {
		new AlertDialog.Builder(this)
				.setTitle("选择风格")
				.setItems(s, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						switch (id) {
						case R.id.btnpaintstyle:
							SharePrefenceUtil.savePenStyle(PaintStyleActivity.this,
									arg1);
							btnpaintstyle.setText(s[arg1]);
							break;
						case R.id.btntextstyle:
							SharePrefenceUtil.saveTextStyle(PaintStyleActivity.this, arg1);
							btntextstyle.setText(s[arg1]);
							break;
						}
						drawLineShow.onPaintChanged();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {

					}
				}).create().show();
	}

}
