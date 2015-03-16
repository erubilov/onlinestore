package jrcom.ru.actionbarcontect.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import jrcom.ru.actionbarcontect.R;
import jrcom.ru.actionbarcontect.fragments.GoodsInCartFragment;
import jrcom.ru.actionbarcontect.fragments.GoodsListFragment;


public class CartActivity extends ActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        if (savedInstanceState == null) {
            GoodsInCartFragment fragment = new GoodsInCartFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment, "goods_list")
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
