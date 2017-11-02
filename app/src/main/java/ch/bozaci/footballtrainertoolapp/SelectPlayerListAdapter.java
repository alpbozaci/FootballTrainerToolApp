package ch.bozaci.footballtrainertoolapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Map<Player, ViewHolder> map;

    public SelectPlayerListAdapter(
            Context context,
            List<Player> playerList,
            MatchActivity.MyPlayerClickListener playerClickListener,
            MatchActivity.MyActivateDeactivatePlayerClickListener activateDeactivatePlayerClickListener)
    {
        this.mPlayerList = playerList;
        this.mPlayerClickListener = playerClickListener;
        this.mActivateDeactivatePlayerClickListener = activateDeactivatePlayerClickListener;
        map = new HashMap<>();

        mLayoutInflater = LayoutInflater.from(context);
    }

    public class ViewHolder
    {
        CheckBox checkBox;
        TextView textViewPlayerName;
        TextView textViewPlayerNo;
        ImageView imageViewPlayerPicture;
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
        final Player player = (Player)getItem(position);

        if (convertView == null)
        {
            convertView = mLayoutInflater.inflate(R.layout.item_select_player, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.checkBox  = (CheckBox) convertView.findViewById(R.id.checkbox_select_player);
            viewHolder.textViewPlayerName  = (TextView) convertView.findViewById(R.id.textview_select_player_name);
            viewHolder.textViewPlayerName  = (TextView) convertView.findViewById(R.id.textview_select_player_no);
            viewHolder.imageViewPlayerPicture = (ImageView) convertView.findViewById(R.id.imageview_select_player_picture);

            viewHolder.textViewPlayerName.setEnabled(false);
            viewHolder.checkBox.setChecked(false);

            convertView.setTag(viewHolder);

            map.put(player, viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.textViewPlayerName.setText(player.toString());
        viewHolder.imageViewPlayerPicture.setImageBitmap(PictureUtil.convertByteArrayToBitmap(player.getPicture()));

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

        viewHolder.textViewPlayerName.setOnClickListener(new View.OnClickListener()
        {
           @Override
           public void onClick(View v)
           {
               mPlayerClickListener.onPlayerClicked(player);
           }
        });

        return convertView;
    }

    public ViewHolder getViewHolder(Player player)
    {
        return map.get(player);
    }
}
