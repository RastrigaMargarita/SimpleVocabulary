package com.margretcraft.simplevocabulary.model.jsonModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SkyEng {
    @GET("api/public/v1/words/search")
    Call<RequestWord[]> loadWord(@Query("search") String word);

}
