package jrcom.ru.actionbarcontect.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import jrcom.ru.actionbarcontect.R;
import jrcom.ru.actionbarcontect.Utils;
import jrcom.ru.actionbarcontect.adapters.GoodsInCartAdapter;
import jrcom.ru.actionbarcontect.adapters.PriceAdapter;
import jrcom.ru.actionbarcontect.model.Goods;
import jrcom.ru.actionbarcontect.provider.DataManager;

/**
 * Created by eugeniy.rubilov on 15/03/2015.
 */
public class GoodsInCartFragment extends Fragment{

    private RecyclerView recyclerView;
    private GoodsInCartAdapter mAdapter;
    private Button addToCartBtn;
    private View recyclerEmptyView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.goods_list_fragment, container, false);

        // get recycler view
        recyclerView = (RecyclerView) rootView.findViewById(R.id.goodsList_lv_goodsFragment);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //start load data
        loadData();
        addToCartBtn = (Button)view.findViewById(R.id.addToCart_btn_goodsFragment);
        addToCartBtn.setText(R.string.send_order);
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });

        recyclerEmptyView = view.findViewById(R.id.recycler_empty);
        view.findViewById(R.id.totalSelected_tv_goodsFragment).setVisibility(View.GONE);

        if (mAdapter.getItemCount() == 0) {
            addToCartBtn.setEnabled(false);
            recyclerEmptyView.setVisibility(View.VISIBLE);
        }
    }

    private void sendData(){
        //make json file
        DataManager.getInstance().getGoodsInCartAsFile();
        //clean all data
        DataManager.getInstance().refreshAllData();
        mAdapter.notifyDataSetChanged();
        //create intent
        startActivity(Intent.createChooser(Utils.getEmailIntent(), getString(R.string.send_order)));
    }

    private void loadData(){
        mAdapter = new GoodsInCartAdapter(DataManager.getInstance().getGoodsInCart(), new PriceAdapter.OnItemChangedListener() {
            @Override
            public void itemChanged(Goods item, int newCount) {
            }

            @Override
            public void itemRemoved(Goods item) {
                DataManager.getInstance().removeGoodsFromCart(item);
                mAdapter.notifyDataSetChanged();
                if (mAdapter.getItemCount() <= 0){
                    addToCartBtn.setEnabled(false);
                    recyclerEmptyView.setVisibility(View.VISIBLE);
                } else {
                    addToCartBtn.setEnabled(true);
                    recyclerEmptyView.setVisibility(View.GONE);
                }
            }
        });
        recyclerView.setAdapter(mAdapter);
    }
}
