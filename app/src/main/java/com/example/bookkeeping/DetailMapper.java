package com.example.bookkeeping;

import java.sql.Time;

public class DetailMapper {
    private Integer id;
    private double mount;
    private String comment;
    private String time;

    @Override
    public String toString() {
        return "DetailMapper{" +
                "id=" + id +
                ", mount=" + mount +
                ", comment='" + comment + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getMount() {
        return mount;
    }

    public void setMount(double mount) {
        this.mount = mount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
