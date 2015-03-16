package jrcom.ru.actionbarcontect.adapters;

/**
 * Created by pc on 15/03/2015.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import jrcom.ru.actionbarcontect.R;
import jrcom.ru.actionbarcontect.StoreApplication;
import jrcom.ru.actionbarcontect.layouts.GoodsItemLayout;
import jrcom.ru.actionbarcontect.model.Goods;
import jrcom.ru.actionbarcontect.provider.DataManager;

public class GoodsInCartAdapter extends RecyclerView.Adapter<GoodsInCartAdapter.ViewHolder> {
    private List<Goods> mDataset;
    private PriceAdapter.OnItemChangedListener onItemChangedListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public GoodsItemLayout goodsItemLayout;
        public ViewHolder(View v) {
            super(v);
            goodsItemLayout = (GoodsItemLayout)v;
        }
    }

    public GoodsInCartAdapter(List<Goods> myDataset, PriceAdapter.OnItemChangedListener onItemChangedListener) {
        mDataset = myDataset;
        this.onItemChangedListener = onItemChangedListener;
    }

    @Override
    public GoodsInCartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.goods_item_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        ((GoodsItemLayout)v).init(false);
        ((GoodsItemLayout)v).setOnItemChangedListener(onItemChangedListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.goodsItemLayout.setContent(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
