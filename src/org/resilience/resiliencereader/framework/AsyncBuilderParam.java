package org.resilience.resiliencereader.framework;


public class AsyncBuilderParam<V>
{
   private V view;
   private ThreeLineListItem item;
   private String guid;

   public AsyncBuilderParam(V view, ThreeLineListItem item, String guid)
   {
      this.view = view;
      this.item = item;
      this.guid = guid;
   }

   public V getView()
   {
      return view;
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
