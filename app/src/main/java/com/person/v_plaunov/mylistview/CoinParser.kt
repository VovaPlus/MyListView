package com.person.v_plaunov.mylistview

import org.xmlpull.v1.XmlPullParser

class CoinParser {
    val coins: ArrayList<Coin?>

    init {
        coins = ArrayList()
    }

    fun parse(xpp: XmlPullParser): Boolean {
        var status = true
        var currentCoin: Coin? = null
        var inEntry = false
        var textValue: String? = ""
        try {
            var eventType = xpp.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = xpp.name
                when (eventType) {
                    XmlPullParser.START_TAG -> if ("mony".equals(tagName, ignoreCase = true)) {
                        inEntry = true
                        currentCoin = Coin(null, null, null, null, null, null)
                    }
                    XmlPullParser.TEXT -> textValue = xpp.text
                    XmlPullParser.END_TAG -> if (inEntry) {
                        if ("mony".equals(tagName, ignoreCase = true)) {
                            coins.add(currentCoin)
                            inEntry = false
                        } else if ("id".equals(tagName, ignoreCase = true)) {
                            currentCoin!!.coinId = textValue
                        } else if ("name".equals(tagName, ignoreCase = true)) {
                            currentCoin!!.coinNominal = textValue
                        }
                    }
                    else -> {}
                }
                eventType = xpp.next()
            }
        } catch (e: Exception) {
            status = false
            e.printStackTrace()
        }
        return status
    }
}