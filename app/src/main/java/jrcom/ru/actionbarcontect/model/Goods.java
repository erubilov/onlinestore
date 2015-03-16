package jrcom.ru.actionbarcontect.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by eugeniy.rubilov on 14/03/2015.
 */
public class Goods implements Serializable, Cloneable{
    @SerializedName("id")
    private int mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("price")
    private float mPrice;
    @SerializedName("count")
    private int mCount;

    public Goods(int id, String name, float price) {
        mId = id;
        mName = name;
        mPrice = price;
    }

    public Goods(int id, String name, float price, int count) {
        mId = id;
        mName = name;
        mPrice = price;
        mCount = count;
    }

    public int getId() {
        return mId;
    }


    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public float getPrice() {
        return mPrice;
    }

    public void setPrice(float price) {
        this.mPrice = price;
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int mCount) {
        this.mCount = mCount;
    }

    public Goods doClone(){
        try {
            return (Goods) clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Goods o = (Goods)super.clone();
        o.mId = mId;
        o.mName = mName;
        o.mPrice = mPrice;
        o.mCount = mCount;
        return o;
    }
}
