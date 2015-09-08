package com.app.fevir.movie.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.fevir.R;
import com.app.fevir.movie.list.adapter.FaAdapter;
import com.app.fevir.movie.list.component.DaggerMovieListComponent;
import com.app.fevir.movie.list.component.MovieListComponent;
import com.app.fevir.movie.list.domain.Card;
import com.app.fevir.movie.list.module.MovieListModule;
import com.app.fevir.movie.list.presenter.MovieListPresenter;
import com.app.fevir.network.api.Cards;
import com.app.fevir.network.domain.CardInfo;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by 1000742 on 15. 1. 5..
 */
public class MovieListFragment extends Fragment implements MovieListPresenter.View {

    public static final String ARG_MENU_NUMBER = "menu_number";
    public static final String API_URL = "http://munsangdong.cafe24.com";
    FaAdapter faAdapter;
    String menu_title;
    @Bind(R.id.lv_items)
    RecyclerView itemListView;
    @Inject
    MovieListPresenter movieListPresenter;
    private int currentPage = 0;
    private PublishSubject<Pair<String, Integer>> requestPublisher;

    public MovieListFragment() { }

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
        MovieListComponent movieListComponent = DaggerMovieListComponent.builder()
                .movieListModule(new MovieListModule(this)).build();
        movieListComponent.inject(this);

        movieListPresenter.log();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int i = getArguments().getInt(ARG_MENU_NUMBER);
        menu_title = getResources().getStringArray(R.array.menu_array)[i];


        faAdapter = new FaAdapter(getActivity());
        itemListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        itemListView.setAdapter(faAdapter);
        itemListView.addOnScrollListener(new EndlessScrollListener(5));

        initRequestPublisher();
        requestPublisher.onNext(new Pair<>(menu_title, currentPage));
    }

    private void initRequestPublisher() {
        requestPublisher = PublishSubject.create();
        requestPublisher
                .onBackpressureBuffer()
                .observeOn(Schedulers.io())
                .subscribe(requestInfo -> {
                    Call<CardInfo> cardInfoCall = new Retrofit.Builder()
                            .baseUrl(API_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                            .create(Cards.class)
                            .getCardInfo(requestInfo.first, requestInfo.second);

                    try {
                        List<Card> content = cardInfoCall.execute().body().getContent();
                        Observable.from(content)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(card -> {
                                    faAdapter.add(card);
                                    faAdapter.notifyDataSetChanged();
                                });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }, Throwable::printStackTrace);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fadong_main, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
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
                currentPage++;
                requestPublisher.onNext(new Pair<>(menu_title, currentPage));
            }
        }

    }
}


