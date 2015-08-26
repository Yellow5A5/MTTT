package com.meitu.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.meitu.activities.R;

public class BaseLayerView extends RelativeLayout {

  /** 饰品数为空*/
  private int STICKER_EMPTY = 0;
  /** 最底层*/
  private int THE_LOWEST_LAYER = 0;
  /** 没找到该饰品，即未选中饰品时（为null）的情况*/
  private int NO_FIND_LAYER = -1;
  /** 最多只能有15张贴纸*/
  private int MAX_COUNT_STICKER = 15;
  /**temp[0]为点击位置到功能按钮左边界的距离，temp[1]为点击位置到功能按钮上边界的距离*/
  private int[] temp = new int[] { 0, 0 };
  /** 存储饰品贴纸对象*/
  private ArrayList<StickerView> stickerLayer;
  /** 存储添加了的饰品的ID*/
  private ArrayList<String> stickerNameList;
  
  private View view;
  private Context mContext;
  /** 用户相册的图片*/
  private ImageView imgViewDisplay;
  /** 边界图片*/
  private ImageView ImgViewFrame;
  /** 存储当前的边框图的ID*/
  private String frameName;
  /** 删除按钮*/
  public ImageView imgViewStickerDelete;
  /** 旋转按钮*/
  public ImageView imgViewStickerRotate;
  /** 装贴纸的布局*/
  private RelativeLayout rLStickerBin;
  
  public BaseLayerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.mContext = context;
    view = LayoutInflater.from(context).inflate(R.layout.control_layer , this, true);
    stickerLayer = new ArrayList<StickerView>();
    stickerNameList = new ArrayList<String>();
    initView();
    initListener();
  }

  private void initListener() {
    imgViewStickerDelete.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        removeSticker();
      }
    });
    imgViewStickerRotate.setOnTouchListener(new OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        int eventaction = event.getAction();
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (eventaction) {
        case MotionEvent.ACTION_DOWN:
          StickerView.PreSticker.setMobilizable(false);
          temp[0] = (int) (event.getX());
          temp[1] = (int) (event.getY());
          break;
        case MotionEvent.ACTION_MOVE:
          v.setX(x - temp[0]);
          v.setY(y - temp[1] - imgViewStickerDelete.getHeight());//减多一个高度
          imgViewStickerDelete.setTranslationX(2 * StickerView.PreSticker.getCenterX() - (x - temp[0] + v.getWidth()));
          imgViewStickerDelete.setTranslationY(2 * StickerView.PreSticker.getCenterY() - (y - temp[1]));
          StickerView.PreSticker.resetScale(v.getX() + v.getWidth()/2, v.getY() + v.getHeight()/2);
          StickerView.PreSticker.resetRotation(v.getX() + v.getWidth()/2, v.getY() + v.getHeight()/2);
          StickerView.PreSticker.saveImgBtnPoint(v.getX() + v.getWidth()/2, v.getY() + v.getHeight()/2);
          break;
        case MotionEvent.ACTION_UP:
          StickerView.PreSticker.saveImgBtnPoint(v.getX() + v.getWidth()/2, v.getY() + v.getHeight()/2);
          StickerView.PreSticker.setMobilizable(true);
          v.performClick();
          break;
        }
        return true;
      }
    });
  }

  private void initView() {
    imgViewStickerDelete = (ImageView)view.findViewById(R.id.img_deleteb);
    imgViewStickerRotate = (ImageView)view.findViewById(R.id.img_rotateb);
    imgViewDisplay = (ImageView) this.findViewById(R.id.img_display);
    ImgViewFrame = (ImageView) this.findViewById(R.id.img_frame);
    rLStickerBin = (RelativeLayout) view.findViewById(R.id.Trelativelayout_sticker_bin);
    imgViewStickerDelete.setScaleType(ScaleType.MATRIX);
    imgViewStickerRotate.setScaleType(ScaleType.MATRIX);
  }

  /**
   * 去除贴纸
   */
  public void removeSticker() {
    if (null != StickerView.PreSticker) {
      stickerNameList.remove(stickerLayer.indexOf(StickerView.PreSticker));
      rLStickerBin.removeViewInLayout(StickerView.PreSticker);
      stickerLayer.remove(StickerView.PreSticker);
      StickerView.PreSticker = null;
      imgViewStickerDelete.setVisibility(View.GONE);
      imgViewStickerRotate.setVisibility(View.GONE);
    }
  }
  
  /**
   * 添加饰品
   * @param id  饰品对应的文件名
   */
  public void addSticker(String name) {
    stickerNameList.add(name);
    Bitmap OrnamentsPicture = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath()+"/material/"+name);
    StickerView NewPicture = new StickerView(mContext,OrnamentsPicture,this);
    rLStickerBin.addView(NewPicture);
    stickerLayer.add(NewPicture);
  }
  
  /**
   * 将贴纸下移一层
   */
  public void toBottomSticker(){
    //temp表示当前选中的饰品贴纸所在的layout层次
    int temp = stickerLayer.indexOf(StickerView.PreSticker);
    //判断这个饰品不在底层且已被选中
    if ((THE_LOWEST_LAYER != temp) && (NO_FIND_LAYER != temp)) {
      rLStickerBin.removeView(StickerView.PreSticker);
      int layterTemp = stickerLayer.indexOf(StickerView.PreSticker);
      rLStickerBin.addView(StickerView.PreSticker, layterTemp - 1);
      stickerLayer.remove(layterTemp);
      stickerLayer.add(layterTemp - 1, StickerView.PreSticker);
      stickerNameList.add(layterTemp - 1, stickerNameList.remove(layterTemp));
    }
  }
  
  /**
   * 将贴纸上移一层
   */
  public void toTopSticker() {
    //temp表示当前选中的饰品贴纸所在的layout层次
    int temp = stickerLayer.indexOf(StickerView.PreSticker);
    //判断这个饰品不在顶层且已被选中
    if (((stickerLayer.size() - 1) != temp) && NO_FIND_LAYER != temp) {
      rLStickerBin.removeView(StickerView.PreSticker);
      int layterTemp = stickerLayer.indexOf(StickerView.PreSticker);
      rLStickerBin.addView(StickerView.PreSticker, layterTemp + 1);
      stickerLayer.remove(layterTemp);
      stickerLayer.add(layterTemp + 1, StickerView.PreSticker);
      stickerNameList.add(layterTemp + 1, stickerNameList.remove(layterTemp));
    }
  }
  
  /**
   * 设置旋转和删除按钮的位置按键的位置
   * @param rotationBtnX 旋转X坐标 
   * @param rotationBtnY 旋转Y坐标
   * @param DeleteBtnX 删除X坐标
   * @param DeleteBtnY 删除Y坐标
   */
  public void setBtnPosition(float rotationBtnX, float rotationBtnY, float DeleteBtnX, float DeleteBtnY){
    imgViewStickerRotate.setTranslationX(rotationBtnX);
    imgViewStickerRotate.setTranslationY(rotationBtnY);
    imgViewStickerDelete.setTranslationX(DeleteBtnX);
    imgViewStickerDelete.setTranslationY(DeleteBtnY);
  }
  
  /**
   * 设置功能按钮可见
   */
  public void setBtnVisible(){
    if (!imgViewStickerDelete.isShown()) {
      imgViewStickerDelete.setVisibility(View.VISIBLE);
      imgViewStickerRotate.setVisibility(View.VISIBLE);
    }
  }
  /**
   * 被销毁前将状态保存。
   */
  public void saveData(Bundle outState) {
    ArrayList<Integer> saveCenterAndID = new ArrayList<Integer>();
    ArrayList<String> saveStickerName = new ArrayList<String>();
    float[] saveScaleAndRotation = new float[stickerNameList.size()*2];
    float[] saveImgBtnRotation = new float[stickerNameList.size()*2];
    float[] saveImgBtnDelete = new float[stickerNameList.size()*2];
    for (int i = 0; i < stickerLayer.size(); i++) {
      saveCenterAndID.add(stickerLayer.get(i).getCenterX());
      saveCenterAndID.add(stickerLayer.get(i).getCenterY());
      saveStickerName.add(stickerNameList.get(i));
      saveScaleAndRotation[2*i]  = stickerLayer.get(i).getScale();
      saveScaleAndRotation[1+2*i]= stickerLayer.get(i).getmRotation();;
      saveImgBtnRotation[2*i]  = stickerLayer.get(i).getRotationBtnX();
      saveImgBtnRotation[1+2*i]= stickerLayer.get(i).getRotationBtnY();;
      saveImgBtnDelete[2*i]  = stickerLayer.get(i).getDeleteBtnX();
      saveImgBtnDelete[1+2*i]= stickerLayer.get(i).getDeleteBtnY();;
    }
    outState.putIntegerArrayList("CenterAndID", saveCenterAndID);
    outState.putStringArrayList("StickerName", saveStickerName);
    outState.putFloatArray("ScaleAndRotation", saveScaleAndRotation);
    outState.putFloatArray("ImgBtnRotation", saveImgBtnRotation);
    outState.putFloatArray("ImgBtnDelete", saveImgBtnDelete);
    outState.putString("Frame", frameName);
  }
  
  /**
   * 销毁后恢复时将数据恢复
   */
  public void restoreData(Bundle savedInstanceState) {
    ArrayList<Integer> saveInt = new ArrayList<Integer>();
    ArrayList<String> saveStickerName = new ArrayList<String>();
    saveInt = savedInstanceState.getIntegerArrayList("CenterAndID");
    saveStickerName = savedInstanceState.getStringArrayList("StickerName");
    float[] saveScaleAndRotation = savedInstanceState.getFloatArray("ScaleAndRotation");
    float[] saveImgBtnRotation = savedInstanceState.getFloatArray("ImgBtnRotation");
    float[] saveImgBtnDelete = savedInstanceState.getFloatArray("ImgBtnDelete");
    for (int i = 0; i < saveInt.size()/2; i++) {
      int centerX = saveInt.get(i*2);
      int centerY = saveInt.get(i*2+1);
      String StickerName  = saveStickerName.get(i);
      float scale = saveScaleAndRotation[i*2];
      float rotation = saveScaleAndRotation[i*2+1];
      float ImgBtnRotationX = saveImgBtnRotation[i*2];
      float ImgBtnRotationY = saveImgBtnRotation[i*2+1];
      float ImgBtnDeleteX = saveImgBtnDelete[i*2];
      float ImgBtnDeleteY = saveImgBtnDelete[i*2+1];
      stickerNameList.add(StickerName);
      Bitmap OrnamentsPicture = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath()+"/material/"+StickerName);
      StickerView NewPicture = new StickerView(mContext,OrnamentsPicture,this);
      rLStickerBin.addView(NewPicture);
      stickerLayer.add(NewPicture);
      NewPicture.setStickerParameter(centerX, centerY, scale,rotation);
      NewPicture.setStickerImgBtnPoint(ImgBtnRotationX, ImgBtnRotationY, ImgBtnDeleteX,ImgBtnDeleteY);
    }
    setFrame(savedInstanceState.getString("Frame"));
    if (StickerView.PreSticker != null) {
      StickerView.PreSticker.RemoveBoder();
    }
    imgViewStickerDelete.setVisibility(View.GONE);
    imgViewStickerRotate.setVisibility(View.GONE);
    super.onRestoreInstanceState(savedInstanceState);
  }
  
  /**
   * 设置需要处理的图片作为背景
   * @param uri
   */
  public void setBackgroundDisplay(Uri uri){
    imgViewDisplay.setImageURI(uri);
  }
  
  /**
   * 设置边框
   * @param frameName 边框文件名
   */
  public void setFrame(String frameName){
    this.frameName = frameName;
    Bitmap frame = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath()+"/material/"+frameName);
    ImgViewFrame.setImageBitmap(frame);
  }
  
  /**
   * 贴纸数量是否已达最大值
   * @return boolean
   */
  public boolean isStickerNumOver() {
    return MAX_COUNT_STICKER <= stickerLayer.size() ? true : false;
  }
  
  /**
   * 贴纸数量是否为空
   * @return boolean
   */
  public boolean isEmpty(){
    return STICKER_EMPTY >= stickerLayer.size() ? true : false;
  }
  
  public int getBtnWidth(){
    return imgViewStickerDelete.getWidth();
  }
  
  public int getBtnHeight(){
    return imgViewStickerDelete.getHeight();
  }
}
