package com.app.fevir.movie.list

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.Bind
import butterknife.ButterKnife
import com.app.fevir.R
import com.app.fevir.movie.detail.MovieDetailActivity
import com.app.fevir.movie.list.adapter.MovieListAdapter
import com.app.fevir.movie.list.component.DaggerMovieListComponent
import com.app.fevir.movie.list.domain.Card
import com.app.fevir.movie.list.module.MovieListModule
import com.app.fevir.movie.list.presenter.MovieListPresenter
import com.app.fevir.views.listener.RecyclerItemClickListener
import javax.inject.Inject

class MovieListFragment : Fragment(), MovieListPresenter.View {
    lateinit internal var movieListAdapter: MovieListAdapter
    lateinit internal var menu_title: String

    @Bind(R.id.lv_items)
    lateinit var itemListView: RecyclerView
    @Inject
    lateinit var movieListPresenter: MovieListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerMovieListComponent.builder().movieListModule(MovieListModule(this)).build().inject(this)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val i = arguments.getInt(ARG_MENU_NUMBER)
        menu_title = resources.getStringArray(R.array.menu_array)[i]


        movieListAdapter = MovieListAdapter(activity)
        movieListAdapter.setRecyclerItemClickListener(object : RecyclerItemClickListener {
            override fun onItemClick(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>, position: Int) {
                val card = (adapter as MovieListAdapter).getItem(position)
                val intent = Intent(activity, MovieDetailActivity::class.java)
                intent.putExtra(MovieDetailActivity.CARD_PROFILE, card.profileImage)
                intent.putExtra(MovieDetailActivity.CARD_NAME, card.name)
                intent.putExtra(MovieDetailActivity.CARD_TIME, card.updatedTime)
                intent.putExtra(MovieDetailActivity.CARD_PICTURE, card.picture)
                intent.putExtra(MovieDetailActivity.CARD_DESCRIPTION, card.description)
                intent.putExtra(MovieDetailActivity.CARD_SOURCE, card.source)
                intent.putExtra(MovieDetailActivity.CARD_ID, card.id)

                startActivity(intent)
            }
        })
        itemListView.layoutManager = LinearLayoutManager(activity)
        itemListView.adapter = movieListAdapter
        itemListView.addOnScrollListener(EndlessScrollListener(5))

        movieListPresenter.onLoadInfo(menu_title)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater!!.inflate(R.layout.fadong_main, container, false)
        ButterKnife.bind(this, rootView)
        return rootView
    }

    override fun addCard(card: Card) {
        movieListAdapter.add(card)
        movieListAdapter.notifyDataSetChanged()
    }

    inner class EndlessScrollListener(visibleThreshold: Int) : RecyclerView.OnScrollListener() {

        private var visibleThreshold = 3

        init {
            this.visibleThreshold = visibleThreshold
        }

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            val layoutManager = recyclerView!!.layoutManager as LinearLayoutManager
            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            val itemCount = recyclerView.adapter.itemCount

            if (itemCount - lastVisibleItemPosition <= visibleThreshold) {
                movieListPresenter.onLoadInfo(menu_title)
            }
        }

    }

    companion object {

        val ARG_MENU_NUMBER = "menu_number"

        fun newInstance(position: Int): Fragment {
            val fragment = MovieListFragment()
            val args = Bundle()
            args.putInt(MovieListFragment.ARG_MENU_NUMBER, position)
            fragment.arguments = args
            return fragment
        }
    }
}


