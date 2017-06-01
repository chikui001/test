package com.example.jiao.myapplication.views;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;
import com.example.jiao.myapplication.R;

public class BitmapShaders extends View {

	private BitmapShader bitmapShader = null;
	private Bitmap bitmap = null;
	private Paint paint = null;
	private ShapeDrawable shapeDrawable = null;
	private int BitmapWidth = 0;
	private int BitmapHeight = 0;

	public BitmapShaders(Context context) {
		super(context);
		//得到图像
		bitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.icon)).getBitmap();
		BitmapWidth = bitmap.getWidth();
		BitmapHeight = bitmap.getHeight();
		//构造渲染器BitmapShader
		bitmapShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
	}

	public BitmapShaders(Context context, AttributeSet attrs) {
		super(context, attrs);
		bitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.icon)).getBitmap();
		BitmapWidth = bitmap.getWidth();
		BitmapHeight = bitmap.getHeight();
		//构造渲染器BitmapShader
		bitmapShader = new BitmapShader(bitmap, Shader.TileMode.MIRROR, Shader.TileMode.REPEAT);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//将图片裁剪为椭圆形
		//构建ShapeDrawable对象并定义形状为椭圆
		shapeDrawable = new ShapeDrawable(new OvalShape());
		//得到画笔并设置渲染器
		shapeDrawable.getPaint().setShader(bitmapShader);
		//设置显示区域
		shapeDrawable.setBounds(20, 20, BitmapWidth - 60, BitmapHeight - 60);
		//绘制shapeDrawable
		shapeDrawable.draw(canvas);
	}

}