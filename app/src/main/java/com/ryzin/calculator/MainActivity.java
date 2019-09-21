package com.ryzin.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBtn();
    }

    //绑定组件方法
    private <V extends View> V $(int id) {
        return (V) findViewById(id);
    }

    private void initBtn() {

        Button btn1 = findViewById(R.id.btn_0);
        btn1.setOnClickListener(new buttonListener(btn1));

    }

    class buttonListener implements View.OnClickListener {
        private Button btn;

        private buttonListener(Button btn) {
            this.btn = btn;
        }

        @Override
        public void onClick(View v) {
            TextView textView_top = findViewById(R.id.textView_top);

            CharSequence cs; //可读可写序列
            Button btn = (Button) v; //为了获取按钮的text
            cs = textView_top.getText() + btn.getText().toString();

            textView_top.setText(cs);
            //Toast.makeText(getApplicationContext(), "按钮点击" , Toast.LENGTH_SHORT).show();
        }
    }
}

/*
//动态添加按钮
public class MainActivity extends AppCompatActivity {

    class buttonProperty {
        private double width;
        private double height;

        public buttonProperty(double width) {
            this.width = width;
        }

        public double getWidth() {
            return width;
        }
    }

    private LinearLayout btn_linearLayout;

    String[] btn_name = {"+/-", "0", ".", "=",
            "1", "2", "3", "+",
            "4", "5", "6", "-",
            "7", "8", "9", "×",
            "C", "()", "%", "÷",
            "←"};
    String[] btn_id = {"btn_plusOrSub", "btn_0", "btn_dot", "btn_equal",
            "btn_1", "btn_2", "btn_3", "btn_plus",
            "btn_4", "btn_5", "btn_6",  "btn_sub",
            "btn_7", "btn_8", "btn_9",  "btn_mul",
            "btn_clear", "btn_bracket", "btn_percent", "btn_div",
            "btn_delete"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBtn();

        btn_linearLayout = findViewById(R.id.btn_linearLayout);
        Map<String, buttonProperty> buttonMap = new HashMap<>();
        buttonMap.put("1", new buttonProperty(300));
    }

    private void initBtn() {

        for(int i = 0; i < btn_name.length; i++) {
            Button btn = new Button(this);
            btn.setText(btn_name[i]);

            btn.setId(i);
            btn.setOnClickListener(new buttonListener(btn));

            //定义LayoutParam
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.width = 200;

            btn_linearLayout.addView(btn, params); //addView是ViewGroup中特有的方法，而单一的View是不存在该方法
        }
    }

    class buttonListener implements View.OnClickListener {
        private Button btn;

        private buttonListener(Button btn) {
            this.btn = btn;
        }

        @Override
        public void onClick(View v) {
            TextView textView_top = findViewById(R.id.textView_top);

            CharSequence cs; //可读可写序列
            Button btn = (Button) v; //为了获取按钮的text
            cs = textView_top.getText() + btn.getText().toString();

            textView_top.setText(cs);
            //Toast.makeText(getApplicationContext(), "按钮点击" , Toast.LENGTH_SHORT).show();
        }
    }
}
 */


