package com.example.jiao.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jiao, <wenliang.jiao@extantfuture.com>
 * @date 2016年12月29日
 */
public class ImageLoaderTestActivity extends AppCompatActivity {

	private ListView testListView;

	private BaseAdapter baseAdapter;

	private RecyclerView testRecyclerView;
	private ImageAdapter imageAdapter;
	private List<String> list = new ArrayList<>();

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_loader_test);
		testListView = (ListView) findViewById(R.id.test_listview);
		testRecyclerView = (RecyclerView) findViewById(R.id.test_ry);

		list.add("http://img1.3lian.com/img013/v4/96/d/45.jpg");
		list.add("http://img5.duitang.com/uploads/item/201508/05/20150805165717_NRTfWi.thumb.700_0.jpeg");
		list.add("http://pic.58pic.com/58pic/15/67/59/23B58PIC6NG_1024.png");
		list.add("http://pic.58pic.com/58pic/13/12/24/21E58PICfx8_1024.jpg");
		list.add("http://img4.duitang.com/uploads/item/201403/08/20140308185645_f2hy4.thumb.600_0.jpeg");
		list.add("http://pic.58pic.com/58pic/15/01/86/60V58PICF8v_1024.jpg");
		list.add("http://pic.58pic.com/58pic/14/78/98/43U58PIC5te_1024.jpg");
		list.add("http://p3.gexing.com/qqpifu/20121004/1342/506d21b392ba9_600x.png");
		list.add("http://img3.fengniao.com/forum/attachpics/765/112/30582220_600.jpg");
		list.add("http://p2.gexing.com/G1/M00/D5/11/rBACE1WzZQST8x4tAAIPFBrgfN4878_600x.jpg");
		list.add("http://img3.fengniao.com/forum/attachpics/765/112/30582218_600.jpg");
		list.add("http://img5.duitang.com/uploads/item/201408/29/20140829194731_eL3Sz.thumb.700_0.jpeg");
		list.add("http://cdn.duitang.com/uploads/item/201201/28/20120128222542_nrnyi.thumb.600_0.jpg");
		list.add("http://img4.duitang.com/uploads/item/201409/06/20140906153515_KwLja.png");
		list.add("http://img2.ph.126.net/T6OwzNcCb-PTV1kEkvbCnQ==/6597201708051956069.jpg");
		list.add("http://imgwww.heiguang.net/uploadfile/2014/0819/20140819115116482.jpg");
		list.add("http://img5.duitang.com/uploads/item/201510/03/20151003082941_Ff3iR.jpeg");
		list.add(null);

		imageAdapter = new ImageAdapter(list);
		testRecyclerView.setAdapter(imageAdapter);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		testRecyclerView.setLayoutManager(linearLayoutManager);

		testListView.setAdapter(new ImageBaseAdapter(list));
	}

	private static class ImageBaseAdapter extends BaseAdapter {

		public LayoutInflater mInflater;
		private List<String> list = new ArrayList<>();

		public ImageBaseAdapter(List<String> list) {
			this.list = list;
			mInflater = LayoutInflater.from(MyApplication.getInstance());
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (null == convertView) {
				convertView = mInflater.inflate(R.layout.recycler_view_image_test, parent, false);
			}
			ImageView view = ViewHolder.get(convertView, R.id.test_iv);
			Picasso.with(MyApplication.getInstance()).load(list.get(position)).placeholder(R.drawable.ic_launcher).into(view);
			return convertView;
		}
	}

	private static class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {

		private List<String> list = new ArrayList<>();

		public ImageAdapter(List<String> list) {
			this.list = list;
		}

		@Override
		public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			ImageViewHolder viewHolder = new ImageViewHolder(
					LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_image_test, parent, false));
			return viewHolder;
		}

		@Override
		public void onBindViewHolder(ImageViewHolder holder, int position) {
			Picasso.with(MyApplication.getInstance()).load(list.get(position)).into(holder.testImageView);
		}

		@Override
		public int getItemCount() {
			return list.size();
		}
	}

	private static class ImageViewHolder extends RecyclerView.ViewHolder {

		private ImageView testImageView;

		public ImageViewHolder(View itemView) {
			super(itemView);
			testImageView = (ImageView) itemView.findViewById(R.id.test_iv);
		}
	}
}
