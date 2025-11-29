package com.example.lab6_lab5_rimantek;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

public class ParserTest {

    private static final String SAMPLE_XML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<rss version=\"2.0\">\n" +
                    "  <channel>\n" +
                    "    <item>\n" +
                    "      <targetCurrency>EUR</targetCurrency>\n" +
                    "      <targetName>Euro</targetName>\n" +
                    "      <exchangeRate>0.850000</exchangeRate>\n" +
                    "      <pubDate>Mon, 01 Jan 2024 12:00:00 GMT</pubDate>\n" +
                    "    </item>\n" +
                    "    <item>\n" +
                    "      <targetCurrency>JPY</targetCurrency>\n" +
                    "      <!-- no targetName here on purpose -->\n" +
                    "      <exchangeRate>110.123456</exchangeRate>\n" +
                    "      <pubDate>Tue, 02 Jan 2024 15:30:00 GMT</pubDate>\n" +
                    "    </item>\n" +
                    "  </channel>\n" +
                    "</rss>";

    @Test
    public void parse_validXml_returnsCurrencyRates() throws Exception {
        ByteArrayInputStream input =
                new ByteArrayInputStream(SAMPLE_XML.getBytes(StandardCharsets.UTF_8));

        List<CurrencyRate> rates = Parser.parse(input);

        assertNotNull(rates);
        assertEquals(2, rates.size());

        CurrencyRate eur = rates.get(0);
        assertEquals("EUR", eur.getCode());
        assertEquals("Euro", eur.getName());
        assertEquals(0.850000, eur.getRate(), 0.000001);
        assertNotNull(eur.getPublishedAt());

        CurrencyRate jpy = rates.get(1);
        assertEquals("JPY", jpy.getCode());
        // when targetName is missing, Parser uses code as name
        assertEquals("JPY", jpy.getName());
        assertEquals(110.123456, jpy.getRate(), 0.000001);
        assertNotNull(jpy.getPublishedAt());
    }

    @Test
    public void parse_skipsItemsWithInvalidRate() throws Exception {
        String xmlWithBadRate =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<rss version=\"2.0\">\n" +
                        "  <channel>\n" +
                        "    <item>\n" +
                        "      <targetCurrency>AAA</targetCurrency>\n" +
                        "      <targetName>BadRate</targetName>\n" +
                        "      <exchangeRate>not-a-number</exchangeRate>\n" +
                        "    </item>\n" +
                        "    <item>\n" +
                        "      <targetCurrency>EUR</targetCurrency>\n" +
                        "      <targetName>Euro</targetName>\n" +
                        "      <exchangeRate>0.9</exchangeRate>\n" +
                        "    </item>\n" +
                        "  </channel>\n" +
                        "</rss>";

        ByteArrayInputStream input =
                new ByteArrayInputStream(xmlWithBadRate.getBytes(StandardCharsets.UTF_8));

        List<CurrencyRate> rates = Parser.parse(input);

        // First item has invalid exchangeRate -> Parser should skip it
        assertEquals(1, rates.size());
        assertEquals("EUR", rates.get(0).getCode());
    }
}
