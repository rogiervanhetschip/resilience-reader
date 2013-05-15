package org.resilience.resiliencereader;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;

public class NoInternetFragment extends DialogFragment
{
   @Override
   public Dialog onCreateDialog(Bundle savedInstanceData)
   {
      // Use the Builder class for convenient dialog construction
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      builder.setMessage(R.string.no_internet_text)
            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
               {
                  public void onClick(DialogInterface dialog, int id)
                  {
                     // Dismiss happens automagically
                  }
               }).setNeutralButton(R.string.connection_settings, new DialogInterface.OnClickListener()
               {
                  public void onClick(DialogInterface dialog, int id)
                  {
                     startActivity(new Intent(Settings.ACTION_SETTINGS));
                  }
               });

      // Create the AlertDialog object and return it
      return builder.create();
   }
}
