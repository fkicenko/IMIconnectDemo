package com.imimobile.connectdemo.utils;

import androidx.core.content.FileProvider;

import com.imimobile.connectdemo.R;

public class MyFileProvider extends FileProvider {

        public MyFileProvider() {
            super(R.xml.provider_paths);
        }


}
