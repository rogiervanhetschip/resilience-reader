package org.resilience.resiliencereader.framework;

import android.os.AsyncTask;

public class SecondLineBuilder extends AsyncTask<SecondLineBuilderParam, Void, String>
{
   private SecondLineBuilderParam param;

   @Override
   protected String doInBackground(SecondLineBuilderParam... params)
   {
      param = params[0];

      // Prevent unnecessary calculations: Another ThreeLineListItem might have been loaded into this TextView, as they
      // are recycled in ThreeLineAdapter.getView
      if (!param.getGuid().equals(param.getTextview().getTag()))
      {
         return null;
      }

      // Calculate!
      String secondLine = param.getItem().getSecondLine();

      // Prevent printing a secondLine with another ThreeLineListItem: Another ThreeLineListItem might have been loaded
      // into this TextView, as they are recycled in ThreeLineAdapter.getView
      if (param.getGuid().equals(param.getTextview().getTag()))
      {
         return secondLine;
      }
      return null;
   }

   // Setting the text is done on the UI thread: Other threads are not allowed to touch it
   @Override
   protected void onPostExecute(String result)
   {
      // Result is given, and our textview has not yet been assigned to a different listitem
      if (result != null && param.getGuid().equals(param.getTextview().getTag()))
      {
         param.getTextview().setText(result);
      }
   }
}
