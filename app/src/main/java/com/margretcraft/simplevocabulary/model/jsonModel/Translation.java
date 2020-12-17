package com.margretcraft.simplevocabulary.model.jsonModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Translation {
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("note")
    @Expose
    private String note;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

