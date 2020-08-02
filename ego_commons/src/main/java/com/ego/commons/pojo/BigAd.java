package com.ego.commons.pojo;


/**
 * 大广告数据
 */
public class BigAd {
    // 备份图片（此功能页面没有实现）不影响整体效果
    private String srcB;
    // 显示图片的高
    private int height;
    // 鼠标悬浮提示信息
    private String alt;
    // 显示图片的宽
    private int width;
    // 显示图片的url
    private String src;
    // 备份图片宽
    private int widthB;
    // 点击图片跳转路径
    private String href;
    // 备份图片高
    private int heightB;

    public String getSrcB() {
        return srcB;
    }

    public void setSrcB(String srcB) {
        this.srcB = srcB;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public int getWidthB() {
        return widthB;
    }

    public void setWidthB(int widthB) {
        this.widthB = widthB;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public int getHeightB() {
        return heightB;
    }

    public void setHeightB(int heightB) {
        this.heightB = heightB;
    }
}
