package com.github.andrei1993ak.finances.app.addEditActivities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.app.BaseActivity;
import com.github.andrei1993ak.finances.control.base.OnTaskCompleted;
import com.github.andrei1993ak.finances.control.base.RequestAdapter;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.control.executors.WalletExecutor;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Transfer;
import com.github.andrei1993ak.finances.model.models.Wallet;
import com.github.andrei1993ak.finances.util.transferRateParser.OnParseCompleted;
import com.github.andrei1993ak.finances.util.transferRateParser.RateJsonParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TransferAddActivity extends BaseActivity implements OnTaskCompleted, OnParseCompleted {

    private EditText newTransferName;
    private TextView newTransferDate;
    private AppCompatSpinner newTransferFromWallet;
    private AppCompatSpinner newTransferToWallet;
    private EditText newTransferFromAmount;
    private EditText newTransferToAmount;
    private List<Wallet> allWallets;
    private SimpleDateFormat dateFormatter;
    private TextView officialRate;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_add_edit_activity);
        setTitle(R.string.newTransfer);
        initFields();
        new WalletExecutor(this).execute(new RequestAdapter<Wallet>().getAllToList(RequestAdapter.SELECTION_ALL));
    }

    private void initFields() {
        newTransferName = (EditText) findViewById(R.id.transfer_name);
        newTransferDate = (TextView) findViewById(R.id.transfer_date);
        newTransferFromWallet = (AppCompatSpinner) findViewById(R.id.transfer_from_wallet);
        newTransferToWallet = (AppCompatSpinner) findViewById(R.id.transfer_to_wallet);
        newTransferFromAmount = (EditText) findViewById(R.id.transfer_from_amount);
        newTransferToAmount = (EditText) findViewById(R.id.transfer_to_amount);
        officialRate = (TextView) findViewById(R.id.official_rate);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_transfer_add_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                addTransfer();
            }
        });
        setDatePickerDialog();
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
        if (newTransferFromWallet.getSelectedItemPosition() == newTransferToWallet.getSelectedItemPosition()) {
            newTransferFromWallet.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            newTransferToWallet.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        } else {
            newTransferFromWallet.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
            newTransferToWallet.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
            transfer.setFromWalletId(allWallets.get(newTransferFromWallet.getSelectedItemPosition()).getId());
            transfer.setToWalletId(allWallets.get(newTransferToWallet.getSelectedItemPosition()).getId());
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
            case WalletExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                allWallets = (List<Wallet>) result.getObject();
                final String[] walletsNames = new String[allWallets.size()];
                int i = 0;
                for (final Wallet wallet : allWallets) {
                    walletsNames[i++] = wallet.getName();
                }
                final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, walletsNames);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                newTransferFromWallet.setAdapter(spinnerAdapter);
                newTransferToWallet.setAdapter(spinnerAdapter);
                newTransferToWallet.setOnItemSelectedListener(new MyItemSelectedListener());
                newTransferFromWallet.setOnItemSelectedListener(new MyItemSelectedListener());
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
            if (newTransferFromWallet.getSelectedItemPosition() == newTransferToWallet.getSelectedItemPosition()) {
                officialRate.setText("");
            } else {
                final long idFrom = (allWallets.get(newTransferFromWallet.getSelectedItemPosition()).getCurrencyId());
                final long idTo = (allWallets.get(newTransferToWallet.getSelectedItemPosition()).getCurrencyId());
                new RateJsonParser(TransferAddActivity.this).execute(idFrom, idTo);
            }
        }

        @Override
        public void onNothingSelected(final AdapterView<?> parent) {

        }
    }
}
