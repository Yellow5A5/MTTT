package com.meitu.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.meitu.activities.R;

public class StickerView extends ImageView {
  
  private Bitmap OrnamentsPicture;
  /** 记录上一次点击的ViewBin对象*/
  public static StickerView PreSticker;
  /** 屏幕宽高*/
  private int displayX,displayY;
  /** 饰品贴纸的宽高*/
  private int stickerWidth,stickerHeight;
  /** 按钮的宽高*/
  private int stickerBtnHeight,stickerBtnWidth;
  /** 当前的缩放比例*/
  private float currentScale = 1;
  /** 当前的旋转度数*/
  private float currentRotation = 0;
  /** 屏幕中心位置坐标*/
  private int centerX,centerY;
  /**temp[0]为点击位置到图片左边界的距离，temp[1]为点击位置到图片上边界的距离*/
  private int[] temp = new int[] { 0, 0 };
  /** 记录按下时对应的屏幕坐标，以计算偏移*/
  private int downX = 0,downY = 0;
  /** 原始图片边角到中心的距离*/
  private float vectorStart;
  /** 保存旋转按钮的坐标*/
  private float rotationBtnX,rotationBtnY;
  /** 保存删除按钮的坐标*/
  private float DeleteBtnX,DeleteBtnY;
  /** 设置是否可以移动*/
  private boolean isMobilizable = true;
  private BaseLayerView baseLayer;
  public StickerView(Context context,Bitmap OrnamentsPicture,BaseLayerView baseLayer) {
    super(context);
    this.OrnamentsPicture = OrnamentsPicture;
    this.baseLayer = baseLayer;
    RemoveBoder();
    PreSticker = StickerView.this;
    initParameterAndContent();
    AddBoder();
  }
  
  /**
   * 参数、控件初始化。
   */
  private void initParameterAndContent() {
    baseLayer.setBtnVisible();
    displayX = getResources().getDisplayMetrics().widthPixels;
    displayY = getResources().getDisplayMetrics().heightPixels;
    stickerWidth = OrnamentsPicture.getWidth();
    stickerHeight = OrnamentsPicture.getHeight();
    stickerBtnWidth = baseLayer.getBtnWidth();
    stickerBtnHeight = baseLayer.getBtnHeight();
    this.setScaleType(ScaleType.MATRIX);  
    this.setImageBitmap(OrnamentsPicture);
    
    centerX = displayX/2;
    centerY = displayY/2;
    vectorStart = (float) Math.sqrt(Math.pow(stickerWidth/2, 2)+Math.pow(stickerHeight/2, 2));
    rotationBtnX = centerX + stickerWidth/2 - stickerBtnWidth/2;
    rotationBtnY = centerY + stickerHeight/2- stickerBtnHeight/2;
    DeleteBtnX = rotationBtnX - stickerWidth;
    DeleteBtnY = rotationBtnY - stickerHeight;
    //初始化 贴纸和两个功能按钮的位置。
    this.setTranslationX(centerX - stickerWidth/2);
    this.setTranslationY(centerY - stickerHeight/2);
    baseLayer.setBtnPosition(rotationBtnX, rotationBtnY, DeleteBtnX, DeleteBtnY);
  }
  
  /**
   * 触屏逻辑。
   */
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (!isMobilizable) {
      return false;
    }
    int x = (int) event.getRawX();
    int y = (int) event.getRawY();
    
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        if (StickerView.this != PreSticker) {
            RemoveBoder();
        }
        AddBoder();
        PreSticker = StickerView.this;
        downX = (int) event.getRawX();
        downY = (int) event.getRawY();
        temp[0] = (int) (x - this.getX());
        temp[1] = (int) (y - this.getY());
        updataImgBtnPosition(event);
        baseLayer.setBtnVisible();
        break;
      case MotionEvent.ACTION_MOVE:
        this.setTranslationX(x - temp[0]);
        this.setTranslationY(y - temp[1]);
        updataImgBtnPosition(event);
        break;
      case MotionEvent.ACTION_UP:
        this.setTranslationX(x - temp[0]);
        this.setTranslationY(y - temp[1]);
        updataPointSave(event);
        break;
      default:
        break;
    }
    return true;
  }
  
  /**
   * 更新功能按键的位置
   * @param event
   */
  private void updataImgBtnPosition(MotionEvent event) {
    baseLayer.setBtnPosition( rotationBtnX + event.getRawX() - downX,
                              rotationBtnY + event.getRawY() - downY, 
                              DeleteBtnX + event.getRawX() - downX,
                              DeleteBtnY + event.getRawY() - downY);
  }
  
  /**
   * 更新功能按钮的保存坐标及图片中心点坐标
   * @param event
   */
  private void updataPointSave(MotionEvent event) {
    //计算偏移量
    int offsetX = (int) (event.getRawX()-downX);
    int offsetY = (int) (event.getRawY()-downY);
    //更新中心点坐标
    centerX += (int) (offsetX);
    centerY += (int) (offsetY);
    //更新删除坐标
    DeleteBtnX += (int) (offsetX);
    DeleteBtnY += (int) (offsetY);
    //更新旋转坐标
    rotationBtnX += (int) (offsetX);
    rotationBtnY += (int) (offsetY);
  }

  /**
   * 绘制饰品的边界，表示给饰品被选中
   */
  private void AddBoder(){
    this.setBackgroundResource(R.drawable.border_img_bg_seleted);
  }
  
  /**
   * 移除上次选中的饰品的边界，表示饰品未被选中
   */
  public void RemoveBoder(){
    if (null != PreSticker) {
      PreSticker.setBackgroundResource(R.drawable.border_img_bg_unseleted);
      PreSticker.getBackground().setAlpha(0);
      PreSticker.setMobilizable(true);
    }
  }
  
  /**
   * 重新计算并设置当前缩放比例
   * @param nowX  边界点X坐标
   * @param nowY  边界点Y坐标
   */
  public void resetScale(float nowX,float nowY){
    float vector = vectorToCenter(nowX, nowY);
    currentScale = vector/vectorStart;
    setScaleX(currentScale);
    setScaleY(currentScale);
  }

  /**
   * 通过余弦定理计算出角度重置旋转
   * @param nowX 当前旋转按钮的X坐标
   * @param nowY 当前旋转按钮的Y坐标
   */
  public void resetRotation(float nowX,float nowY){
    //vectorNow、vectorStart、vectorToPoint为三角形的三条边
    float vectorNow = vectorToCenter(nowX, nowY);
    float vectorToPoint = vectorToPoint(nowX, nowY, centerX + stickerWidth/2  , centerY + stickerHeight/2);
    double yuXianDingLi =  (Math.pow(vectorStart, 2) + Math.pow(vectorNow, 2) - Math.pow(vectorToPoint, 2)) / (2 * vectorStart * vectorNow) ;
    if (isPositionRotate(nowX, nowY)) {
      currentRotation = (float) arccos(yuXianDingLi);
    }else {
      currentRotation = -(float) arccos(yuXianDingLi);
    }
    setRotation(currentRotation);
  }

  /** 通过向量叉乘结果判断是否为正向旋转*/
  private boolean isPositionRotate(float nowX,float nowY){
    float isPosition = (( - stickerHeight / 2) * (nowX - centerX)) - ((- stickerWidth / 2) * (nowY - centerY));
    return isPosition > 0 ? true : false;
  }
  
  /**
   * 计算两点之间的距离
   * @param X1 第一个点的X坐标
   * @param Y1 第一个点的Y坐标
   * @param X2 第二个点的X坐标
   * @param Y2 第二个点的Y坐标
   * @return 两点之间的距离
   */
  private float vectorToPoint(float X1, float Y1,float X2,float Y2){
    return (float) Math.sqrt(Math.pow(Math.abs(X2-X1), 2) + Math.pow(Math.abs(Y2-Y1), 2));
  }
  
  /**
   * 根据点坐标计算点到贴纸中心的距离
   * @param X  点的X坐标
   * @param Y  点的Y坐标
   * @return  点到中心的距离
   */
  private float vectorToCenter(float X, float Y) {
    return vectorToPoint(X, Y, centerX, centerY);
  }
  
  /**
   * 保存旋转放大后的两个功能按钮的坐标
   * @param X 旋转按钮的中心X坐标
   * @param Y 旋转按钮的中心Y坐标
   */
  public void saveImgBtnPoint(float X,float Y){
    rotationBtnX = X - stickerBtnWidth/2;
    rotationBtnY = Y - stickerBtnHeight/2;
    DeleteBtnX = 2 * centerX - X - stickerBtnWidth/2;
    DeleteBtnY = 2 * centerY - Y - stickerBtnHeight/2;
  }
/*  
  *//**
   * 位置控制方法
   * @param view  控件
   * @param x View左上角点横轴坐标
   * @param y View左上角点纵轴坐标
   *//*
  public void setLayout(View view, int x, int y) {
    MarginLayoutParams margin = new MarginLayoutParams(getViewWidth(view),getViewHeight(view));
    margin.setMargins(x, y, 
        getResources().getDisplayMetrics().widthPixels-(x+getViewWidth(view)),//减去View的宽度使得其被拖动到左下角的时候不会被缩小
        getResources().getDisplayMetrics().heightPixels-(y+getViewHeight(view)));//同上。
    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
        margin);
    view.setLayoutParams(layoutParams);
  }
  
  *//**
   * 通过测量获取View的宽度（因为无法在View完成绘制前无法直接获取到其宽高，所以在此需要自己测量获取）
   * @param view 需要测量的View
   * @return  View的宽度
   *//*
  public int getViewWidth(View view) {
    view.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    return view.getMeasuredWidth();
  }
  
  *//**
   * 通过测量获取View的高度（因为无法在View完成绘制前无法直接获取到其宽高，所以在此需要自己测量获取）
   * @param view 需要测量的View
   * @return  View的高度
   *//*
  public int getViewHeight(View view) {
    view.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    return view.getMeasuredHeight();
  }
  
  *//**
   * 显示矩阵中的值
   * @param matrix 
   *//*
  void showToast(Matrix matrix) {
      String string = "";
      float[] values = new float[9];
      matrix.getValues(values);
      for (int i = 0; i < values.length; i++) {
          string += "matrix.at" + i + "=" + values[i];
      }
      Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
      Log.d("TEST", string);
  }
  */
  /** 
   * cos反函数转换为角度
   * @param a double 余弦值 
   * @return double   角度(360) 
   */ 
  public static double arccos(double a) {
    double b = 90.0, c0 = 0.0, c1 = 180.0;
    if (a < 1 && a > -1) {
      do {
        if (Math.cos(b * Math.PI / 180) >= a) {
          c0 = b;
          b = (c0 + c1) / 2;
        }
        if (Math.cos(b * Math.PI / 180) <= a) {
          c1 = b;
          b = (c0 + c1) / 2;
        }
      } while (Math.abs(c0 - c1) > 0.00001);
    }
    return b;
  }
  
  public int getCenterX(){
    return centerX;
  }
  
  public int getCenterY(){
    return centerY;
  }
  
  public float getScale(){
    return currentScale;
  }
  
  public float getmRotation(){
    return currentRotation;
  }
  
  public float getRotationBtnX() {
    return rotationBtnX;
  }

  public float getRotationBtnY() {
    return rotationBtnY;
  }

  public float getDeleteBtnX() {
    return DeleteBtnX;
  }

  public float getDeleteBtnY() {
    return DeleteBtnY;
  }
  
  public void setStickerParameter(int X,int Y,float Scale,float Rotation){
    centerX = X;
    centerY = Y;
    currentScale = Scale;
    currentRotation = Rotation;
    setX(X - stickerWidth/2);
    setY(Y - stickerHeight/2);
    setScaleX(currentScale);
    setScaleY(currentScale);
    setRotation(currentRotation);
  }

  public void setStickerImgBtnPoint(float imgBtnRotationX,float imgBtnRotationY, 
                                    float imgBtnDeleteX, float imgBtnDeleteY) {
    this.rotationBtnX = imgBtnRotationX;
    this.rotationBtnY = imgBtnRotationY;
    this.DeleteBtnX = imgBtnDeleteX;
    this.DeleteBtnY = imgBtnDeleteY;
  }
  
  /**
   * 设置饰品可移动性
   * @param isCan 
   */
  public void setMobilizable(boolean isCan) {
    if (isCan) {
      isMobilizable = true;
    }else {
      isMobilizable = false;
    }
  }
}
