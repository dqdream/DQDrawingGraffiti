package com.sxdtdx.fourscrawl.tool;

import android.content.Context;
import android.widget.Toast;
/**
 * 自定义Toast 类
 * @author dq
 *
 */
public class ToastUtil {
   public static void ShortMake(Context context,String text)
   {
	   Toast.makeText(context, text, 0).show();
   }
   
   public static void LongMake(Context context,String text)
   {
	   Toast.makeText(context, text, 1).show();
   }
}
