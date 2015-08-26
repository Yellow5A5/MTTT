package com.meitu.fragments;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.meitu.activities.R;
import com.meitu.adapters.ImgAdapter;
import com.meitu.adapters.PagerToolAdapter;

public class SelectSticker extends Fragment{
  
  private final static String GX_KEY = "GXKey";
  private final static String ZD_KEY = "ZDKey";
  private final static int PAGER_ONE = 0;
  private final static int PAGER_TWO = 1;
  private static SelectSticker mSelectSticker;
  private View view;
  /** 存储不同pager上的Gridview对象*/
  private List<GridView> gVListResource;
  /** 左边饰品页面*/
  private GridView gVStickerLeft;
  /** 右边饰品页面*/
  private GridView gVStickerRight;
  /** 饰品页PagerView的收起按钮*/
  private ImageView imgViewPagerClose;
  /** 左边饰品按钮  右边饰品按钮*/
  private LinearLayout lLOrnamentsBottomRight,lLOrnamentsBottomLeft;
  private ViewPager viewPagerStickers;
  private OnClickListener BtnListener;
  private OnItemClickListener stickerGVItemListener;
  private OnPageChangeListener PageChangeListener;
  private OnStickerItemClickListener mOnStickerItemClickListener;
  
  private ArrayList<String> gXStickerList;
  private ArrayList<String> zDStickerList;
  /** 回调接口*/
  public interface OnStickerItemClickListener{
    public void onStickerItemClick(int pagerNumber,int position);
  }
  public void setOnStickerItemClickListener(OnStickerItemClickListener mOnStickerItemClickListener){
    this.mOnStickerItemClickListener = mOnStickerItemClickListener;
  }
  
  public static SelectSticker getInstance(ArrayList<String> gXStickerList,ArrayList<String> zDStickerList){
    if (mSelectSticker == null) {
      mSelectSticker = new SelectSticker();
      Bundle bundle = new Bundle();
      bundle.putStringArrayList(GX_KEY, gXStickerList);
      bundle.putStringArrayList(ZD_KEY, zDStickerList);
      mSelectSticker.setArguments(bundle);
    }
    return mSelectSticker;
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater,
      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.select_sticker_fragment, container, false);
    Bundle bundle = mSelectSticker.getArguments();
    gXStickerList = bundle.getStringArrayList(GX_KEY);
    zDStickerList = bundle.getStringArrayList(ZD_KEY);
    initView();
    initGVContent();
    initGvItemListener();
    initPagerChangeListener();
    initPagerContent();
    initListener();
    setStickerPagerInvisable();
    return view;
  }
  
  private void initView(){
    imgViewPagerClose = (ImageView)view.findViewById(R.id.img_close_pager);
    lLOrnamentsBottomLeft = (LinearLayout) view.findViewById(R.id.lable_ornaments_bottom_left);
    lLOrnamentsBottomRight = (LinearLayout) view.findViewById(R.id.lable_ornaments_bottom_right);
    viewPagerStickers = (ViewPager) view.findViewById(R.id.viewpager_tool);
  }
  
  private void initListener() {
    gVStickerLeft.setOnItemClickListener(stickerGVItemListener);
    gVStickerRight.setOnItemClickListener(stickerGVItemListener);
    viewPagerStickers.setOnPageChangeListener(PageChangeListener);
    BtnListener = new OnClickListener() {
      @Override
      public void onClick(View v) {
        switch (v.getId()) {
        case R.id.img_close_pager:
          setStickerPagerInvisable();
          break;
        case R.id.lable_ornaments_bottom_left:
          if (viewPagerStickers.getCurrentItem() == PAGER_TWO) {
            viewPagerStickers.setCurrentItem(0);
          }
          break;
        case R.id.lable_ornaments_bottom_right:
          if (viewPagerStickers.getCurrentItem() == PAGER_ONE) {
            viewPagerStickers.setCurrentItem(1);
          }
          break;
        default:
          break;
        }
      }
    };
    imgViewPagerClose.setOnClickListener(BtnListener);
    lLOrnamentsBottomLeft.setOnClickListener(BtnListener);
    lLOrnamentsBottomRight.setOnClickListener(BtnListener);
  }
  
  /**
   * 设置GridView的参数
   * @param gV
   */
  private void gVsetParameter(GridView gV,ArrayList<String> name) {//TODO
    ImgAdapter stickerLeftAdapter = new ImgAdapter(getActivity(),name);
    gV.setNumColumns(3);
    gV.setAdapter(stickerLeftAdapter);
    gVListResource.add(gV);
  }
  
  /**
   * 设置页面隐藏
   */
  private void setStickerPagerInvisable() {
    getFragmentManager().beginTransaction()
        .setTransition(FragmentTransaction.TRANSIT_EXIT_MASK)
        .hide(SelectSticker.this)
        .commit();
  }
  
  /**
   * 初始化饰品页（PagerView）内容
   */
  private void initPagerContent() {
    PagerToolAdapter ToolAdapter = new PagerToolAdapter(gVListResource);
    viewPagerStickers.setAdapter(ToolAdapter);
  }
  
  /**
   * 对饰品页面（PagerView）的滑动监听事件初始化化（监听更新底部标题栏状态）
   */
  private void initPagerChangeListener() {
    PageChangeListener = new OnPageChangeListener() {
      @Override
      public void onPageSelected(int position) {
        switch (position) {
        case 0:
          lLOrnamentsBottomLeft.setBackgroundColor(Color.parseColor("#FF1493"));
          lLOrnamentsBottomRight.setBackgroundColor(Color.GRAY);
          break;
        case 1:
          lLOrnamentsBottomLeft.setBackgroundColor(Color.GRAY);
          lLOrnamentsBottomRight.setBackgroundColor(Color.parseColor("#FF1493"));
          break;
        default:
          break;
        }
      }
      @Override
      public void onPageScrolled(int arg0, float arg1, int arg2) {}
      @Override
      public void onPageScrollStateChanged(int arg0) {}
    };
  }
  
  /**
   * 对饰品和边界选择页（GridView）的选项（Item）监听器的初始化。
   */
  private void initGvItemListener() {
    stickerGVItemListener = new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position,
          long id) {
        int pagerNumber = viewPagerStickers.getCurrentItem();
        mOnStickerItemClickListener.onStickerItemClick(pagerNumber,position);
        getFragmentManager().beginTransaction().hide(SelectSticker.this).commit();
      }
    };
  }
  
  /**
   * 填充控件内容
   */
  private void initGVContent(){
    gVListResource = new ArrayList<GridView>();
    gVStickerLeft = new GridView(getActivity());
    gVStickerRight = new GridView(getActivity());
    gVsetParameter(gVStickerLeft,gXStickerList);
    gVsetParameter(gVStickerRight,zDStickerList);
  }
}
