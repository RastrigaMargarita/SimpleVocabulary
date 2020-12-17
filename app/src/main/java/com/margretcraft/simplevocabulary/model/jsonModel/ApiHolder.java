package com.margretcraft.simplevocabulary.model.jsonModel;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiHolder {
    private final SkyEng skyEng;

    public ApiHolder() {
        Retrofit retrofit;

        retrofit = new Retrofit.Builder()
                .baseUrl("https://dictionary.skyeng.ru/")
                .addConverterFactory(GsonConverterFactory.create(gson()))
                .build();
        skyEng = retrofit.create(SkyEng.class);

    }

    private Gson gson() {
        return new GsonBuilder().setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithoutExposeAnnotation().create();
    }

    public SkyEng getSkyEng() {
        return skyEng;
    }
}
