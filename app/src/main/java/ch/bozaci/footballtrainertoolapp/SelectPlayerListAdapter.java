package ch.bozaci.footballtrainertoolapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ch.bozaci.footballtrainertoolapp.dao.Player;
import ch.bozaci.footballtrainertoolapp.util.PictureUtil;

/**
 * Created by Alp.Bozaci on 29.08.2017.
 */

public class SelectPlayerListAdapter extends BaseAdapter
{
    private List<Player> mPlayerList;
    private LayoutInflater mLayoutInflater;
    private MatchActivity.MyPlayerClickListener mPlayerClickListener;
    private MatchActivity.MyActivateDeactivatePlayerClickListener mActivateDeactivatePlayerClickListener;

    public SelectPlayerListAdapter(
            Context context,
            List<Player> playerList,
            MatchActivity.MyPlayerClickListener playerClickListener,
            MatchActivity.MyActivateDeactivatePlayerClickListener activateDeactivatePlayerClickListener)
    {
        this.mPlayerList = playerList;
        this.mPlayerClickListener = playerClickListener;
        this.mActivateDeactivatePlayerClickListener = activateDeactivatePlayerClickListener;

        mLayoutInflater = LayoutInflater.from(context);
    }

    public class ViewHolder
    {
        CheckBox checkBox;
        TextView textView;
        ImageView imageView;
    }

    @Override
    public int getCount()
    {
        return mPlayerList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mPlayerList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final ViewHolder viewHolder;

        if (convertView == null)
        {
            convertView = mLayoutInflater.inflate(R.layout.item_select_player, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.checkBox  = (CheckBox)  convertView.findViewById(R.id.checkbox_select_player);
            viewHolder.textView  = (TextView)  convertView.findViewById(R.id.textview_select_player);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageview_match_picture);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Player player = (Player)getItem(position);

        viewHolder.textView.setText(player.toString());
        viewHolder.imageView.setImageBitmap(PictureUtil.convertByteArrayToBitmap(player.getPicture()));

        viewHolder.textView.setEnabled(false);
        viewHolder.checkBox.setChecked(false);

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (viewHolder.checkBox.isChecked())
                {
                    mActivateDeactivatePlayerClickListener.onActivatePlayerClicked(player, viewHolder);
                }
                else
                {
                    mActivateDeactivatePlayerClickListener.onDeactivatePlayerClicked(player, viewHolder);
                }
            }
        });

        viewHolder.textView.setOnClickListener(new View.OnClickListener()
        {
           @Override
           public void onClick(View v)
           {
               mPlayerClickListener.onPlayerClicked(player);
           }
        });

        return convertView;
    }
}
