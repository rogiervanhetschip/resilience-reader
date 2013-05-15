package org.resilience.resiliencereader.framework;

import android.widget.TextView;

public class SecondLineBuilderParam
{
   private TextView textview;
   private ThreeLineListItem item;
   private String guid;

   public SecondLineBuilderParam(TextView textview, ThreeLineListItem item)
   {
      this.textview = textview;
      this.item = item;
   }

   public TextView getTextview()
   {
      return textview;
   }

   public ThreeLineListItem getItem()
   {
      return item;
   }

   public String getGuid()
   {
      return guid;
   }

}
