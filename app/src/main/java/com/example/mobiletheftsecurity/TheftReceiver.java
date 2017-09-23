package com.example.mobiletheftsecurity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.sax.StartElementListener;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class TheftReceiver extends BroadcastReceiver {
	MyDataBase mdb;
	String num;
	SharedPreferences sp;
	TelephonyManager tm;
	String simserial1, simserial2 = "";
	String network = "";
	LocationManager lm;
	Context c;
	double c_lat;
	double c_long;

	String address;

	LocationListener ll = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			c_lat = location.getLatitude();

			c_long = location.getLongitude();
			getAddress();
			Toast.makeText(c, c_lat + "," + c_long, Toast.LENGTH_LONG).show();
			Intent in = new Intent(c, ChangePassword.class);
			in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			c.startActivity(in);
		}
	};

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		c = context;
		Toast.makeText(context, "boot received", Toast.LENGTH_LONG).show();
		turnGPSOn(context);
		tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0, ll);
		lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		simserial1=tm.getSimSerialNumber();
		network=tm.getNetworkOperatorName();
		mdb=new MyDataBase(context);
		mdb.open();
		Cursor c=mdb.retrieveData();
		sp=context.getSharedPreferences("simDetails",0);
		simserial2=sp.getString("simno",null);

		while (c.moveToNext()) {
		num=c.getString(1);	
		}
		if(!simserial1.equals(simserial2))
		{
        String messageToSend = ("I lost my phone and the new network is "+network+"/n"+"The latitude and longitude is:"+c_lat+", "+c_long+"/n"+"Adddress:"+address);
        SmsManager.getDefault().sendTextMessage(num, null,
                messageToSend, null, null);
		}
	}
	
	private void turnGPSOn(Context con)	
	{   
		Toast.makeText(con, "GPS On",Toast.LENGTH_LONG).show();

	    String provider = Settings.Secure.getString(con.getContentResolver(), 
	    		Settings.Secure.LOCATION_PROVIDERS_ALLOWED);   
	    if(!provider.contains("gps"))
	    {      
	        final Intent poke = new Intent();  
	        poke.setClassName("com.android.settings","com.android.settings.widget.SettingsAppWidgetProvider");        
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);   
	        poke.setData(Uri.parse("3"));      
	        con.sendBroadcast(poke);  
	     }
	  }
	
	
	void getAddress(){
		
        try{
            Geocoder gcd = new Geocoder(c, Locale.getDefault());
            List<Address> addresses = 
                gcd.getFromLocation(c_lat, c_long,1);
            if (addresses.size() > 0) {
                StringBuilder result = new StringBuilder();
                for(int i = 0; i < addresses.size(); i++){
                    Address address =  addresses.get(i);
                    int maxIndex = address.getMaxAddressLineIndex();
                    for (int x = 0; x <= maxIndex; x++ ){
                        result.append(address.getAddressLine(x));
                        result.append(",");
                    }               
                  
                }
                address=result.toString();
                Toast.makeText(c,address,Toast.LENGTH_LONG).show();
            }
        }
        catch(IOException ex){
            Toast.makeText(c,address,Toast.LENGTH_LONG).show();

        }
    }
	

}
