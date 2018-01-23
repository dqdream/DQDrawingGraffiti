package com.sxdtdx.fourscrawl.tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
/**
 * SharePrefence π§æﬂ¿‡
 * @author dq
 *
 */
public class SharePrefenceUtil {
	public static void saveCanvasWidth(Context context, float width) {
		SharedPreferences sp = context.getSharedPreferences("width",
				context.MODE_PRIVATE);
		sp.edit().putFloat("width", width).commit();
	}

	public static Float getCanvasWidth(Context context) {
		SharedPreferences sp = context.getSharedPreferences("width",
				context.MODE_PRIVATE);
		return sp.getFloat("width", 0);
	}
	
	public static void saveCanvasHight(Context context, float hight) {
		SharedPreferences sp = context.getSharedPreferences("hight",
				context.MODE_PRIVATE);
		sp.edit().putFloat("hight", hight).commit();
	}

	public static Float getCanvasHight(Context context) {
		SharedPreferences sp = context.getSharedPreferences("hight",
				context.MODE_PRIVATE);
		return sp.getFloat("hight", 0);
	}
	
	public static void saveSDPath(Context context, String path) {
		SharedPreferences sp = context.getSharedPreferences("path",
				context.MODE_PRIVATE);
		sp.edit().putString("path", path).commit();
	}
	
	public static String getSDPath(Context context) {
		SharedPreferences sp = context.getSharedPreferences("path",
				context.MODE_PRIVATE);
		return sp.getString("path","");
	}
	public static void saveImageIsSize(Context context, int b) {
		SharedPreferences sp = context.getSharedPreferences("b",
				context.MODE_PRIVATE);
		sp.edit().putInt("b", b).commit();
	}
	
	public static int getImageIsSize(Context context) {
		SharedPreferences sp = context.getSharedPreferences("b",
				context.MODE_PRIVATE);
		return sp.getInt("b",0);
	}
	public static void savePaintColor(Context context,int paintColor){
		SharedPreferences sp=context.getSharedPreferences("paintColor", context.MODE_PRIVATE);
		Editor editor=sp.edit();
		editor.putInt("paintColor", paintColor);
		editor.commit();
	}
	public static Integer getPaintColor(Context context){
		SharedPreferences sp=context.getSharedPreferences("paintColor", context.MODE_PRIVATE);
		return sp.getInt("paintColor", Color.BLUE);
	}
	public static void saveBackgroundColor(Context context,int backgroundColor){
		SharedPreferences sp=context.getSharedPreferences("backgroundColor", context.MODE_PRIVATE);
		Editor editor=sp.edit();
		editor.putInt("backgroundColor", backgroundColor);
		editor.commit();
	}
	public static Integer getBackgroundColor(Context context){
		SharedPreferences sp=context.getSharedPreferences("backgroundColor", context.MODE_PRIVATE);
		return sp.getInt("backgroundColor", Color.WHITE);
	}
	
	public static void savePenWidth(Context context,int width){
		SharedPreferences sp=context.getSharedPreferences("PenWidth", context.MODE_PRIVATE);
		Editor editor=sp.edit();
		editor.putInt("PenWidth", width);
		editor.commit();
	}
	
	public static int getPenWidth(Context context){
		SharedPreferences sp=context.getSharedPreferences("PenWidth", context.MODE_PRIVATE);
		return sp.getInt("PenWidth", 5);
	}
	public static void savePenStyle(Context context,int style){
		SharedPreferences sp=context.getSharedPreferences("PenStyle", context.MODE_PRIVATE);
		Editor editor=sp.edit();
		editor.putInt("PenStyle", style);
		editor.commit();
	}
	
	public static int getPenStyle(Context context){
		SharedPreferences sp=context.getSharedPreferences("PenStyle", context.MODE_PRIVATE);
		return sp.getInt("PenStyle", 0);
	}
	
	public static void saveTextStyle(Context context,int TextStyle){
		SharedPreferences sp=context.getSharedPreferences("TextStyle", context.MODE_PRIVATE);
		Editor editor=sp.edit();
		editor.putInt("TextStyle", TextStyle);
		editor.commit();
	}
	
	public static int getTextStyle(Context context){
		SharedPreferences sp=context.getSharedPreferences("TextStyle", context.MODE_PRIVATE);
		return sp.getInt("TextStyle", 0);
	}
	
	
	public static void savePenPoint(Context context,int penpoint){
		SharedPreferences sp=context.getSharedPreferences("penpoint", context.MODE_PRIVATE);
		Editor editor=sp.edit();
		editor.putInt("penpoint", penpoint);
		editor.commit();
	}
	
	public static int getPenPoint(Context context){
		SharedPreferences sp=context.getSharedPreferences("penpoint", context.MODE_PRIVATE);
		return sp.getInt("penpoint", 5);
	}
	
	public static void saveCacheNum(Context context,int CacheNum){
		SharedPreferences sp=context.getSharedPreferences("CacheNum", context.MODE_PRIVATE);
		Editor editor=sp.edit();
		editor.putInt("CacheNum", CacheNum);
		editor.commit();
	}
	
	public static int getCacheNum(Context context){
		SharedPreferences sp=context.getSharedPreferences("CacheNum", context.MODE_PRIVATE);
		return sp.getInt("CacheNum", 21);
	}
	
	public static void savePenText(Context context,int PenText){
		SharedPreferences sp=context.getSharedPreferences("PenText", context.MODE_PRIVATE);
		Editor editor=sp.edit();
		editor.putInt("PenText", PenText);
		editor.commit();
	}
	
	public static int getPenText(Context context){
		SharedPreferences sp=context.getSharedPreferences("PenText", context.MODE_PRIVATE);
		return sp.getInt("PenText", 20);
	}
}
