package com.person.v_plaunov.mylistview;

import android.text.TextUtils;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CoinFileParser {
    private ArrayList<Coin> coins;

    public CoinFileParser() {
        coins = new ArrayList<>();
    }

    public ArrayList<Coin> getCoins() {
        return coins;
    }

    public boolean parse(String fName) {
        boolean status = true;
        Coin currentCoin = null;
        boolean inEntry = false;
        String textValue = "";
        //InputStream is = null;
        //File f = null;

        try {
            InputStream is = new FileInputStream(fName);
            //f = new File(fName);
            //is = new FileInputStream(f);
            XmlPullParser xpp = Xml.newPullParser();
            xpp.setInput(is,null);
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("mony".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            String id = xpp.getAttributeValue(null,"id");
                            String name = xpp.getAttributeValue(null,"name");
                            String state = xpp.getAttributeValue(null, "parent");
                            String theme = xpp.getAttributeValue(null, "theme");
                            String mint = xpp.getAttributeValue(null, "monygroup");
                            String year = xpp.getAttributeValue(null, "year");
                            currentCoin = new Coin(id, name,state,year,theme, null);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(inEntry){
                            if("mony".equalsIgnoreCase(tagName)){
                                coins.add(currentCoin);
                                inEntry = false;
                            } else if("id".equalsIgnoreCase(tagName)){
                                currentCoin.setCoinId(textValue);
                            } else if("name".equalsIgnoreCase(tagName)){
                                currentCoin.setCoinNominal(textValue);
                            } else if ("main".equalsIgnoreCase(tagName)) {
                                String image = xpp.getAttributeValue(null,"image");
                                String storage = xpp.getAttributeValue(null,"placenumb");
                                currentCoin.setCoinImg(image);
                                currentCoin.setCoinStorage(storage);
                            } else if ("size".equalsIgnoreCase(tagName)) {
                                String diameter = xpp.getAttributeValue(null,"diameter");
                                if (!TextUtils.isEmpty(diameter))
                                    currentCoin.setCoinDiam(diameter);
                                String weight = xpp.getAttributeValue(null,"weight");
                                if (!TextUtils.isEmpty(weight))
                                    currentCoin.setCoinWeight(weight);
                                String height = xpp.getAttributeValue(null,"height");
                                if (!TextUtils.isEmpty(height))
                                    currentCoin.setCoinHeight(height);
                            } else if("description".equalsIgnoreCase(tagName)){
                                String description = xpp.getAttributeValue(null,"content");
                                currentCoin.setCoinDescription(description);
                            } else if ("market".equalsIgnoreCase(tagName)) {
                                String datebuy = xpp.getAttributeValue(null,"datebuy");
                                Integer interval = 0;
                                //Date date = fmt.parse(datebuy);
                                if (!TextUtils.isEmpty(datebuy)) {
                                    interval = Integer.parseInt(datebuy);
                                    SimpleDateFormat fmt = new SimpleDateFormat("dd.mm.yyyy");
                                    Calendar calend = Calendar.getInstance();
                                    calend.set(1970, 1, 1);
                                    calend.add(Calendar.SECOND, interval);
                                    String s = calend.toString();
                                    Date date = calend.getTime();
                                    currentCoin.setCoinDate(date);
                                }
                                // Получить атрибут costnumizmat
                                String costnumizmat = xpp.getAttributeValue(null,"costnumizmat");
                                if (!TextUtils.isEmpty(costnumizmat))
                                    currentCoin.setCoinPrice(costnumizmat);
                                // Получить атрибут costforsale
                                String costforsale = xpp.getAttributeValue(null,"costforsale");
                                if (!TextUtils.isEmpty(costforsale))
                                    currentCoin.setCoinPriceForSale(costforsale);
                                // Получить атрибут costbuy
                                String costbuy = xpp.getAttributeValue(null,"costbuy");
                                if (!TextUtils.isEmpty(costbuy))
                                    currentCoin.setCoinPriceBuy(costbuy);
                            } else if("averce".equalsIgnoreCase(tagName)){
                                String avlegend = xpp.getAttributeValue(null,"legend");
                                currentCoin.setCoinLoO(avlegend);
                            } else if("reverce".equalsIgnoreCase(tagName)){
                                String revlegend = xpp.getAttributeValue(null,"legend");
                                currentCoin.setCoinLoR(revlegend);
                            }
                        }
                        break;
                    default:
                }
                eventType = xpp.next();
            }
        }
        catch (Exception e){
            status = false;
            e.printStackTrace();
        }
        return  status;
    }
}

