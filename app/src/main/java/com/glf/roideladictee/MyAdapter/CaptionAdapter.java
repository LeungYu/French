package com.glf.roideladictee.MyAdapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.glf.roideladictee.MyEnum.TestStatus;
import com.glf.roideladictee.MyEnum.VideoMode;
import com.glf.roideladictee.R;
import com.glf.roideladictee.VideoAddNewWord;

import java.util.List;

/**
 * Created by 11951 on 2018-04-24.
 * 字幕的适配器，用于生成字幕
 */

public class CaptionAdapter extends RecyclerView.Adapter<CaptionAdapter.ViewHolder>{
    private List<String> captions;
    private VideoMode videoMode;
    private List<Boolean> isTestCaptions;
    private Typeface typeface;

    public CaptionAdapter(VideoMode videoMode){
        super();
        this.videoMode = videoMode;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.captions_item,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(typeface != null)holder.mTextView.getPaint().setTypeface(typeface);
        if(videoMode == VideoMode.TEST&&isTestCaptions.get(position)){
            if(VideoAddNewWord.testStatus == TestStatus.WRONG) {
                holder.mTextView.setText(captions.get(position));
                holder.mTextView.setTextColor(Color.parseColor("#da1911"));
            }else if(VideoAddNewWord.testStatus == TestStatus.RIGHT){
                holder.mTextView.setText(captions.get(position));
                holder.mTextView.setTextColor(Color.parseColor("#00c13f"));
            }else{
                holder.mTextView.setText(captions.get(position).replaceAll(".","_"));
            }
        }else{
            holder.mTextView.setText(captions.get(position));
        }

        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener != null){
                    mOnItemClickListener.onItemClick(holder.mTextView, captions.get(position),isTestCaptions.get(position));
                }
            }
        });
        if(videoMode == VideoMode.ADD)holder.mTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(VideoAddNewWord.playing){
                    ((TextView)v).setTextColor(Color.parseColor("#ffffff"));
                    return false;
                }
                if (event.getAction() == 0) {
                    ((TextView)v).setTextColor(Color.parseColor("#00c13f"));
                }
                if (event.getAction() == 1 || event.getAction() == 3) {
                    ((TextView)v).setTextColor(Color.parseColor("#ffffff"));
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return captions.size();
    }

    public interface OnItemClickListener {
        void onItemClick(TextView mTextview, String caption,Boolean isTestCaption);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    public void setCaptions(List<String> captions,List<Boolean> isTestCaptions){
        this.captions = captions;
        this.isTestCaptions = isTestCaptions;
    }

    public void setTypeface(Typeface typeface){
        this.typeface = typeface;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;
        public ViewHolder(TextView v){
            super(v);
            mTextView = v;
        }
    }
}
