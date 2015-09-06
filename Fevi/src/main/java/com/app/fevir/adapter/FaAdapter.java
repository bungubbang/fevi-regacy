package com.app.fevir.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.fevir.MovieActivity;
import com.app.fevir.R;
import com.app.fevir.adapter.dto.Card;
import com.app.fevir.support.CircleTransform;
import com.app.fevir.support.ContextString;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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

        adapterHolder.name = (TextView) v.findViewById(R.id.fa_name);
        adapterHolder.description = (TextView) v.findViewById(R.id.fa_description);
        adapterHolder.profile = (ImageView) v.findViewById(R.id.fa_profile);
        adapterHolder.picture = (ImageView) v.findViewById(R.id.fa_picture);
        adapterHolder.time = (TextView) v.findViewById(R.id.fa_time);
        adapterHolder.faList = (LinearLayout) v.findViewById(R.id.fa_list);


        return adapterHolder;
    }

    @Override
    public void onBindViewHolder(CardAdapterHolder holder, int position) {

        Card card = getItem(position);

        holder.name.setText(card.getName());
        holder.description.setText(card.getDescription());
        holder.time.setText(card.getUpdated_time());

        Picasso.with(context).load(card.getProfile_image()).transform(new CircleTransform()).into(holder.profile);
        Picasso.with(context).load(card.getPicture()).into(holder.picture);

        holder.faList.setOnClickListener(new DetailClickListener(card));
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    static class CardAdapterHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView profile;
        ImageView picture;
        TextView description;
        TextView time;
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
            intent.putExtra(ContextString.cardProfile, card.getProfile_image());
            intent.putExtra(ContextString.cardName, card.getName());
            intent.putExtra(ContextString.cardTime, card.getUpdated_time());
            intent.putExtra(ContextString.cardPicture, card.getPicture());
            intent.putExtra(ContextString.cardDescription, card.getDescription());
            intent.putExtra(ContextString.cardSource, card.getSource());

            v.getContext().startActivity(intent);
        }
    }
}
