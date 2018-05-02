package com.glf.roideladictee.MyLitePal;

import org.litepal.crud.DataSupport;

/**
 * Created by 11951 on 2018-04-27.
 * LitePal用数据项，字幕Ass数据结构
 */

public class CaptionAss extends DataSupport{
    private int id;
    private long start;
    private long end;
    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
