package com.example.lab6_lab5_rimantek;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;

import android.widget.ListView;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

@RunWith(AndroidJUnit4.class)
public class MainActivityUiTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void title_isShownOnLaunch() {
        onView(withId(R.id.textTitle))
                .check(matches(isDisplayed()))
                .check(matches(withText("USD exchange rates")));
    }

    @Test
    public void typingFilter_filtersVisibleListItems() {
        Locale.setDefault(Locale.US);

        activityRule.getScenario().onActivity(activity -> {
            CurrencyRate eur = new CurrencyRate("EUR", "Euro", 0.85, new Date());
            CurrencyRate gbp = new CurrencyRate("GBP", "British Pound", 0.75, new Date());
            CurrencyRate jpy = new CurrencyRate("JPY", "Japanese Yen", 110.123, new Date());

            ListView listView = activity.findViewById(R.id.listRates);
            CurrencyRateAdapter adapter = (CurrencyRateAdapter) listView.getAdapter();
            adapter.replaceAll(Arrays.asList(eur, gbp, jpy));
        });

        onView(withId(R.id.listRates)).check(matches(isDisplayed()));

        onView(withId(R.id.editFilter))
                .perform(typeText("eur"), closeSoftKeyboard());

        onData(anything())
                .inAdapterView(withId(R.id.listRates))
                .atPosition(0)
                .onChildView(withId(R.id.textCurrencyCode))
                .check(matches(withText("EUR")));
    }

    @Test
    public void filterByNameAlsoWorks() {
        Locale.setDefault(Locale.US);

        activityRule.getScenario().onActivity(activity -> {
            CurrencyRate eur = new CurrencyRate("EUR", "Euro", 0.85, new Date());
            CurrencyRate gbp = new CurrencyRate("GBP", "British Pound", 0.75, new Date());
            CurrencyRate jpy = new CurrencyRate("JPY", "Japanese Yen", 110.123, new Date());

            ListView listView = activity.findViewById(R.id.listRates);
            CurrencyRateAdapter adapter = (CurrencyRateAdapter) listView.getAdapter();
            adapter.replaceAll(Arrays.asList(eur, gbp, jpy));
        });

        onView(withId(R.id.editFilter))
                .perform(typeText("pound"), closeSoftKeyboard());

        onData(anything())
                .inAdapterView(withId(R.id.listRates))
                .atPosition(0)
                .onChildView(withId(R.id.textCurrencyCode))
                .check(matches(withText("GBP")));

        onData(anything())
                .inAdapterView(withId(R.id.listRates))
                .atPosition(0)
                .onChildView(withId(R.id.textCurrencyName))
                .check(matches(withText(containsString("British Pound"))));
    }
}
