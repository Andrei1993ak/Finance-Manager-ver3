package com.github.andrei1993ak.finances.view.addEditActivities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.control.executors.PurseExecutor;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Purse;
import com.github.andrei1993ak.finances.util.transferRateParser.OnParseCompleted;
import com.github.andrei1993ak.finances.util.transferRateParser.RateJsonParser;
import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.control.base.OnTaskCompleted;
import com.github.andrei1993ak.finances.control.base.RequestHolder;
import com.github.andrei1993ak.finances.model.models.Transfer;

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
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        if (getSharedPreferences(App.PREFS, Context.MODE_PRIVATE).getBoolean(App.THEME, false)) {
            setTheme(R.style.Dark);
        }
        super.onCreate(savedInstanceState);
        setTitle(R.string.newTransfer);
        setContentView(R.layout.transfer_add_edit_activity);
        findViewsByIds();
        setDatePickerDialog();
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_transfer_add_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                addTransfer();
            }
        });
        new PurseExecutor(this).execute(new RequestHolder<Purse>().getAllToList(RequestHolder.SELECTION_ALL));
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

    private void setDatePickerDialog() {
        dateFormatter = new SimpleDateFormat(getResources().getString(R.string.dateFormat), Locale.US);
        final Calendar today = Calendar.getInstance();
        final DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(final DatePicker view, final int year, final int month, final int dayOfMonth) {
                final Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                newTransferDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        newTransferDate.setText(dateFormatter.format(today.getTime()));
        newTransferDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialog.show();
            }
        });
    }

    public void addTransfer() {

        final Transfer transfer = checkFields();
        if (transfer != null) {
            final Intent intent = new Intent();
            intent.putExtra(TableQueryGenerator.getTableName(Transfer.class), transfer);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Nullable
    private Transfer checkFields() {
        final Transfer transfer = new Transfer();
        boolean flag = true;
        if (newTransferName.getText().toString().length() == 0) {
            newTransferName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        } else {
            newTransferName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
            transfer.setName(newTransferName.getText().toString());
        }
        try {
            transfer.setDate(dateFormatter.parse(newTransferDate.getText().toString()).getTime());
            final double fromAmount = Double.parseDouble(newTransferFromAmount.getText().toString());
            if (fromAmount <= 0) {
                newTransferFromAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
                flag = false;
            } else {
                newTransferFromAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
                transfer.setFromAmount(fromAmount);
            }
        } catch (final NumberFormatException e) {
            newTransferFromAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        } catch (final ParseException e) {
            flag = false;
        }
        try {
            final double toAmount = Double.parseDouble(newTransferToAmount.getText().toString());
            if (toAmount <= 0) {
                newTransferToAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
                flag = false;
            } else {
                newTransferToAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
                transfer.setToAmount(toAmount);
            }
        } catch (final NumberFormatException e) {
            newTransferToAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        }
        if (newTransferFromPurse.getSelectedItemPosition() == newTransferToPurse.getSelectedItemPosition()) {
            newTransferFromPurse.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            newTransferToPurse.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        } else {
            newTransferFromPurse.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
            newTransferToPurse.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
            transfer.setFromPurseId(allPurses.get(newTransferFromPurse.getSelectedItemPosition()).getId());
            transfer.setToPurseId(allPurses.get(newTransferToPurse.getSelectedItemPosition()).getId());
        }
        if (!flag) {
            return null;
        } else {
            return transfer;
        }
    }

    @Override
    public void onTaskCompleted(final Result result) {
        switch (result.getId()) {
            case PurseExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                allPurses = (List<Purse>) result.getObject();
                final String[] pursesNames = new String[allPurses.size()];
                int i = 0;
                for (final Purse purse : allPurses) {
                    pursesNames[i++] = purse.getName();
                }
                final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pursesNames);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                newTransferFromPurse.setAdapter(spinnerAdapter);
                newTransferToPurse.setAdapter(spinnerAdapter);
                newTransferToPurse.setOnItemSelectedListener(new MyItemSelectedListener());
                newTransferFromPurse.setOnItemSelectedListener(new MyItemSelectedListener());
                break;
        }
    }

    @Override
    public void onParseCompleted(final Double result) {
        if (result < 0) {
            officialRate.setText(R.string.checkInternet);
        } else {
            officialRate.setText(String.format(Locale.US, "%.4f", result));
        }
    }

    private class MyItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
            if (newTransferFromPurse.getSelectedItemPosition() == newTransferToPurse.getSelectedItemPosition()) {
                officialRate.setText("");
            } else {
                final long idFrom = (allPurses.get(newTransferFromPurse.getSelectedItemPosition()).getCurrencyId());
                final long idTo = (allPurses.get(newTransferToPurse.getSelectedItemPosition()).getCurrencyId());
                new RateJsonParser(TransferAddActivity.this).execute(idFrom, idTo);
            }
        }

        @Override
        public void onNothingSelected(final AdapterView<?> parent) {

        }
    }
}
