package com.glf.roideladictee.MyAdapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.glf.roideladictee.R;
import com.glf.roideladictee.fr_app.fr_contest;

import java.text.SimpleDateFormat;

/**
 * 自定义适配器
 */
public class ParticipateCotestAdapter extends BaseAdapter {

    private static Typeface typeface;
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Context context;

    fr_contest[] frcontests;

    LayoutInflater layoutInflater;

    public ParticipateCotestAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public ParticipateCotestAdapter(Context context, fr_contest[] frcontests, Typeface typeface) {
        this.typeface=typeface;
        this.context = context;
        this.frcontests = frcontests;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return frcontests.length;
    }

    @Override
    public fr_contest getItem(int i) {
        return frcontests[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i,View convertView,ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.participate_contest_item, null);
            holder = new ViewHolder(context,convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.bindData(frcontests[i]);
        return convertView;
    }

    static int counter = 1;

    static class ViewHolder {
        Context context;
        TextView title,info;
        ImageButton action;
        int id;

        public ViewHolder(Context context,View v) {
            this.context = context;
            title = (TextView) v.findViewById(R.id.contest);
            info = (TextView) v.findViewById(R.id.contest_info);
            action = (ImageButton) v.findViewById(R.id.imageButton_action);
            id = counter++;
        }

        public void bindData(fr_contest frcontest) {
            title.setText(frcontest.getMovie_name());
            info.setText(sdf.format(frcontest.getContest_datetime()));
        }
    }

}
