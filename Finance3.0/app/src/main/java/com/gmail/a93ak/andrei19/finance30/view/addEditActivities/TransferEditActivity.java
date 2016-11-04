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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.Executors.PurseExecutor;
import com.gmail.a93ak.andrei19.finance30.control.Executors.TransferExecutor;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.pojos.Purse;
import com.gmail.a93ak.andrei19.finance30.modelVer2.TableQueryGenerator;
import com.gmail.a93ak.andrei19.finance30.modelVer2.pojos.Transfer;
import com.gmail.a93ak.andrei19.finance30.util.TransferRateParser.OnParseCompleted;
import com.gmail.a93ak.andrei19.finance30.util.TransferRateParser.RateJsonParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TransferEditActivity extends AppCompatActivity implements OnTaskCompleted, OnParseCompleted {

    private EditText editTransferName;
    private TextView editTransferDate;
    private AppCompatSpinner editTransferFromPurse;
    private AppCompatSpinner editTransferToPurse;
    private EditText editTransferFromAmount;
    private EditText editTransferToAmount;
    private List<Purse> allPurses;
    private SimpleDateFormat dateFormatter;
    private TextView officialRate;
    private Transfer transfer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_add_edit_activity);
        findViewsByIds();
        setDatePickerDialog();
        final long transferId = getIntent().getLongExtra(DBHelper.INCOME_KEY_ID, -1);
        RequestHolder<Transfer> transferRequestHolder = new RequestHolder<>();
        transferRequestHolder.setGetRequest(transferId);
        new TransferExecutor(this).execute(transferRequestHolder.getGetRequest());
    }

    private void findViewsByIds() {
        editTransferName = (EditText) findViewById(R.id.transfer_name);
        editTransferDate = (TextView) findViewById(R.id.transfer_date);
        editTransferFromPurse = (AppCompatSpinner) findViewById(R.id.transfer_from_purse);
        editTransferToPurse = (AppCompatSpinner) findViewById(R.id.transfer_to_purse);
        editTransferFromAmount = (EditText) findViewById(R.id.transfer_from_amount);
        editTransferToAmount = (EditText) findViewById(R.id.transfer_to_amount);
        officialRate = (TextView) findViewById(R.id.official_rate);
        ((TextView) findViewById(R.id.transfers)).setText(R.string.editing);
    }

    private void setDatePickerDialog() {
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        final Calendar today = Calendar.getInstance();
        final DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                editTransferDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        editTransferDate.setText(dateFormatter.format(today.getTime()));
        editTransferDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    private boolean checkFields() {
        boolean flag = true;
        if (editTransferName.getText().toString().length() == 0) {
            editTransferName.setBackground(getResources().getDrawable(R.drawable.shape_red_field));
            flag = false;
        } else {
            editTransferName.setBackground(getResources().getDrawable(R.drawable.shape_green_field));
        }
        try {
            final double fromAmount = Double.parseDouble(editTransferFromAmount.getText().toString());
            if (fromAmount <= 0) {
                editTransferFromAmount.setBackground(getResources().getDrawable(R.drawable.shape_red_field));
                flag = false;
            } else {
                editTransferFromAmount.setBackground(getResources().getDrawable(R.drawable.shape_green_field));
            }
        } catch (NumberFormatException e) {
            editTransferFromAmount.setBackground(getResources().getDrawable(R.drawable.shape_red_field));
            flag = false;
        }
        try {
            final double toAmount = Double.parseDouble(editTransferToAmount.getText().toString());
            if (toAmount <= 0) {
                editTransferToAmount.setBackground(getResources().getDrawable(R.drawable.shape_red_field));
                flag = false;
            } else {
                editTransferToAmount.setBackground(getResources().getDrawable(R.drawable.shape_green_field));
            }
        } catch (NumberFormatException e) {
            editTransferToAmount.setBackground(getResources().getDrawable(R.drawable.shape_red_field));
            flag = false;
        }
        if (editTransferFromPurse.getSelectedItemPosition() == editTransferToPurse.getSelectedItemPosition()) {
            editTransferFromPurse.setBackground(getResources().getDrawable(R.drawable.shape_red_field));
            editTransferToPurse.setBackground(getResources().getDrawable(R.drawable.shape_red_field));
            flag = false;
        } else {
            editTransferFromPurse.setBackground(getResources().getDrawable(R.drawable.shape_green_field));
            editTransferToPurse.setBackground(getResources().getDrawable(R.drawable.shape_green_field));
        }
        return flag;
    }

    public void addEditTransfer(View view) {

        if (checkFields()) {
            try {
                final Transfer transfer = new Transfer();
                transfer.setId(this.transfer.getId());
                transfer.setName(editTransferName.getText().toString());
                transfer.setDate(dateFormatter.parse(editTransferDate.getText().toString()).getTime());
                transfer.setFromPurseId(allPurses.get(editTransferFromPurse.getSelectedItemPosition()).getId());
                transfer.setToPurseId(allPurses.get(editTransferToPurse.getSelectedItemPosition()).getId());
                transfer.setFromAmount(Double.parseDouble(editTransferFromAmount.getText().toString()));
                transfer.setToAmount(Double.parseDouble(editTransferToAmount.getText().toString()));
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
    public void onTaskCompleted(Result result) {
        switch (result.getId()) {
            case TransferExecutor.KEY_RESULT_GET:
                transfer = (Transfer) result.getT();
                editTransferName.setText(transfer.getName());
                editTransferDate.setText(dateFormatter.format(transfer.getDate()));
                editTransferFromAmount.setText(String.valueOf(transfer.getFromAmount()));
                editTransferToAmount.setText(String.valueOf(transfer.getToAmount()));
                RequestHolder<Purse> purseRequestHolder = new RequestHolder<>();
                purseRequestHolder.setGetAllToListRequest(0);
                new PurseExecutor(this).execute(purseRequestHolder.getGetAllToListRequest());
                break;
            case PurseExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                allPurses = (List<Purse>) result.getT();
                final String[] pursesNames = new String[allPurses.size()];
                int i = 0;
                int fromPursePosition = 0;
                int toPursePosition = 0;
                for (Purse purse : allPurses) {
                    if (purse.getId() == transfer.getFromPurseId()) {
                        fromPursePosition = i;
                    } else if (purse.getId() == transfer.getToPurseId()) {
                        toPursePosition = i;
                    }
                    pursesNames[i++] = purse.getName();
                }
                final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, pursesNames);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                editTransferFromPurse.setAdapter(spinnerAdapter);
                editTransferToPurse.setAdapter(spinnerAdapter);
                editTransferFromPurse.setSelection(fromPursePosition);
                editTransferToPurse.setSelection(toPursePosition);
                editTransferToPurse.setOnItemSelectedListener(new TransferEditActivity.MyItemSelectedListener());
                editTransferFromPurse.setOnItemSelectedListener(new TransferEditActivity.MyItemSelectedListener());
                break;
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
            if (editTransferFromPurse.getSelectedItemPosition() == editTransferToPurse.getSelectedItemPosition()) {
                officialRate.setText("");
            } else {
                final long idFrom = (allPurses.get(editTransferFromPurse.getSelectedItemPosition()).getCurrency_id());
                final long idTo = (allPurses.get(editTransferToPurse.getSelectedItemPosition()).getCurrency_id());
                new RateJsonParser(TransferEditActivity.this).execute(idFrom, idTo);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
