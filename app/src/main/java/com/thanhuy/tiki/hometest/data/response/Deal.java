package com.thanhuy.tiki.hometest.data.response;

import java.util.Date;
import java.util.UUID;

public class Deal {
    private String productName;
    private String productThumbnail;
    private Double  productPrice;
    private Date startedDate;
    private Date endDate;
    private String uuid;
    private long countDownTime;

    public Deal(String name, String thumbnail, Double price, Date start, Date end){
        uuid = UUID.randomUUID().toString();
        productName = name;
        productThumbnail = thumbnail;
        productPrice = price;
        startedDate = start;
        endDate = end;

    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductThumbnail() {
        return productThumbnail;
    }

    public void setProductThumbnail(String productThumbnail) {
        this.productThumbnail = productThumbnail;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public Date getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(Date startedDate) {
        this.startedDate = startedDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public long getCountDownTime() {
        return countDownTime;
    }

    public void setCountDownTime(long countDownTime) {
        if(countDownTime < 0 ){
            this.countDownTime = 0L;
        } else {
            this.countDownTime = countDownTime;
        }

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
