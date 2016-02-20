package com.app.fevir.movie.detail.presenter;

import android.content.Intent;

import com.app.fevir.consts.VikiCastConsts;
import com.app.fevir.movie.detail.MovieDetailActivity;
import com.app.fevir.movie.list.domain.Card;

import javax.inject.Inject;

public class MovieDetailPresenterImpl implements MovieDetailPresenter {
    private final View view;

    @Inject
    public MovieDetailPresenterImpl(View view) {
        this.view = view;
    }

    @Override
    public void onShare(String name, String facebookId) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(name)
                .append("\n")
                .append(VikiCastConsts.WEB_URL)
                .append("/#")
                .append(facebookId);

        view.sendFacebookLink(buffer.toString());
    }

    @Override
    public Card getCardFromIntent(Intent intent) {

        String cardName = intent.getStringExtra(MovieDetailActivity.Companion.getCARD_NAME());
        String cardTime = intent.getStringExtra(MovieDetailActivity.Companion.getCARD_TIME());
        String cardProfile = intent.getStringExtra(MovieDetailActivity.Companion.getCARD_PROFILE());
        String cardPicture = intent.getStringExtra(MovieDetailActivity.Companion.getCARD_PICTURE());
        String cardDescription = intent.getStringExtra(MovieDetailActivity.Companion.getCARD_DESCRIPTION());
        String cardSource = intent.getStringExtra(MovieDetailActivity.Companion.getCARD_SOURCE());
        String cardId = intent.getStringExtra(MovieDetailActivity.Companion.getCARD_ID());

        return new Card.Builder()
                .id(cardId)
                .name(cardName)
                .createdTime(cardTime)
                .profileImage(cardProfile)
                .picture(cardPicture)
                .description(cardDescription)
                .source(cardSource)
                .createCard();
    }

    @Override
    public void onViewWeb(String id) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(VikiCastConsts.WEB_URL)
                .append("/#")
                .append(id);

        view.sendWeb(buffer.toString());
    }
}
