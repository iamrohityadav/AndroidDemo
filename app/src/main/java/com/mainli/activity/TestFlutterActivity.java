package com.mainli.activity;

import android.os.Bundle;

import com.seekting.demo_lib.Demo;

import io.flutter.app.FlutterActivity;
import io.flutter.plugins.GeneratedPluginRegistrant;

@Demo(title = "测试flutter", group = {"UI"})
public class TestFlutterActivity extends FlutterActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneratedPluginRegistrant.registerWith(this);
    }
}
