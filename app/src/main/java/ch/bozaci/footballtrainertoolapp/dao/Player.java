package ch.bozaci.footballtrainertoolapp.dao;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Alp.Bozaci on 23.08.2017.
 */

public class Player implements Serializable
{
    public static final String TABLE = "player";

    public static final String COLUMN_ID            = "id";
    public static final String COLUMN_FIRSTNAME     = "firstname";
    public static final String COLUMN_LASTNAME      = "lastname";
    public static final String COLUMN_PLAYERNUMBER  = "playernumber";
    public static final String COLUMN_PICTURE       = "picture";

    private Integer id;
    private String firstName;
    private String lastName;
    private Integer playerNumber;
    private byte[] picture;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public Integer getPlayerNumber()
    {
        return playerNumber;
    }

    public void setPlayerNumber(Integer playerNumber)
    {
        this.playerNumber = playerNumber;
    }

    public byte[] getPicture()
    {
        return picture;
    }

    public void setPicture(byte[] picture)
    {
        this.picture = picture;
    }

    @Override
    public String toString()
    {
        return firstName + " " + lastName + " [ " + playerNumber + " ]";
    }
}
