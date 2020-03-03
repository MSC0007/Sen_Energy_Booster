package sn.sonko.eb;

import java.text.DateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class Activite0 extends Activity

{
	private TextView tvdt0 = null;
	private LinearLayout arbrexml = null;
	private Button conn, activer, desactiver = null;	
	private BluetoothAdapter BA ;
	private static long back_pressed;	
	private final static int IDENTIFIANT_BOITE  = 0;
   

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    
	    {
	        super.onCreate(savedInstanceState);	      
	        arbrexml = (LinearLayout)LinearLayout.inflate(this, R.layout.layout0,null);
	        
	        BA = BluetoothAdapter.getDefaultAdapter();	      
	        tvdt0 = (TextView) arbrexml.findViewById(R.id.heure0);
	        conn = (Button) arbrexml.findViewById(R.id.conn_bt1);
	        activer = (Button) arbrexml.findViewById(R.id.bt_activer);
	        desactiver = (Button) arbrexml.findViewById(R.id.bt_eteindre);
	      
	        
	        Calendar calendrier = Calendar.getInstance();
		    String  currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendrier.getTime());								
		    tvdt0.setText(currentDate);
		    
	//-------------------------------------ACTIVATION_BLUETOOTH----------------------------------------------        
	        if (!BA.isEnabled()) 
		        {									          
		           Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		           startActivityForResult(getVisible, 0);
		           Toast.makeText(getApplicationContext(), "Activation Bluetooth",Toast.LENGTH_LONG).show();		           
		        } 
        
	        else 
		        {														        	  
		           Toast.makeText(getApplicationContext(), "Bluetooth Déja Activé", Toast.LENGTH_LONG).show();
		        }
	        
	     
	     
	//----------------------------LISTE_PERIPH_BLUETOOTH-----------------------------------------------------------
	      conn.setOnClickListener(new OnClickListener() 
										   
	       									{												
												@Override
												public void onClick(View v)
													{
												            
																		if (BA.isEnabled()) 
																			{																
																				Intent i0 = new Intent(getApplicationContext(), Activite1.class);																				
					     											            startActivity(i0);	
																			    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);																			  
																			}
																		
																		else 
																			{
																			   Toast.makeText(getApplicationContext(), "Veuillez activer le Bluetooth !", Toast.LENGTH_LONG).show();	
																			}
																
													 }	
													
											}
	    		   					); 
	      
  //---------------------------------------ACTIVER_BT_AVEC_BOUTON--------------------------------------------------------------------------------------
	      activer.setOnClickListener(new OnClickListener() 
									      {
											
											@Override
											public void onClick(View v) 
												{
													// TODO Auto-generated method stub
												 if (!BA.isEnabled())
													 {														
														 Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
													     startActivityForResult(getVisible, 0);											        
												         Toast.makeText(getApplicationContext(), "Activation bluetooth",Toast.LENGTH_LONG).show();
												      } 
												 
												 else 
													  {
												         Toast.makeText(getApplicationContext(), "Déja activé", Toast.LENGTH_LONG).show();
												      }
												}
										  }
									      
	    		                     );
	      
	      
//------------------------------------------DESACTIVER_BT---------------------------------------------------------------------------------------
	      
	      desactiver.setOnClickListener(new OnClickListener() 
										      {
												
												@Override
												public void onClick(View v) 
													{
														// TODO Auto-generated method stub
													  BA.disable();
												      Toast.makeText(getApplicationContext(), "Bluetooth désactivé" ,Toast.LENGTH_LONG).show();
													}
											  }
	      
                                        );

	      
  //--------------------------------------------------------------------------------------------------------------------------------------------	      
	      
	    
	
	       
	        setContentView(arbrexml);
	        
	      
	    } //---------------FIN_METHODE_ONCREATE----------------------------------------------------------------
    
    
 //----------------------QUITTER_BUTTON_MSG--------------------------------------------------------------------------   
    @Override
	public void onBackPressed() // Methode pour quitter l'application
	{
	        if (back_pressed + 2000 > System.currentTimeMillis()) 
	        	super.onBackPressed();	        
	        
	        else Toast.makeText(getBaseContext(), "Appuyez encore pour quitter !", Toast.LENGTH_SHORT).show();
	        back_pressed = System.currentTimeMillis();
	        BA.disable();
	        stopWifi();
	}
    
   
//--------------------------------DESACTIVATION WIFI---------------------------------------------------------------------------------    
    public void stopWifi()
	    {
	    	WifiManager wM = (WifiManager) getSystemService(WIFI_SERVICE); 
	    	wM.setWifiEnabled(false);
	    }
    
//------------------------BOITES_DE_DIALOGUE-----------------------------------------------------------------------------------------------
    
    /*

     * Appelée qu'à la première création d'une boîte de dialogue

     * Les fois suivantes, on se contente de récupérer la boîte de dialogue déjà créée…

     * Sauf si la méthode « onPrepareDialog » modifie la boîte de dialogue.

    */
    
    public Dialog onCreateDialog(int identifiant)
           {
    	    // On construit la première boîte de dialogue, que l'on insère dans « box »
    	
    	          Dialog box = new Dialog(this);
    	          box.setTitle("A propos de l'application !"); 		    	  
		    	  box.setContentView(R.layout.boite_dialog_perso);		    	 		
		    	  return box;
    	    }

    public void onPrepareDialog (int identifiant, Dialog box) 
       {
       
       }
    
    
//--------------------------------------LES_MENUS----------------------------------------------------------------------------------    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
		    {
		        // Inflate the menu; this adds items to the action bar if it is present.
		        getMenuInflater().inflate(R.menu.activite0, menu);
		        return true;
		    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
		    {
		        // Handle action bar item clicks here. The action bar will
		        // automatically handle clicks on the Home/Up button, so long
		        // as you specify a parent activity in AndroidManifest.xml.
		        int id = item.getItemId();
		        if (id == R.id.action_settings) 
			        {
			            //return true;
			            Toast.makeText(getApplicationContext(), "Parametre",Toast.LENGTH_LONG).show();
			        }
		        
		        
		        if (id == R.id.menu_about) 
			        {			         
		        	   showDialog(IDENTIFIANT_BOITE);		        	
			        }
		        
		        
		        if (id == R.id.menu_help) 
		        {
 		    	    Intent i4 = new Intent(getApplicationContext(), Activite4.class);						
					startActivity(i4);	
				    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		        }
		        
		        return super.onOptionsItemSelected(item);
		    }
    
//-------------------------FIN_MENUS-------------------------------------------------------------------------    

} //************************ FIN_CLASSE_ACTIVITE0

