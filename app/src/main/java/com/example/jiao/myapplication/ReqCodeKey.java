// Copyright (c) 2015. ExtantFuture Inc. All Rights Reserved.
/**
 *
 */
package com.example.jiao.myapplication;

/**
 * Can only use lower 16 bits for requestCode
 * so 高八位使用01
 *
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2015年12月10日
 */
public class ReqCodeKey {

	public static final int REQUEST_CODE_NICKNAME = 0x0101;

	public static final int REQUEST_CODE_CAMERA = 0x0102;// 拍照修改头像
	public static final int REQUEST_CODE_LOCATION = 0x0103;// 本地相册修改头像
	public static final int REQUEST_CODE_CROP = 0x0104;// 系统裁剪头像

	public static final int REQUEST_CODE_LOCATION_NEW = 0x0106;

	public static final int REQUEST_FIND_HEART_POSITION = 0x0105;

}
