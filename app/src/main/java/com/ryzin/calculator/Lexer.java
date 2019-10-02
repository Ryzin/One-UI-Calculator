package com.ryzin.calculator;

//Lexer类，主要起一个词法分析器的作用，注意这里采用正则表达式简化了代码

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 此类用于将算术表达式解析成包含操作数和操作符的链表,扮演分词器的角色
public class Lexer {
    private List<String> list;// 用于存储表达式的链表

    public List<String> getList() {
        return list;
    }

    public Lexer(String expression){
        list=new ArrayList<String>();

        // 使用正则表达式后，代码简洁多了
        String regExp = "(\\d+(\\.*)\\d*)|(\\+)|(\\-)|(\\*)|(\\/)|(\\()|(\\))";

        Pattern pattern=Pattern.compile(regExp);
        Matcher matcher=pattern.matcher(expression);
        while(matcher.find()){
            list.add(matcher.group(0));
        }
    }

    public void print(){
        for(String str:list){
            System.out.println(str);
        }
    }
}