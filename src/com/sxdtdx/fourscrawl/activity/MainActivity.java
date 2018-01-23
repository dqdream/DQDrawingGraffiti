package com.sxdtdx.fourscrawl.activity;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.sxdtdx.fourscrawl.R;
import com.sxdtdx.fourscrawl.tool.SharePrefenceUtil;
import com.sxdtdx.fourscrawl.view.MainDraw;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 起始页面
 * 
 * @author dq
 * 
 */
public class MainActivity extends Activity implements OnClickListener {
	Button btnMainNew, btnMainLoad, btnMainYes, btnMainNo;
	RelativeLayout relMainDrawSize;
	EditText editMainWidth, editMainHight;
	MainDraw mainDraw;
	ImageView imageView;
	int screenWidth;
	int screenHeight;
	Uri mImgUri = null;
	private Typeface ty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		screenHeight = getWindowManager().getDefaultDisplay().getHeight();
		ty = Typeface.createFromAsset(this.getAssets(), "fonts/a3.ttf");
		findID();
		setOnClick();
		init();
	}

	private void init() {
		editMainWidth.setText(screenWidth + "");
		editMainHight.setText(screenHeight + "");
		SharePrefenceUtil.saveSDPath(this, "");
		SharePrefenceUtil.saveCanvasWidth(this,
				Float.parseFloat(editMainWidth.getText().toString()));
		SharePrefenceUtil.saveCanvasHight(this,
				Float.parseFloat(editMainHight.getText().toString()));
	}

	private void findID() {
		btnMainNew = (Button) findViewById(R.id.btnMainNew);
		btnMainLoad = (Button) findViewById(R.id.btnMainLoad);
		btnMainYes = (Button) findViewById(R.id.btnMainYes);
		btnMainNo = (Button) findViewById(R.id.btnMainNo);
		relMainDrawSize = (RelativeLayout) findViewById(R.id.relMainDrawSize);
		editMainWidth = (EditText) findViewById(R.id.editMainWidth);
		editMainHight = (EditText) findViewById(R.id.editMainHight);
		setButtonType();
	}

	/**
	 * 设置字体风格
	 */
	public void setButtonType() {
		int sysVersion = Integer.parseInt(VERSION.SDK);
		if (sysVersion >= 9) {
			btnMainNew.setTypeface(ty);
			btnMainLoad.setTypeface(ty);
			btnMainYes.setTypeface(ty);
			btnMainNo.setTypeface(ty);
			editMainWidth.setTypeface(ty);
			editMainHight.setTypeface(ty);
		}
	}

	private void setOnClick() {
		btnMainNew.setOnClickListener(this);
		btnMainLoad.setOnClickListener(this);
		btnMainYes.setOnClickListener(this);
		btnMainNo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnMainNew:
			Animation anvis = AnimationUtils.loadAnimation(this,
					R.anim.mainl_drawsize_vis);
			relMainDrawSize.setVisibility(View.VISIBLE);
			relMainDrawSize.setAnimation(anvis);
			break;
		case R.id.btnMainLoad:
			ShowGraphDialog();
			break;
		case R.id.btnMainYes:
			SharePrefenceUtil.saveSDPath(this, "");
			SharePrefenceUtil.saveCanvasWidth(this,
					Float.parseFloat(editMainWidth.getText().toString()));
			SharePrefenceUtil.saveCanvasHight(this,
					Float.parseFloat(editMainHight.getText().toString()));
			Intent i = new Intent(this, ScrawlActivity.class);
			startActivity(i);
			break;
		case R.id.btnMainNo:
			Animation angone = AnimationUtils.loadAnimation(this,
					R.anim.mainl_drawsize_gone);
			relMainDrawSize.setAnimation(angone);
			relMainDrawSize.setVisibility(View.GONE);
			break;
		}

	}

	/**
	 * 载入选择 dialog
	 */
	public void ShowGraphDialog() {
		new AlertDialog.Builder(this)
				.setTitle("选择来源")
				.setItems(new String[] { "本地", "拍照" },
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								switch (arg1) {
								case 0:
									Intent intent = new Intent(
											Intent.ACTION_GET_CONTENT); // 自带的浏览文件Activity
									intent.addCategory(Intent.CATEGORY_OPENABLE);
									intent.setType("image/*"); // 这是到达该image路径下，的所有文件，默认为内存卡的
									startActivityForResult(Intent
											.createChooser(intent, "选择图片"), 0);
									break;
								case 1:
									Intent intCamera = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);
									ContentValues values = new ContentValues();
									SimpleDateFormat format = new SimpleDateFormat(
											"yyyyMMddHHmmss");
									String imgName = format.format(new Date());
									values.put("_display_name", imgName);
									values.put("title", imgName);
									values.put("description", imgName);
									values.put("picasa_id", imgName);
									mImgUri = getContentResolver()
											.insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
													values);
									intCamera.putExtra(MediaStore.EXTRA_OUTPUT,
											mImgUri);
									startActivityForResult(intCamera, 1);
									break;
								}
							}
						}).setNegativeButton("取消", null).create().show();
	}

	/**
	 * 对返回的图片进行处理
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0:
			if (data == null) {
				return;
			}
			mImgUri = data.getData();
			getImage();
			break;
		case 1:
			getImage();
			break;
		}

	}

	/**
	 * 获取 本地 或 照相 图片 URL 并判断是否 过大
	 */
	private void getImage() {
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 10;
			Bitmap b = BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(mImgUri), null, options);
			if (b == null) {
				return;
			}
			int i = b.getWidth() * 10 / screenWidth;
			int j = b.getHeight() * 10 / screenHeight;
			if (i > 1 || j > 1) {
				if (i > j) {
					SharePrefenceUtil.saveImageIsSize(this, i);
				} else {
					SharePrefenceUtil.saveImageIsSize(this, j);
				}
			} else {
				SharePrefenceUtil.saveImageIsSize(this, 1);
			}
			if (!b.isRecycled()) {
				b.recycle();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		SharePrefenceUtil.saveSDPath(this, mImgUri.toString());
		Intent i = new Intent(this, ScrawlActivity.class);
		startActivity(i);
	}

}
