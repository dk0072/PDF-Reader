package com.dktechnology.pdfreader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookmarkedAdapter extends RecyclerView.Adapter<BookmarkedViewHolder> {

    private final Context context;
    private final List<File> pdfFiles;
    private final OnPdfClick listener;
    private final OnLoveClicked listener_loved;
    ArrayList<String> newPath;

    public BookmarkedAdapter(Context context, List<File> pdfFiles, OnPdfClick listener, OnLoveClicked listener_loved) {
        this.context = context;
        this.pdfFiles = pdfFiles;
        this.listener = listener;
        this.listener_loved = listener_loved;
    }


    @NonNull
    @Override
    public BookmarkedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookmarkedViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_bookmarked, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull BookmarkedViewHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.pdfName.setText(pdfFiles.get(holder.getAdapterPosition()).getName());
        holder.pdfName.setSelected(true);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    listener.OnPdfClicked(pdfFiles.get(holder.getAdapterPosition()));
                }
                catch(IndexOutOfBoundsException c){
                    Toast.makeText(v.getContext(),"Error - sorry for such experience", Toast.LENGTH_SHORT).show();

                }
            }
        });




        holder.image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try{
                    if(listener_loved != null){
                        if(holder.getAdapterPosition() != RecyclerView.NO_POSITION){

                            listener_loved.OnLovedRemoveClicked(holder.getAdapterPosition());

                            notifyItemRemoved(holder.getAdapterPosition());

                        }
                    }
                }



                catch (IndexOutOfBoundsException a){
                    Toast.makeText(v.getContext(),"Error - sorry for such experience", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return this.pdfFiles.size();
    }
}