package ch.bozaci.footballtrainertoolapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ch.bozaci.footballtrainertoolapp.dao.Player;
import ch.bozaci.footballtrainertoolapp.util.PictureUtil;

/**
 * Created by Alp.Bozaci on 29.08.2017.
 */

public class PlayerEvaluateListAdapter extends BaseAdapter
{
    private Context mContext;
    private List<Player> mPlayerList;
    private LayoutInflater mLayoutInflater;
    private PlayerEvaluateListActivity.MyPlayerEvaluateClickListener mPlayerClickListener;

    public PlayerEvaluateListAdapter(
            Context context,
            List<Player> playerList,
            PlayerEvaluateListActivity.MyPlayerEvaluateClickListener playerClickListener)
    {
        this.mContext = context;
        this.mPlayerList = playerList;
        this.mPlayerClickListener = playerClickListener;

        mLayoutInflater = LayoutInflater.from(context);
    }

    public class ViewHolder
    {
        ImageView imageViewPlayerPicture;
        TextView textViewPlayerName;
        Button buttonPlayerEvaluate;
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

            convertView = mLayoutInflater.inflate(R.layout.item_player_evaluate, parent, false);
            viewHolder.imageViewPlayerPicture = (ImageView) convertView.findViewById(R.id.imageview_evaluate_player_picture);
            viewHolder.textViewPlayerName     = (TextView) convertView.findViewById(R.id.textview_evaluate_player_name);
            viewHolder.buttonPlayerEvaluate   = (Button) convertView.findViewById(R.id.button_evaluate_player_value);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageViewPlayerPicture.setImageBitmap(PictureUtil.convertByteArrayToBitmap(player.getPicture()));
        viewHolder.textViewPlayerName.setText(player.getFirstName() + " " + player.getLastName());
        viewHolder.buttonPlayerEvaluate.setText(mContext.getResources().getString(R.string.evaluate));

        viewHolder.buttonPlayerEvaluate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mPlayerClickListener.onClicked(player);
            }
        });

        return convertView;
    }
}
