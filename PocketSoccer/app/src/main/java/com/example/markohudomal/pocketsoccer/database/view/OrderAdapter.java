package com.example.markohudomal.pocketsoccer.database.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.markohudomal.pocketsoccer.R;
import com.example.markohudomal.pocketsoccer.database.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private final LayoutInflater mInflater;

    private List<Order> mOrders;

    public OrderAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private final TextView orderItemView;

        private OrderViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            orderItemView = itemView.findViewById(R.id.textView);
        }
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        if (mOrders != null) {
            Order order = mOrders.get(position);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(Integer.toString(order.getId()))
                    .append(" - (")
                    .append(order.getDate().toString())
                    .append(") ")
                    .append(order.getName());
            holder.orderItemView.setText(stringBuilder.toString());
        } else {
            holder.orderItemView.setText("No Order");
        }
    }

    @Override
    public int getItemCount() {
        if (mOrders != null)
            return mOrders.size();
        else
            return 0;
    }

    public void setOrders(List<Order> order) {
        mOrders = order;
        notifyDataSetChanged();
    }

    public Order getOrderAtPosition(int position) {
        return mOrders.get(position);
    }
}

