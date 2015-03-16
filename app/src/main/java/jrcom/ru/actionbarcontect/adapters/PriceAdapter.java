package jrcom.ru.actionbarcontect.adapters;

/**
 * Created by eugeniy.rubilov on 14/03/2015.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import jrcom.ru.actionbarcontect.R;
import jrcom.ru.actionbarcontect.layouts.GoodsItemLayout;
import jrcom.ru.actionbarcontect.model.Goods;
import jrcom.ru.actionbarcontect.provider.DataManager;

public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.ViewHolder> {
    private List<Goods> mDataset;
    private List<Goods> mSelectedGoods;
    private OnItemChangedListener onItemChangedListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public GoodsItemLayout goodsItemLayout;
        public ViewHolder(View v) {
            super(v);
            goodsItemLayout = (GoodsItemLayout)v;
        }
    }

    public PriceAdapter(List<Goods> myDataset) {
        mDataset = myDataset;
        onItemChangedListener = new OnItemChangedListener() {
            @Override
            public void itemChanged(Goods item, int newCount) {
                DataManager.getInstance().updateCartCandidateList(item, newCount);
            }

            @Override
            public void itemRemoved(Goods item) {

            }
        };
    }

    /**
     * reinit adapter
     * @param myDataset - new values
     */
    public void setNewGoods(List<Goods> myDataset) {
        mDataset = myDataset;
        notifyDataSetChanged();
    }

    @Override
    public PriceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.goods_item_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        ((GoodsItemLayout)v).init(true);
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

    public interface OnItemChangedListener {
        public void itemChanged(Goods item, int newCount);
        public void itemRemoved(Goods item);
    }

}
