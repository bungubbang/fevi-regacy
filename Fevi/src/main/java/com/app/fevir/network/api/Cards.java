package com.app.fevir.network.api;

import com.app.fevir.network.domain.CardInfo;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface Cards {

    @GET("/api/card")
    Observable<CardInfo> getCardsInfo(@Query("category") String menu, @Query("page") int page);

    @GET("/api/card")
    Observable<CardInfo> getCardInfo(@Query("id") String id);
}
