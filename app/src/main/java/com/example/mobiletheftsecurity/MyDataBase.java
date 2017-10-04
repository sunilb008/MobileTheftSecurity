package com.example.mobiletheftsecurity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDataBase {
	
	SQLiteDatabase sdb;
    MyHelper mh;
    
    
    public MyDataBase(Context con)
    {
    	mh=new MyHelper(con,"contacts", null,1);
    }
    
    public void open() 
    {
         sdb=mh.getWritableDatabase();
	}
    public void insertdata(String n, String m) 
    {
        ContentValues cv=new ContentValues();
        cv.put("num",n);
        cv.put("email",m);
        sdb.insert("contacts", null,cv);
		//Toast.makeText(,"Inserted data"+cv,Toast.LENGTH_LONG).show();
	}
    public Cursor retrieveData() 
    {
     Cursor c=sdb.query("contacts", null,null,null,null,null,null);
	   return c;
    }
	
	
	class MyHelper extends SQLiteOpenHelper
	{

		public MyHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("create table contacts(_id integer primary key," +
					"num text,email text);");
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
			
		}
		
	}

}
