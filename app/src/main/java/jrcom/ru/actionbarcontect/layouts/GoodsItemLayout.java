package jrcom.ru.actionbarcontect.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import jrcom.ru.actionbarcontect.R;
import jrcom.ru.actionbarcontect.adapters.PriceAdapter;
import jrcom.ru.actionbarcontect.model.Goods;

/**
 * Created by eugeniy.rubilov on 14.03.2015.
 */
public class GoodsItemLayout extends LinearLayout {

    private Goods mGoods;
    private PriceAdapter.OnItemChangedListener onItemChangedListener;

    private TextView goodsName;
    private TextView goodsPrice;
    private TextView goodsSum;
    private ImageView deleteGoods;
    private SeekBar goodsCountSeekBar;

    public GoodsItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GoodsItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GoodsItemLayout(Context context) {
        super(context);
    }

    public void init(boolean isEditMode) {
        goodsName = (TextView) findViewById(R.id.goodsName_tv_recycler);
        goodsPrice = (TextView) findViewById(R.id.goodsPrice_tv_recycler);
        goodsSum = (TextView) findViewById(R.id.goodsSum_tv_recycler);

        deleteGoods = (ImageView) findViewById(R.id.deleteGoods_iv_goodItem);
        goodsCountSeekBar = (SeekBar) findViewById(R.id.goodsCount_seek_recycler);

        if (isEditMode) {
            goodsCountSeekBar.setVisibility(View.VISIBLE);
            deleteGoods.setVisibility(View.GONE);
            goodsCountSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    goodsSum.setText(getContext().getString(R.string.items_count_text, i, i * mGoods.getPrice()));
                    onItemChangedListener.itemChanged(mGoods, i);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    //update model

                }
            });
        } else {


            goodsCountSeekBar.setVisibility(View.GONE);
            deleteGoods.setVisibility(View.VISIBLE);
            deleteGoods.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemChangedListener.itemRemoved(mGoods);
                }
            });
        }
    }

    public void setContent(Goods goods) {
        mGoods = goods;
        goodsName.setText(goods.getName());
        goodsPrice.setText(String.valueOf(goods.getPrice()));
        goodsSum.setText(getContext().getString(R.string.items_count_text, goods.getCount(), goods.getCount() * goods.getPrice()));
        goodsCountSeekBar.setProgress(goods.getCount());
    }

    public void setOnItemChangedListener(PriceAdapter.OnItemChangedListener onItemChangedListener) {
        this.onItemChangedListener = onItemChangedListener;
    }
}
