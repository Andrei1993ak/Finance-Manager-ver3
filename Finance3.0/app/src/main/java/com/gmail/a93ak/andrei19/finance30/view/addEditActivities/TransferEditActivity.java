package com.gmail.a93ak.andrei19.finance30.view.addEditActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.modelVer2.TableQueryGenerator;
import com.gmail.a93ak.andrei19.finance30.modelVer2.pojos.Transfer;

public class TransferEditActivity extends AppCompatActivity implements OnTaskCompleted {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Transfer transfer = new Transfer();
        transfer.setId(getIntent().getLongExtra(Transfer.ID, -1));
        transfer.setName("Test");
        transfer.setDate(1478198100000L);
        transfer.setFromAmount(50.10);
        transfer.setToAmount(100.20);
        transfer.setFromPurseId(1);
        transfer.setToPurseId(2);
        Intent intent = new Intent();
        intent.putExtra(TableQueryGenerator.getTableName(Transfer.class),transfer);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onTaskCompleted(Result result) {

    }
}
