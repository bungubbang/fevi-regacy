package com.app.fevir.movie.list.presenter;

import com.app.fevir.movie.list.domain.Card;

public interface MovieListPresenter {

    void onLoadInfo(String menu_title);

    interface View {

        void addCard(Card card);
    }
}
