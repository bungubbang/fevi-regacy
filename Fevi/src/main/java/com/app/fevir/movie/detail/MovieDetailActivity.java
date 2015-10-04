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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.app.fevir.movie.detail.di.DaggerMovieDetailComponent;
import com.app.fevir.movie.detail.di.MovieDetailModule;
import com.app.fevir.movie.detail.presenter.MovieDetailPresenter;
import com.app.fevir.movie.list.domain.Card;
import com.app.fevir.util.picaso.AnalyticsUtil;
import com.app.fevir.util.picaso.CircleTransform;
import com.squareup.picasso.Picasso;
import com.vikicast.app.R;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;


public class MovieDetailActivity extends AppCompatActivity implements MovieDetailPresenter.View {

    public static final String CARD_NAME = "CARD_NAME";
    public static final String CARD_TIME = "CARD_TIME";
    public static final String CARD_PROFILE = "CARD_PROFILE";
    public static final String CARD_PICTURE = "CARD_PICTURE";
    public static final String CARD_DESCRIPTION = "CARD_DESCRIPTION";
    public static final String CARD_SOURCE = "CARD_SOURCE";
    public static final String CARD_ID = "CARD_ID";

    @Bind(R.id.profile_layout)
    View vgTitle;
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
    @Inject
    MovieDetailPresenter movieDetailPresenter;
    private Card card;
    private boolean landscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);

        DaggerMovieDetailComponent.builder()
                .movieDetailModule(new MovieDetailModule(this))
                .build()
                .inject(this);

        card = movieDetailPresenter.getCardFromIntent(getIntent());

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

        videoView.setOnErrorListener((mp1, what, extra) -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(MovieDetailActivity.this, "Error", Toast.LENGTH_SHORT).show();
            return false;
        });

        videoView.setOnCompletionListener(mp -> {
            if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            AnalyticsUtil.sendEvent("video completed", "app", card.getId());

        });

        setupActionbar();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        AnalyticsUtil.sendScreenName("VideoDetailView?id=" + card.getId());
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.clear();
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        return true;
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
            setFullScreen();
            ViewGroup.LayoutParams layoutParams = videoView.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            videoView.setLayoutParams(layoutParams);
        } else {
            getSupportActionBar().show();
            vgTitle.setVisibility(View.VISIBLE);
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
        } else if (item.getItemId() == R.id.action_share) {
            movieDetailPresenter.onShare(card.getName(), card.getId());
        } else if (item.getItemId() == R.id.action_open_web) {
            movieDetailPresenter.onViewWeb(card.getId());
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fullscreen_icon)
    void onFullScreenClick() {
        // 미디어 인텐트 호출
        if (!landscape) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            AnalyticsUtil.sendEvent("full-screen", "app", card.getId());
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        landscape = !landscape;
    }

    @Override
    public void shareWebLink(String shareMsg) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shareMsg);
        startActivity(Intent.createChooser(intent, getString(R.string.share)));
        AnalyticsUtil.sendEvent("shareLink", "app", card.getId());
    }

    @Override
    public void moveWeb(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        Intent chooser = Intent.createChooser(intent, getString(R.string.open_browser));
        startActivity(chooser);
        AnalyticsUtil.sendEvent("moveWeb", "app", card.getId());
    }
}
