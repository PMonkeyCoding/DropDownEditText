package com.example.dropedittext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

@SuppressLint("ClickableViewAccessibility")
public class MainActivity extends Activity implements OnClickListener {

	private ImageView dropview_image;
	private EditText dropview_edit;
	private ListView dropview_list;
	private ListAdapter listAdapter;
	public static MainActivity staticMainActivity;
	public Drawable draw_user;
	private Set<String> setStr;
	private List<String> listStr;
	public PopupWindow popupWindow;
	public Button dropview_button;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		staticMainActivity = this;
		//初始化界面
		initView();
		
		//初始化下拉列表中数据，以防getSharedPreferences抛null异常
		setStr = new HashSet<String>();
		
		//设置dropview_edit左边的图片
		draw_user = getResources().getDrawable(R.drawable.username);
		draw_user.setBounds(1, 1, 60, 60);//第一0是距左边距离，第二0是距上边距离，50分别是长宽
		dropview_edit.setCompoundDrawables(draw_user, null, null, null);//将图片放在EditText左边
		
		dropview_button.setOnClickListener(this);
		dropview_image.setOnClickListener(this);
	}
	private void initView(){
		dropview_edit = (EditText)this.findViewById(R.id.dropview_edit);
		dropview_image = (ImageView)this.findViewById(R.id.dropview_image);
		//保存数据的按钮，保存的数据将显示在下拉列表中
		dropview_button = (Button)this.findViewById(R.id.dropview_button);
	}
	/**
	 * 存储editText里的信息到SharePreferences
	 * @param str 需要保存的数据
	 */
	private void restoreRecord(String str){
		SharedPreferences sha = getSharedPreferences("ForME",
				Context.MODE_PRIVATE);
		Editor editor = sha.edit();
		setStr = sha.getStringSet("userName", setStr);
		setStr.add(str);
		for(int i = 0;i < 20;i++){
			setStr.add("qwertyuiopasdfghjklzxcvbnm"+i);
		}
		
		editor.putStringSet("userName", setStr);
		editor.commit();
		initRecord();
	}
	/**
	 * 初始化下拉列表的数据，以备下拉列表时使用
	 */
	private void initRecord(){
		listStr = new ArrayList<String>();
		SharedPreferences sha = getSharedPreferences("ForME",
				Context.MODE_PRIVATE);
		setStr = sha.getStringSet("userName", setStr);
		Iterator<String> it=setStr.iterator();
		while(it.hasNext())
	    {
			String str=(String)it.next();
			listStr.add(str);
	    }      
	}
	/**
	 * 删除Sharepreferences中选中的数据
	 * @param str 需要删除的数据
	 */
	public void deleteRecord(String str){
		SharedPreferences sha = getSharedPreferences("ForME",
				Context.MODE_PRIVATE);
		Editor editor = sha.edit();
		setStr = sha.getStringSet("userName", setStr);
		if(setStr.contains(str)){
			setStr.remove(str);
		}
		editor.putStringSet("userName", setStr);
		editor.commit();
		initRecord();
	}
	/**
	 * 显示下拉列表的popupWindow弹框
	 * @param view 弹出的popupWindow参照的view
	 */
	@SuppressWarnings("deprecation")
	private void showPopupWindow(View view){
		View contentView = LayoutInflater.from(MainActivity.this).inflate(
                R.layout.popup_list, null);
		dropview_list = (ListView)contentView.findViewById(R.id.dropview_list);
		listAdapter = new ListAdapter(listStr);
		dropview_list.setAdapter(listAdapter);
		dropview_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long arg3) {
				dropview_edit.setText(listStr.get(position).toString());
				popupWindow.dismiss();
			}
		});
		popupWindow = new PopupWindow(contentView,
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
		popupWindow.setOutsideTouchable(true);
		
		popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
	            return false;
			}
        });
        //如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        // 设置好参数之后再show
        popupWindow.showAsDropDown(view, 0, 0);
        popupWindow.setAnimationStyle(R.style.AnimationFade);
	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		// 主页中保存数据的button
		if(view.getId() == R.id.dropview_button){
			restoreRecord(dropview_edit.getText().toString());
			Toast.makeText(staticMainActivity, "保存成功", Toast.LENGTH_LONG).show();
		}
		//弹出下拉列表的image
		else if(view.getId() == R.id.dropview_image){
			//获取所有列表中的数据
			initRecord();
			showPopupWindow(view);
		}
	}
}