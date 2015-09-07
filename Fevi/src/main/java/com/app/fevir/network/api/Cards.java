package com.app.fevir.network.api;

import com.app.fevir.network.domain.CardInfo;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface Cards {

    @GET("/api/card")
    Call<CardInfo> getCardInfo(@Query("category") String menu, @Query("page") int page);
}
