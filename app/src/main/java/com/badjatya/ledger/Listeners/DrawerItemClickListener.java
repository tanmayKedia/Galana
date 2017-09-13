package com.badjatya.ledger.Listeners;

import android.content.Intent;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.badjatya.ledger.activities.GalanaActivity;
import com.badjatya.ledger.activities.GirviActivity;

import com.badjatya.ledger.Rakam.RakamList.RakamActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.spec.ECField;
import java.util.Calendar;

/**
 * Created by Tanmay on 9/27/2016.
 */
public class DrawerItemClickListener implements ListView.OnItemClickListener {

    GalanaActivity context;
    DrawerLayout drawerLayout;
    private static final int REQUEST_CODE_PICK_DB=003;

    public  DrawerItemClickListener(GalanaActivity context,DrawerLayout drawerLayout)
    {
        this.context=context;
        this.drawerLayout=drawerLayout;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectItem(position);
    }

    private void selectItem(int position) {

        if (position == 0) {
            Intent i=new Intent(context,GirviActivity.class);
            drawerLayout.closeDrawer(Gravity.LEFT);
            context.resumeFlag=true;
            context.startActivity(i);
        }
        else if (position == 1) {
            Intent i=new Intent(context,RakamActivity.class);
            drawerLayout.closeDrawer(Gravity.LEFT);
            context.startActivity(i);
        }
        else if(position==2)
        {
            //backup code here
            drawerLayout.closeDrawer(Gravity.LEFT);
            final String inFileName =context.getDatabasePath("galana.db").getPath();
            File dbFile = new File(inFileName);
            FileInputStream fis=null;
            OutputStream output=null;
            try {

                fis = new FileInputStream(dbFile);
                String outFileName = Environment.getExternalStorageDirectory()+"/galana_backup"+Calendar.getInstance().getTime().toString()+".db";
                output = new FileOutputStream(outFileName);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer))>0){
                    output.write(buffer, 0, length);
                }
                Toast.makeText(context,"Your file was successfully saved at "+Environment.getExternalStorageDirectory().getAbsolutePath(),Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {
                Log.d("Voldemort", e.toString());
                Toast.makeText(context,"There was error accessing the database file",Toast.LENGTH_LONG).show();
            }
            finally {
                // Close the streams
                try
                {
                    if(fis!=null)
                    {
                        fis.close();
                    }
                    if(output!=null)
                        {
                            output.flush();
                            output.close();
                        }
                }
                catch (IOException ioe)
                {
                    Log.d("Voldemort", ioe.toString());
                }
            }
        }

        else if(position==3)
        {   drawerLayout.closeDrawer(Gravity.LEFT);
            context.resumeFlag=true;
            Intent mediaIntent = new Intent(Intent.ACTION_GET_CONTENT);
            mediaIntent.setType("*/*"); //set mime type as per requirement
            context.startActivityForResult(mediaIntent,REQUEST_CODE_PICK_DB);
        }
    }
}
