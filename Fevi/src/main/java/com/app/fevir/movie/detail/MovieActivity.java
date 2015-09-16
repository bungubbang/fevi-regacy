package com.app.fevir.movie.detail;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.app.fevir.R;
import com.app.fevir.deligate.MyApplication;
import com.app.fevir.movie.list.domain.Card;
import com.app.fevir.util.picaso.CircleTransform;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;


public class MovieActivity extends AppCompatActivity {

    public static final String CARD_NAME = "CARD_NAME";
    public static final String CARD_TIME = "CARD_TIME";
    public static final String CARD_PROFILE = "CARD_PROFILE";
    public static final String CARD_PICTURE = "CARD_PICTURE";
    public static final String CARD_DESCRIPTION = "CARD_DESCRIPTION";
    public static final String CARD_SOURCE = "CARD_SOURCE";

    @Bind(R.id.profile_layout)
    View vgTitle;
    @Bind(R.id.vg_description)
    View vgDescription;
    @Bind(R.id.fa_name)
    TextView tvName;
    @Bind(R.id.fa_time)
    TextView tvTime;
    @Bind(R.id.fa_description)
    TextView tvDescription;
    @Bind(R.id.fa_profile)
    ImageView ivProfile;
    @Bind(R.id.fa_picture)
    VideoView videoView;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    private Card card;

    private boolean landscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);


        Tracker tracker = ((MyApplication) getApplication()).getTracker();
        tracker.setScreenName("VideoView");


        card = getCardFromIntent(getIntent());

        tvName.setText(card.getName());
        tvTime.setText(card.getCreatedTime());
        tvDescription.setText(card.getDescription());

        Picasso.with(this).load(card.getProfileImage()).transform(new CircleTransform()).into(ivProfile);

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

        videoView.setOnCompletionListener(mp -> {
            if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });

        setupActionbar();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            Observable.just(1)
                    .delay(3, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(integer -> {
                        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            setFullScreen();
                        }
                    });
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
            vgTitle.setVisibility(View.GONE);
            vgDescription.setVisibility(View.GONE);
            setFullScreen();
            ViewGroup.LayoutParams layoutParams = videoView.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            videoView.setLayoutParams(layoutParams);
        } else {
            getSupportActionBar().show();
            vgTitle.setVisibility(View.VISIBLE);
            vgDescription.setVisibility(View.VISIBLE);
            setNormalScreen();
            ViewGroup.LayoutParams layoutParams = videoView.getLayoutParams();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200f, getResources().getDisplayMetrics());
            videoView.setLayoutParams(layoutParams);
        }
    }

    private void setNormalScreen() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    private void setFullScreen() {


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return;
        }


        int uiFlag = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

            uiFlag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            uiFlag |= View.SYSTEM_UI_FLAG_IMMERSIVE;
        }

        getWindow().getDecorView().setSystemUiVisibility(uiFlag);
    }

    private void setupActionbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_movie_detail);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(card.getName());
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setIcon(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onBackPressed() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        if (!landscape) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        landscape = !landscape;
    }

}
