package com.meitu.activities;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.meitu.fragments.SelectFrame;
import com.meitu.fragments.SelectFrame.OnFrameItemOnClickListener;
import com.meitu.fragments.SelectSticker;
import com.meitu.fragments.SelectSticker.OnStickerItemClickListener;
import com.meitu.fragments.WidgetBtn;
import com.meitu.fragments.WidgetBtn.OnWidgetClickListener;
import com.meitu.greendaohelper.DbTool;
import com.meitu.util.SaveTool;
import com.meitu.util.SaxTool;
import com.meitu.view.BaseLayerView;
import com.meitu.view.StickerView;


public class SinglePictureDeal extends FragmentActivity {

  /** 最终需要保存的View页面*/
  private RelativeLayout rLViewToSave;
  /** 饰品选择的内容页*/
  private LinearLayout lLOrnaments;
  /** 边框选择的内容页*/
  private LinearLayout lLFrame;
  /** 生成并保存图片按钮*/
  private ImageButton imgBtnSavePicture;
  /** 生成并保存图片按钮*/
  private ImageButton imgBtnHome;
  /** 边框选择页*/
  private SelectFrame selectFrameFrag;
  /** 贴纸选择页*/
  private SelectSticker selectStickerFrag;
  /** 小工具栏*/
  private WidgetBtn widgetControl;
  /** 数据库工具*/
  private DbTool dbTool;
  /** 自定义图片处理控制控件*/
  private BaseLayerView baseLayer;
  
  private FragmentManager fm;
  private OnClickListener toolBtnListener;
  public ArrayList<String> nameGaoXiao = new ArrayList<String>();
  public ArrayList<String> nameGaoXiaoThumbnail = new ArrayList<String>();
  public ArrayList<String> nameFrame = new ArrayList<String>();
  public ArrayList<String> nameFrameThumbnail = new ArrayList<String>();
  public ArrayList<String> nameZheDang = new ArrayList<String>();
  public ArrayList<String> nameZheDangThumbnail = new ArrayList<String>();
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.operate_picture);
    dbTool = new DbTool(getApplicationContext()); 
    dbTool.NewDbInstance();//数据库存储文件名
    saxToXML("materials.xml","material");
    fm = getSupportFragmentManager();
    initView();
    initWidgetFragment();
    initFrameFragment();
    initStickerFragment();
    initImgBtnListener();
    setAllListener();
    Intent intent = getIntent();
    Bundle bundle = intent.getExtras();
    Uri uri = (Uri) bundle.get("uri");
    baseLayer.setBackgroundDisplay(uri);
  }
  
  private void initView() {
    rLViewToSave = (RelativeLayout) this.findViewById(R.id.relativelayout_save_picture);
    lLOrnaments = (LinearLayout)this.findViewById(R.id.linearlayout_btn_ornaments_add);
    lLFrame = (LinearLayout)this.findViewById(R.id.linearlayout_btn_frame_add);
    imgBtnSavePicture = (ImageButton) this.findViewById(R.id.btn_save);
    imgBtnHome = (ImageButton) this.findViewById(R.id.btn_gohome);
    baseLayer = (BaseLayerView)this.findViewById(R.id.base_layer);
  }
  
  /**
   * 初始化贴纸选择的Fragment页面
   */
  private void initStickerFragment(){
    selectStickerFrag = SelectSticker.getInstance(nameGaoXiaoThumbnail,nameZheDangThumbnail);
    fm.beginTransaction().add(R.id.fragment_sticker_container, selectStickerFrag).commit();
    selectStickerFrag.setOnStickerItemClickListener(new OnStickerItemClickListener() {
      @Override
      public void onStickerItemClick(int pagerNumber, int position) {
        if (baseLayer.isStickerNumOver()) {
          Toast.makeText(SinglePictureDeal.this, "饰品数超过15个啦，请删除部分后再添加", Toast.LENGTH_LONG).show();
        } else {
          switch (pagerNumber) {
          case 0:baseLayer.addSticker(nameGaoXiao.get(position));break;
          case 1:baseLayer.addSticker(nameZheDang.get(position));break;
          default:
            break;
          }
        }
      }
    });
  }
  
  /**
   * 初始化边框选择的Fragment页面
   */
  private void initFrameFragment() {
    selectFrameFrag = SelectFrame.getInstance(nameFrameThumbnail);
    fm.beginTransaction().add(R.id.fragment_frame_container, selectFrameFrag).commit();
    selectFrameFrag.setOnFrameItemOnClickListener(new OnFrameItemOnClickListener() {
      @Override
      public void OnFrameItemOnClick(View view, int position) {
        String frameName = nameFrame.get(position);
        baseLayer.setFrame(frameName);
      }
    });
  }

  /**
   * 小工具栏初始化
   */
  private void initWidgetFragment() {
    widgetControl = (WidgetBtn) fm.findFragmentById(R.id.fragment_animation);
    widgetControl.setOnWidgetClickListener(new OnWidgetClickListener() {
      @Override
      public void onWidgetClick(int ID) {
          switch (ID) {
          case R.id.img_widget_totop:baseLayer.toTopSticker();break;
          case R.id.img_widget_tobottom:baseLayer.toBottomSticker();break;
          case R.id.img_widget_delete:baseLayer.removeSticker();break;
          }
      }
    });
  }

  /**
   * 读取assets里面的Xml文档获得文件名
   * @param assetName XML文件名
   * @param elementName 元素名
   */
  private void saxToXML(String assetName,String elementName) {
    AssetManager am = getResources().getAssets();
    InputStream inputstream = null;
    try {
      inputstream = am.open(assetName);
      List<HashMap<String, String>> list = SaxTool.readXML(inputstream, elementName);
      putInDB(list);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 遍历List里面的map元素，在这可以分类添加到数据库中。
   * @param list
   */
  private void putInDB(List<HashMap<String, String>> list) {
    for (HashMap<String, String> map : list) {
      if (map.get("id").equals("biankuang")) {
        nameFrameThumbnail.add(map.get("thumbnailname"));
        nameFrame.add(map.get("name"));
//        dbTool.addBianKuangEntity(map.get("thumbnailname"),map.get("name"));
      }else if (map.get("id").equals("gaoxiaobiaoqing")) {
        nameGaoXiaoThumbnail.add(map.get("thumbnailname"));
        nameGaoXiao.add(map.get("name"));
//        dbTool.addGaoXiaoEntity(map.get("thumbnailname"),map.get("name"));
      }else if (map.get("id").equals("zhedang")) {
        nameZheDangThumbnail.add(map.get("thumbnailname"));
        nameZheDang.add(map.get("name"));
//        dbTool.addZheDangEntity(map.get("thumbnailname"),map.get("name"));
      }
    }
  }

  /**
   * 将所有监听器设置给对应的View对象
   */
  private void setAllListener() {
    lLOrnaments.setOnClickListener(toolBtnListener);
    lLFrame.setOnClickListener(toolBtnListener);
    imgBtnHome.setOnClickListener(toolBtnListener);
    imgBtnSavePicture.setOnClickListener(toolBtnListener);
  }
  
  @Override
  protected void onSaveInstanceState(Bundle outState) {
    baseLayer.saveData(outState);
    super.onSaveInstanceState(outState);
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    baseLayer.restoreData(savedInstanceState);
    super.onRestoreInstanceState(savedInstanceState);
  }

  /**
   * 界面功能按钮监听初始化
   */
  private void initImgBtnListener() {
    toolBtnListener = new OnClickListener() {
      @Override
      public void onClick(View v) {
        switch (v.getId()) {
        case R.id.linearlayout_btn_ornaments_add:
          fm.beginTransaction().show(selectStickerFrag).setTransition(FragmentTransaction.TRANSIT_ENTER_MASK).commit();
          break;
        case R.id.linearlayout_btn_frame_add:
          fm.beginTransaction().show(selectFrameFrag).setTransition(FragmentTransaction.TRANSIT_ENTER_MASK).commit();
          break;
        case R.id.btn_gohome:
            turnBack();
          break;
        case R.id.btn_save:
          StickerView.PreSticker.RemoveBoder();
          rLViewToSave.setDrawingCacheEnabled(true);
          Bitmap bitmap = rLViewToSave.getDrawingCache();
          try {
            SaveTool.saveMyBitmap(bitmap);
            Toast.makeText(SinglePictureDeal.this, "成功保存！",Toast.LENGTH_SHORT).show();
          } catch (IOException e) {
            Toast.makeText(SinglePictureDeal.this, "诶，保存失败。",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
          }
          break;
        default:
          break;
        }
      }
    };
  }

  /**
   * 饰品数量不为0的时候弹出确认退出框
   */
  private void showFinishDialog() {
    AlertDialog.Builder builder = new Builder(SinglePictureDeal.this);
    builder.setMessage("确定放弃编辑退出？");
    builder.setTitle("提示");
    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        finish();
      }
    });
    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });
    builder.create().show();
  }
  
  /**
   * 返回时触发
   */
  private void turnBack(){
    if(!baseLayer.isEmpty()){
      showFinishDialog();
    }else {
      finish();
    }
  }
  
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      if (!selectStickerFrag.isHidden()) {
        fm.beginTransaction().hide(selectStickerFrag).commit();
      }else if (!selectFrameFrag.isHidden()) {
        fm.beginTransaction().hide(selectFrameFrag).commit();
      }else {
        turnBack();
      }
    }
    return false;
  }
}
