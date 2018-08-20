package com.wzc.labelview.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wzc.labelview.LabelView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LabelView labelView = (LabelView) findViewById(R.id.right_top_label);
        labelView.setText("广告");
        labelView.setDegrees(LabelView.DEGREES_RIGHT);
        labelView.setLabelBackgroundColor(getResources().getColor(android.R.color.holo_purple));
        labelView.setTextColor(getResources().getColor(android.R.color.white));
        labelView.setTextStyle(LabelView.TEXT_STYLE_NORMAL);
        labelView.setTextSize(getResources().getDimension(R.dimen.text_size));
        labelView.setTextLeftPadding(getResources().getDimension(R.dimen.padding));
        labelView.setTextTopPadding(getResources().getDimension(R.dimen.padding));
        labelView.setTextRightPadding(getResources().getDimension(R.dimen.padding));
        labelView.setTextBottomPadding(getResources().getDimension(R.dimen.padding));
    }
}
