package com.telkom.mobilesalesreport;

public class DataClass {
    private String trxDate, article, qty, price;

    public DataClass() {
    }

    public DataClass(String trxDate, String article, String qty, String price) {
        this.trxDate = trxDate;
        this.article = article;
        this.qty = qty;
        this.price = price;
    }

    public String getTrxDate() {
        return trxDate;
    }

    public String getArticle() {
        return article;
    }

    public String getQty() {
        return qty;
    }

    public String getPrice() {
        return price;
    }
}
