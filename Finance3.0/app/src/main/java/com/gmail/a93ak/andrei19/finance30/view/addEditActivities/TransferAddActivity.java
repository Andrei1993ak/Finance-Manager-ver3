package com.gmail.a93ak.andrei19.finance30.view.addEditActivities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.Executors.PurseExecutor;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.pojos.Purse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TransferAddActivity extends AppCompatActivity implements OnTaskCompleted {

    private EditText newTransferName;
    private TextView newTransferDate;
    private AppCompatSpinner newTransferFromPurse;
    private AppCompatSpinner newTransferToPurse;
    private EditText newTransferFromAmount;
    private EditText newTransferToAmount;
    private List<Purse> allPurses;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_add_activity);
        findViewsByIds();
        RequestHolder<Purse> purseRequestHolder = new RequestHolder<>();
        purseRequestHolder.setGetAllToListRequest(0);
        new PurseExecutor(this).execute(purseRequestHolder.getGetAllToListRequest());
        setDatePickerDialog();

//        Transfer transfer = new Transfer();
//        transfer.setName("Test");
//        transfer.setDate(1478198100000L);
//        transfer.setFromAmount(100.10);
//        transfer.setToAmount(200.20);
//        transfer.setFromPurseId(1);
//        transfer.setToPurseId(2);
//        Intent intent = new Intent();
//        intent.putExtra(TableQueryGenerator.getTableName(Transfer.class),transfer);
//        setResult(RESULT_OK, intent);
//        finish();
    }


    private boolean checkFields() {
        boolean flag = true;
        if (newTransferName.getText().toString().length() == 0) {
            newTransferName.setBackground(getResources().getDrawable(R.drawable.shape_red_field));
            flag = false;
        } else {
            newTransferName.setBackground(getResources().getDrawable(R.drawable.shape_green_field));
        }
        try {
            final double fromAmount = Double.parseDouble(newTransferFromAmount.getText().toString());
            if (fromAmount <= 0) {
                newTransferFromAmount.setBackground(getResources().getDrawable(R.drawable.shape_red_field));
                flag = false;
            } else {
                newTransferFromAmount.setBackground(getResources().getDrawable(R.drawable.shape_green_field));
            }
        } catch (NumberFormatException e) {
            newTransferFromAmount.setBackground(getResources().getDrawable(R.drawable.shape_red_field));
            flag = false;
        }
        try {
            final double toAmount = Double.parseDouble(newTransferToAmount.getText().toString());
            if (toAmount <= 0) {
                newTransferToAmount.setBackground(getResources().getDrawable(R.drawable.shape_red_field));
                flag = false;
            } else {
                newTransferToAmount.setBackground(getResources().getDrawable(R.drawable.shape_green_field));
            }
        } catch (NumberFormatException e) {
            newTransferToAmount.setBackground(getResources().getDrawable(R.drawable.shape_red_field));
            flag = false;
        }
        if (newTransferFromPurse.getSelectedItemPosition() == newTransferToPurse.getSelectedItemPosition()) {
            newTransferFromPurse.setBackground(getResources().getDrawable(R.drawable.shape_red_field));
            newTransferToPurse.setBackground(getResources().getDrawable(R.drawable.shape_red_field));
            flag = false;
        } else {
            newTransferFromPurse.setBackground(getResources().getDrawable(R.drawable.shape_green_field));
            newTransferToPurse.setBackground(getResources().getDrawable(R.drawable.shape_green_field));
        }
        return flag;
    }

    private void setDatePickerDialog() {
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        final Calendar today = Calendar.getInstance();
        final DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                newTransferDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        newTransferDate.setText(dateFormatter.format(today.getTime()));
        newTransferDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    private void findViewsByIds() {
        newTransferName = (EditText) findViewById(R.id.new_transfer_name);
        newTransferDate = (TextView) findViewById(R.id.new_transfer_date);
        newTransferFromPurse = (AppCompatSpinner) findViewById(R.id.new_transfer_from_purse);
        newTransferToPurse = (AppCompatSpinner) findViewById(R.id.new_transfer_to_purse);
        newTransferFromAmount = (EditText) findViewById(R.id.new_transfer_from_amount);
        newTransferToAmount = (EditText) findViewById(R.id.new_transfer_to_amount);
    }

    @Override
    public void onTaskCompleted(Result result) {
        switch (result.getId()) {
            case PurseExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                allPurses = (List<Purse>) result.getT();
                final String[] pursesNames = new String[allPurses.size()];
                int i = 0;
                for (Purse purse : allPurses) {
                    pursesNames[i++] = purse.getName();
                }
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, pursesNames);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                newTransferFromPurse.setAdapter(spinnerAdapter);
                newTransferToPurse.setAdapter(spinnerAdapter);
                break;
        }
    }

    public void addNewTransfer(View view) {
        checkFields();
    }
}
