package com.app.fevir.interfaces.web.presenter;

import com.app.fevir.interfaces.web.model.OpenModel;
import com.app.fevir.movie.list.domain.Card;
import com.app.fevir.network.api.Cards;

import javax.inject.Inject;

import retrofit.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class OpenPresenterImpl implements OpenPresenter {
    private final View view;
    private final OpenModel openModel;
    private final Retrofit retrofit;

    @Inject
    public OpenPresenterImpl(View view, OpenModel openModel, Retrofit retrofit) {
        this.view = view;
        this.openModel = openModel;
        this.retrofit = retrofit;
    }

    @Override
    public void onInit(String lastPath, Action1<? super Card> subscribe, Action1<Throwable> error) {
        if (openModel.containHashUrl(lastPath)) {

            retrofit.create(Cards.class)
                    .getCardInfo(lastPath)
                    .flatMap(cardInfo -> Observable.from(cardInfo.getContent()))
                    .first()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscribe, error);

        } else {
            view.showHome();
        }
    }
}
