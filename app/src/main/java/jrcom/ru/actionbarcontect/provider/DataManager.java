package jrcom.ru.actionbarcontect.provider;

import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jrcom.ru.actionbarcontect.StoreApplication;
import jrcom.ru.actionbarcontect.Utils;
import jrcom.ru.actionbarcontect.model.Goods;
import jrcom.ru.actionbarcontect.model.Order;

/**
 * Created by eugeniy.rubilov on 15.03.2015.
 */
public class DataManager {

    public final static String DATA_BROADCAST = "data_broadcast_nir79ncw3klkjs9";

    private List<Goods> mGoodsFromServer;
    private List<Goods> mGoodsInCart;
    private List<Goods> mGoodsCandidate = new ArrayList<>();
    private Map<Integer, Goods> mGoodsInCartSaver = new HashMap<>();

    private float mCandidateSum;
    private float mCartSum;

    public static DataManager getInstance(){
        return StoreApplication.getInstance().getDataManager();
    }

    public void setGoods(String goods){
        StoreApplication.getInstance().saveGoodsList(goods);
        mGoodsFromServer = getGoods();
        mGoodsCandidate.clear();
    }

    public List<Goods> getGoods() {
        if (mGoodsFromServer == null) {
            mGoodsFromServer = new Gson().fromJson(StoreApplication.getInstance().readGoodsList(), new TypeToken<ArrayList<Goods>>() {
            }.getType());
        }
        return mGoodsFromServer;
    }

    public void getGoodsInCartAsFile(){
        //prepare order
        List<Order> order = new ArrayList<>(mGoodsInCart.size());
        for (Goods goods : mGoodsInCart){
            order.add(new Order(goods.getName(), goods.getCount()));
        }

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Utils.writeFile(gson.toJson(order));
    }

    public void updateCartCandidateList(Goods goods, int newCount) {
        if (newCount <= 0) {
            mGoodsCandidate.remove(goods);
        } else if (!mGoodsCandidate.contains(goods)) {
            mGoodsCandidate.add(goods);
        }
        mCandidateSum = mCandidateSum - goods.getCount() * goods.getPrice() + newCount * goods.getPrice();
        goods.setCount(newCount);
        StoreApplication.getInstance().sendBroadcast(new Intent(DATA_BROADCAST).putExtra("sum", mCandidateSum));
    }

    public void removeGoodsFromCart(Goods goods){
        mGoodsInCart.remove(goods);
        mGoodsInCartSaver.remove(goods.getId());
        mCartSum -= goods.getCount() * goods.getPrice();
    }

    public void addGoodsToCart(){
        for (Goods goods : mGoodsCandidate){
            if (mGoodsInCartSaver.containsKey(goods.getId())){
                Goods placedGoods = mGoodsInCartSaver.get(goods.getId());
                placedGoods.setCount(placedGoods.getCount() + goods.getCount());
            } else {
                mGoodsInCartSaver.put(goods.getId(), goods.doClone());
            }
            goods.setCount(0);
        }
        mCartSum += mCandidateSum;
        mCandidateSum = 0;
    }

    public List<Goods> getGoodsInCart(){
        mGoodsInCart = new ArrayList<>(mGoodsInCartSaver.size());
        for (Map.Entry<Integer, Goods> entry : mGoodsInCartSaver.entrySet()){
            mGoodsInCart.add(entry.getValue());
        }
        return mGoodsInCart;
    }

    public void refreshAllData(){
        mCartSum = 0;
        mCandidateSum = 0;
        mGoodsCandidate.clear();
        mGoodsInCart.clear();
        mGoodsInCartSaver.clear();
    }
    public float getCartSum() {
        return mCartSum;
    }

}
