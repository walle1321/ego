package com.ego.item.pojo;

public class Param {
    private String K;
    private String V;

    public Param() {
    }

    public String getK() {
        return K;
    }

    public void setK(String k) {
        K = k;
    }

    public String getV() {
        return V;
    }

    public void setV(String v) {
        V = v;
    }

    public Param(String k, String v) {
        K = k;
        V = v;
    }
}
