package sn.sonko.eb;

import java.text.DateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Activite4 extends Activity

{
	private LinearLayout arbrexml4;
	private TextView tvdt4;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
					{
						super.onCreate(savedInstanceState);						
						arbrexml4 = (LinearLayout)LinearLayout.inflate(this, R.layout.layout4,null);
						//----------------CREATION DE LA BASE DE DONNEES-----------------------------------------------------------------
						final  SQLiteDatabase db = openOrCreateDatabase("arduino",MODE_PRIVATE,null); //Creation de la base de donnees (arduino).
						db.execSQL("CREATE TABLE IF NOT EXISTS regulateurs(id INTEGER PRIMARY KEY AUTOINCREMENT ,addr_mac TEXT NOT NULL, amp_h TEXT NOT NULL, watt_h TEXT NOT NULL);");//Creation de la table "regulateurs".
						 	

						
						 tvdt4 = (TextView) arbrexml4.findViewById(R.id.heure4);						    
						 Calendar calendrier = Calendar.getInstance();
						 String  currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendrier.getTime());								
						 tvdt4.setText(currentDate);
						
						
						 setContentView(arbrexml4);
	
	
					}
	
	

} //*******************FIN_CLASSE*************************************
