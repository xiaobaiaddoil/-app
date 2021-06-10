package com.example.myapplication.Bean;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;

public class WordInfo {

    private String translate;
    private String us_phonetic;
    private String uk_phonetic;
    private Map<String, JSONArray> Word;
    private Map<String, String> wfs;

    public Map<String, String> getWfs() {
        return wfs;
    }

    public void setWfs(Map<String, String> wfs) {
        this.wfs = wfs;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public String getUs_phonetic() {
        return us_phonetic;
    }

    public void setUs_phonetic(String us_phonetic) {
        this.us_phonetic = us_phonetic;
    }

    public String getUk_phonetic() {
        return uk_phonetic;
    }

    public void setUk_phonetic(String uk_phonetic) {
        this.uk_phonetic = uk_phonetic;
    }

    public Map<String, JSONArray> getWord() {
        return Word;
    }

    public void setWord(Map<String,JSONArray> word) {
        Word = word;
    }
}