package com.telkom.mobilesalesreport;

public class DataClass {
    private String trxCode,trxDate, article, qty, price, rowid;

    public DataClass() {
    }

    public DataClass(String trxCode, String trxDate, String article, String qty, String price, String rowid) {
        this.trxCode = trxCode;
        this.trxDate = trxDate;
        this.article = article;
        this.qty = qty;
        this.price = price;
        this.rowid = rowid;
    }

    public String getRowid() {
        return rowid;
    }

    public String getTrxCode() {
        return trxCode;
    }

    public void setTrxCode(String trxCode) {
        this.trxCode = trxCode;
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
