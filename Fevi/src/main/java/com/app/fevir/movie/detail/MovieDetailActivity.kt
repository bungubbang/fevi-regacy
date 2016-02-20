package com.app.fevir.movie.detail

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.TypedValue
import android.view.*
import android.widget.*
import butterknife.Bind
import butterknife.ButterKnife
import butterknife.OnClick
import com.app.fevir.R
import com.app.fevir.deligate.MyApplication
import com.app.fevir.movie.detail.di.DaggerMovieDetailComponent
import com.app.fevir.movie.detail.di.MovieDetailModule
import com.app.fevir.movie.detail.presenter.MovieDetailPresenter
import com.app.fevir.movie.list.domain.Card
import com.app.fevir.util.picaso.CircleTransform
import com.squareup.picasso.Picasso
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MovieDetailActivity : AppCompatActivity(), MovieDetailPresenter.View {

    @Bind(R.id.profile_layout)
    lateinit var vgTitle: View
    @Bind(R.id.vg_description)
    lateinit var vgDescription: View
    @Bind(R.id.fa_name)
    lateinit var tvName: TextView
    @Bind(R.id.fa_time)
    lateinit var tvTime: TextView
    @Bind(R.id.fa_description)
    lateinit var tvDescription: TextView
    @Bind(R.id.fa_profile)
    lateinit var ivProfile: ImageView
    @Bind(R.id.fa_picture)
    lateinit var videoView: VideoView
    @Bind(R.id.progressBar)
    lateinit var progressBar: ProgressBar
    @Inject
    lateinit var movieDetailPresenter: MovieDetailPresenter
    private var card: Card? = null
    private var landscape: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_movie)
        ButterKnife.bind(this)

        DaggerMovieDetailComponent.builder().movieDetailModule(MovieDetailModule(this)).build().inject(this)

        val tracker = (application as MyApplication).tracker
        tracker?.setScreenName("VideoView")


        card = movieDetailPresenter.getCardFromIntent(intent)

        tvName.text = card!!.name
        tvTime.text = card!!.createdTime
        tvDescription.text = card!!.description

        Picasso.with(this).load(card!!.profileImage).transform(CircleTransform()).into(ivProfile)

        val mc = MediaController(this)
        mc.setAnchorView(videoView)

        videoView.setVideoURI(Uri.parse(card!!.source))
        videoView.setMediaController(mc)

        videoView.setBackgroundColor(Color.BLACK)

        videoView.setOnPreparedListener { mp ->
            progressBar.visibility = View.GONE
            videoView.start()
            videoView.setBackgroundColor(Color.TRANSPARENT)
        }

        videoView.setOnErrorListener { mp1, what, extra ->
            progressBar.visibility = View.GONE
            Toast.makeText(this@MovieDetailActivity, "Error", Toast.LENGTH_SHORT).show()
            false
        }

        videoView.setOnCompletionListener { mp ->
            if (resources.configuration.orientation != Configuration.ORIENTATION_PORTRAIT) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        }

        setupActionbar()

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {

        menu.clear()
        menuInflater.inflate(R.menu.menu_movie_detail, menu)
        return true
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_UP) {
            Observable.just(1).delay(3, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe { integer ->
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setFullScreen()
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            supportActionBar!!.hide()
            vgTitle.visibility = View.GONE
            vgDescription.visibility = View.GONE
            setFullScreen()
            val layoutParams = videoView.layoutParams
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            videoView.layoutParams = layoutParams
        } else {
            supportActionBar!!.show()
            vgTitle.visibility = View.VISIBLE
            vgDescription.visibility = View.VISIBLE
            setNormalScreen()
            val layoutParams = videoView.layoutParams
            layoutParams.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200f, resources.displayMetrics).toInt()
            videoView.layoutParams = layoutParams
        }
    }

    private fun setNormalScreen() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }

    private fun setFullScreen() {


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return
        }


        var uiFlag = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

            uiFlag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            uiFlag = uiFlag or View.SYSTEM_UI_FLAG_IMMERSIVE
        }

        window.decorView.systemUiVisibility = uiFlag
    }

    private fun setupActionbar() {
        val toolbar = findViewById(R.id.toolbar_movie_detail) as Toolbar
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.title = card!!.name
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayUseLogoEnabled(false)
        actionBar?.setIcon(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onBackPressed() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        } else if (item.itemId == R.id.action_share) {
            movieDetailPresenter.onShare(card!!.name, card!!.id)
        } else if (item.itemId == R.id.action_open_web) {
            movieDetailPresenter.onViewWeb(card!!.id)
        }

        return super.onOptionsItemSelected(item)
    }

    @OnClick(R.id.fullscreen_icon)
    fun onFullScreenClick() {
        // 미디어 인텐트 호출
        if (!landscape) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        landscape = !landscape
    }

    override fun sendFacebookLink(shareMsg: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("text/plain")
        intent.putExtra(Intent.EXTRA_TEXT, shareMsg)
        startActivity(Intent.createChooser(intent, getString(R.string.share)))
    }

    override fun sendWeb(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        val chooser = Intent.createChooser(intent, getString(R.string.open_browser))
        startActivity(chooser)
    }

    companion object {

        val CARD_NAME = "CARD_NAME"
        val CARD_TIME = "CARD_TIME"
        val CARD_PROFILE = "CARD_PROFILE"
        val CARD_PICTURE = "CARD_PICTURE"
        val CARD_DESCRIPTION = "CARD_DESCRIPTION"
        val CARD_SOURCE = "CARD_SOURCE"
        val CARD_ID = "CARD_ID"
    }
}
