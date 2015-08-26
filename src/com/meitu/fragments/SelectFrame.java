package com.meitu.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

import com.meitu.activities.R;
import com.meitu.adapters.ImgAdapter;


public class SelectFrame extends Fragment {

  private final static String selectFrameKey = "SFKey";
  private static SelectFrame mSelectFrame;
  private View view;
  private GridView gVFrameContent;
  private ImageView imgViewCloseFramePager;
  private OnItemClickListener frameGVItemListener;
  private OnFrameItemOnClickListener mOnFrameItemOnClickListener;
  private ArrayList<String> frameList;
  
  /** 回调接口*/
  public interface OnFrameItemOnClickListener{
    public void OnFrameItemOnClick(View view, int position);
  }
  
  public void setOnFrameItemOnClickListener(OnFrameItemOnClickListener mOnFrameItemOnClickListener){
    this.mOnFrameItemOnClickListener = mOnFrameItemOnClickListener;
  }
  
  public static SelectFrame getInstance(ArrayList<String> frameList){
    if (mSelectFrame == null) {
      mSelectFrame = new SelectFrame();
    }
    Bundle bundle = new Bundle();
    bundle.putStringArrayList(selectFrameKey, frameList);
    mSelectFrame.setArguments(bundle);
    return mSelectFrame;
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater,
      @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.select_frame_fragment, container, false);
    Bundle bundle = mSelectFrame.getArguments();
    frameList = bundle.getStringArrayList(selectFrameKey);
    initView();
    initListener();
    initGVContent();
    setFragmentInVisable();
    return view;
  }


  private void initGVContent() {
    ImgAdapter frameItemAdapter = new ImgAdapter(getActivity(),frameList);
    gVFrameContent.setAdapter(frameItemAdapter);
    gVFrameContent.refreshDrawableState();
  }


  private void initView() {
    gVFrameContent = (GridView) view.findViewById(R.id.viewpager_frame);
    imgViewCloseFramePager = (ImageView) view.findViewById(R.id.img_close_frampager);
  }
  
  private void initListener() {
    frameGVItemListener = new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position,
          long id) {
        mOnFrameItemOnClickListener.OnFrameItemOnClick(view, position);
        setFragmentInVisable();
      }
    };
    gVFrameContent.setOnItemClickListener(frameGVItemListener);
    
    imgViewCloseFramePager.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        setFragmentInVisable();
      }
    });
  }

  private void setFragmentInVisable() {
    getFragmentManager().beginTransaction()
    .setTransition(FragmentTransaction.TRANSIT_EXIT_MASK)
    .hide(SelectFrame.this)
    .commit();
  }
}
