package com.example.dropedittext;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "ViewHolder", "InflateParams" })
public class ListAdapter extends BaseAdapter{

	private TextView list_text;
	private ImageView list_image;
	private List<String> listStr;
	private String str ; 
	public  ListAdapter(List<String> listStr) {
		this.listStr = listStr;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listStr.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = LayoutInflater.from(MainActivity.staticMainActivity).inflate(
					R.layout.listview_list, null);
		list_text = (TextView)convertView.findViewById(R.id.list_text);
		list_image = (ImageView)convertView.findViewById(R.id.list_image);
		list_text.setText(listStr.get(position));
		list_image.setTag(list_text.getText());
		list_image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				str = view.getTag().toString();
				// TODO Auto-generated method stub
				AlertDialog.Builder dialog = new Builder(MainActivity.staticMainActivity);
				dialog.setMessage("确认删除"+str+"吗？");
				dialog.setTitle("提示");
				dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(MainActivity.staticMainActivity, "删除了 "+str+" 的数据", Toast.LENGTH_LONG).show();
						MainActivity.staticMainActivity.deleteRecord(str);
						MainActivity.staticMainActivity.popupWindow.dismiss();
					}
				});
				dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						arg0.dismiss();
					}
				});
				dialog.create().show();
			}
		});
		return convertView;
		
	}

}
