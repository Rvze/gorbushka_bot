package com.tsypk.coresupplier.exception

class IncorrectPriceException(price: Int) :
    RuntimeException("Got price $price is incorrect")

class NotIphoneIdException(input: String) :
    RuntimeException("Can not extract iphoneFullModel from input=$input")

class NotAirPodsIdException(input : String) :
        RuntimeException("Can not extract airpods full model from input=$input")