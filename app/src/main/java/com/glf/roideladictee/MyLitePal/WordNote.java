package com.glf.roideladictee.MyLitePal;

import org.litepal.crud.DataSupport;

/**
 * Created by 11951 on 2018-05-02.
 */

public class WordNote extends DataSupport {
    int id;
    String word;
    String info;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

}
