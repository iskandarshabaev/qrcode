package com.ishabaev.qrcodes.model;


import android.support.annotation.NonNull;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class QrCode extends RealmObject {

    @PrimaryKey
    private String text;

    private int format;

    private long timestamp;

    private int statusCode;

    public QrCode() {
    }

    public QrCode(@NonNull String text, int format, long timestamp, int statusCode) {
        this.text = text;
        this.format = format;
        this.timestamp = timestamp;
        this.statusCode = statusCode;
    }

    @NonNull
    public String getText() {
        return text;
    }

    public int getFormat() {
        return format;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setText(@NonNull String text) {
        this.text = text;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
