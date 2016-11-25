package com.example;

public class Demo {

    public static void main(String args[]) throws Exception {
        Demo demo = new Demo();
        String str = demo.getInstance(String.class);
        System.out.println(str);

    }

    public <T> T getInstance(Class<T> cls) {
        try {
            return cls.newInstance();
        } catch (Exception e) {
            return null;
        }
    }
}
