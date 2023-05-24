package com.person.v_plaunov.mylistview

import android.text.TextUtils
import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.FileInputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class CoinFileParser {
    val coins: ArrayList<Coin?>

    init {
        coins = ArrayList()
    }

    fun parse(fName: String?): Boolean {
        var status = true
        var currentCoin: Coin? = null
        var inEntry = false
        var textValue = ""
        //InputStream is = null;
        //File f = null;
        try {
            val `is`: InputStream = FileInputStream(fName)
            //f = new File(fName);
            //is = new FileInputStream(f);
            val xpp = Xml.newPullParser()
            xpp.setInput(`is`, null)
            var eventType = xpp.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = xpp.name
                when (eventType) {
                    XmlPullParser.START_TAG -> if ("mony".equals(tagName, ignoreCase = true)) {
                        inEntry = true
                        val id = xpp.getAttributeValue(null, "id")
                        val name = xpp.getAttributeValue(null, "name")
                        val state = xpp.getAttributeValue(null, "parent")
                        val theme = xpp.getAttributeValue(null, "theme")
                        val mint = xpp.getAttributeValue(null, "monygroup")
                        val year = xpp.getAttributeValue(null, "year")
                        currentCoin = Coin(id, name, state, year, theme, null)
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
                        } else if ("main".equals(tagName, ignoreCase = true)) {
                            val image = xpp.getAttributeValue(null, "image")
                            val storage = xpp.getAttributeValue(null, "placenumb")
                            currentCoin!!.coinImg = image
                            currentCoin.coinStorage = storage
                        } else if ("size".equals(tagName, ignoreCase = true)) {
                            val diameter = xpp.getAttributeValue(null, "diameter")
                            if (!TextUtils.isEmpty(diameter)) currentCoin!!.coinDiam = diameter
                            val weight = xpp.getAttributeValue(null, "weight")
                            if (!TextUtils.isEmpty(weight)) currentCoin!!.coinWeight = weight
                            val height = xpp.getAttributeValue(null, "height")
                            if (!TextUtils.isEmpty(height)) currentCoin!!.coinHeight = height
                        } else if ("description".equals(tagName, ignoreCase = true)) {
                            val description = xpp.getAttributeValue(null, "content")
                            currentCoin!!.coinDescription = description
                        } else if ("market".equals(tagName, ignoreCase = true)) {
                            val datebuy = xpp.getAttributeValue(null, "datebuy")
                            var interval = 0
                            //Date date = fmt.parse(datebuy);
                            if (!TextUtils.isEmpty(datebuy)) {
                                interval = datebuy.toInt()
                                val fmt = SimpleDateFormat("dd.mm.yyyy")
                                val calend = Calendar.getInstance()
                                calend[1970, 1] = 1
                                calend.add(Calendar.SECOND, interval)
                                val s = calend.toString()
                                val date = calend.time
                                currentCoin!!.setCoinDate(date)
                            }
                            // Получить атрибут costnumizmat
                            val costnumizmat = xpp.getAttributeValue(null, "costnumizmat")
                            if (!TextUtils.isEmpty(costnumizmat)) currentCoin!!.coinPrice =
                                costnumizmat
                            // Получить атрибут costforsale
                            val costforsale = xpp.getAttributeValue(null, "costforsale")
                            if (!TextUtils.isEmpty(costforsale)) currentCoin!!.coinPriceForSale =
                                costforsale
                            // Получить атрибут costbuy
                            val costbuy = xpp.getAttributeValue(null, "costbuy")
                            if (!TextUtils.isEmpty(costbuy)) currentCoin!!.coinPriceBuy = costbuy
                        } else if ("averce".equals(tagName, ignoreCase = true)) {
                            val avlegend = xpp.getAttributeValue(null, "legend")
                            currentCoin!!.coinLoO = avlegend
                        } else if ("reverce".equals(tagName, ignoreCase = true)) {
                            val revlegend = xpp.getAttributeValue(null, "legend")
                            currentCoin!!.coinLoR = revlegend
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