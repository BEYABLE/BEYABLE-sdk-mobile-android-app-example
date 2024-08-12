package com.beyable.sdkdemo.utils

import java.math.BigDecimal
import java.text.NumberFormat

/**
 *
 * Created by MarKinho on 08/08/2024.
 *
 * Wisepear Techlab
 * All rights reserved
 *
 **/

fun formatPrice(price: Double): String {
    return NumberFormat.getCurrencyInstance().format(
        BigDecimal(price).movePointLeft(2)
    )
}