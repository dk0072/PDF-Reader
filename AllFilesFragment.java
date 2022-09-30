package com.dktechnology.pdfreader;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Debug;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;


public class AllFilesFragment extends Fragment implements OnPdfClick,OnLoveClicked{

    private MainAdapter adapter;
    private List<File> pdfList;
    private RecyclerView rv;
    private TextView loadingText;
    private TextView AppName;
    EditText st;
    private ImageView Showsearch;
    private ImageView Closesearch;
    FragmentManager fm;
    FavouriteFragment ff = new FavouriteFragment();
    private  ArrayList<String> pathList = new ArrayList<>();
    ArrayList<String> newPath;




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fm = requireActivity().getSupportFragmentManager();
        fm
                .beginTransaction()
                .add(R.id.favouritefrag,ff)
                .show(ff)
                .commit();


        st = getActivity().findViewById(R.id.ST);
        AppName = getActivity().findViewById(R.id.AppName);
        Showsearch = getActivity().findViewById(R.id.search);
        Closesearch = getActivity().findViewById(R.id.Closesearch);
        // Inflate the layout for this fragment

        loadingText = getActivity().findViewById(R.id.splashText);

        Showsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowSearch();
            }
        });

        Closesearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CloseSearch();
            }
        });

        GetPermission();


        st.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filterSearch(s.toString());
            }
        });

        ActivityManager.TaskDescription description = new ActivityManager.TaskDescription("PDF Reader", BitmapFactory.decodeResource(this.getResources(),R.drawable.pdf_logo));
        requireActivity().setTaskDescription(description);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowInsetsController insetsController = requireActivity().getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars());
            }
        } else {
            requireActivity().getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }
        return inflater.inflate(R.layout.fragment_all_files, container, false);

    }





    private void filterSearch(String text) {
        ArrayList<File> filterList = new ArrayList<>();

        for(File f : pdfList){

            if(f.getName().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))){
                filterList.add(f);
            }
        }
        adapter.filterList(filterList);
    }

    public void GetPermission() {
        Dexter.withContext(getActivity()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                DisplayPDF();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    public ArrayList<File> findPdf(File file){
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();


      try{
          for (File f : files) {


              if(f.isDirectory() && !f.isHidden()){
                  arrayList.addAll(findPdf(f));
              }
              else{
                  if(f.getName().endsWith(".pdf")){
                      arrayList.add(f);
                      //Collections.sort(arrayList);

                  }
              }
          }

          Set<File> hs = new HashSet<>(arrayList);
          hs.addAll(arrayList);
          arrayList.clear();
          arrayList.addAll(hs);
      }catch (NullPointerException nullPointerException){}



        return arrayList;
    }

    public void DisplayPDF(){
        rv = requireActivity().findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new GridLayoutManager(getContext(),1));
        pdfList = new ArrayList<>();
        pdfList.addAll(findPdf(Environment.getExternalStorageDirectory()));
        adapter = new MainAdapter(getActivity(),pdfList,this,this);
        loadingText.setVisibility(View.GONE);
        rv.setAdapter(adapter);
    }

    public void ShowSearch(){

        st.requestFocus();
        getActivity();
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(st,InputMethodManager.SHOW_IMPLICIT);

        AppName.setVisibility(View.GONE);
        Showsearch.setVisibility(View.GONE);


        st.setVisibility(View.VISIBLE);
        Closesearch.setVisibility(View.VISIBLE);

    }

    public void CloseSearch(){
        st.clearFocus();
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        View view = new View(getContext());
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);

        st.setText(null);
        AppName.setVisibility(View.VISIBLE);
        Showsearch.setVisibility(View.VISIBLE);


        st.setVisibility(View.GONE);
        Closesearch.setVisibility(View.GONE);
    }





    @Override
    public void OnLovedClicked(int typeOfAction,File name) {



       // Toast.makeText(getContext(), name.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        SharedPreferences sp = requireActivity().getSharedPreferences("FilePath", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = sp.edit();

        Gson g = new Gson();
        String paths = sp.getString("AllPath",null);



        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        newPath = g.fromJson(paths,type);

        if(newPath != null){

            pathList.addAll(newPath);
        }


        pathList.add(name.getAbsolutePath());

        Set<String> hs = new HashSet<>(pathList);

        hs.addAll(pathList);
        pathList.clear();
        pathList.addAll(hs);

        Gson gson = new Gson();
        String json = gson.toJson(pathList);



        edt.putString("AllPath",json);


        edt.apply();
        ff.RefreshAndAdd(name);

    }

    @Override
    public void OnLovedRemoveClicked(int typeOfAction) {
        Toast.makeText(getContext(),"Removed",Toast.LENGTH_LONG).show();
    }


    @Override
    public void OnPdfClicked(File file) {
        startActivity(new Intent(getContext(),pdfView.class).putExtra("path",file.getAbsolutePath()));
    }




}