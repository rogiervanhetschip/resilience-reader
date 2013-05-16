package org.resilience.resiliencereader.framework;

import android.graphics.drawable.Drawable;

public interface ThreeLineListItem
{
   public String getFirstLine();

   public String getFirstLine(boolean forceLoad);

   public String getSecondLine();

   public String getSecondLine(boolean forceLoad);

   public String getThirdLine();

   public String getThirdLine(boolean forceLoad);

   public Drawable getDrawable();

   public Drawable getDrawable(boolean forceLoad);

   public Drawable getImage();

   public Drawable getImage(boolean forceLoad);

   public long getId();

   public String getGuid();
}
