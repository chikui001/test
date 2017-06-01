package com.example.jiao.myapplication;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.example.jiao.myapplication.activity.LoginActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginTest {

	@Rule
	public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);

	@Test
	public void loginWithWrongPassword() {
		onView(withId(R.id.name)).perform(typeText("android"), closeSoftKeyboard());
		onView(withId(R.id.password)).perform(typeText("wrong"), closeSoftKeyboard());
		onView(withId(R.id.submit)).perform(click());

		onView(withId(R.id.result)).check(matches(withText("登录失败")));

	}

	@Test
	public void loginWithRightPassword() {
		onView(withId(R.id.name)).perform(typeText("android"), closeSoftKeyboard());
		onView(withId(R.id.password)).perform(typeText("123456"), closeSoftKeyboard());
		onView(withId(R.id.submit)).perform(click());

		onView(withId(R.id.result)).check(matches(withText("登录成功")));

	}
}