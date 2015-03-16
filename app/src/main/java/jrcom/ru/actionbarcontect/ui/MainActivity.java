package jrcom.ru.actionbarcontect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import jrcom.ru.actionbarcontect.R;
import jrcom.ru.actionbarcontect.StoreApplication;
import jrcom.ru.actionbarcontect.Utils;
import jrcom.ru.actionbarcontect.fragments.GoodsListFragment;
import jrcom.ru.actionbarcontect.provider.DataManager;


public class MainActivity extends ActionBarActivity implements GoodsListFragment.OnChangeMenuItemsListener {

    private float sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StoreApplication.getInstance().initDataManager();
        setContentView(R.layout.activity_main);
        Utils.checkNetworkAvailable(this);

        if (savedInstanceState == null) {
            GoodsListFragment fragment = new GoodsListFragment();
            fragment.setOnChangeMenuItemsListener(this);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment, "goods_list")
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_compose).setTitle("Корзина: " + sum);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_compose) {
            startActivity(new Intent(this, CartActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        changeItemCart(DataManager.getInstance().getCartSum());
    }

    @Override
    public void changeItemCart(float sum) {
        this.sum = sum;
        invalidateOptionsMenu();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
