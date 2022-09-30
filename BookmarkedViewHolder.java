package com.dktechnology.pdfreader;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class BookmarkedViewHolder extends RecyclerView.ViewHolder {
    public TextView pdfName;
    public CardView cardView;
    public ImageView image;




    public BookmarkedViewHolder(@NonNull View itemView){
        super(itemView);

        pdfName = itemView.findViewById(R.id.pdf_name);
        cardView = itemView.findViewById(R.id.pdf_cardview_bookmarked);
        image = itemView.findViewById(R.id.Remove_Bookmark);


    }
}
