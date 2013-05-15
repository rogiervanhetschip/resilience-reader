package org.resilience.resiliencereader.framework;

import android.os.AsyncTask;

public class SecondLineBuilder extends AsyncTask<SecondLineBuilderParam, Void, Void>
{

   @Override
   protected Void doInBackground(SecondLineBuilderParam... params)
   {
      SecondLineBuilderParam param = params[0];

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
         param.getTextview().setText(secondLine);
      }
      return null;
   }
}
