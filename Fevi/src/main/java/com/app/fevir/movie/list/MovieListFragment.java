package com.app.fevir.movie.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.fevir.R;
import com.app.fevir.movie.detail.MovieDetailActivity;
import com.app.fevir.movie.list.adapter.MovieListAdapter;
import com.app.fevir.movie.list.component.DaggerMovieListComponent;
import com.app.fevir.movie.list.domain.Card;
import com.app.fevir.movie.list.module.MovieListModule;
import com.app.fevir.movie.list.presenter.MovieListPresenter;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieListFragment extends Fragment implements MovieListPresenter.View {

    public static final String ARG_MENU_NUMBER = "menu_number";
    MovieListAdapter movieListAdapter;
    String menu_title;

    @Bind(R.id.lv_items)
    RecyclerView itemListView;
    @Inject
    MovieListPresenter movieListPresenter;

    public static Fragment newInstance(int position) {
        Fragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putInt(MovieListFragment.ARG_MENU_NUMBER, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerMovieListComponent.builder()
                .movieListModule(new MovieListModule(this))
                .build()
                .inject(this);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int i = getArguments().getInt(ARG_MENU_NUMBER);
        menu_title = getResources().getStringArray(R.array.menu_array)[i];


        movieListAdapter = new MovieListAdapter(getActivity());
        movieListAdapter.setRecyclerItemClickListener((adapter, position) -> {
            Card card = ((MovieListAdapter) adapter).getItem(position);
            Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.CARD_PROFILE, card.getProfileImage());
            intent.putExtra(MovieDetailActivity.CARD_NAME, card.getName());
            intent.putExtra(MovieDetailActivity.CARD_TIME, card.getUpdatedTime());
            intent.putExtra(MovieDetailActivity.CARD_PICTURE, card.getPicture());
            intent.putExtra(MovieDetailActivity.CARD_DESCRIPTION, card.getDescription());
            intent.putExtra(MovieDetailActivity.CARD_SOURCE, card.getSource());
            intent.putExtra(MovieDetailActivity.CARD_ID, card.getId());

            startActivity(intent);
        });
        itemListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        itemListView.setAdapter(movieListAdapter);
        itemListView.addOnScrollListener(new EndlessScrollListener(5));

        movieListPresenter.onLoadInfo(menu_title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fadong_main, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void addCard(Card card) {
        movieListAdapter.add(card);
        movieListAdapter.notifyDataSetChanged();
    }

    public class EndlessScrollListener extends RecyclerView.OnScrollListener {

        private int visibleThreshold = 3;

        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            int itemCount = recyclerView.getAdapter().getItemCount();

            if (itemCount - lastVisibleItemPosition <= visibleThreshold) {
                movieListPresenter.onLoadInfo(menu_title);
            }
        }

    }
}


