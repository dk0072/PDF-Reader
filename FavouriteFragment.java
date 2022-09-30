package com.dktechnology.pdfreader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class FavouriteFragment extends Fragment implements OnPdfClick,OnLoveClicked{


    private BookmarkedAdapter adapter;
    private RecyclerView rv;
    ArrayList<String> newPath;
    ArrayList<File> file = new ArrayList<>();


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv = getActivity().findViewById(R.id.rv2);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new GridLayoutManager(getContext(),1));

        DisplayPDF();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_favourite, container, false);

    }

    public void DisplayPDF(){



        SharedPreferences sp = getActivity().getSharedPreferences("FilePath", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String paths = sp.getString("AllPath","null");
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        newPath = gson.fromJson(paths,type);

        if(paths.equals("null")){return;}

        for (String f : newPath)
        {
            file.add(new File(f));
        }

        Set<File> hs = new HashSet<>(file);
        hs.addAll(file);
        file.clear();
        file.addAll(hs);


        adapter = new BookmarkedAdapter(getActivity(),file,this,this);

        rv.setAdapter(adapter);

    }

    public void removeItem(int At){

        SharedPreferences sp = requireActivity().getSharedPreferences("FilePath", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = sp.edit();


        Gson gson = new Gson();

        String paths = sp.getString("AllPath","null");
        Type type = new TypeToken<ArrayList<String>>(){}.getType();

        newPath = gson.fromJson(paths,type);

        if(paths.equals("null")){return;}

        newPath.remove(file.get(At).getAbsolutePath());

        Set<String> hs = new HashSet<>(newPath);

        hs.addAll(newPath);
        newPath.clear();
        newPath.addAll(hs);

        Gson g = new Gson();
        String json = g.toJson(newPath);

        edt.putString("AllPath",json);

        edt.apply();;



        file.remove(At);
        Set<File> hashSet = new HashSet<>(file);
        hashSet.addAll(file);
        file.clear();
        file.addAll(hashSet);

        adapter.notifyItemRemoved(At);
        adapter.notifyItemChanged(At);


    }

    public void RefreshAndAdd(File f){

        file.add(f);

        Set<File> hs = new HashSet<>(file);
        hs.addAll(file);
        file.clear();
        file.addAll(hs);

        Collections.sort(file);

        adapter = new BookmarkedAdapter(getActivity(),file,this,this);

        rv.setAdapter(adapter);

    }



    @Override
    public void OnPdfClicked(File file) {
        startActivity(new Intent(getContext(),pdfView.class).putExtra("path",file.getAbsolutePath()));
    }


    @Override
    public void OnLovedClicked(int typeOfAction, File FileName) {

    }

    @Override
    public void OnLovedRemoveClicked(int pos) {

        removeItem(pos);
        Toast.makeText(getContext(),"Removed !!",Toast.LENGTH_SHORT).show();
    }
}