package com.app.fevir.interfaces.web;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.app.fevir.MainActivity;
import com.app.fevir.interfaces.web.di.DaggerOpenComponent;
import com.app.fevir.interfaces.web.di.OpenModule;
import com.app.fevir.interfaces.web.presenter.OpenPresenter;
import com.app.fevir.movie.detail.MovieDetailActivity;
import com.app.fevir.movie.list.domain.Card;
import com.app.fevir.util.picaso.AnalyticsUtil;

import javax.inject.Inject;

public class OpenActivity extends AppCompatActivity implements OpenPresenter.View {

    @Inject
    OpenPresenter openPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerOpenComponent.builder()
                .openModule(new OpenModule(this))
                .build()
                .inject(this);

        Intent intent = getIntent();

        if ((intent.getFlags() & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0) {
            showHome();
            return;
        }

        String lastPath = intent.getData().getFragment();

        openPresenter.onInit(lastPath, this::showDetail, this::showHome);

    }

    @Override
    public void showDetail(Card card) {
        Intent mainIntent = new Intent(OpenActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent detailIntent = new Intent(OpenActivity.this, MovieDetailActivity.class);
        detailIntent.putExtra(MovieDetailActivity.CARD_PROFILE, card.getProfileImage());
        detailIntent.putExtra(MovieDetailActivity.CARD_NAME, card.getName());
        detailIntent.putExtra(MovieDetailActivity.CARD_TIME, card.getUpdatedTime());
        detailIntent.putExtra(MovieDetailActivity.CARD_PICTURE, card.getPicture());
        detailIntent.putExtra(MovieDetailActivity.CARD_DESCRIPTION, card.getDescription());
        detailIntent.putExtra(MovieDetailActivity.CARD_SOURCE, card.getSource());
        detailIntent.putExtra(MovieDetailActivity.CARD_ID, card.getId());

        startActivities(new Intent[]{mainIntent, detailIntent});

        AnalyticsUtil.sendEvent("open-link", "card", card.getId());

    }

    @Override
    public void showHome() {
        Intent mainIntent = new Intent(OpenActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);

        AnalyticsUtil.sendEvent("open-link", "home");

    }
}
