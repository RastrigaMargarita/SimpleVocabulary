package com.margretcraft.simplevocabulary.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Word {
    private String word;
    private String translate;
    private String examples;
    private int repiting;
    private String pathToSound;
    @PrimaryKey(autoGenerate = true)
    private int _ID;

    @Ignore
    public Word(String word, String translate, String examples, int repiting, String pathToSound, int ID) {
        this.word = word;
        this.translate = translate;
        this.examples = examples;
        this.repiting = repiting;
        this.pathToSound = pathToSound;
    }
    public Word(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public String getExamples() {
        return examples;
    }

    public void setExamples(String examples) {
        this.examples = examples;
    }

    public int getRepiting() {
        return repiting;
    }

    public void setRepiting(int repiting) {
        this.repiting = repiting;
    }

    public String getPathToSound() {
        return pathToSound;
    }

    public void setPathToSound(String pathToSound) {
        this.pathToSound = pathToSound;
    }

    public int getID() {
        return _ID;
    }

    public void setID(int ID) {
        this._ID = ID;
    }

}
