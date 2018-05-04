package com.glf.roideladictee.fr_app;

import java.util.Date;

/**
 * Created by 11951 on 2018-05-04.
 */

public class fr_contest {
    String contest_id;
    Date contest_datetime;
    String movie_name;
    String mov_file;
    String mov_srt;

    public fr_contest(String contest_id, Date contest_datetime, String movie_name, String mov_file, String mov_srt) {
        this.contest_id = contest_id;
        this.contest_datetime = contest_datetime;
        this.movie_name = movie_name;
        this.mov_file = mov_file;
        this.mov_srt = mov_srt;
    }

    public String getContest_id() {
        return contest_id;
    }

    public void setContest_id(String contest_id) {
        this.contest_id = contest_id;
    }

    public String getMov_srt() {
        return mov_srt;
    }

    public void setMov_srt(String mov_srt) {
        this.mov_srt = mov_srt;
    }

    public String getMov_file() {
        return mov_file;
    }

    public void setMov_file(String mov_file) {
        this.mov_file = mov_file;
    }

    public String getMovie_name() {
        return movie_name;
    }

    public void setMovie_name(String movie_name) {
        this.movie_name = movie_name;
    }

    public Date getContest_datetime() {
        return contest_datetime;
    }

    public void setContest_datetime(Date contest_datetime) {
        this.contest_datetime = contest_datetime;
    }
}
