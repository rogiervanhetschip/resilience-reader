package org.resilience.resiliencereader.framework;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

public class ListItemImageBuilder extends AsyncTask<ListItemImageBuilderParam, Void, Drawable>
{
   private ListItemImageBuilderParam param;

   @Override
   protected Drawable doInBackground(ListItemImageBuilderParam... params)
   {
      param = params[0];

      // Prevent unnecessary calculations: Another ThreeLineListItem might have been loaded into this TextView, as they
      // are recycled in ThreeLineAdapter.getView
      if (!param.getGuid().equals(param.getView().getTag()))
      {
         return null;
      }

      // Calculate!
      Drawable drawableImage = param.getItem().getImage();

      // Prevent displaying an image with another ThreeLineListItem: Another ThreeLineListItem might have been loaded
      // into this ImageView, as they are recycled in ThreeLineAdapter.getView
      if (param.getGuid().equals(param.getView().getTag()))
      {
         return drawableImage;
      }
      return null;
   }

   // Setting the text is done on the UI thread: Other threads are not allowed to touch it
   @Override
   protected void onPostExecute(Drawable result)
   {
      // Result is given, and our ImageView has not yet been assigned to a different listitem
      if (result != null && param.getGuid().equals(param.getView().getTag()))
      {
         param.getView().setImageDrawable(result);
      }
   }
}
