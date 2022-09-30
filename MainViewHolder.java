package com.dktechnology.pdfreader;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MainViewHolder extends RecyclerView.ViewHolder {

    public TextView pdfName;
    public CardView cardView;
    public ImageView image;




    public MainViewHolder(@NonNull View itemView){
        super(itemView);

        pdfName = itemView.findViewById(R.id.pdf_name);
        cardView = itemView.findViewById(R.id.pdf_cardview);
        image = itemView.findViewById(R.id.bookmarked);


    }

}
