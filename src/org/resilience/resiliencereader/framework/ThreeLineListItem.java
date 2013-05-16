package org.resilience.resiliencereader.framework;

import android.graphics.drawable.Drawable;
import android.net.Uri;

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

   public Uri getImageUri();

   public long getId();

   public String getGuid();
}
