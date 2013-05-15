package org.resilience.resiliencereader.framework;

public interface ThreeLineListItem
{
   public String getFirstLine();

   public String getFirstLine(boolean load);

   public String getSecondLine();

   public String getSecondLine(boolean load);

   public String getThirdLine();

   public String getThirdLine(boolean load);

   public long getId();

   public String getGuid();
}
