package com.jms.scan.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jms.scan.R;
import com.jms.scan.util.common.SoftInputUtil;


public class MyWindow extends Dialog {

	private TextView title, message;
	private EditText inputET;
	private ImageView icon;
	private Button canale, sure, ok;
	private LinearLayout btLayout;
	private View contentView, buttomLine;
	private Context context;
	private int buttons;
	private String btMessage;
	private View.OnClickListener listener;
	private View.OnClickListener nullListener = new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			dismiss();
		}
	};
	private LinearLayout ll_dialog_title;

	public MyWindow(Context context) {
		super(context, R.style.flashmode_DialogTheme);
		this.context = context;
		buttons = 0;
		initView();
		Window window = getWindow();

	}

	private void initView() {
		contentView = View.inflate(context, R.layout.mydialog_layout, null);
		title = (TextView) contentView.findViewById(R.id.tv_dialog_title);
		ll_dialog_title = (LinearLayout) contentView.findViewById(R.id.ll_dialog_title);
		message = (TextView) contentView.findViewById(R.id.bt_dialog_message);
		inputET = (EditText) contentView.findViewById(R.id.et_input);
		btLayout = (LinearLayout) contentView.findViewById(R.id.ll_button_layout);
		canale = (Button) contentView.findViewById(R.id.bt_cancel_button);
		sure = (Button) contentView.findViewById(R.id.bt_sure_button);
		ok = (Button) contentView.findViewById(R.id.bt_dialog_ok);
		icon = (ImageView) contentView.findViewById(R.id.iv_dialog_title);
		buttomLine = contentView.findViewById(R.id.v_buttom_line);
	}

	public void setPositiveButton(int id, View.OnClickListener listener) {
		if (listener == null) {
			listener = this.nullListener;
		}
		buttons += 1;
		String btMessage = context.getString(id);
		this.listener = listener;
		this.btMessage = btMessage;
		sure.setText(btMessage);
		sure.setOnClickListener(listener);
	}

	public void setPositiveButton(String btMessage, View.OnClickListener listener) {
		if (listener == null) {
			listener = this.nullListener;
		}
		buttons += 1;
		this.listener = listener;
		this.btMessage = btMessage;
		setPositiveButton(btMessage);
		sure.setOnClickListener(listener);
	}

	public void setPositiveButton(String btMessage) {
		sure.setText(btMessage);
	}

	public void setNegativeButton(int id, View.OnClickListener listener) {
		if (listener == null) {
			listener = this.nullListener;
		}
		buttons += 1;
		String btMessage = context.getString(id);
		this.listener = listener;
		this.btMessage = btMessage;
		canale.setText(btMessage);
		canale.setOnClickListener(listener);
	}

	public void setNegativeButton(String btMessage, View.OnClickListener listener) {
		if (listener == null) {
			listener = this.nullListener;
		}
		buttons += 1;
		this.listener = listener;
		this.btMessage = btMessage;
		canale.setText(btMessage);
		canale.setOnClickListener(listener);
	}

	@Override
	public void setTitle(int id) {
		title.setText(context.getString(id));
	}

	public void setTitle(String messag) {
		title.setText(messag);
	}

	public void setMessage(int id) {
		message.setText(context.getString(id));
	}

	public void setMessage(String messag) {
		message.setText(messag);
	}

	public void showInput() {
		message.setVisibility(View.GONE);
		inputET.setVisibility(View.VISIBLE);
		SoftInputUtil.showInputMethod(context, inputET);
	}

	public void setInput(String input) {
		inputET.setText(input);
	}

	public void setInputWatch(TextWatcher watcher) {
		message.setVisibility(View.GONE);
		inputET.setVisibility(View.VISIBLE);
		inputET.addTextChangedListener(watcher);
	}

	public void setInputHint(String hint) {
		inputET.setHint(hint);
	}

	public String getInput() {
		return inputET.getText().toString();
	}

	public void setIcon(int resId) {
		icon.setVisibility(View.VISIBLE);
		icon.setImageResource(resId);
	}

	public interface OnMultiChoiceClickListener {
		public void doInBack(boolean[] isSelect);
	};

	@Override
	public void show() {
		if (buttons >= 2) {
			btLayout.setVisibility(View.VISIBLE);
			ok.setVisibility(View.GONE);
		} else if (buttons == 1) {
			btLayout.setVisibility(View.GONE);
			ok.setVisibility(View.VISIBLE);
			ok.setText(btMessage);
			ok.setOnClickListener(listener);
		} else {
			ok.setVisibility(View.GONE);
			buttomLine.setVisibility(View.GONE);
		}
		setContentView(contentView);
		super.show();
	}

	public void hideTitle() {
		ll_dialog_title.setVisibility(View.GONE);
	}

}
