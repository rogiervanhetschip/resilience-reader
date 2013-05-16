package org.resilience.resiliencereader.framework;

import android.widget.TextView;

public class SecondLineBuilderParam extends AsyncBuilderParam<TextView>
{

   public SecondLineBuilderParam(TextView view, ThreeLineListItem item, String guid)
   {
      super(view, item, guid);
   }

}
