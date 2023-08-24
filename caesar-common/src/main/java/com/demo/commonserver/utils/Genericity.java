package com.demo.commonserver.utils;

public class Genericity<K, V> {

    private K key;

    private V value;

    public Genericity(K key, V value){
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public static void main(String[] args) {
        Genericity<String, String> stringStringGenericity = new Genericity<>("123", "345");
        Genericity<Integer, Integer> integerIntegerGenericity = new Genericity<>(123, 345);
        if (stringStringGenericity.getClass().equals(integerIntegerGenericity.getClass())){
            System.out.println("YES");
        }else {
            System.out.println("NO");
        }
    }
}
