package com.example.jiao.myapplication.views;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2016年11月29日
 */
public enum InquiryGuideType {

	/**
	 * 问诊tab页
	 */
	COPY_INQUIRY_TAB(1),
	/**
	 * 预问诊页
	 */
	COPY_PRE_INQUIRY(2),
	/**
	 * 问诊中页
	 */
	COPY_INQUIRY(3),
	/**
	 * 免责生命
	 */
	DISCLAIMER(4),;

	private int type;

	InquiryGuideType(int type) {
		this.type = type;
	}
}
