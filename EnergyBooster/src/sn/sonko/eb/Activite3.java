package sn.sonko.eb;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;





public class Activite3 extends Activity 
  {
	
	private final static int IDENTIFIANT_BOITE  = 0;
	private LinearLayout arbrexml4;
	private TextView tvdt,tvid2, infos, infos2;
	int annee, mois, jour;
   // private String a,m,j,date;
	private Button bouton_show, bouton_sync; //Boutons d'insertion et de recupeation des donnees
	private RequestQueue requestQueue;
	private String mac, wh, ah; //Pour l'insertion des donnees dans la base de données distante "arduino"
	private String date_ins=null, ampere_heure, watt_heure; //Pour la selection des donnees
    private ProgressBar pgbr, pgbr2; // Barres de progression pour les boutons historique et synchronisation
    //private String reponse;
	
    String str7;
	private String insertUrl = "http://192.168.7.2/ecosolaire/insertData.php"; //URL d'insertion des donnees
	private String showUrl = "http://192.168.7.2/ecosolaire/showData.php";  //URL de selection des donnees
	
  
	@Override
     protected void onCreate(Bundle savedInstanceState)
					{
						super.onCreate(savedInstanceState);		
						//----------------CREATION DE LA BASE DE DONNEES-----------------------------------------------------------------
						final  SQLiteDatabase db = openOrCreateDatabase("arduino",MODE_PRIVATE,null); //Creation de la base de donnees (arduino).
						db.execSQL("CREATE TABLE IF NOT EXISTS regulateurs(id INTEGER PRIMARY KEY AUTOINCREMENT ,addr_mac TEXT NOT NULL, amp_h TEXT NOT NULL, watt_h TEXT NOT NULL);");//Creation de la table "regulateurs".
						 	
						
						arbrexml4 = (LinearLayout)LinearLayout.inflate(this, R.layout.layout3,null);
						
						tvid2 = (TextView)arbrexml4.findViewById(R.id.tvid2);
						tvdt = (TextView)arbrexml4.findViewById(R.id.heure3);
						infos = (TextView)arbrexml4.findViewById(R.id.infos);	
						infos2 = (TextView)arbrexml4.findViewById(R.id.infos2);
						bouton_show = (Button)arbrexml4.findViewById(R.id.bouton_bd);
						bouton_sync = (Button)arbrexml4.findViewById(R.id.bouton_sync);
						pgbr = (ProgressBar)arbrexml4.findViewById(R.id.pgbr);
						pgbr2 = (ProgressBar)arbrexml4.findViewById(R.id.pgbr2);
						
						requestQueue = Volley.newRequestQueue(getApplicationContext());
						
						Calendar calendrier = Calendar.getInstance(); //Recuperation de la date du systeme Android
					    String  currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendrier.getTime());								
					    tvdt.setText(currentDate);
					    
					    //ah = new String("100");
					    //wh = new String("250");
						
						/*annee = calendrier.get(Calendar.YEAR);
						mois = calendrier.get(Calendar.MONTH);
						jour = calendrier.get(Calendar.DATE);
					    
						a = Integer.toString(annee);
						m = Integer.toString(mois);
						j = Integer.toString(jour);
						date=j+ "/ " +m+ " / "+a;*/
						
					
						 Intent i2 = getIntent(); //Intent pour recuperer les extras (id,mac) de l'equipement connecté
					   	  
					   	  if (i2 != null) 
					   		  {
					   			  String str = "";
					   			  if (i2.hasExtra("id")) // vérifie qu'une valeur est associée à la clé “id”
					   			  { 
					   			      str = i2.getStringExtra("id"); // on récupère la valeur associée à la clé
					   			  }					   			  
					   			  
					   			  
					   			
					   			 tvid2.setText("Connecté à : " + str);
					   			 
					   			  mac = "";
					   			  if (i2.hasExtra("mac")) // vérifie qu'une valeur est associée à la clé “id”
					   			  { 
					   			      mac = i2.getStringExtra("mac"); // on récupère la valeur associée à la clé
					   			  }
					   			
					   			
					   			 
					   		  }
					   	  
					   	  
					  
//----------------------------------------ECOUTE_BOUTON_SHOW------------------------------------------------------------
			 bouton_show.setOnClickListener(new View.OnClickListener() 
					        
					        {

					            @Override
					            public void onClick(View view) 
					            
					            {	
					            	 infos.setText("");
					            	 infos.setText("Traitement en cours . . .");					            	 
					            	 

					            	 Thread t = new Thread() 
						            	 {
						            	 
						            	 
											public void run() 
												{
												  super.run();	
												  
												  for (int i = 0; i < 100;) 
													  {
														try
															{
																sleep(500);
															} 
														
														catch (InterruptedException e) 
															{
																// TODO: handle exception
															   e.printStackTrace();
															}
														
														    
															pgbr.setProgress(i);
															i+=10;
															
															if (date_ins !=null)
																{																	
																	pgbr.setProgress(100);																	
																}
															
														
													  }
												  
												}
										
						            	 };
						            	 
						            	 t.start(); //Demarrage Thread t
	//--------------------------------------------------------------------------------------------------------------------------------------------------------------				                
					                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, showUrl, new Response.Listener<JSONObject>() 
					                 {
					                    
					                	@Override
					                    public void onResponse(JSONObject response) 
					                	
					                	{
					                        System.out.println(response.toString());
					                        
					                        
					                        
					                        try 
					                          {
						                            JSONArray regulateurs = response.getJSONArray("regulateurs");
						                            infos.setText("");
						                            
						                            for (int i = 0; i < regulateurs.length(); i++) 
						                            
							                            {
							                                JSONObject regulateur = regulateurs.getJSONObject(i);
							                                
							                                date_ins = regulateur.getString("date");							                                						                                
							                                ampere_heure = regulateur.getString("ampere_heure");
							                                watt_heure = regulateur.getString("watt_heure");
							                                
							                               
							                                infos.append("\n");
							                                infos.append("Date: " + date_ins +"\n");
							                              /*  int k = Integer.valueOf(watt_heure);
							                                
							                                if (k >= 1000) 
								                                {
																	k/=10;
																	infos.append(k + " K Wattheures (kWh) produits "+ "\n" + ampere_heure + " Ampèreheurres (Ah) produits"+"\n");
																}
							                                else
							                                { }
							                                */
								                                infos.append(watt_heure + " Wattheures (Wh) produits "+ "\n" + ampere_heure + " Ampèreheurres (Ah) produits"+"\n");
								                                infos.append("________________________________________\n");
						
							                            }
						                            
						                                   
					                          } 
					                        
					                        catch (JSONException e) 
					                        
						                        {
						                            e.printStackTrace();
						                        }

					                    } // Fin onReponse
					                	
					                	
					                } , 
					                
					                new Response.ErrorListener() 
					                    {
					                       @Override
					                       public void onErrorResponse(VolleyError error) 
						                       {
						                          System.out.append(error.getMessage());	
						                       }
					                    }
					                                                                   ); // Fin new JasonObjectRequest()
					                
					                requestQueue.add(jsonObjectRequest);
					            } // Fin onClick
					            
					        }
					        
					        		); // Fin show.setOnClickkListener()
					
  //------------------------------ECOUTE_BONTON_INSERTION-------------------------------------------------------------------------- 					   	  
					   	  
			 			  
					       bouton_sync.setOnClickListener(new View.OnClickListener() 
				        	{
					            @Override
					            public void onClick(View view) 
					            	{
					            	
					                   infos2.setText("");
					            	   infos2.setText("Traitement en cours . . .");
					            	   
					            	   
					          	//---SELECTION DES DONNEES DANS LA BASE LOCALE--------------------------------------
					            	Cursor resultSet = db.rawQuery("Select * from regulateurs ",null);					   		    	
					   		    	
					   		    	for(resultSet.moveToFirst(); !resultSet.isAfterLast(); resultSet.moveToNext())
					   		    	      {		    				        
					   						mac = resultSet.getString(1);
					   					    ah = resultSet.getString(2);										
					   						wh = resultSet.getString(3);					
					   						
					   		    	      }
					   		    //----------------------------------------------------------------------------------	
					            	   
					   		  
					            	   
					            	 Thread t2 = new Thread() 
						            	 {
						            	 
						            	 
											public void run() 
												{
												  super.run();	
												  
												  for (int i = 0; i < 101;) 
													  {
														try
															{
																sleep(500);
															} 
														
														catch (InterruptedException e) 
															{
																// TODO: handle exception
															   e.printStackTrace();
															}
														
														    
															pgbr2.setProgress(i);
															i+=10;
															
															
																
															pgbr2.setProgress(100);
															 
															 //Toast.makeText(Activite3.this, "Effectué",Toast.LENGTH_LONG).show();
																
															 //infos2.setText("Appuyez sur le bouton \"Synchroniser les données \" pour synchroniser l\'application avec les serveurs d\' Ecosolaire !");
															
														
															
														
													  }
												  
												}
										
						            	 };
						            	 
						            	 t2.start(); //Demarrage Thread t
//-----------------------------------------------------------------------------------------------------------------------------------------------						            	 
					            	
					            		StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() 
					            		 {
					            			
						                    @Override
						                    public void onResponse(String response) 
						                    
						                     {
				                               System.out.println(response.toString());
				                               // reponse = response.toString();
				                             }
						                    
				                         },
					            		
					            		 new Response.ErrorListener()
				                          {
							                    @Override
							                    public void onErrorResponse(VolleyError error) 
								                    {
								
								                    }
				                          }      
				                                                                )//FinStringRequest()
					            		
					            		{

						                    @Override
						                    protected Map<String, String> getParams() throws AuthFailureError 
							                     {
							                        Map<String,String> parameters  = new HashMap<String, String>();
							                        parameters.put("addr_mac", mac); // addr_mac est champ de la table regulateurs
							                        parameters.put("ampere_heure", ah);
							                        parameters.put("watt_heure",wh);
							
							                        return parameters;
							                     }
				                        };
				                      
				                          requestQueue.add(request);
				             } // Fin onCick()

				        } //Fin setOnclickListener
				        
				        		               ); //Fin setOnclickListener parentheses    
					
						
						 setContentView(arbrexml4);
						
					} // Fin onCreate ()

	
//-----------------METHODE_ANIMATION_RETOUR---------------------------------------------------------	
	 public void finish()
			{ 
				super.finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);			
			}
//---------------------------------------------------------------------------------------------------------------------
	 
	
	 
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
 		        getMenuInflater().inflate(R.menu.activite3, menu);
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

 } //*********************FIN_CLASSE************************
