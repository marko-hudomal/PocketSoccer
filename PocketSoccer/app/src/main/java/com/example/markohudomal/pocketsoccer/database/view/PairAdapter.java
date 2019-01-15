package com.example.markohudomal.pocketsoccer.database.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.markohudomal.pocketsoccer.R;
import com.example.markohudomal.pocketsoccer.StatisticsActivity;
import com.example.markohudomal.pocketsoccer.StatisticsDuelActivity;
import com.example.markohudomal.pocketsoccer.database.Pair;
import com.example.markohudomal.pocketsoccer.extras.StaticValues;

import java.util.List;

public class PairAdapter extends RecyclerView.Adapter<PairAdapter.PairViewHolder> {
    private final LayoutInflater mInflater;
    public Context mContext;

    private static MediaPlayer click;
    private List<Pair> mPairs;

    public PairAdapter(Context context) {

        //Media sounds
        click = MediaPlayer.create(context, R.raw.click3);

        mContext=context;
        mInflater = LayoutInflater.from(context);
    }

    class PairViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameView1;
        private final TextView nameView2;
        private final TextView scoreView;
        private final CardView cardView;

        private Pair pair;

        private PairViewHolder(View itemView) {
            super(itemView);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });


            nameView1 = itemView.findViewById(R.id.textView1Holder);
            nameView2 = itemView.findViewById(R.id.textView2Holder);
            scoreView = itemView.findViewById(R.id.textViewScoreHolder);

            cardView = itemView.findViewById(R.id.cardViewHolder);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.slide_out_right));
                    click.start();

                    //Start activity StatisticsPair
                    Intent intent = new Intent(mContext,StatisticsDuelActivity.class);
                    intent.putExtra("name1",pair.getName1());
                    intent.putExtra("name2",pair.getName2());
                    intent.putExtra("wins1",pair.getWins1());
                    intent.putExtra("wins2",pair.getWins2());
                    Log.d("MY_LOG","[Start activity StatisticsDuel] name1: "+pair.getName1()+", name2: "+pair.getName2());
                    mContext.startActivity(intent);
                }
            });
        }

        public Pair getPair() {
            return pair;
        }

        public void setPair(Pair pair) {
            this.pair = pair;
        }
    }

    @Override
    public PairAdapter.PairViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new PairAdapter.PairViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PairAdapter.PairViewHolder holder, int position) {
        if (mPairs != null) {
            Pair pair = mPairs.get(position);

            holder.nameView1.setText(pair.getName1());
            holder.scoreView.setText(pair.getWins1()+" : "+pair.getWins2());
            holder.nameView2.setText(pair.getName2());

            //Bold winner
            if (pair.getWins1()>pair.getWins2())
            {
                holder.nameView1.setTextColor(Color.parseColor(StaticValues.COLOR_WINNER1));
                holder.nameView2.setTextColor(Color.GRAY);
            }else
            if (pair.getWins2()>pair.getWins1())
            {
                holder.nameView1.setTextColor(Color.GRAY);
                holder.nameView2.setTextColor(Color.parseColor(StaticValues.COLOR_WINNER1));
            }

            //SET PAIR TO HOLDER
            holder.setPair(pair);
        } else {
            //holder.gameItemView1.setText("No data");
            //holder.gameItemView2.setText("No data");
        }

    }

    @Override
    public int getItemCount() {
        if (mPairs != null)
            return mPairs.size();
        else
            return 0;
    }

    public void setPairs(List<Pair> pairs) {
        mPairs = pairs;
        notifyDataSetChanged();
    }


}

