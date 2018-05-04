package com.glf.roideladictee.MyClass;

/**
 * Created by 11951 on 2018-05-04.
 */

public class fr_join {
    String phone_number;
    String contest_id;
    Boolean contest_status;
    Float contest_mark;

    public fr_join(String phone_number, String contest_id, Float contest_mark, Boolean contest_status) {
        this.phone_number = phone_number;
        this.contest_id = contest_id;
        this.contest_mark = contest_mark;
        this.contest_status = contest_status;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Boolean getContest_status() {
        return contest_status;
    }

    public void setContest_status(Boolean contest_status) {
        this.contest_status = contest_status;
    }

    public Float getContest_mark() {
        return contest_mark;
    }

    public void setContest_mark(Float contest_mark) {
        this.contest_mark = contest_mark;
    }

    public String getContest_id() {
        return contest_id;
    }

    public void setContest_id(String contest_id) {
        this.contest_id = contest_id;
    }
}
