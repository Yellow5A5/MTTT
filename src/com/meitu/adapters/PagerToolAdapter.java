package com.meitu.adapters;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * 初始化自定义的PagerToolAdapter（饰品选择页）
 */
public class PagerToolAdapter extends PagerAdapter{

  private List<GridView> gridViewList;
  public PagerToolAdapter(List<GridView> listgV) {
    this.gridViewList = listgV;
  }
  @Override
  public int getCount() {
    return gridViewList.size();
  }
  
  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    container.addView(gridViewList.get(position));
    return gridViewList.get(position);
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView(gridViewList.get(position));
  }
  
  @Override
  public boolean isViewFromObject(View arg0, Object arg1) {
    return arg0 == arg1;
  }
  
}