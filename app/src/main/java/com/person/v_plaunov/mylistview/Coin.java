package com.person.v_plaunov.mylistview;

import java.util.Date;

public class Coin {
    String coinId = null;
    String coinNominal = null;
    String coinState = null;
    String coinImg = null;
    String coinTheme = null;
    String coinMint = null;
    String coinYear = null;
    String coinDiam = null;
    String coinWeight = null;
    String coinHeight = null;
    String coinPrice = null;
    String coinPriceForSale = null;
    String coinPriceBuy = null;
    Date coinDate = null;
    String coinSeller = null;
    String coinMetal = null;
    String coinQuality = null;
    Date coinCreated = null;
    String coinStorage = null;
    String coinEdge = null;
    String coinLoO = null;
    String coinLoR = null;
    String coinDescription = null;

    public Coin(String coinId, String coinNominal, String coinState, String coinYear, String coinTheme, String coinDescription) {
        super();
        this.coinId = coinId;
        this.coinNominal = coinNominal;
        this.coinState = coinState;
        this.coinImg = coinImg;
        this.coinTheme = coinTheme;
        this.coinMint = coinMint;
        this.coinYear = coinYear;
        this.coinDiam = coinDiam;
        this.coinWeight = coinWeight;
        this.coinHeight = coinHeight;
        this.coinPrice = coinPrice;
        this.coinPriceForSale = coinPriceForSale;
        this.coinPriceBuy = coinPriceBuy;
        this.coinDate = coinDate;
        this.coinSeller = coinSeller;
        this.coinMetal = coinMetal;
        this.coinQuality = coinQuality;
        this.coinCreated = coinCreated;
        this.coinStorage = coinStorage;
        this.coinEdge = coinEdge;
        this.coinLoO = coinLoO;
        this.coinLoR = coinLoR;
        this.coinDescription = coinDescription;
    }
    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public String getCoinNominal() {
        return coinNominal;
    }

    public void setCoinNominal(String coinNominal) {
        this.coinNominal = coinNominal;
    }

    public String getCoinState() {
        return coinState;
    }

    public void setCoinState(String coinState) {
        this.coinState = coinState;
    }

    public String getCoinImg() { return coinImg; }

    public void setCoinImg(String coinImg) { this.coinImg = coinImg; }

    public String getCoinTheme() {
        return coinTheme;
    }

    public void setCoinTheme(String coinTheme) {
        this.coinTheme = coinTheme;
    }

    public String getCoinMint() {
        return coinMint;
    }

    public void setCoinMint(String coinMint) {
        this.coinMint = coinMint;
    }

    public String getCoinYear() {
        return coinYear;
    }

    public void setCoinYear(String coinYear) {
        this.coinYear = coinYear;
    }

    public String getCoinDiam() {
        return coinDiam;
    }

    public void setCoinDiam(String coinDiam) { this.coinDiam = coinDiam; }

    public String getCoinWeight() {
        return coinWeight;
    }

    public void setCoinWeight(String coinWeight) {
        this.coinWeight = coinWeight;
    }

    public String getCoinHeight() { return coinHeight; }

    public void setCoinHeight(String coinHeight) { this.coinHeight = coinHeight; }

    public String getCoinPrice() {
        return coinPrice;
    }

    public void setCoinPrice(String coinPrice) {
        this.coinPrice = coinPrice;
    }

    public String getCoinPriceForSale() {
        return coinPriceForSale;
    }

    public void setCoinPriceForSale(String coinPriceForSale) { this.coinPriceForSale = coinPriceForSale; }

    public String getCoinPriceBuy() {
        return coinPriceBuy;
    }

    public void setCoinPriceBuy(String coinPriceBuy) {
        this.coinPriceBuy = coinPriceBuy;
    }

    public Date getcoinDate() {
        return coinDate;
    }

    public void setCoinDate(Date coinDate) {
        this.coinDate = coinDate;
    }

    public String getCoinSeller() {
        return coinSeller;
    }

    public void setCoinSeller(String coinSeller) {
        this.coinSeller = coinSeller;
    }

    public String getCoinMetal() {
        return coinMetal;
    }

    public void setCoinMetal(String coinMetal) {
        this.coinMetal = coinMetal;
    }

    public String getCoinQuality() {
        return coinQuality;
    }

    public void setCoinQuality(String coinQuality) {
        this.coinQuality = coinQuality;
    }

    public Date getCoinCreated() {
        return coinCreated;
    }

    public void setCoinCreated(Date coinCreated) {
        this.coinCreated = coinCreated;
    }

    public String getCoinStorage() {
        return coinStorage;
    }

    public void setCoinStorage(String coinStorage) {
        this.coinStorage = coinStorage;
    }

    public String getCoinEdge() {
        return coinEdge;
    }

    public void setCoinEdge(String coinEdge) {
        this.coinEdge = coinEdge;
    }

    public String getCoinLoO() {
        return coinLoO;
    }

    public void setCoinLoO(String coinLoO) {
        this.coinLoO = coinLoO;
    }

    public String getCoinLoR() {
        return coinLoR;
    }

    public void setCoinLoR(String coinLoR) {
        this.coinLoR = coinLoR;
    }

    public String getCoinDescription() {
        return coinDescription;
    }

    public void setCoinDescription(String coinDescription) { this.coinDescription = coinDescription; }

    @Override
    public String toString() {
        return  coinNominal + " " + coinState + " "
                + coinYear + " " + coinTheme + " " + coinDescription + " " + coinImg + " " + coinDate;
    }
}
