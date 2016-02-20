package com.app.fevir.movie.list.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.Bind
import butterknife.ButterKnife
import com.app.fevir.R
import com.app.fevir.movie.list.domain.Card
import com.app.fevir.util.picaso.CircleTransform
import com.app.fevir.views.listener.RecyclerItemClickListener
import com.squareup.picasso.Picasso
import java.util.*

/**
 * Created by 1000742 on 15. 1. 5..
 */
internal class MovieListAdapter(private val context: Context) : RecyclerView.Adapter<MovieListAdapter.CardHolder>() {
    lateinit  var cardList: MutableList<Card>
    private var recyclerItemClickListener: RecyclerItemClickListener? = null

    init {
        cardList = ArrayList()
    }

    fun add(card: Card) {
        cardList.add(card)
    }

    fun getItem(position: Int): Card = cardList.get(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.fragment_facebook, parent, false)
        val adapterHolder = CardHolder(v)

        ButterKnife.bind(adapterHolder, v)

        return adapterHolder
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {

        val card = getItem(position)

        holder.name.text = card.name
        holder.description.text = card.description
        holder.time.text = card.updatedTime

        Picasso.with(context).load(card.profileImage).transform(CircleTransform()).into(holder.profile)
        Picasso.with(context).load(card.picture).into(holder.picture)

        holder.faList.setOnClickListener { v ->
            if (recyclerItemClickListener != null) {
                recyclerItemClickListener!!.onItemClick(this@MovieListAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>, position)
            }
        }
    }

    override fun getItemCount(): Int = cardList.size

    fun setRecyclerItemClickListener(recyclerItemClickListener: RecyclerItemClickListener) {
        this.recyclerItemClickListener = recyclerItemClickListener
    }

    internal class CardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @Bind(R.id.fa_name)
        lateinit var name: TextView
        @Bind(R.id.fa_profile)
        lateinit var profile: ImageView
        @Bind(R.id.fa_picture)
        lateinit var picture: ImageView
        @Bind(R.id.fa_description)
        lateinit var description: TextView
        @Bind(R.id.fa_time)
        lateinit var time: TextView
        @Bind(R.id.fa_list)
        lateinit var faList: LinearLayout
    }

}
