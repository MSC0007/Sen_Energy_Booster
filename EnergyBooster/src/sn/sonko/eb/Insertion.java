package sn.sonko.eb;

import android.app.Activity;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class Insertion extends Activity

{
	
	
	
	public Insertion ()
	    {
		
		
	    }
	
	public void inserer (String mac, String ah,  String wh)
	{
	 //----------------CREATION DE LA BASE DE DONNEES-----------------------------------------------------------------
	 final SQLiteDatabase db = openOrCreateDatabase("arduino",MODE_PRIVATE,null); //Creation de la base de donnees (arduino).
	 db.execSQL("CREATE TABLE IF NOT EXISTS regulateurs(id INTEGER PRIMARY KEY AUTOINCREMENT ,addr_mac TEXT NOT NULL, amp_h TEXT NOT NULL, watt_h TEXT NOT NULL);");//Creation de la table "regulateurs".
	 
	 try {
		db.execSQL("INSERT INTO regulateurs(id,addr_mac,amp_h,watt_h) VALUES(NULL,'"+mac+"','"+ah+"','"+wh+"');");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	}
	

}
