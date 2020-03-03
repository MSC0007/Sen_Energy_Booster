package sn.sonko.eb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity

{
	private static int SPLASH_TIME_OUT = 3000;
	
	 protected void onCreate(Bundle savedInstanceState) 
	     {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.splash);
	        
	        new Handler().postDelayed(new Runnable() {
				
				@Override
															public void run() 
															{
																// TODO Auto-generated method stub
																Intent i1 = new Intent(SplashScreen.this, Activite0.class);															
																
																startActivity(i1);	
																
																finish();
																
															}
									                 }, SPLASH_TIME_OUT
	        						);
	     }

	
	
	

}
