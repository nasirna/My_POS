package com.kpbdstudio.mypos.adapter;


import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.kpbdstudio.mypos.R;


public class HistoryViewHolder extends RecyclerView.ViewHolder{

    public TextView deliveryDate;
    public TextView deliveryStatus;
    public TextView orderName;
    public TextView orderPrice;
    public TextView orderTracking;

    public HistoryViewHolder(View itemView) {
        super(itemView);

        deliveryDate = (TextView)itemView.findViewById(R.id.delivery_date);
        deliveryStatus = (TextView)itemView.findViewById(R.id.delivery_status);
        orderName = (TextView)itemView.findViewById(R.id.order_name);
        orderPrice = (TextView)itemView.findViewById(R.id.order_price);
        orderTracking = (TextView)itemView.findViewById(R.id.order_tracking);
    }
}
