package com.person.v_plaunov.mylistview;

import org.xmlpull.v1.XmlPullParser;
import java.util.ArrayList;

public class CoinParser {
    private ArrayList<Coin> coins;

    public CoinParser() {
        coins = new ArrayList<>();
    }

    public ArrayList<Coin> getCoins() {
        return coins;
    }

    public boolean parse(XmlPullParser xpp) {
        boolean status = true;
        Coin currentCoin = null;
        boolean inEntry = false;
        String textValue = "";

        try {
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("mony".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            currentCoin = new Coin(null,null,null,null,null, null);
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

