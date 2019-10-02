package com.ryzin.calculator;

//Inlet类，入口类，这个类的主要用途是验证用户输入的算术表达式

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// 此类用于把算术表达式送入解析器
public class Inlet {
    public static void main(String[] args) throws IOException{
        // 取得用户输入的表达式
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String rawExpression = null;
        System.out.print("请输入算术表达式:");
        rawExpression = br.readLine();

        // 得到合法的算术表达式
        String expression="";
        for(int i=0;i<rawExpression.length();i++){
            // 拿到表达式的每个字符
            char c=rawExpression.charAt(i);
            //System.out.print(c+",");

            if(Character.isDigit(c) || c=='+' || c=='-' || c=='*' || c=='/' || c=='(' || c==')' || c=='.'){
                //System.out.print(c);
                expression+=c;
            }else{
                System.out.print(" "+c+"不是合法的算术表达式字符.");
                System.exit(0);
            }
        }

        // 送去解析
        Lexer p=new Lexer(expression);
        //p.print();

        // 转为后序表达式
        Trans t=new Trans(p.getList());
        //t.print();

        // 计算结果
        Calculator c=new Calculator(t.getPostfixList());
        System.out.print(expression+"="+c.getResult());
    }
}