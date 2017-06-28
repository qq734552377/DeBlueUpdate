package ucast.com.deblueupdate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
	private String[] installNames;
	private LayoutInflater inflate;
	private int[] states;
	private ArrayList<AppEntity> lists;
	public MyAdapter(Context context, String[] installNames, int states[]) {
		super();
		this.installNames = installNames;
		this.states=states;
		inflate=LayoutInflater.from(context);
	}

	public MyAdapter(Context context, ArrayList<AppEntity> lists){
		this.inflate=LayoutInflater.from(context);
		this.lists=lists;
	}

	public MyAdapter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		AppEntity appEntity=lists.get(position);
		if(convertView==null){
			holder=new ViewHolder();
			convertView =inflate.inflate(R.layout.listview_item, null);
			holder.tv=(TextView) convertView.findViewById(R.id.tv);
			holder.iv=(ImageView) convertView.findViewById(R.id.iv);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
			
		}
		holder.tv.setText(appEntity.getInstallName());
		holder.iv.setBackgroundResource(appEntity.getState());
		return convertView;
	}
	class ViewHolder{
		TextView tv;
		ImageView iv;
	}
}
