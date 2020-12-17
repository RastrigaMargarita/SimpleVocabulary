package com.margretcraft.simplevocabulary.model.jsonModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Meaning {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("partOfSpeechCode")
    @Expose
    private String partOfSpeechCode;
    @SerializedName("translation")
    @Expose
    private Translation translation;
    @SerializedName("previewUrl")
    @Expose
    private String previewUrl;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("transcription")
    @Expose
    private String transcription;
    @SerializedName("soundUrl")
    @Expose
    private String soundUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPartOfSpeechCode() {
        return partOfSpeechCode;
    }

    public void setPartOfSpeechCode(String partOfSpeechCode) {
        this.partOfSpeechCode = partOfSpeechCode;
    }

    public Translation getTranslation() {
        return translation;
    }

    public void setTranslation(Translation translation) {
        this.translation = translation;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTranscription() {
        return transcription;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    public String getSoundUrl() {
        return soundUrl;
    }

    public void setSoundUrl(String soundUrl) {
        this.soundUrl = soundUrl;
    }

}

