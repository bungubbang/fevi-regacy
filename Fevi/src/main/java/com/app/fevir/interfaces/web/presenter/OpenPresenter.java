package com.app.fevir.interfaces.web.presenter;

import com.app.fevir.movie.list.domain.Card;

public interface OpenPresenter {

    void onInit(String lastPath);

    interface View {

        void showDetail(Card hashId);

        void showHome();
    }
}
