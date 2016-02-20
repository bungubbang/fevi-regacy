package com.app.fevir.interfaces.web

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.app.fevir.MainActivity
import com.app.fevir.interfaces.web.di.DaggerOpenComponent
import com.app.fevir.interfaces.web.di.OpenModule
import com.app.fevir.interfaces.web.presenter.OpenPresenter
import com.app.fevir.movie.detail.MovieDetailActivity
import com.app.fevir.movie.list.domain.Card

import javax.inject.Inject

class OpenActivity : AppCompatActivity(), OpenPresenter.View {

    @Inject
    lateinit var openPresenter: OpenPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerOpenComponent.builder().openModule(OpenModule(this)).build().inject(this)

        val intent = intent

        if (intent.flags and Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY != 0) {
            showHome()
            return
        }

        val lastPath = intent.data.fragment

        openPresenter?.onInit(lastPath)

    }

    override fun showDetail(card: Card) {
        val mainIntent = Intent(this@OpenActivity, MainActivity::class.java)
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        val detailIntent = Intent(this@OpenActivity, MovieDetailActivity::class.java)
        detailIntent.putExtra(MovieDetailActivity.CARD_PROFILE, card.profileImage)
        detailIntent.putExtra(MovieDetailActivity.CARD_NAME, card.name)
        detailIntent.putExtra(MovieDetailActivity.CARD_TIME, card.updatedTime)
        detailIntent.putExtra(MovieDetailActivity.CARD_PICTURE, card.picture)
        detailIntent.putExtra(MovieDetailActivity.CARD_DESCRIPTION, card.description)
        detailIntent.putExtra(MovieDetailActivity.CARD_SOURCE, card.source)
        detailIntent.putExtra(MovieDetailActivity.CARD_ID, card.id)

        startActivities(arrayOf(mainIntent, detailIntent))

    }

    override fun showHome() {
        val mainIntent = Intent(this@OpenActivity, MainActivity::class.java)
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(mainIntent)
    }
}
