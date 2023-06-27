package com.person.v_plaunov.mylistview

import java.util.*

class Coin(
    coinId: String?,
    coinNominal: String?,
    coinState: String?,
    coinYear: String?,
    coinTheme: String?,
    coinDescription: String?
) {
    var coinId: String? = null
    var coinNominal: String? = null
    var coinState: String? = null
    var coinImg: String? = null
    var coinTheme: String? = null
    var coinMint: String? = null
    var coinYear: String? = null
    var coinDiam: String? = null
    var coinWeight: String? = null
    var coinHeight: String? = null
    var coinPrice: String? = null
    var coinPriceForSale: String? = null
    var coinPriceBuy: String? = null
    @JvmField var coinDate: Date? = null
    var coinSeller: String? = null
    var coinMetal: String? = null
    var coinQuality: String? = null
    var coinCreated: Date? = null
    var coinStorage: String? = null
    var coinEdge: String? = null
    var coinLoO: String? = null
    var coinLoR: String? = null
    var coinDescription: String? = null

    init {
        this.coinId = coinId
        this.coinNominal = coinNominal
        this.coinState = coinState
        coinImg = coinImg
        this.coinTheme = coinTheme
        coinMint = coinMint
        this.coinYear = coinYear
        coinDiam = coinDiam
        coinWeight = coinWeight
        coinHeight = coinHeight
        coinPrice = coinPrice
        coinPriceForSale = coinPriceForSale
        coinPriceBuy = coinPriceBuy
        coinDate = coinDate
        coinSeller = coinSeller
        coinMetal = coinMetal
        coinQuality = coinQuality
        coinCreated = coinCreated
        coinStorage = coinStorage
        coinEdge = coinEdge
        coinLoO = coinLoO
        coinLoR = coinLoR
        this.coinDescription = coinDescription
    }

    fun getcoinDate(): Date? {
        return coinDate
    }

    fun setCoinDate(coinDate: Date?) {
        this.coinDate = coinDate
    }

    override fun toString(): String {
        return (coinNominal + " " + coinState + " "
                + coinYear + " " + coinTheme + " " + coinDescription + " " + coinImg + " " + coinDate)
    }
}