package jrcom.ru.actionbarcontect.fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import jrcom.ru.actionbarcontect.R;
import jrcom.ru.actionbarcontect.adapters.PriceAdapter;
import jrcom.ru.actionbarcontect.model.Goods;
import jrcom.ru.actionbarcontect.provider.DataManager;
import jrcom.ru.actionbarcontect.api.ApiManager;

/**
 * Created by eugeniy.rubilov on 14/03/2015.
 */
public class GoodsListFragment extends Fragment{

    private OnChangeMenuItemsListener onChangeMenuItemsListener;
    private RecyclerView recyclerView;
    private PriceAdapter adapter;
    private TextView totalCandidateSum;
    private ProgressDialog pd;
    //receiver for notify sum changed
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            totalCandidateSum.setText(getString(R.string.total_price, intent.getFloatExtra("sum", 0f)));
        }
    };

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

        pd = new ProgressDialog(getActivity());
        pd.setMessage(getString(R.string.action_loading));
        pd.show();
        loadData();
        Button addToCartBtn = (Button)view.findViewById(R.id.addToCart_btn_goodsFragment);
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCart();
            }
        });
        totalCandidateSum = (TextView)view.findViewById(R.id.totalSelected_tv_goodsFragment);
    }

    private void updateCart(){
        //reset list items
        totalCandidateSum.setText(getString(R.string.total_price, 0f));
        DataManager.getInstance().addGoodsToCart();
        adapter.notifyDataSetChanged();

        if (onChangeMenuItemsListener != null){
            onChangeMenuItemsListener.changeItemCart(DataManager.getInstance().getCartSum());
        }
    }

    /**
     * Notify menu item cart in activity
     * @param onChangeMenuItemsListener
     */
    public void setOnChangeMenuItemsListener(OnChangeMenuItemsListener onChangeMenuItemsListener) {
        this.onChangeMenuItemsListener = onChangeMenuItemsListener;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null){
            adapter.notifyDataSetChanged();
        }
        IntentFilter intentFilter = new IntentFilter(DataManager.DATA_BROADCAST);
        getActivity().registerReceiver(br, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(br);
    }

    private List<Goods> loadData(){

        ApiManager.downloadFile(new ApiManager.ResponseCallback() {
            private Object response;
            @Override
            public void onSuccess(Object goods) {
                response = goods;
                process();
            }

            @Override
            public void onFailure(int code, String reason) {
                //get from cache
                if (code == -1) {
                    response = DataManager.getInstance().getGoods();
                } else {
                    response = null;
                }
                process();
            }

            private void process(){
                try {
                    if (response!= null) {
                        if (adapter == null) {
                            adapter = new PriceAdapter((List<Goods>) response);
                            recyclerView.setAdapter(adapter);
                        } else {
                            adapter.setNewGoods((List<Goods>) response);
                        }
                    } else {
                        Toast.makeText(getActivity(), R.string.file_broken, Toast.LENGTH_LONG).show();
                    }
                } catch (ClassCastException ex){
                    ex.printStackTrace();
                }
                finally {
                    pd.dismiss();
                }
            }
        });
        return DataManager.getInstance().getGoods();
    }

    /**
     * Notify activity then item menu changed
     */
    public interface OnChangeMenuItemsListener {
        public void changeItemCart(float sum);
    }
}
