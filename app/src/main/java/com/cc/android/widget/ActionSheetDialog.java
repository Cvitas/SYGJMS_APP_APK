package com.cc.android.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

import com.cc.android.R;

public class ActionSheetDialog implements OnItemClickListener
{

	private OnSheetItemClickListener onSheetItemClickListener;
	private ArrayList<SheetData> al;
	private SheetAdapter adapter;
	private ScrollView scrollView;
	private Button cancel;
	private ListView listView;
	private TextView title;
	private Context context;
	private DisplayMetrics dm;
	public Dialog dialog;
	// 屏幕宽度和高度。
	private int screen_width = 0;
	private int screen_height = 0;

	/**
	 * @param context
	 *            上下文对象。必须传递Activity本身，因为只有一个Activity才能添加一个Dialog窗体。
	 * @param al
	 *            存放有每一项名字字符串的字符串数组。
	 */
	public ActionSheetDialog(Context context, ArrayList<SheetData> al)
	{
		this.context = context;
		dm = context.getResources().getDisplayMetrics();
		this.al = al;
		adapter = new SheetAdapter(context, al);
		// 获取屏幕宽度和高度。
		screen_width = dm.widthPixels;
		screen_height = dm.heightPixels;
	}

	public ActionSheetDialog builder(String titleName)
	{
		// 获取Dialog布局
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_actionsheet_layout, null);

		title = (TextView) view.findViewById(R.id.actionsheet_title);
		title.setText(titleName);

		// 获取自定义Dialog布局中的控件
		scrollView = (ScrollView) view.findViewById(R.id.actionsheet_scroll);
		cancel = (Button) view.findViewById(R.id.actionsheet_btn_cancel);
		cancel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				if (onSheetItemClickListener != null)
				{
					onSheetItemClickListener.onCancel();
				}
				dialog.dismiss();
			}
		});
		listView = (ListView) view.findViewById(R.id.actionsheet_list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		new ListViewHeight().setListViewHeightBasedOnChildren(listView);
		// 设置Dialog最小宽度为屏幕宽度
		view.setMinimumWidth(screen_width);

		// 定义Dialog布局和参数
		dialog = new Dialog(context, R.style.ActionDialogStyle);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(view);
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.x = 0;
		lp.y = 0;
		dialogWindow.setAttributes(lp);
		return this;
	}

	public void show()
	{
		if (al.size() > 7)
		{
			LinearLayout.LayoutParams params = (LayoutParams) scrollView.getLayoutParams();
			params.height = screen_height / 2;
			scrollView.setLayoutParams(params);
		}
		dialog.show();
	}

	public interface OnSheetItemClickListener
	{
		/** 通过接口向调用者返还的数据。
		 * @param
		 *
		 */
		void onSheetItemClick(int index, SheetData data);

		/**
		 * 点击取消按钮回调
		 */
		void onCancel();
	}

	public class ListViewHeight
	{
		/**
		 * @param listView
		 *            传入需要设置高度的ListView，通过计算后自动给传入的ListView设置高度。
		 */
		public void setListViewHeightBasedOnChildren(ListView listView)
		{
			ListAdapter listAdapter = listView.getAdapter();
			if (listAdapter == null)
			{
				// pre-condition
				return;
			}
			int totalHeight = 0;
			for (int i = 0; i < listAdapter.getCount(); i++)
			{
				View listItem = listAdapter.getView(i, null, listView);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}
			ViewGroup.LayoutParams params = listView.getLayoutParams();
			params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
			listView.setLayoutParams(params);
		}
	}

	private class SheetAdapter extends BaseAdapter
	{
		private Context context;
		private List<SheetData> al;
		private LayoutInflater inflater;

		public SheetAdapter(Context cont, List<SheetData> al)
		{
			this.context = cont;
			this.al = al;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount()
		{
			return al.size();
		}

		@Override
		public Object getItem(int position)
		{
			return al.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			Tag tag = new Tag();
			SheetData sheetData = (SheetData) this.getItem(position);
			if (convertView == null)
			{
				if (sheetData.pos == 0)
				{
					convertView = LayoutInflater.from(context).inflate(R.layout.dialog_actionsheet_item_0, null);
				}
				else if(sheetData.pos == 1)
				{
				}
				else if (sheetData.pos == 2)
				{
					convertView = LayoutInflater.from(context).inflate(R.layout.dialog_actionsheet_item_2, null);
				}
			}
			tag.name = (TextView) convertView.findViewById(R.id.actionsheet_item_name);
			tag.name.setText(sheetData.name);
			tag.icon = (ImageView) convertView.findViewById(R.id.actionsheet_item_icon);
			if (sheetData.iconRes == 0)
			{
				tag.icon.setVisibility(View.GONE);
			}
			else
			{
				tag.icon.setVisibility(View.VISIBLE);
				tag.icon.setImageResource(sheetData.iconRes);
			}
			return convertView;
		}

		class Tag
		{
			TextView name;
			ImageView icon;
		}
	}

	public class SheetData
	{
		public int iconRes = 0;
		public String name;
		//0居中 1 图片在前面 2 图片在后面
		public int pos = 0;
		public Object object;

		public SheetData(int iconRes, String name,int pos)
		{
			super();
			this.iconRes = iconRes;
			this.name = name;
			this.pos =pos;
		}

		public SheetData(int iconRes, String name,int pos,Object object)
		{
			super();
			this.iconRes = iconRes;
			this.name = name;
			this.pos =pos;
			this.object = object;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		if (onSheetItemClickListener != null)
		{
			SheetData sheetData = al.get(position);
			if (sheetData.pos == 0)
				dialog.dismiss();
			onSheetItemClickListener.onSheetItemClick(position, al.get(position));
		}
	}

	public void setOnSheetItemClickListener(OnSheetItemClickListener Listener)
	{
		onSheetItemClickListener = Listener;
	}

}
