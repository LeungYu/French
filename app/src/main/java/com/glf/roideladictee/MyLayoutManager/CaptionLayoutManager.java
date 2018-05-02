package com.glf.roideladictee.MyLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.glf.roideladictee.VideoAddNewWord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 11951 on 2018-04-24.
 */

public class CaptionLayoutManager extends RecyclerView.LayoutManager {

    class ViewLayout{
        View view;
        int l;
        int t;
        int r;
        int b;
        ViewLayout(View view,int l,int t,int r,int b){
            this.view = view;
            this.l = l;
            this.t = t;
            this.r = r;
            this.b = b;
        }
    }

    public int mpadding = 15 * VideoAddNewWord.percentx;

    private List<ViewLayout> mViewLayout= new ArrayList<>();

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);

        int sumWidth = getWidth();

        int curLineWidth = - mpadding, curLineTop = 0;
        int lastLineMaxHeight = 0;
        mViewLayout.clear();
        for (int i = 0; i < getItemCount(); i++) {
            View view = recycler.getViewForPosition(i);
            addView(view);
            measureChildWithMargins(view, 0, 0);
            int width = getDecoratedMeasuredWidth(view);
            int height = getDecoratedMeasuredHeight(view);

            curLineWidth += width + mpadding;

            if (curLineWidth <= sumWidth) {//不需要换行
                mViewLayout.add(new ViewLayout(view, curLineWidth - width, curLineTop, curLineWidth, curLineTop + height));
                //比较当前行多有item的最大高度
                lastLineMaxHeight = Math.max(lastLineMaxHeight, height);
            } else {//换行
                showView(sumWidth - curLineWidth + width);
                mViewLayout.clear();
                curLineWidth = width;
                if (lastLineMaxHeight == 0) {
                    lastLineMaxHeight = height;
                }
                //记录当前行top
                curLineTop += lastLineMaxHeight;

                mViewLayout.add(new ViewLayout(view, 0, curLineTop, width, curLineTop + height));
                lastLineMaxHeight = height;
            }
        }
        if(getItemCount()>0){
            showView(sumWidth - curLineWidth);
        }
    }

    protected void showView(int indent){
        indent /= 2;
        for (ViewLayout viewLayout:mViewLayout) {
            layoutDecorated(viewLayout.view,viewLayout.l+indent,viewLayout.t,viewLayout.r+indent,viewLayout.b);
        }
    }

}
