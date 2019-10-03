package com.ryzin.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    public static final  String TAG = "MainActivity";
    Map<String, Integer> buttonIdMap = new HashMap<>();
    Map<Integer, Object> methodMap = new HashMap<>();

    public boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches(); //是整数返回true,否则返回false
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBtn();
        initMethodMap();
        initTextView();
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
                "btn_clean", "btn_left_bracket", "btn_right_bracket","btn_div",
                "imgBtn_history", "imgBtn_ruler", "imgBtn_science", "imgBtn_delete"};

        //存放button的id到buttonIdMap
        for(int i = 0; i < btn_name.length; ++i) {
            int view_id = getResources().getIdentifier(btn_name[i], "id", "com.ryzin.calculator");
            buttonIdMap.put(btn_name[i], view_id);
        }

        //批量添加按钮监听事件
        int i = 0;
        for(; i < btn_name.length - 4; ++i) {
            int value = buttonIdMap.get(btn_name[i]);
            Button btn = findViewById(value);
            btn.setOnTouchListener(new ButtonListener());
        }

        for(; i < btn_name.length; ++i) {
            int value = buttonIdMap.get(btn_name[i]);
            ImageButton imgBtn = findViewById(value);
            imgBtn.setOnClickListener(new ControlButtonListener(imgBtn));
        }

    }

    private void initMethodMap() {
        //方法工厂
        String[] num_name = {"btn_0", "btn_1", "btn_2", "btn_3",
                "btn_4", "btn_5", "btn_6", "btn_7",
                "btn_8", "btn_9"};
        int id;
        for(int i = 0; i < num_name.length; ++i) {
            id = buttonIdMap.get(num_name[i]);
            methodMap.put(id, new Number());
        }
        String[] operator_name = {"btn_plus", "btn_sub", "btn_mul", "btn_div",
                "btn_right_bracket", "btn_dot"};
        for(int i = 0; i < operator_name.length; ++i) {
            id = buttonIdMap.get(operator_name[i]);
            methodMap.put(id, new BasicOperator());
        }

        id = buttonIdMap.get("btn_left_bracket");
        methodMap.put(id, new LeftBracket());
        id = buttonIdMap.get("btn_right_bracket");
        methodMap.put(id, new RightBracket());

        id = buttonIdMap.get("btn_clean");
        methodMap.put(id, new Clean());
        id = buttonIdMap.get("btn_plusOrSub");
        methodMap.put(id, new PlusOrSub());
        id = buttonIdMap.get("btn_equal");
        methodMap.put(id, new Equal());

        id = buttonIdMap.get("imgBtn_history");
        methodMap.put(id, new History());
        id = buttonIdMap.get("imgBtn_ruler");
        methodMap.put(id, new Ruler());
        id = buttonIdMap.get("imgBtn_science");
        methodMap.put(id, new Science());
        id = buttonIdMap.get("imgBtn_delete");
        methodMap.put(id, new Delete());
        //more method

    }

    private void initTextView() {
        TextView textView_top = findViewById(R.id.textView_top);

        //为textview添加文本变化监听事件，每次变化都判断表达式合法性并计算结果
        textView_top.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //s--未改变之前的内容
                //start--内容被改变的开始位置
                //count--原始文字被删除的个数
                //after--新添加的内容的个数

                //---------start和count结合从s中获取被删除的内容-------
                //String deleText = s.toString().substring(start, start + count);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //s--改变之后的新内容
                //start--内容被改变的开始位置
                //before--原始文字被删除的个数
                //count--新添加的内容的个数

                //---------start和count结合从s中获取新添加的内容-------
                //String addText = s.toString().substring(start, start + count);
            }
            @Override
            public void afterTextChanged(Editable s) {
                TextView textView_top = findViewById(R.id.textView_top);
                TextView textView_bottom = findViewById(R.id.textView_bottom);

                //更新序列
                CharSequence cs; //可读可写序列
                cs = textView_top.getText();

                String expression = cs.toString();
                if(expression.equals(""))
                    return;

                //修正表达式
                expression = expression.replace('×', '*');
                expression = expression.replace('÷', '/');

                ExpressionValidity ev = new ExpressionValidity();

                if(!ev.checkExpression(expression)) { //检测表达式是否正确
                    cs = "";
                    textView_bottom.setText(cs);
                    return;
                }

                // 送去解析
                Lexer p = new Lexer(expression);
                //p.print();

                // 转为后序表达式
                Trans t = new Trans(p.getList());
                //t.print();

                // 计算结果
                Calculator c = new Calculator(t.getPostfixList());
                cs = c.getResult();

                textView_bottom.setText(cs);
            }
        });
    }

    class BasicOperator {
        public void apply(View v) {
            TextView textView_top = findViewById(R.id.textView_top);

            //更新序列
            CharSequence cs; //可读可写序列
            Button btn = (Button) v; //为了获取按钮的text
            cs = textView_top.getText();
            int length = cs.length();
            if(length > 0) {
                cs = cs + btn.getText().toString();
            }
            textView_top.setText(cs);
        }
    }

    class LeftBracket {
        public void apply(View v) {
            TextView textView_top = findViewById(R.id.textView_top);

            //更新序列
            CharSequence cs; //可读可写序列
            cs = textView_top.getText();
            int length = cs.length();
            if(length == 0) {
                cs = cs + "(";
                textView_top.setText(cs);
                return;
            }

            String lastChar = cs.subSequence(length - 1, length).toString(); //获取序列最后一个字符

            if(isInteger(lastChar) || lastChar.equals(")"))
                cs = cs + "×";

            cs = cs + "(";
            textView_top.setText(cs);
        }
    }

    class RightBracket {
        public void apply(View v) {
            TextView textView_top = findViewById(R.id.textView_top);

            //更新序列
            CharSequence cs; //可读可写序列
            cs = textView_top.getText();
            cs = cs + ")";
            textView_top.setText(cs);
        }
    }

    class Number {
        public void apply(View v) {
            TextView textView_top = findViewById(R.id.textView_top);

            //更新序列
            CharSequence cs; //可读可写序列
            Button btn = (Button) v; //为了获取按钮的text
            cs = textView_top.getText() + btn.getText().toString();

            textView_top.setText(cs);
        }
    }

    class PlusOrSub {
        public void apply(View v) {
            Toast.makeText(getApplicationContext(), "负数功能待实现" , Toast.LENGTH_SHORT).show();
        }
    }

    class Clean {
        public void apply(View v) {
            TextView textView_top = findViewById(R.id.textView_top);
            //更新序列
            textView_top.setText("");
        }
    }

    class Equal {
//        private int countLetter(String str, String letter) {
//            int positionOfLetter = str.indexOf("(");
//            int countNumberOfLetters = 0;
//
//            while (positionOfLetter != -1) {
//                countNumberOfLetters++;
//                positionOfLetter = str.indexOf("(", positionOfLetter + 1);
//            }
//
//            return countNumberOfLetters;
//        }

        public void apply(View v) {
            TextView textView_top = findViewById(R.id.textView_top);
            TextView textView_bottom = findViewById(R.id.textView_bottom);

            if(textView_bottom.getText().equals(""))
                return;

            Animation outAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.result_push_up_out_animation);
            textView_bottom.startAnimation(outAnimation);
            outAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    TextView textView_top = findViewById(R.id.textView_top);
                    textView_top.setText("");
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    TextView textView_top = findViewById(R.id.textView_top);
                    TextView textView_bottom = findViewById(R.id.textView_bottom);
                    textView_top.setText(textView_bottom.getText());
                    textView_bottom.setText("");
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }
    }

    class History {
        public void apply(View v) {
            Toast.makeText(getApplicationContext(), "历史功能待实现" , Toast.LENGTH_SHORT).show();
        }
    }

    class Ruler {
        public void apply(View v) {
            Toast.makeText(getApplicationContext(), "单位转换功能待实现" , Toast.LENGTH_SHORT).show();
        }
    }

    class Science {
        public void apply(View v) {
            Toast.makeText(getApplicationContext(), "科学计算功能待实现" , Toast.LENGTH_SHORT).show();
        }
    }

    class Delete {
        public void apply(View v) {
            TextView textView_top = findViewById(R.id.textView_top);

            //更新序列
            int length = textView_top.getText().length(); //可读可写序列
            Log.d(TAG, length + "");
            CharSequence cs;
            if(length > 0) {
                cs = textView_top.getText().subSequence(0, length - 1);
                textView_top.setText(cs);
            }
        }
    }

    class ButtonListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //按下操作
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Object obj = methodMap.get(v.getId()); //根据id从methodMap获取方法对象

                //执行方法对象的通用apply方法(通过反射)
                try {
                    Method m = obj.getClass().getMethod("apply", View.class); //获取apply方法，参数为View类型
                    try {
                        m.invoke(obj, v); //执行方法，返回类型为Object
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        Log.i("IllegalAccessException | InvocationTargetException", "invoke apply method failure");
                        e.printStackTrace();
                    }
                } catch (NoSuchMethodException e) {
                    Log.i("NoSuchMethodException", "get method failure");
                    e.printStackTrace();
                }

//                java.lang.reflect.Method[] method = obj.getClass().getDeclaredMethods();//获取所有方法
//                for(java.lang.reflect.Method m: method) { //执行所有方法
//                    System.out.println(m.getName());
//                    if (m.getName().startsWith("get")) {
//                        Object o = null;
//                        try {
//                            o = m.invoke(obj, v); //执行方法，返回值存放在o
//                        } catch (IllegalAccessException | InvocationTargetException e) {
//                            e.printStackTrace();
//                        }
//                        if (o != null && !"".equals(o.toString())) {
//                            Log.i("method_return_value", o.toString());
//                        }
//                    }
//                }

                //缩放动画
                ScaleAnimation scaleAnimation = (ScaleAnimation) AnimationUtils
                        .loadAnimation(MainActivity.this, R.anim.text_action_down_animation);
                v.startAnimation(scaleAnimation);

//               ScaleAnimation animation = new ScaleAnimation(1, 0.8f, 1, 0.8f,
//               Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//               animation.setDuration(100);//设置动画持续时间
//               v.startAnimation(animation);
                //Toast.makeText(getApplicationContext(), "按钮点击" , Toast.LENGTH_SHORT).show();

                //Log.d(TAG, "---onTouchEvent action:ACTION_DOWN");
                return false; //如果返回true ，那么就把事件拦截，onclick无法响应；返回false，就同时执行onClick方法。
            }
            //抬起操作
            if (event.getAction() == MotionEvent.ACTION_UP) {
                ScaleAnimation scaleAnimation = (ScaleAnimation) AnimationUtils
                        .loadAnimation(MainActivity.this, R.anim.text_action_up_animation);
                v.startAnimation(scaleAnimation);

                //透明度渐变动画
//                Animation alphaAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha);
//                v.startAnimation(alphaAnimation);//开始动画
//                alphaAnimation.setFillAfter(true);//动画结束后保持状态
//                //添加动画监听
//                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//                        //动画开始时回调
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        //动画结束时回调
//                        Toast.makeText(MainActivity.this, "动画结束", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//                        //动画重复时回调
//                    }
//                });

                //Log.d(TAG, "---onTouchEvent action:ACTION_UP");
                return false;
            }
            //移动操作
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                //Log.d(TAG, "---onTouchEvent action:ACTION_MOVE");
                return false;
            }
            //取消操作
            if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                //Log.d(TAG, "---onTouchEvent action:ACTION_CANCEL");
                return false;
            }
            return true;
        }
    }

    class ControlButtonListener implements View.OnClickListener {
        private ImageButton imgBtn;
        String[] imgBtn_name = {"imgBtn_history", "imgBtn_ruler", "imgBtn_science", "imgBtn_delete"};


        private ControlButtonListener(ImageButton imgBtn) {
            this.imgBtn = imgBtn;
        }

        @Override
        public void onClick(View v) {
            Object obj = methodMap.get(v.getId()); //根据id从methodMap获取方法对象

            //执行方法对象的通用apply方法(通过反射)
            try {
                Method m = obj.getClass().getMethod("apply", View.class); //获取apply方法，参数为View类型
                try {
                    m.invoke(obj, v); //执行方法，返回类型为Object
                } catch (IllegalAccessException | InvocationTargetException e) {
                    Log.i("IllegalAccessException | InvocationTargetException", "invoke apply method failure");
                    e.printStackTrace();
                }
            } catch (NoSuchMethodException e) {
                Log.i("NoSuchMethodException", "get method failure");
                e.printStackTrace();
            }
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


