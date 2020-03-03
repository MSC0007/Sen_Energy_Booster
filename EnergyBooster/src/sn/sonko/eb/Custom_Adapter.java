package sn.sonko.eb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Custom_Adapter  extends ArrayAdapter<String>

{

	
	    private final Context context;
	    private final String[] texte;
	    private final String[] unite;
	    
	    private final Integer[] images;
	    
	    public Custom_Adapter(Context context, String[] texte,String[] unite,Integer[] images) 
		    
	         {
		        super(context, R.layout.list_item, texte);
		        this.context = context;
		        this.texte = texte;
		        this.unite = unite;
		        this.images = images;
		     }

	    
	    @Override
	    public View getView(int position,View view,  ViewGroup parent) 
				    {
				        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				        
				        View rowView= inflater.inflate(R.layout.list_item, null, true);
				        
				        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
			
				        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
				        
				        TextView txtUnite = (TextView) rowView.findViewById(R.id.txt_Unite);
				        
				        
				        txtTitle.setText(texte[position]);	
				        
				        imageView.setImageResource(images[position]);
				        
				        txtUnite.setText(unite[position]);
			
				        return rowView;
				    }
	
	
	

}
