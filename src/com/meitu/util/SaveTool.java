package com.meitu.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

public class SaveTool {
  /**
   * 将Bitmap图片保存到本地
   * @param mBitmap 
   * @throws IOException
   */
  @SuppressLint("SimpleDateFormat") 
  public static void saveMyBitmap(Bitmap mBitmap) throws IOException {
    Date dt=new Date();
    DateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");//设置显示格式
    String nowTime= new String(df.format(dt));
    String path = Environment.getExternalStorageDirectory().getPath();
    Log.e("save To path right?", path);
    File f = new File(path + "/DCIM/"+ nowTime + "_XPT.jpg");
    f.createNewFile();
    FileOutputStream fOut = null;
    try {
      fOut = new FileOutputStream(f);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
    try {
      fOut.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      fOut.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
