package com.dktechnology.pdfreader;

import android.view.View;

import java.io.File;

public interface OnLoveClicked {

    void OnLovedClicked(int typeOfAction , File FileName);
    void OnLovedRemoveClicked(int position);

}
