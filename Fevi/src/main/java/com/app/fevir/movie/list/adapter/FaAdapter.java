package com.app.fevir.movie.list.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.fevir.R;
import com.app.fevir.movie.detail.MovieActivity;
import com.app.fevir.movie.list.domain.Card;
import com.app.fevir.util.picaso.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 1000742 on 15. 1. 5..
 */
public class FaAdapter extends RecyclerView.Adapter<FaAdapter.CardAdapterHolder> {

    private Context context;
    private List<Card> cardList;

    public FaAdapter(Context context) {
        super();
        this.context = context;
        cardList = new ArrayList<>();
    }

    public void add(Card card) {
        cardList.add(card);
    }

    public Card getItem(int position) {
        return cardList.get(position);
    }

    @Override
    public CardAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.fragment_facebook, parent, false);
        CardAdapterHolder adapterHolder = new CardAdapterHolder(v);

        ButterKnife.bind(adapterHolder, v);

        return adapterHolder;
    }

    @Override
    public void onBindViewHolder(CardAdapterHolder holder, int position) {

        Card card = getItem(position);

        holder.name.setText(card.getName());
        holder.description.setText(card.getDescription());
        holder.time.setText(card.getUpdatedTime());

        Picasso.with(context).load(card.getProfileImage()).transform(new CircleTransform()).into(holder.profile);
        Picasso.with(context).load(card.getPicture()).into(holder.picture);

        holder.faList.setOnClickListener(new DetailClickListener(card));
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    static class CardAdapterHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.fa_name)
        TextView name;
        @Bind(R.id.fa_profile)
        ImageView profile;
        @Bind(R.id.fa_picture)
        ImageView picture;
        @Bind(R.id.fa_description)
        TextView description;
        @Bind(R.id.fa_time)
        TextView time;
        @Bind(R.id.fa_list)
        LinearLayout faList;

        public CardAdapterHolder(View itemView) {
            super(itemView);
        }
    }

    private class DetailClickListener implements TextView.OnClickListener {

        private Card card;

        private DetailClickListener(Card card) {
            this.card = card;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), MovieActivity.class);
            intent.putExtra(MovieActivity.CARD_PROFILE, card.getProfileImage());
            intent.putExtra(MovieActivity.CARD_NAME, card.getName());
            intent.putExtra(MovieActivity.CARD_TIME, card.getUpdatedTime());
            intent.putExtra(MovieActivity.CARD_PICTURE, card.getPicture());
            intent.putExtra(MovieActivity.CARD_DESCRIPTION, card.getDescription());
            intent.putExtra(MovieActivity.CARD_SOURCE, card.getSource());

            v.getContext().startActivity(intent);
        }
    }
}
