package com.app.fevir.movie.detail.presenter;

import android.content.Intent;

import com.app.fevir.movie.list.domain.Card;

public interface MovieDetailPresenter {

    void onShare(String name, String facebookId);

    Card getCardFromIntent(Intent intent);

    void onViewWeb(String id);

    interface View {

        void shareWebLink(String shareMsg);

        void moveWeb(String url);
    }
}
