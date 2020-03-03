package sn.sonko.eb;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Activite1 extends Activity
{
	
	private TextView mBluetoothStatus, tvdt1;	
    private Button mScanBtn;  
    BluetoothAdapter mBTAdapter;
    private Set<BluetoothDevice> mPairedDevices;
    private ArrayAdapter<String> mBTArrayAdapter;
    private ListView mDevicesListView;
    private final static int IDENTIFIANT_BOITE  = 0;
    
    boolean fail = false;
    
    private final String TAG = Activite1.class.getSimpleName();
    Handler mHandler; // Our main handler that will receive callback notifications
    ConnectedThread mConnectedThread; // bluetooth background worker thread to send and receive data
    BluetoothSocket mBTSocket = null; // bi-directional client-to-client data path

    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier
	
    // #defines for identifying shared types between calling functions
    //private final static int REQUEST_ENABLE_BT = 1; // used to identify adding bluetooth names
    private final static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status
    private final static int MESSAGE_READ = 2; 
	
   
    private String str, mac;
	private LinearLayout arbrexml3 = null;
  
	

	
	
  
protected void onCreate(Bundle savedInstanceState) 


{
 
    super.onCreate(savedInstanceState);
    arbrexml3 = (LinearLayout)LinearLayout.inflate(this, R.layout.layout1,null);	      ;
    mDevicesListView = (ListView)arbrexml3.findViewById(R.id.lv3);
    mScanBtn = (Button)arbrexml3.findViewById(R.id.bt_scan);
    mBluetoothStatus = (TextView)arbrexml3.findViewById(R.id.tv_bt_status);
    
    mBTArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
    mBTAdapter = BluetoothAdapter.getDefaultAdapter();    
 
    mDevicesListView.setAdapter(mBTArrayAdapter);
    mDevicesListView.setOnItemClickListener(mDeviceClickListener);
    
    listPairedDevices(mDevicesListView); // Affichage des peripheriques associes
    
    tvdt1 = (TextView) arbrexml3.findViewById(R.id.heure1);
    
    Calendar calendrier = Calendar.getInstance();
    String  currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendrier.getTime());								
    tvdt1.setText(currentDate);
    
 //---------------------------ACK_CONNEXION-------------------------------------------------------------------------   
    mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {

        	if(msg.what == CONNECTING_STATUS)
            {
                if(msg.arg1 == 1)
                   {
                	 str =  (String)(msg.obj);
                     mBluetoothStatus.setText("Connecté à " + str);                  
                     
                     Intent i3 = new Intent(getApplicationContext(), Activite2.class);	
                     i3.putExtra("id", str); //Envoi du nom du periphérique a l'activite 2
                     i3.putExtra("mac", mac);
	                 startActivity(i3);	
	                 overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    
                   }
                else
                    mBluetoothStatus.setText("Echec Connexion");
            }
        	
            
        }
    };
  
 
 //-----------------------VERIFICATION_PERIPHERIQUES_BLUETOOTH------------------------------------------------   
    
    if (mBTArrayAdapter == null)
	    {
	        // Peripherique incompatible BT  
	        Toast.makeText(getApplicationContext(),"Aucun péripherique trouvé!",Toast.LENGTH_SHORT).show();
	    } 
    
//------------------------ECOUTE_BOUTON_ACTUALISER-----------------------------------------------------------    
    mScanBtn.setOnClickListener(new View.OnClickListener() 
								    {
								        @Override
								        public void onClick(View v) 
										        { 								        	      
								        	        discover(v); //Recherche des périphériques bluetooth													
										        }
								    }
    							);
    
   
    
    setContentView(arbrexml3); 
    
} //*******************FIN_DE_LA_METHODE_ONCREATE*****************************


//-----------------RECHERCHE_PERIPHERIQUES_BT--------------------------------------------------------------------------------------

private void discover(View view)
{
    // Check if the device is already discovering
    if(mBTAdapter.isDiscovering())
	    {
    	    mBTAdapter.cancelDiscovery();	   
	        mBluetoothStatus.setText("Recherche annulée");	        
	    }
    
    else
    {
        if(mBTAdapter.isEnabled()) 
	        {
	            mBTArrayAdapter.clear(); // Effacer la listeView
	            mBTAdapter.startDiscovery();
	            mBluetoothStatus.setText("Recherche en cours ...");	          
	            registerReceiver(blReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));	         
	          
	        }
        
        else
	        {
	            Toast.makeText(getApplicationContext(), "Bluetooth désactivé", Toast.LENGTH_SHORT).show();
	        }
    }
    
    
}


//--------------------UTILISATION_D'UN_BROADCASTRECEIVER_POUR_VISIBILITE_PERIPHERIQUES_PROCHE--------------------------------------------


final BroadcastReceiver blReceiver = new BroadcastReceiver()
{
    @Override
    public void onReceive(Context context, Intent intent)
	    {
	        String action = intent.getAction();
	        if(BluetoothDevice.ACTION_FOUND.equals(action))
		        {
		            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);	
		            mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress()); //Ajout de peripheriques a la listeView
		            mBTArrayAdapter.notifyDataSetChanged();
		        }
	    }
};


//-----------------LISTE_PERIPHERIQUES_ASSOCIES------------------------------------------------------------------------------------------

private void listPairedDevices(View view)
{
    mPairedDevices = mBTAdapter.getBondedDevices();
    if(mBTAdapter.isEnabled())     
	    {	       
	        for (BluetoothDevice device : mPairedDevices)
	            mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
	            mBluetoothStatus.setText("Périphériques associés");	          
	    }
    
    else
        Toast.makeText(getApplicationContext(), "Bluetooth désactivé", Toast.LENGTH_SHORT).show();
}


//----------------------VERICATION_CLIC_SUR_LISTVIEW-------------------------------------------------------------

public AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() 

{	
	
	
    public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3)
    {

    	
        if(!mBTAdapter.isEnabled()) 
	        {
	            Toast.makeText(getBaseContext(), "Bluetooth désactivé", Toast.LENGTH_SHORT).show();
	            return;
	        }

       
          
        String info = ((TextView) v).getText().toString();  // Recuperation de l'adresse MAC, 17 derniers caracteres de la vue.
        final String address = info.substring(info.length() - 17);
        final String name = info.substring(0,info.length() - 17);
       
        mac=address;       
        mBluetoothStatus.setText("Connexion en cours...");    
       
        
        new Thread()      // Creation thread pour eviter le crash de l'interface
        {
            public void run()
            {
                

                BluetoothDevice device = mBTAdapter.getRemoteDevice(address);

                try 
	                {
	                    mBTSocket = createBluetoothSocket(device);
	                }
                
                catch (IOException e) 
	                {
	                    fail = true;
	                    Toast.makeText(getBaseContext(), "Echec création Socket", Toast.LENGTH_SHORT).show();
	                }
               
                try 
	                {
	                    mBTSocket.connect();   // Etablissement Connexion Socket.
	                } 
                
                catch (IOException e)
                     {
	                    try 
		                    {
		                        fail = true;
		                        mBTSocket.close();
		                        mHandler.obtainMessage(CONNECTING_STATUS, -1, -1).sendToTarget();
		                    } 
                    
	                    catch (IOException e2) 
		                    {
		                        //Saisir code d'association
		                        Toast.makeText(getBaseContext(), "Echec création Socket", Toast.LENGTH_SHORT).show();
		                    }
                }
                
                
                if(fail == false) // Creation socket reussi
	                {
	                    mConnectedThread = new ConnectedThread(mBTSocket);
	                    mConnectedThread.start();
	
	                    mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, name).sendToTarget();
	                   
	           	       
	                }
                
            }
            
        }
        
        .start(); //Demarrage du Thread
      
        
        
        
    }
    
};


//-----------------------CREATION_SOCKET_BLUETOOTH------------------------------------------------------------------------------

private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException 

{
    try 
	    {
	        final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
	        return (BluetoothSocket) m.invoke(device, BTMODULEUUID);
	    } 
    
    catch (Exception e) 
	    {
	        Log.e(TAG, "Impossible de créer une Connection RFComm non sécurisée ",e);
	    }
    return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
}

//-------------------------CLASS_INTERNE_CONNECTEDTHREAD------------------------------------------------------------------------------------

private class ConnectedThread extends Thread 

{
	private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;

    public ConnectedThread(BluetoothSocket socket) 
    
    {
    	
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Recuperer les flux entrant et sortant en utilisant des objets temporaire (tempIn, tempOut), car
        // mmInStream et mmOutStream sont de type final
        try 
	        {
	            tmpIn = socket.getInputStream();
	            tmpOut = socket.getOutputStream();
	        } 
        
        catch (IOException e) 
             { 
        	
             }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }
    
    public void run() 
    		{
    			byte[] buffer = new byte[1024];  // Tableau pour stocker les bytes recus
                int bytes; // Pour stocker le nombre de bytes recus
      
                while (true) 
                {
			            try {			                
					           bytes = mmInStream.available(); // Lecture flux entrant
					                
					           if(bytes != 0) 
			                        {					                	
					                    buffer = new byte[1024];
					                    SystemClock.sleep(100); //Temps d'attente de reception des donnees.Depend de la vitesse d'envoi.
					                    bytes = mmInStream.available(); 
					                    bytes = mmInStream.read(buffer, 0, bytes); 
					                    Activite2.mHandler2.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget(); // Envoi des bytes recus a l'activite.
					                }
			                } 
			            
			            
			            catch (IOException e) 
				            {
				                e.printStackTrace();				
				                break;
				            }
                }
                
          }
 
 //--------------------------------------------------------------------------------------------------------
    
    public void cancel()
    {
    	try 
	        {
	    	 
	       	  mmSocket.close();
	        }

    	catch (IOException e) 
    	    {
    	  
    	    }
    }


  //--------------------------------------------------------------------------------------------------------  
    
}	//**********************Fin_CONNECTED_THREAD******************************


//------------------------METHODE_ANIMATION_RETOUR--------------------------------------------------------------------

      public void finish()
		{    
			super.finish();
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);			
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
  		        getMenuInflater().inflate(R.menu.activite1, menu);
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

} //*****************FIN_CLASSE********************************