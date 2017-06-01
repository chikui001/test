package com.example.jiao.myapplication;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2017年05月10日
 */
public class LoginPresenter {

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public UserManager userManager;

	public void login(String a, String b) {
		userManager.performLogin(a, b);
	}
}
