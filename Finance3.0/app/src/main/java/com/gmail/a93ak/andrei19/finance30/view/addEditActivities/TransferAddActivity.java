package com.gmail.a93ak.andrei19.finance30.view.addEditActivities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
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
import com.gmail.a93ak.andrei19.finance30.modelVer2.TableQueryGenerator;
import com.gmail.a93ak.andrei19.finance30.modelVer2.pojos.Transfer;
import com.gmail.a93ak.andrei19.finance30.util.TransferRateParser.OnParseCompleted;
import com.gmail.a93ak.andrei19.finance30.util.TransferRateParser.RateSjonParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TransferAddActivity extends AppCompatActivity implements OnTaskCompleted, OnParseCompleted {

    private EditText newTransferName;
    private TextView newTransferDate;
    private AppCompatSpinner newTransferFromPurse;
    private AppCompatSpinner newTransferToPurse;
    private EditText newTransferFromAmount;
    private EditText newTransferToAmount;
    private List<Purse> allPurses;
    private SimpleDateFormat dateFormatter;
    private TextView officialRate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_add_edit_activity);
        findViewsByIds();
        setDatePickerDialog();
        RequestHolder<Purse> purseRequestHolder = new RequestHolder<>();
        purseRequestHolder.setGetAllToListRequest(0);
        new PurseExecutor(this).execute(purseRequestHolder.getGetAllToListRequest());
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
        newTransferName = (EditText) findViewById(R.id.transfer_name);
        newTransferDate = (TextView) findViewById(R.id.transfer_date);
        newTransferFromPurse = (AppCompatSpinner) findViewById(R.id.transfer_from_purse);
        newTransferToPurse = (AppCompatSpinner) findViewById(R.id.transfer_to_purse);
        newTransferFromAmount = (EditText) findViewById(R.id.transfer_from_amount);
        newTransferToAmount = (EditText) findViewById(R.id.transfer_to_amount);
        officialRate = (TextView) findViewById(R.id.official_rate);
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
                final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, pursesNames);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                newTransferFromPurse.setAdapter(spinnerAdapter);
                newTransferToPurse.setAdapter(spinnerAdapter);
                newTransferToPurse.setOnItemSelectedListener(new MyItemSelectedListener());
                newTransferFromPurse.setOnItemSelectedListener(new MyItemSelectedListener());
                break;
        }
    }

    public void addEditTransfer(View view) {

        if (checkFields()) {
            try {
                final Transfer transfer = new Transfer();
                transfer.setName(newTransferName.getText().toString());
                transfer.setDate(dateFormatter.parse(newTransferDate.getText().toString()).getTime());
                transfer.setFromPurseId(allPurses.get(newTransferFromPurse.getSelectedItemPosition()).getId());
                transfer.setToPurseId(allPurses.get(newTransferToPurse.getSelectedItemPosition()).getId());
                transfer.setFromAmount(Double.parseDouble(newTransferFromAmount.getText().toString()));
                transfer.setToAmount(Double.parseDouble(newTransferToAmount.getText().toString()));
                Intent intent = new Intent();
                intent.putExtra(TableQueryGenerator.getTableName(Transfer.class), transfer);
                setResult(RESULT_OK, intent);
                finish();
            } catch (ParseException e) {
                checkFields();
            }
        }
    }

    @Override
    public void onParseCompleted(Double result) {
        if (result < 0) {
            officialRate.setText(R.string.checkInternet);

        } else {
            officialRate.setText(String.format("%.4f", result));
        }
    }

    private class MyItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (newTransferFromPurse.getSelectedItemPosition() == newTransferToPurse.getSelectedItemPosition()) {
                officialRate.setText("");
            } else {
                final long idFrom = (allPurses.get(newTransferFromPurse.getSelectedItemPosition()).getCurrency_id());
                final long idTo = (allPurses.get(newTransferToPurse.getSelectedItemPosition()).getCurrency_id());
                new RateSjonParser(TransferAddActivity.this).execute(idFrom, idTo);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

}
