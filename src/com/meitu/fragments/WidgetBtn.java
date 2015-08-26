package com.meitu.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.meitu.activities.R;

public class WidgetBtn extends Fragment {
  
  private final static int FIRST_TIME_CLICK = 0;
  
  private View view;
  private ImageView imgViewToTop;
  private ImageView imgViewToBottom;
  private ImageView imgViewDelete;
  private ImageView imgViewClose;
  private ImageView imgViewOpen;
  private ImageView imgViewLock;
  
  private OnClickListener widgetClickListener;
  /** 展开按钮的动画集合*/
  private AnimationSet openAnimation;
  private TranslateAnimation toTopAnimationOpen;
  private TranslateAnimation toBottomAnimationOpen;
  private TranslateAnimation deleteAnimationOpen;
  
  /** 收起按钮的动画集合*/
  private AnimationSet closeAnimation;
  private TranslateAnimation toTopAnimationClose;
  private TranslateAnimation toBottomAnimationClose;
  private TranslateAnimation deleteAnimationClose;
  
  /**记录纵轴变化距离参数*/
  private float saveToTopStartY=0,saveToBottomStartY=0,saveDeleteStartY=0;
  
  private OnWidgetClickListener mOnWidgetClickListener;
  
  public interface OnWidgetClickListener{
    public void onWidgetClick(int ID);
  }
  
  public void setOnWidgetClickListener(OnWidgetClickListener mOnWidgetClickListener){
    this.mOnWidgetClickListener = mOnWidgetClickListener;
  }
  
  
  @Override
  public View onCreateView(LayoutInflater inflater,
      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.widget_operate, container, false);
    initView();
    return view;
  }

  /** 界面控件初始化*/
  private void initView(){
    imgViewToTop = (ImageView)view.findViewById(R.id.img_widget_totop);
    imgViewToBottom = (ImageView)view.findViewById(R.id.img_widget_tobottom);
    imgViewDelete = (ImageView)view.findViewById(R.id.img_widget_delete);
    imgViewClose = (ImageView)view.findViewById(R.id.img_widget_close);
    imgViewOpen = (ImageView)view.findViewById(R.id.img_widget_open);
    imgViewLock = (ImageView)view.findViewById(R.id.img_widget_lock);
    initListener();
    imgViewToTop.setOnClickListener(widgetClickListener);
    imgViewToBottom.setOnClickListener(widgetClickListener);
    imgViewDelete.setOnClickListener(widgetClickListener);
    imgViewClose.setOnClickListener(widgetClickListener);
    imgViewOpen.setOnClickListener(widgetClickListener);
    imgViewLock.setOnClickListener(widgetClickListener);
  }

  /** 功能按钮监听器初始化*/
  private void initListener() {
    widgetClickListener = new OnClickListener() {
      @Override
      public void onClick(View v) {
        switch (v.getId()) {
        case R.id.img_widget_close:
          //如果是第一次点击，则进行动画的初始化 及 变化距离参数的初始化

          if (saveToTopStartY == FIRST_TIME_CLICK) 
          {
            initParameter();
            initAnimation();
          }
          CloseAnimation();
          break;
        case R.id.img_widget_open:
          OpenAnimation();
          break;
        case R.id.img_widget_lock:
          break;
        default:
          mOnWidgetClickListener.onWidgetClick(v.getId());
          break;
        }
      }
    };
  }

  /** 变化距离参数初始化*/
  private void initParameter() {
    //初始化的时候是得不到正确的数值的，因为View还没被绘制出来。所以下面的initanimation（）方法也是存在这样的问题的。
    saveToTopStartY = imgViewClose.getY() - imgViewToTop.getY();
    saveToBottomStartY = imgViewClose.getY() - imgViewToBottom.getY();
    saveDeleteStartY = imgViewClose.getY() - imgViewDelete.getY();
  }
  
  /** 动画初始化*/
  private void initAnimation() {
    toTopAnimationOpen = new TranslateAnimation(0 , 0, saveToTopStartY, 0);
    toBottomAnimationOpen = new TranslateAnimation(0 , 0, saveToBottomStartY, 0);
    deleteAnimationOpen = new TranslateAnimation(0 , 0, saveDeleteStartY, 0);
    toTopAnimationOpen.setDuration(500);
    toTopAnimationOpen.setFillAfter(true);
    toBottomAnimationOpen.setDuration(500);
    toBottomAnimationOpen.setFillAfter(true);
    deleteAnimationOpen.setDuration(500);
    deleteAnimationOpen.setFillAfter(true);
    
    toTopAnimationClose = new TranslateAnimation(0, 0, 0, saveToTopStartY);
    toBottomAnimationClose = new TranslateAnimation(0, 0, 0, saveToBottomStartY);
    deleteAnimationClose = new TranslateAnimation(0, 0, 0, saveDeleteStartY);
    toTopAnimationClose.setDuration(500);
    toTopAnimationClose.setFillAfter(true);
    toBottomAnimationClose.setDuration(500);
    toBottomAnimationClose.setFillAfter(true);
    deleteAnimationClose.setDuration(500);
    deleteAnimationClose.setFillAfter(true);
    
    closeAnimation = new AnimationSet(true);
    closeAnimation.addAnimation(toTopAnimationClose);
    closeAnimation.addAnimation(toBottomAnimationClose);
    closeAnimation.addAnimation(deleteAnimationClose);
    
    openAnimation = new AnimationSet(true);
    openAnimation.addAnimation(toTopAnimationOpen);
    openAnimation.addAnimation(toBottomAnimationOpen);
    openAnimation.addAnimation(deleteAnimationOpen);
    
    AnimationListener animationWidgetListener = new AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {
        if (animation.equals(toTopAnimationOpen) ) {
          setWidgetVisible();
        }
      }
      @Override
      public void onAnimationRepeat(Animation animation) {
      }
      @Override
      public void onAnimationEnd(Animation animation) {
        if (animation.equals(toTopAnimationClose)) {
          setWidgetGone();
          imgViewClose.setVisibility(View.GONE);
          imgViewOpen.setVisibility(View.VISIBLE);
        }else {
          imgViewOpen.setVisibility(View.GONE);
          imgViewClose.setVisibility(View.VISIBLE);
        }
      }
    };
    toTopAnimationOpen.setAnimationListener(animationWidgetListener);
    toTopAnimationClose.setAnimationListener(animationWidgetListener);
  }
 /** 展开 功能按钮动画*/
 private void OpenAnimation() {
    imgViewToTop.setAnimation(toTopAnimationOpen);
    imgViewToBottom.setAnimation(toBottomAnimationOpen);
    imgViewDelete.setAnimation(deleteAnimationOpen);
    openAnimation.startNow();
  }
  
 /** 收起 功能按钮动画*/
  private void CloseAnimation() {
    imgViewToTop.setAnimation(toTopAnimationClose);
    imgViewToBottom.setAnimation(toBottomAnimationClose);
    imgViewDelete.setAnimation(deleteAnimationClose);
    closeAnimation.startNow();
  }
  
  /** 设置 功能按钮可见*/
  private void setWidgetVisible() {
    imgViewToTop.setVisibility(View.VISIBLE);
    imgViewToBottom.setVisibility(View.VISIBLE);
    imgViewDelete.setVisibility(View.VISIBLE);
  }
  
  /** 设置 功能按钮消失*/
  private void setWidgetGone() {
    imgViewToTop.setVisibility(View.GONE);
    imgViewToBottom.setVisibility(View.GONE);
    imgViewDelete.setVisibility(View.GONE);
  }
}
