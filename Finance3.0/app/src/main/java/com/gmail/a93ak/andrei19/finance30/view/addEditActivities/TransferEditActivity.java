package com.gmail.a93ak.andrei19.finance30.view.addEditActivities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.gmail.a93ak.andrei19.finance30.App;
import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.executors.PurseExecutor;
import com.gmail.a93ak.andrei19.finance30.control.executors.TransferExecutor;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.TableQueryGenerator;
import com.gmail.a93ak.andrei19.finance30.model.models.Purse;
import com.gmail.a93ak.andrei19.finance30.model.models.Transfer;
import com.gmail.a93ak.andrei19.finance30.util.transferRateParser.OnParseCompleted;
import com.gmail.a93ak.andrei19.finance30.util.transferRateParser.RateJsonParser;

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
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        if (getSharedPreferences(App.PREFS, Context.MODE_PRIVATE).getBoolean(App.THEME, false)) {
            setTheme(R.style.Dark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_add_edit_activity);
        findViewsByIds();
        setDatePickerDialog();
        final long transferId = getIntent().getLongExtra(Transfer.ID, -1);
        new TransferExecutor(this).execute(new RequestHolder<Transfer>().get(transferId));
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
        dateFormatter = new SimpleDateFormat(getResources().getString(R.string.dateFormat), Locale.US);
        final Calendar today = Calendar.getInstance();
        final DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(final DatePicker view, final int year, final int month, final int dayOfMonth) {
                final Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                editTransferDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        editTransferDate.setText(dateFormatter.format(today.getTime()));
        editTransferDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialog.show();
            }
        });
    }

    @Nullable
    private Transfer checkFields() {
        final Transfer transfer = new Transfer();
        boolean flag = true;
        if (editTransferName.getText().toString().length() == 0) {
            editTransferName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        } else {
            editTransferName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
            transfer.setName(editTransferName.getText().toString());
        }
        try {
            transfer.setDate(dateFormatter.parse(editTransferDate.getText().toString()).getTime());
            final double fromAmount = Double.parseDouble(editTransferFromAmount.getText().toString());
            if (fromAmount <= 0) {
                editTransferFromAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
                flag = false;
            } else {
                editTransferFromAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
                transfer.setFromAmount(fromAmount);
            }
        } catch (final NumberFormatException e) {
            editTransferFromAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        } catch (final ParseException e) {
            flag = false;
        }
        try {
            final double toAmount = Double.parseDouble(editTransferToAmount.getText().toString());
            if (toAmount <= 0) {
                editTransferToAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
                flag = false;
            } else {
                editTransferToAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
                transfer.setToAmount(toAmount);
            }
        } catch (final NumberFormatException e) {
            editTransferToAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        }
        if (editTransferFromPurse.getSelectedItemPosition() == editTransferToPurse.getSelectedItemPosition()) {
            editTransferFromPurse.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            editTransferToPurse.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        } else {
            editTransferFromPurse.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
            editTransferToPurse.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
            transfer.setFromPurseId(allPurses.get(editTransferFromPurse.getSelectedItemPosition()).getId());
            transfer.setToPurseId(allPurses.get(editTransferToPurse.getSelectedItemPosition()).getId());
        }
        if (!flag) {
            return null;
        } else {
            return transfer;
        }
    }

    public void addEditTransfer(final View view) {

        final Transfer transfer = checkFields();
        if (transfer != null) {
            transfer.setId(this.transfer.getId());
            final Intent intent = new Intent();
            intent.putExtra(TableQueryGenerator.getTableName(Transfer.class), transfer);
            setResult(RESULT_OK, intent);
            finish();

        }
    }

    @Override
    public void onTaskCompleted(final Result result) {
        switch (result.getId()) {
            case TransferExecutor.KEY_RESULT_GET:
                transfer = (Transfer) result.getObject();
                editTransferName.setText(transfer.getName());
                editTransferDate.setText(dateFormatter.format(transfer.getDate()));
                editTransferFromAmount.setText(String.valueOf(transfer.getFromAmount()));
                editTransferToAmount.setText(String.valueOf(transfer.getToAmount()));
                final RequestHolder<Purse> purseRequestHolder = new RequestHolder<>();
                new PurseExecutor(this).execute(purseRequestHolder.getAllToList(RequestHolder.SELECTION_ALL));
                break;
            case PurseExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                allPurses = (List<Purse>) result.getObject();
                final String[] pursesNames = new String[allPurses.size()];
                int i = 0;
                int fromPursePosition = 0;
                int toPursePosition = 0;
                for (final Purse purse : allPurses) {
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
    public void onParseCompleted(final Double result) {
        if (result < 0) {
            officialRate.setText(R.string.checkInternet);

        } else {
            officialRate.setText(String.format("%.4f", result));
        }
    }

    private class MyItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
            if (editTransferFromPurse.getSelectedItemPosition() == editTransferToPurse.getSelectedItemPosition()) {
                officialRate.setText("");
            } else {
                final long idFrom = (allPurses.get(editTransferFromPurse.getSelectedItemPosition()).getCurrencyId());
                final long idTo = (allPurses.get(editTransferToPurse.getSelectedItemPosition()).getCurrencyId());
                new RateJsonParser(TransferEditActivity.this).execute(idFrom, idTo);
            }
        }

        @Override
        public void onNothingSelected(final AdapterView<?> parent) {

        }
    }
}
