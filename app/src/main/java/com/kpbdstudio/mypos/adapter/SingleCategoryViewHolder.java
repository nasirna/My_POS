package com.kpbdstudio.mypos.adapter;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.kpbdstudio.mypos.R;


public class SingleCategoryViewHolder extends RecyclerView.ViewHolder{

    public ImageView foodImage;
    public TextView foodName;
    public TextView foodPrice;
    public ImageView foodDetails;

    public SingleCategoryViewHolder(View itemView) {
        super(itemView);

        foodImage = (ImageView)itemView.findViewById(R.id.food_image);
        foodName = (TextView)itemView.findViewById(R.id.food_name);
        foodPrice = (TextView)itemView.findViewById(R.id.food_price);
        foodDetails = (ImageView) itemView.findViewById(R.id.food_detail);
    }
}
