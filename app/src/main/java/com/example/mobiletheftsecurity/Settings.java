package com.example.mobiletheftsecurity;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Settings extends Activity {
	EditText et1,et2;
	MyDataBase mdb;
	Button b;
    String n;
    String m;
    String message;
    ListView lv;
    MyAdapter ma;
    ArrayList<String> al1,al2;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_panicsettings);
		mdb=new MyDataBase(this);
		mdb.open();
		al1=new ArrayList<String>();
		al2=new ArrayList<String>();
	    et1=(EditText) findViewById(R.id.phone1);
	    et2=(EditText) findViewById(R.id.mail1);
        b=(Button) findViewById(R.id.save_details);
        lv=(ListView) findViewById(R.id.listView1);
        ma=new MyAdapter();
        lv.setAdapter(ma);
		updateData();

        b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				n=et1.getText().toString();
				m=et2.getText().toString();
				mdb.insertdata(n,m);
				updateData();
			}
		});
	}
	
	public void updateData() {
      Cursor c=mdb.retrieveData();
      al1.clear();
      al2.clear();
      if(c!=null)
      {
    	  while(c.moveToNext())
    	  {
    		  al1.add(c.getString(1));
    		  al2.add(c.getString(2));
    	  }
      }
      ma.notifyDataSetChanged();
      
	}
	
	class MyAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return al1.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return al1.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			RelativeLayout rl=(RelativeLayout) getLayoutInflater().inflate(R.layout.custom,parent,false);
			TextView tv1=(TextView) rl.findViewById(R.id.textView1);
			TextView tv2=(TextView) rl.findViewById(R.id.textView2);
			tv1.setText(al1.get(position));
			tv2.setText(al2.get(position));
			return rl;
		}
		
	}

}
