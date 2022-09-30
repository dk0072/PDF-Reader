package com.dktechnology.pdfreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class pdfView extends AppCompatActivity {

    String path = "";
    Boolean fromOther = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        PDFView pv = findViewById(R.id.pdf_view);
        path = getIntent().getStringExtra("path");



        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if(Intent.ACTION_VIEW.equals(action) && type != null){
            fromOther = true;

            Uri uri = intent.getData();

            if(uri != null){

                pv.fromUri(uri).load();
            }
        }


        if (!fromOther){
            File file = new File(path);
            Uri uri = Uri.fromFile(file);
            pv.fromUri(uri).load();
        }

    }
}