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

public class PlayerSelectListAdapter extends BaseAdapter
{
    private List<Player> mPlayerList;
    private LayoutInflater mLayoutInflater;
    private PlayerSelectListActivity.MyActivateDeactivatePlayerClickListener mActivateDeactivatePlayerClickListener;

    public PlayerSelectListAdapter(
            Context context,
            List<Player> playerList,
            PlayerSelectListActivity.MyActivateDeactivatePlayerClickListener activateDeactivatePlayerClickListener)
    {
        this.mPlayerList = playerList;
        this.mActivateDeactivatePlayerClickListener = activateDeactivatePlayerClickListener;

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
            viewHolder = new ViewHolder();

            convertView = mLayoutInflater.inflate(R.layout.item_player_select, parent, false);
            viewHolder.checkBox               = (CheckBox) convertView.findViewById(R.id.checkbox_select_player);
            viewHolder.textViewPlayerName     = (TextView) convertView.findViewById(R.id.textview_select_player_name);
            viewHolder.textViewPlayerNo       = (TextView) convertView.findViewById(R.id.textview_select_player_no);
            viewHolder.imageViewPlayerPicture = (ImageView) convertView.findViewById(R.id.imageview_select_player_picture);

            viewHolder.textViewPlayerName.setEnabled(false);
            viewHolder.checkBox.setChecked(false);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textViewPlayerName.setText(player.getFirstName() + " " + player.getLastName());
        viewHolder.textViewPlayerNo.setText("" + player.getPlayerNumber());
        viewHolder.imageViewPlayerPicture.setImageBitmap(PictureUtil.convertByteArrayToBitmap(player.getPicture()));

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (viewHolder.checkBox.isChecked())
                {
                    viewHolder.textViewPlayerName.setEnabled(true);
                    mActivateDeactivatePlayerClickListener.onActivatePlayerClicked(player);
                }
                else
                {
                    viewHolder.textViewPlayerName.setEnabled(false);
                    mActivateDeactivatePlayerClickListener.onDeactivatePlayerClicked(player);
                }
            }
        });

        return convertView;
    }
}
