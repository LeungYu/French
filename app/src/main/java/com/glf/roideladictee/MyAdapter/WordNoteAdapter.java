package com.glf.roideladictee.MyAdapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.glf.roideladictee.MyLitePal.WordNote;
import com.glf.roideladictee.R;

import java.io.File;
import java.util.ArrayList;

/**
 * 自定义适配器
 */
public class WordNoteAdapter extends BaseAdapter {

    private static Typeface typeface;

    Context context;

    WordNote[] wordNotes;

    LayoutInflater layoutInflater;

    public WordNoteAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public WordNoteAdapter(Context context, WordNote[] wordNotes, Typeface typeface) {
        this.typeface=typeface;
        this.context = context;
        this.wordNotes = wordNotes;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return wordNotes.length;
    }

    @Override
    public WordNote getItem(int i) {
        return wordNotes[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i,View convertView,ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.word_note_item, null);
            holder = new ViewHolder(context,convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.bindData(wordNotes[i]);
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
            title = (TextView) v.findViewById(R.id.word);
            info = (TextView) v.findViewById(R.id.word_info);
            action = (ImageButton) v.findViewById(R.id.imageButton_action);
            id = counter++;
        }

        public void bindData(WordNote wordNote) {
            title.setText(wordNote.getWord());
//            info.setText(wordNote.getInfo());
        }
    }

}
