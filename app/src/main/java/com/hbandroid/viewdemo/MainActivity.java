package com.hbandroid.viewdemo;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.hbandroid.viewdemo.view.HBLevelView;
import com.hbandroid.viewdemo.view.MySearchView;

public class MainActivity extends AppCompatActivity {

    private HBLevelView levelView;

    private MySearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchView = (MySearchView) findViewById(R.id.searchView);
//        levelView = (HBLevelView) findViewById(R.id.levelView);
//        addView();
    }

    private void addView() {
        WindowManager manager = getWindowManager();
        Button button = new Button(this);
        button.setText("add button");
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, 0, 0, PixelFormat.TRANSPARENT);
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        layoutParams.x = 100;
        layoutParams.y = 100;
        manager.addView(button, layoutParams);

    }

    public void click(View view) {
//        levelView.resetLevelProgress(0,100,26);
        if (searchView.getmStatus() == MySearchView.STATE_NORMAL) {
            searchView.setMode(MySearchView.STATE_SEARCH_ING);
        } else if (searchView.getmStatus() == MySearchView.STATE_SEARCH_ING) {
            searchView.setMode(MySearchView.STATE_SEARCH_END);
        } else if (searchView.getmStatus() == MySearchView.STATE_SEARCH_END) {
            searchView.setMode(MySearchView.STATE_END);
        } else if (searchView.getmStatus() == MySearchView.STATE_END) {
            searchView.setMode(MySearchView.STATE_NORMAL);
        }
    }

}
