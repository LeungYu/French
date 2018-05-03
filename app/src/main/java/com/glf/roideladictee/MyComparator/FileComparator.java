package com.glf.roideladictee.MyComparator;

import java.io.File;
import java.util.Comparator;

/**
 * Created by 11951 on 2018-05-03.
 */

public class FileComparator implements Comparator<File> {
    @Override
    public int compare(File file, File t1) {
        return file.getName().compareTo(t1.getName());
    }
}
