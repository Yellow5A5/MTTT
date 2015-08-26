package com.meitu.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.meitu.activities.R;

public class ImgAdapter extends BaseAdapter {
  private LayoutInflater mInflater;
  /** 传入带有文件名的List*/
  private List<String> mDatas;

  public ImgAdapter(Context context, List<String> mDatas) {
    mInflater = LayoutInflater.from(context);
    this.mDatas = mDatas;
  }

  @Override
  public int getCount() {
    return mDatas.size();
  }

  @Override
  public Object getItem(int position) {
    return mDatas.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder = null;
    if (convertView == null) {
      convertView = mInflater.inflate(R.layout.girdview_item, parent, false);
      viewHolder = new ViewHolder();
      viewHolder.mTextView = (ImageView) convertView.findViewById(R.id.img_tool_item);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }
    Drawable draw = BitmapDrawable.createFromPath(Environment.getExternalStorageDirectory().getPath()+"/material/"+mDatas.get(position));
    viewHolder.mTextView.setImageDrawable(draw);
    return convertView;
  }

  private final class ViewHolder {
    ImageView mTextView;
  }

}
