package com.app.fevir.http;

import com.app.fevir.http.domain.CardInfo;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface MenuApi {

    @GET("/api/card")
    Call<CardInfo> getCardInfo(@Query("category") String menu, @Query("page") int page);
}
