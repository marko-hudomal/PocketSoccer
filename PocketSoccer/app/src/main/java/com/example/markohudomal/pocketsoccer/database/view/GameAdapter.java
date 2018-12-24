package com.example.markohudomal.pocketsoccer.database.view;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.markohudomal.pocketsoccer.R;
import com.example.markohudomal.pocketsoccer.database.Game;
import com.example.markohudomal.pocketsoccer.extras.StaticValues;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private final LayoutInflater mInflater;
    public Context mContext;

    private static MediaPlayer click;
    private List<Game> mGames;

    public GameAdapter(Context context) {

        //Media sounds
        click = MediaPlayer.create(context, R.raw.click3);

        mContext=context;
        mInflater = LayoutInflater.from(context);
    }

    class GameViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameView1;
        private final TextView nameView2;
        private final TextView scoreView;

        private final TextView dateView;

        private final CardView cardView;

        private GameViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            nameView1 = itemView.findViewById(R.id.textView1Holder);
            nameView2 = itemView.findViewById(R.id.textView2Holder);
            scoreView = itemView.findViewById(R.id.textViewScoreHolder);

            dateView = itemView.findViewById(R.id.textViewDateHolder);

            cardView = itemView.findViewById(R.id.cardViewHolder);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_shake));

                }
            });
        }
    }

    @Override
    public GameAdapter.GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item_game, parent, false);
        return new GameAdapter.GameViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GameAdapter.GameViewHolder holder, int position) {
        if (mGames != null) {
            Game game = mGames.get(position);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(game.getDate());
            SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy | HH:mm:ss");
            String formattedDate = df.format(calendar.getTime());

            holder.dateView.setText(formattedDate);
            holder.nameView1.setText(game.getName1());
            holder.scoreView.setText(game.getGoals1()+" : "+game.getGoals2());
            holder.nameView2.setText(game.getName2());

            //Bold winner
            if (game.getGoals1()>game.getGoals2())
            {
                holder.nameView1.setTextColor(Color.parseColor(StaticValues.COLOR_WINNER2));
            }else
                if (game.getGoals2()>game.getGoals1())
                {
                    holder.nameView2.setTextColor(Color.parseColor(StaticValues.COLOR_WINNER2));
                }

        } else {
            //holder.gameItemView1.setText("No data");
            //holder.gameItemView2.setText("No data");
        }
    }

    @Override
    public int getItemCount() {
        if (mGames != null)
            return mGames.size();
        else
            return 0;
    }

    public void setGames(List<Game> games) {
        mGames = games;
        notifyDataSetChanged();
    }


    public static String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }
}

