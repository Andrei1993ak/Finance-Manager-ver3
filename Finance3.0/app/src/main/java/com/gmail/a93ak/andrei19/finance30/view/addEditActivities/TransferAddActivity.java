package com.gmail.a93ak.andrei19.finance30.view.addEditActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.modelVer2.pojos.Transfer;

public class TransferAddActivity extends AppCompatActivity implements OnTaskCompleted {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Transfer transfer = new Transfer();
        transfer.setName("Test");
        transfer.setDate(1478198100000L);
        transfer.setFromAmount(100.10);
        transfer.setToAmount(200.20);
        transfer.setFromPurseId(1);
        transfer.setToPurseId(2);
        Intent intent = new Intent();
        intent.putExtra("transfer",transfer);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onTaskCompleted(Result result) {

    }
}
