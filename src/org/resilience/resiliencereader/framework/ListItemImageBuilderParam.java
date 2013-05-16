package org.resilience.resiliencereader.framework;

import android.widget.ImageView;

public class ListItemImageBuilderParam extends AsyncBuilderParam<ImageView>
{
   public ListItemImageBuilderParam(ImageView view, ThreeLineListItem item, String guid)
   {
      super(view, item, guid);
   }

}
