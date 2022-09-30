package com.dktechnology.pdfreader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainViewHolder>{

    private Context context;

    public MainAdapter(Context context, List<File> pdfFiles, OnPdfClick listener, OnLoveClicked listener_loved) {
        this.context = context;
        this.pdfFiles = pdfFiles;
        this.listener = listener;
        this.listener_loved = listener_loved;
    }

    private List<File> pdfFiles;
    private OnPdfClick listener;
    private OnLoveClicked listener_loved;
    private  ArrayList<String> pathList = new ArrayList<>();
    ArrayList<String> newPath;
    ArrayList<File> file = new ArrayList<>();




    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item,parent,false));

    }




    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, @SuppressLint("RecyclerView") int position) {

    holder.pdfName.setText(pdfFiles.get(position).getName());
    holder.pdfName.setSelected(true);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            listener.OnPdfClicked(pdfFiles.get(position));
        }
    });



        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                listener_loved.OnLovedClicked(1,pdfFiles.get(position));
                holder.image.setImageResource(R.drawable.bookmarked);
                Toast.makeText(v.getContext(), "Bookmarked !!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return pdfFiles.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<File> fl){
        pdfFiles = fl;
        notifyDataSetChanged();
    }
}
