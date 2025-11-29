package com.example.lab6_lab5_rimantek;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Date;
import java.util.Locale;

public class CurrencyRateTest {

    @Test
    public void constructor_and_getters_workCorrectly() {
        Date now = new Date();
        CurrencyRate rate = new CurrencyRate("EUR", "Euro", 0.859574, now);

        assertEquals("EUR", rate.getCode());
        assertEquals("Euro", rate.getName());
        assertEquals(0.859574, rate.getRate(), 0.000001);
        assertEquals(now, rate.getPublishedAt());
    }

    @Test
    public void toString_containsCodeRateAndName() {
        Locale.setDefault(Locale.US); // to make formatting deterministic
        CurrencyRate rate = new CurrencyRate("GBP", "British Pound", 0.789123, null);

        String text = rate.toString();

        assertTrue(text.contains("GBP"));
        assertTrue(text.contains("British Pound"));
        // string has "0.789123" with 6 decimals
        assertTrue(text.contains("0.789123"));
    }
}
