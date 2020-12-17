package com.margretcraft.simplevocabulary.model.jsonModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestWord {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("meanings")
    @Expose
    private Meaning[] meanings = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Meaning[] getMeanings() {
        return meanings;
    }

    public void setMeanings(Meaning[] meanings) {
        this.meanings = meanings;
    }
}
