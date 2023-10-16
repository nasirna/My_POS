package com.kpbdstudio.mypos.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.kpbdstudio.mypos.R;


public class CategoryViewHolder extends RecyclerView.ViewHolder{

    public TextView categoryName;
    public ImageView categoryImage;
    public View mItemView;


    public CategoryViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        categoryName = (TextView)itemView.findViewById(R.id.category_name);
        categoryImage = (ImageView)itemView.findViewById(R.id.category_image);
    }
}
