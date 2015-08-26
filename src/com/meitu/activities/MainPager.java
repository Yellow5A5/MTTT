package com.meitu.activities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

import com.meitu.util.ZipTool;

/**
 * MainPager
 * @author HuangWeiwu <br/>
 * @功能  仿美图贴贴单张贴图功能
 * @时间 2015.08.04
*/

public class MainPager extends FragmentActivity {

  public interface OnWidgetCallBack{
    public void onWidgetClick();
  }
  
  /** 单张图片按键*/
  private ImageButton imgBtnSinglePicture;
  private OnClickListener SingleBtnListener;
  
  /** 正确的返回码*/
  private int SUCCESS_CODE = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.single_picture_main);
    upMatrialZip();
    initListener();
    initView();
  }
    
  /**
   * 判断是否已经解压过资源文件，没有的话就将assets里面material.zip解压到SD卡的material目录中
   */
  private void upMatrialZip() {
    File f = new File(Environment.getExternalStorageDirectory().getPath()+"/material");
    if (!f.exists() && !f.isDirectory()) {
      f.mkdirs();
      new Thread(new Runnable() {
        @Override
        public void run() {
          AssetManager am = getResources().getAssets();
          try {
            InputStream inputStream = am.open("material.zip");
            ZipTool.outZip(inputStream, Environment.getExternalStorageDirectory().getPath()+"/material");
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }).start();
    }else {
      return;
    }

  }

  /**
   * 初始化控件
   */
  private void initView() {
    imgBtnSinglePicture = (ImageButton)this.findViewById(R.id.btn_single_picture);
    imgBtnSinglePicture.setOnClickListener(SingleBtnListener);
  }
    
  /**
   * 监听功能初始化
   */
  private void initListener() {
    SingleBtnListener = new OnClickListener() {
      @Override
      public void onClick(View v) {
        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Intent intent = new Intent(Intent.ACTION_PICK, uri);  
        startActivityForResult(intent, SUCCESS_CODE);
      }
    };
  }
    
  /**
   * 接受选择后返回的图片
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if ((requestCode == SUCCESS_CODE) && (resultCode == RESULT_OK)) {
      Uri uri = data.getData();
      Intent intent = new Intent(MainPager.this, SinglePictureDeal.class);
      intent.putExtra("uri", uri);
      startActivity(intent);
    }
    super.onActivityResult(requestCode, resultCode, data);
  }
  
}
