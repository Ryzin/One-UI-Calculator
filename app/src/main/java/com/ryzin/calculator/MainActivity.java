package com.ryzin.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MainActivity extends AppCompatActivity {
    public static final  String TAG = "MainActivity";
    private Map<String, int> btnId_map = new

//    private Map<MotionEvent, String> onTouchSwitchMap = new HashMap<>();
//    private OnTouchSwitch onTouchSwitch = new OnTouchSwitch();

//    public class OnTouchSwitch{
//        void actionDown(View v) {
//            ScaleAnimation animation = new ScaleAnimation(1, 0.8f, 1, 0.8f,
//                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//            animation.setDuration(100);//设置动画持续时间
//            v.startAnimation(animation);
//
//            Log.d(TAG, "---onTouchEvent action:ACTION_DOWN");
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBtn();
    }

    //绑定组件方法
//    private <V extends View> V $(int id) {
//        return (V) findViewById(id);
//    }

    //@SuppressLint("ClickableViewAccessibility")

    private void initBtn() {

        String[] btn_name = {"btn_plusOrSub", "btn_0", "btn_dot", "btn_equal",
                "btn_1", "btn_2", "btn_3", "btn_plus",
                "btn_4", "btn_5", "btn_6",  "btn_sub",
                "btn_7", "btn_8", "btn_9",  "btn_mul",
                "btn_clean", "btn_brackets", "btn_percent", "btn_div"};

        //批量添加按钮监听事件
        for(int i = 0; i < btn_name.length; ++i) {
            int view_id = getResources().getIdentifier(btn_name[i], "id", "com.ryzin.calculator");
            Button btn = findViewById(view_id);
            btn.setOnTouchListener(new buttonListener());
        }

        String[] imgBtn_name = {"imgBtn_history", "imgBtn_ruler", "imgBtn_science", "imgBtn_delete"};
        //批量添加按钮监听事件
        for(int i = 0; i < imgBtn_name.length; ++i) {
            int view_id = getResources().getIdentifier(imgBtn_name[i], "id", "com.ryzin.calculator");
            ImageButton imgBtn = findViewById(view_id);
            imgBtn.setOnTouchListener(new buttonListener());
        }
    }





    OnTouchActionFactory onTouchActionFactory = new OnTouchActionFactory(); //动作事件处理工厂



    public class OnTouchActionFactory {
        Map<String, Operation> OnTouchActionMap = new HashMap<>();

        OnTouchActionFactory() {
            actionPerformedMap.put("equal", new Equal());
        }

        public Optional<Operation> getOperation(String actionPerformed) {
            return Optional.ofNullable(OnTouchActionMap.get(actionPerformed));
        }
    }

    public interface Operation {
        void apply();
    }

    public class Equal implements Operation {
        public void apply() {

        }
    }




    class buttonListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            onTouchActionFactory
                    .getOperation(v.getId)
                    .orElseThrow() -> new IllegalArgumentException("非法")
                    .apply(e);
        }

//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            //按下操作
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//
//                TextView textView_top = findViewById(R.id.textView_top);
//
//                //更新序列
//                CharSequence cs; //可读可写序列
//                Button btn = (Button) v; //为了获取按钮的text
//                cs = textView_top.getText() + btn.getText().toString();
//
//                textView_top.setText(cs);
//
//                //缩放动画
//                ScaleAnimation scaleAnimation = (ScaleAnimation) AnimationUtils
//                        .loadAnimation(MainActivity.this, R.anim.text_action_down_animation);
//                v.startAnimation(scaleAnimation);
//
////               ScaleAnimation animation = new ScaleAnimation(1, 0.8f, 1, 0.8f,
////               Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
////               animation.setDuration(100);//设置动画持续时间
////               v.startAnimation(animation);
//                //Toast.makeText(getApplicationContext(), "按钮点击" , Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "---onTouchEvent action:ACTION_DOWN");
//                return false; //如果返回true ，那么就把事件拦截，onclick无法响应；返回false，就同时执行onClick方法。
//            }
//            //抬起操作
//            if (event.getAction() == MotionEvent.ACTION_UP) {
//                ScaleAnimation scaleAnimation = (ScaleAnimation) AnimationUtils
//                        .loadAnimation(MainActivity.this, R.anim.text_action_up_animation);
//                v.startAnimation(scaleAnimation);
//
//                //透明度渐变动画
////                Animation alphaAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);
////                v.startAnimation(alphaAnimation);//开始动画
////                alphaAnimation.setFillAfter(true);//动画结束后保持状态
////                //添加动画监听
////                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
////                    @Override
////                    public void onAnimationStart(Animation animation) {
////                        //动画开始时回调
////                    }
////
////                    @Override
////                    public void onAnimationEnd(Animation animation) {
////                        //动画结束时回调
////                        Toast.makeText(MainActivity.this, "动画结束", Toast.LENGTH_SHORT).show();
////                    }
////
////                    @Override
////                    public void onAnimationRepeat(Animation animation) {
////                        //动画重复时回调
////                    }
////                });
//
//                Log.d(TAG, "---onTouchEvent action:ACTION_UP");
//                return false;
//            }
//            //移动操作
//            if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                Log.d(TAG, "---onTouchEvent action:ACTION_MOVE");
//                return false;
//            }
//            //取消操作
//            if (event.getAction() == MotionEvent.ACTION_CANCEL) {
//                Log.d(TAG, "---onTouchEvent action:ACTION_CANCEL");
//                return false;
//            }
//            return true;
//        }
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


