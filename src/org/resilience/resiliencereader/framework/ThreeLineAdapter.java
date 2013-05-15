package org.resilience.resiliencereader.framework;

import java.util.AbstractList;

import org.resilience.resiliencereader.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ThreeLineAdapter extends BaseAdapter
{

   private AbstractList<ThreeLineListItem> list;

   public ThreeLineAdapter(AbstractList list)
   {
      // TODO: Check if contents of list are ThreeLineListItems
      this.list = list;
   }

   @Override
   public int getCount()
   {
      return list.size();
   }

   @Override
   public Object getItem(int index)
   {
      return list.get(index);
   }

   @Override
   public long getItemId(int index)
   {
      return list.get(index).getId();
   }

   @Override
   public View getView(int index, View convertView, ViewGroup root)
   {
      View view = null;
      if (convertView == null) // Herbruik van views is goedkoper, dus dat doen we hier
      {
         LayoutInflater inflater = (LayoutInflater) root.getContext().getApplicationContext()
               .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

         view = inflater.inflate(R.layout.three_line_list_item, null);
      }
      else
      {
         view = convertView;
      }
      ThreeLineListItem item = list.get(index);

      // TODO: findViewById is SLOW! Holder pattern is faster.
      TextView textView1 = (TextView) view.findViewById(R.id.listItemFirstLine);
      textView1.setText(item.getFirstLine());

      // Description is heavy: Unescape HTML4 takes some time. Defer those actions to a background thread, if necessary
      TextView textView2 = (TextView) view.findViewById(R.id.listItemSecondLine);
      textView2.setTag(item.getGuid());
      String secondLine = item.getSecondLine(false);
      if (secondLine == null)
      {
         SecondLineBuilder builder = new SecondLineBuilder();
         SecondLineBuilderParam param = new SecondLineBuilderParam(textView2, item);
         builder.execute(param);
         // Leave some vertical space
         textView2.setText("\n\n");
      }
      else
      {
         textView2.setText(secondLine);
      }

      TextView textView3 = (TextView) view.findViewById(R.id.listItemThirdLine);
      textView3.setText(item.getThirdLine());

      return view;
   }
}
