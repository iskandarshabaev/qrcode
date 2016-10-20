package com.ishabaev.qrcodes.api;


import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

public interface Api {

    @GET
    Observable<Response<ResponseBody>> makeRequest(@Url String url);

}
