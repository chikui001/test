//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.jiao.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class EmojiconEditText extends EditText {

	private int mEmojiconSize;
	private int mEmojiconAlignment;
	private int mEmojiconTextSize;
	private boolean mUseSystemDefault = false;

	public EmojiconEditText(Context context) {
		super(context);
		this.mEmojiconSize = (int) this.getTextSize();
		this.mEmojiconTextSize = (int) this.getTextSize();
	}

	public EmojiconEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init(attrs, context);
	}

	public EmojiconEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.init(attrs, context);
	}

	private void init(AttributeSet attrs, Context context) {
		this.mEmojiconSize = 56;
		this.mEmojiconAlignment = 1;
		this.mUseSystemDefault = false;
		this.mEmojiconTextSize = 36;
		this.setText(this.getText());
	}

	protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
		this.updateText();
	}

	public void setEmojiconSize(int pixels) {
		this.mEmojiconSize = pixels;
		this.updateText();
	}

	private void updateText() {
	}

	public void setUseSystemDefault(boolean useSystemDefault) {
		this.mUseSystemDefault = useSystemDefault;
	}
}
