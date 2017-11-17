package ch.bozaci.footballtrainertoolapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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

public class PlayerListAdapter extends BaseAdapter
{
    private List<Player> mPlayerList;
    private LayoutInflater mLayoutInflater;

    public PlayerListAdapter(Context context, List<Player> playerList)
    {
        this.mPlayerList = playerList;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public class ViewHolder
    {
        TextView textViewPlayerName;
        TextView textViewPlayerNo;
        ImageView imageViewPlayerFoto;
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
            convertView = mLayoutInflater.inflate(R.layout.item_player, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.textViewPlayerName  = (TextView)  convertView.findViewById(R.id.textview_player_name);
            viewHolder.textViewPlayerNo    = (TextView)  convertView.findViewById(R.id.textview_player_no);
            viewHolder.imageViewPlayerFoto = (ImageView) convertView.findViewById(R.id.imageview_player_picture);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textViewPlayerName.setText(player.getFirstName() + " " + player.getLastName());
        viewHolder.textViewPlayerNo.setText("" + player.getPlayerNumber());
        viewHolder.imageViewPlayerFoto.setImageBitmap(PictureUtil.convertByteArrayToBitmap(player.getPicture()));

        if (player.getState().equals(Player.PlayerState.ACTIVE))
        {
            viewHolder.textViewPlayerName.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            viewHolder.textViewPlayerName.setTextColor(Color.BLACK);
        }
        else
        {
            viewHolder.textViewPlayerName.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
            viewHolder.textViewPlayerName.setTextColor(Color.GRAY);
        }

        return convertView;
    }

}
