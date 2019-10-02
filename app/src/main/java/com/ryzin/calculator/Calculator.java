package com.ryzin.calculator;

//Calculator类 计算后续表达式运算结果类，它接受经过Trans类处理的postfixList，
// 又采用了栈进行辅助，计算结果方式是见到操作数先入栈，见到操作符则从栈中弹出
// 两个操作数进行运算，得到结果后再入栈，执行完毕后弹出栈的顶项（必是最后一项）
// 即是算术表达式的最终结果

import java.util.List;
import java.util.Stack;

// 此类用于计算后续表达式的值
public class Calculator {
    private Stack<String> stack;

    public Calculator(List<String> list){
        stack=new Stack<String>();

        for(String str:list){
            // 这个分支是当前项是操作符号的情况
            if(str.equals("+") || str.equals("-") || str.equals("*") || str.equals("/") || str.equals("(") || str.equals(")")  ){
                float op2=Float.parseFloat(stack.pop());
                float op1=Float.parseFloat(stack.pop());
                float result=0;

                if(str.equals("+")){
                    result=op1+op2;
                }else if(str.equals("-")){
                    result=op1-op2;
                }else if(str.equals("*")){
                    result=op1*op2;
                }else if(str.equals("/")){
                    result=op1/op2;
                }

                stack.push(String.valueOf(result));
            }else{
                // 如果是操作数直接入栈
                stack.push(str);
            }
        }
    }

    // 取得结果
    public String getResult(){
        return stack.peek();
    }
}