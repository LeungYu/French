package com.glf.roideladictee.MyLitePal;

import com.glf.roideladictee.MyEnum.WordNoteType;

import org.litepal.crud.DataSupport;

/**
 * Created by 11951 on 2018-05-02.
 */

public class WordNote extends DataSupport {
    int id;
    String word;
    String wordNoteType;

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

    public String getWordNoteType() {
        return wordNoteType;
    }

    public void setWordNoteType(WordNoteType wordNoteType) {
        this.wordNoteType = wordNoteType == WordNoteType.ADDNEWWORD?"ADDNEWWORD":"IDONTKNOW";
    }
}
