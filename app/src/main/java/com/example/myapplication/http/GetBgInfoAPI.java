package com.example.myapplication.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GetBgInfoAPI {
    @GET
    Call<ResponseBody> getJson(@Url String url);
}
