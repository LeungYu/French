package com.glf.roideladictee.MyComparator;

import java.io.File;
import java.util.Comparator;

/**
 * Created by 11951 on 2018-05-03.
 */

public class FileComparator implements Comparator<File> {
    @Override
    public int compare(File file, File t1) {
        String s1 = file.getName(),s2 = t1.getName();
        if(s1.length() * s2.length() > 0){//防止没名字
            if(s1.toLowerCase().charAt(0)==s2.toLowerCase().charAt(0)){
                return s1.compareTo(s2);
            }else{
                return file.getName().toLowerCase().compareTo(t1.getName().toLowerCase());
            }
        }else return s1.length() - s2.length();
    }
}
