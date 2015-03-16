package jrcom.ru.actionbarcontect.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by eugeniy.rubilov on 14.03.2015.
 */
public class Order {
    @SerializedName("name")
    private String mName;
    @SerializedName("count")
    private int mCount;

    public Order(String mName, int mCount) {
        this.mName = mName;
        this.mCount = mCount;
    }
}
