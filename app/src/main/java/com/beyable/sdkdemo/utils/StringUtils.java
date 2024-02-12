package com.beyable.sdkdemo.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Gol D. Marko on 09/02/2024.
 * <p>
 **/

public class StringUtils {

    public static String doubleToPrice(double dbl,char currency) {
        Locale locale =null;
        if(currency=='€') {
            locale  = new Locale("fr", "FR");
        }else {
            locale  = new Locale("en", "EN");
        }//Add locales as per need.
        DecimalFormatSymbols sym = new DecimalFormatSymbols(locale);
        sym.setGroupingSeparator('.');
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        decimalFormat.applyPattern(currency+"##,###.00");
        decimalFormat.setDecimalFormatSymbols(sym);
        return decimalFormat.format(dbl);
    }
}
