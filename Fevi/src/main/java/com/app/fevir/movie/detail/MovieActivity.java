package com.app.fevir.movie.detail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.app.fevir.MyApplication;
import com.app.fevir.R;
import com.app.fevir.movie.list.domain.Card;
import com.app.fevir.util.picaso.CircleTransform;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MovieActivity extends Activity {

    public static final String CARD_NAME = "CARD_NAME";
    public static final String CARD_TIME = "CARD_TIME";
    public static final String CARD_PROFILE = "CARD_PROFILE";
    public static final String CARD_PICTURE = "CARD_PICTURE";
    public static final String CARD_DESCRIPTION = "CARD_DESCRIPTION";
    public static final String CARD_SOURCE = "CARD_SOURCE";

    @Bind(R.id.fa_name)
    TextView name;
    @Bind(R.id.fa_time)
    TextView time;
    @Bind(R.id.fa_description)
    TextView description;
    @Bind(R.id.fa_profile)
    ImageView profile;
    @Bind(R.id.fa_picture)
    VideoView videoView;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;


    private Card card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);
        // TODO
        // Make sure that Analytics tracking has started
        // Get the tracker
        Tracker tracker = ((MyApplication) getApplication()).getTracker();

        // Set screen name
        tracker.setScreenName("VideoView");


        card = getCardFromIntent(getIntent());

        name.setText(card.getName());
        time.setText(card.getCreatedTime());
        description.setText(card.getDescription());

        Picasso.with(this).load(card.getProfileImage()).transform(new CircleTransform()).into(profile);

        MediaController mc = new MediaController(this);
        mc.setAnchorView(videoView);

        videoView.setVideoURI(Uri.parse(card.getSource()));
        videoView.setMediaController(mc);

        videoView.setBackgroundColor(Color.BLACK);

        videoView.setOnPreparedListener(mp -> {
            progressBar.setVisibility(View.GONE);
            videoView.start();
            videoView.setBackgroundColor(Color.TRANSPARENT);
        });

    }

    private Card getCardFromIntent(Intent intent) {

        String cardName = intent.getStringExtra(CARD_NAME);
        String cardTime = intent.getStringExtra(CARD_TIME);
        String cardProfile = intent.getStringExtra(CARD_PROFILE);
        String cardPicture = intent.getStringExtra(CARD_PICTURE);
        String cardDescription = intent.getStringExtra(CARD_DESCRIPTION);
        String cardSource = intent.getStringExtra(CARD_SOURCE);

        return new Card.Builder()
                .name(cardName)
                .createdTime(cardTime)
                .profileImage(cardProfile)
                .picture(cardPicture)
                .description(cardDescription)
                .source(cardSource)
                .createCard();
    }

    @OnClick(R.id.fullscreen_icon)
    void onFullScreenClick() {
        // 미디어 인텐트 호출
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(card.getSource()), "video/*");
        startActivity(intent);
    }

    @OnClick(R.id.closeButton)
    void onCloseClick() {
        finish();
    }
}
