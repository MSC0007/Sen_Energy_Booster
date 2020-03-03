package sn.sonko.eb;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.UUID;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

 public class Activite2 extends Activity

{
	ListView liste1 ;
	private LinearLayout arbrexml2 = null;            
    static Handler mHandler2;
    private TextView tvid = null;
    private final static int IDENTIFIANT_BOITE  = 0;    
    private Button hist_btn;    
    private TextView tvdt2;
    public String ah, wh, s, readMessage, str,mac, rM;

    private final static int MESSAGE_READ = 2; // Utilisé dans le handler "Passerelle" pour identifier les messages recus
    //private final static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status
    
    Integer[] images = {R.drawable.panneau,R.drawable.puissance,R.drawable.tension,R.drawable.courant,R.drawable.batterie,R.drawable.tension ,R.drawable.etat,R.drawable.niveau,R.drawable.temperature};    
    String [] texte = new String [] {"PANNEAU SOLAIRE","    Puissance : --", "    Tension : --","    Courant : --","BATTERIE", "    Tension : --", "    Etat charge : --", "    Niveau charge : --","    Temperature : --"};    
    String [] unite = new String [] {" ","W", "V","A"," ", "V", " ", "%", "°C"};   
    String [] serial_tab_data = new String[9];
   
   	
	  
    @Override
    public void onCreate(Bundle savedInstanceState) 
    
	    
    {
    	   super.onCreate(savedInstanceState);
    	   arbrexml2 = (LinearLayout)LinearLayout.inflate(this, R.layout.layout2,null);
    	   
		    final  SQLiteDatabase db = openOrCreateDatabase("arduino",MODE_PRIVATE,null); //Creation de la base de donnees (arduino).
		    db.execSQL("CREATE TABLE IF NOT EXISTS regulateurs(id INTEGER PRIMARY KEY AUTOINCREMENT ,addr_mac TEXT NOT NULL, amp_h TEXT NOT NULL, watt_h TEXT NOT NULL);");//Creation de la table "regulateurs".
		
	        tvid = (TextView)arbrexml2.findViewById(R.id.tvid);
	        tvdt2 = (TextView)arbrexml2.findViewById(R.id.heure2);
	  
	        
	        hist_btn = (Button)arbrexml2.findViewById(R.id.hist_btn);
	        liste1 = (ListView)arbrexml2.findViewById(R.id.lv1);
	       
	        
	        Calendar calendrier = Calendar.getInstance();
		    String  currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendrier.getTime());	
		    tvdt2.setText(currentDate);
		    
		    final Custom_Adapter adapter = new Custom_Adapter(Activite2.this,texte,unite,images);
		 
	
		//--------------RECUPERATION DU NOM ET DE L'ADRESSE MAC DU REGULATEUR------------------------------------- 
		   Intent i1 = getIntent(); //Intent pour recuperer id de l'equipement connecté
		   	  
		   	  if (i1 != null) 
		   		  {
		   			  str = "";
		   			  if (i1.hasExtra("id")) // vérifie qu'une valeur est associée à la clé “id”
		   			  { 
		   			      str = i1.getStringExtra("id"); // on récupère la valeur associée à la clé
		   			  }
		   			  
		   			  tvid.setText("Connecté à : " + str);
		   			  
		   			  
		   			  mac = "";
		   			  if (i1.hasExtra("mac")) // vérifie qu'une valeur est associée à la clé “mac”
		   			  { 
		   			      mac = i1.getStringExtra("mac"); // on récupère la valeur associée à la clé
		   			  
		   			  }
		   			  
		   			
		   		   }
		   	  
		   	  //*******************************************************************************************
		
		
	       mHandler2 = new Handler() // Passerelle chargée de recuperer les messages (statut, texte)
	         {
	             public void handleMessage(android.os.Message msg)
	             {
	                    
	                 if(msg.what == MESSAGE_READ)
	                 {
	                    
	                     try 
	                     {
	                         readMessage = new String((byte[]) msg.obj, "UTF-8");
	                         
	                         s = readMessage; // On stock les données serie dans (s)	                         
	                         serial_tab_data = s.split(","); // Separation des données et stockage dans un tableau
	        				 
	         				
	        				 //******PANNEAUX********************
	        				 texte[1]= "    Puissance :  " +serial_tab_data [0]; // Puissance Panneaux
	        				 texte[2]= "    Tension :  " +serial_tab_data [1]; // Tension Panneaux
	        				 texte[3]= "    Courant :  " +serial_tab_data [2]; // Courant Panneaux
	        				 texte[4]="BATTERIE"; // Vide (-)
	        				 
	        				 //******BATTERIES*******************
	        				 texte[5]= "    Tension :  " +serial_tab_data [3]; // Tension Batterie
	        				 texte[6]= "    Etat de charge :  " +serial_tab_data [4]; // Etat Charge Batterie
	        				 texte[7]= "    Niveau Charge :  " +serial_tab_data [5]; // Niveau Charge Batterie
	        				 texte[8]= "    Temperature :  " +serial_tab_data [6]; //Temperature
	        				 ah = String.valueOf(serial_tab_data[7].toString()); // On stock le nombre d'Ah produit
	        				 wh = String.valueOf(serial_tab_data [8].toString()); // On stock le nombre de wh produit
	                         
	        				
	        				 adapter.notifyDataSetChanged(); // Informe le changement de donnees	
							
						     
	                     } 
	                     
	                     catch (UnsupportedEncodingException e) 
	                     {
	                         e.printStackTrace();
	                     } 
	              
	                 }
	                
	             }
	         };
	         
	         
	         //---------------------------------------------------------------------------------------
	    
	       
	           liste1.setAdapter (adapter);	
	           	  
	    		   	  
	   	    
	   	//---------------------Insertion des données dans la base de donnéees locale 'arduino'  	  
					  
			   	     // db.execSQL("INSERT INTO regulateurs(id,addr_mac,amp_h,watt_h) VALUES(NULL,'"+mac+"','"+ah+"','"+wh+"');");   			

				
				 //Toast.makeText(Activite2.this, "Insertion OK",Toast.LENGTH_SHORT).show();
	    		   	  
		//-----------------------------ECOUTE BOUTON HISTORIQUE---------------------------------	    
	         
	   	  hist_btn.setOnClickListener(new OnClickListener() 
										   	  {
												
													@Override
													public void onClick(View arg0) 
														{
															// TODO Auto-generated method stub
														    startWifi();
														    Intent i2 = new Intent(getApplicationContext(), Activite3.class);
															i2.putExtra("id", str); //Envoi du nom du periphérique a l'activite 3 avec putExtra qui prend en parametre une clé(id) et une valeur (str2)
															i2.putExtra("mac", mac);														
													        startActivity(i2);	
														    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
														}
											  }
	   			  					 );	   	  
	   	  
	        //-----------------------------------------------------------------------------------------------------
	      
	        setContentView(arbrexml2); 
	    }
    
 

  //--------------------METHODE_ANIMATION_RETOUR------------------------------------------------------------    
    
	    public void finish()
		    {
				super.finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);				
			}
	
    
  //--------------------------------ACTIVATION WIFI---------------------------------------------------------------------------------   
	   
	    public void startWifi()
		    {
		    	WifiManager wM = (WifiManager) getSystemService(WIFI_SERVICE); 
		    	wM.setWifiEnabled(true);		    
		    }
    //--------------------------------DESACTIVATION WIFI---------------------------------------------------------------------------------    
   /* public void stopWifi()
	    {
	    	WifiManager wM = (WifiManager) getSystemService(WIFI_SERVICE); 
	    	wM.setWifiEnabled(false);
	    }*/
    
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
		        getMenuInflater().inflate(R.menu.activite2, menu);
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
} //*************FIN_CLASSE***************************
