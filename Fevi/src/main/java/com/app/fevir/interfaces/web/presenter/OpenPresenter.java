package com.app.fevir.interfaces.web.presenter;

import com.app.fevir.movie.list.domain.Card;

import rx.functions.Action1;

public interface OpenPresenter {

    void onInit(String lastPath, Action1<? super Card> subscribe);

    interface View {

        void showDetail(Card hashId);

        void showHome();
    }
}
