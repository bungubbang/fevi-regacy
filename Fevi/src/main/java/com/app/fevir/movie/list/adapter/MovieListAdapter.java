package com.app.fevir.movie.list.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.fevir.movie.list.domain.Card;
import com.app.fevir.util.picaso.CircleTransform;
import com.app.fevir.views.listener.RecyclerItemClickListener;
import com.squareup.picasso.Picasso;
import com.vikicast.app.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 1000742 on 15. 1. 5..
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.CardHolder> {

    private Context context;
    private List<Card> cardList;
    private RecyclerItemClickListener recyclerItemClickListener;

    public MovieListAdapter(Context context) {
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
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.fragment_facebook, parent, false);
        CardHolder adapterHolder = new CardHolder(v);

        ButterKnife.bind(adapterHolder, v);

        return adapterHolder;
    }

    @Override
    public void onBindViewHolder(CardHolder holder, int position) {

        Card card = getItem(position);

        holder.name.setText(card.getName());
        holder.description.setText(card.getDescription());
        holder.time.setText(card.getUpdatedTime());

        Picasso.with(context).load(card.getProfileImage()).transform(new CircleTransform()).into(holder.profile);
        Picasso.with(context).load(card.getPicture()).into(holder.picture);

        holder.faList.setOnClickListener(v -> {
            if (recyclerItemClickListener != null) {
                recyclerItemClickListener.onItemClick(MovieListAdapter.this, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public void setRecyclerItemClickListener(RecyclerItemClickListener recyclerItemClickListener) {
        this.recyclerItemClickListener = recyclerItemClickListener;
    }

    static class CardHolder extends RecyclerView.ViewHolder {
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

        public CardHolder(View itemView) {
            super(itemView);
        }
    }

}
