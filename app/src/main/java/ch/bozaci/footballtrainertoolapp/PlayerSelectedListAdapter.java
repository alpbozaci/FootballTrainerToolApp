package ch.bozaci.footballtrainertoolapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class PlayerSelectedListAdapter extends BaseAdapter
{
    private List<Player> mPlayerList;
    private LayoutInflater mLayoutInflater;
    private MatchActivity.MyPlayerClickListener mPlayerClickListener;

    private Map<Player, ViewHolder> map;

    public PlayerSelectedListAdapter(
            Context context,
            List<Player> playerList,
            MatchActivity.MyPlayerClickListener playerClickListener)
    {
        this.mPlayerList = playerList;
        this.mPlayerClickListener = playerClickListener;

        mLayoutInflater = LayoutInflater.from(context);

        map = new HashMap<>();
    }

    public class ViewHolder
    {
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
            viewHolder = new ViewHolder();

            convertView = mLayoutInflater.inflate(R.layout.item_player_selected, parent, false);
            viewHolder.textViewPlayerName     = (TextView) convertView.findViewById(R.id.textview_select_player_name);
            viewHolder.textViewPlayerNo       = (TextView) convertView.findViewById(R.id.textview_select_player_no);
            viewHolder.imageViewPlayerPicture = (ImageView) convertView.findViewById(R.id.imageview_select_player_picture);

            viewHolder.textViewPlayerName.setEnabled(false);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        map.put(player, viewHolder);

        viewHolder.textViewPlayerName.setText(player.getFirstName() + " " + player.getLastName());
        viewHolder.textViewPlayerNo.setText("" + player.getPlayerNumber());
        viewHolder.imageViewPlayerPicture.setImageBitmap(PictureUtil.convertByteArrayToBitmap(player.getPicture()));

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
