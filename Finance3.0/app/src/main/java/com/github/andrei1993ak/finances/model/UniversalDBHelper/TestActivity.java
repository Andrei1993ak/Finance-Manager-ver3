package com.github.andrei1993ak.finances.model.UniversalDBHelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.andrei1993ak.finances.model.models.Wallet;

import java.util.List;


public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testUniversalDbHelper();
    }

    private void testUniversalDbHelper() {
        final UniversalDBHelper<Wallet> helper = new UniversalDBHelper<>();
        final Wallet wallet = helper.get(1,Wallet.class);
        final List<Wallet> wallets =  helper.getAllToList(Wallet.class);
//        final long addCount = helper.add(new Wallet("КТо молодец",1,23.45));
        final int count = wallets.size();
//        final boolean eq = wallet.getName().equals(wallets.get(0).getName());
//        Toast.makeText(this,String.valueOf(addCount),Toast.LENGTH_LONG).show();
        Toast.makeText(this,String.valueOf(count),Toast.LENGTH_LONG).show();
    }
}
