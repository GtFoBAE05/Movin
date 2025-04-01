package com.imannuel.movin.intrabanktransferservice.utility;

import java.text.DecimalFormat;
import java.util.Random;

public class CurrencyUtility {
    public static String decimalAmountFormatter(Integer value) {
        DecimalFormat decimalFormat = new DecimalFormat("###0.00");
        return decimalFormat.format(value);
    }

    public static Integer generateUniqueCode(Integer minNumber){
        Random random = new Random();
        return random.nextInt(minNumber, 999);
    }
}
