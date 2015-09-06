package com.app.fevir.movie.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.fevir.R;
import com.app.fevir.adapter.FaAdapter;
import com.app.fevir.adapter.dto.Card;
import com.app.fevir.http.MenuApi;
import com.app.fevir.http.domain.CardInfo;

import java.io.IOException;
import java.util.List;

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
public class FacebookFragment extends Fragment {

    public static final String ARG_MENU_NUMBER = "menu_number";
    public static final String API_URL = "http://munsangdong.cafe24.com";
    FaAdapter faAdapter;
    String menu_title;
    private int currentPage = 0;
    private PublishSubject<Pair<String, Integer>> requestPublisher;

    public FacebookFragment() { }

    public static Fragment newInstance(int position) {
        Fragment fragment = new FacebookFragment();
        Bundle args = new Bundle();
        args.putInt(FacebookFragment.ARG_MENU_NUMBER, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d("INFO", "onActivityCreated");
        initRequestPublisher();
        requestPublisher.onNext(new Pair<>(menu_title, currentPage));
    }

    private void initRequestPublisher() {
        requestPublisher = PublishSubject.create();
        requestPublisher
                .onBackpressureBuffer()
                .observeOn(Schedulers.io())
                .subscribe(requestInfo -> {
                    Log.d("INFO", "hahahahah");
                    Call<CardInfo> cardInfoCall = new Retrofit.Builder()
                            .baseUrl(API_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                            .create(MenuApi.class)
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

        int i = getArguments().getInt(ARG_MENU_NUMBER);
        menu_title = getResources().getStringArray(R.array.menu_array)[i];

        RecyclerView itemListView = (RecyclerView) rootView.findViewById(R.id.lv_items);


        faAdapter = new FaAdapter(getActivity());
        itemListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        itemListView.setAdapter(faAdapter);
        itemListView.addOnScrollListener(new EndlessScrollListener(5));
        Log.d("INFO", "onCreateView");


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


