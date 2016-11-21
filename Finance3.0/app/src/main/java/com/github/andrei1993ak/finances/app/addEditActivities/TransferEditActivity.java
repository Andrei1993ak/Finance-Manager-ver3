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
import com.github.andrei1993ak.finances.control.executors.TransferExecutor;
import com.github.andrei1993ak.finances.control.executors.WalletExecutor;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Transfer;
import com.github.andrei1993ak.finances.model.models.Wallet;
import com.github.andrei1993ak.finances.util.Constants;
import com.github.andrei1993ak.finances.util.transferRateParser.OnParseCompleted;
import com.github.andrei1993ak.finances.util.transferRateParser.RateJsonParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TransferEditActivity extends BaseActivity implements OnTaskCompleted, OnParseCompleted {

    private EditText editTransferName;
    private TextView editTransferDate;
    private AppCompatSpinner editTransferFromWallet;
    private AppCompatSpinner editTransferToWallet;
    private EditText editTransferFromAmount;
    private EditText editTransferToAmount;
    private List<Wallet> allWallets;
    private SimpleDateFormat dateFormatter;
    private TextView officialRate;
    private Transfer transfer;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_add_edit_activity);
        setTitle(R.string.editing);
        initFields();
        final long transferId = getIntent().getLongExtra(Transfer.ID, -1);
        new TransferExecutor(this).execute(new RequestAdapter<Transfer>().get(transferId));
    }

    private void initFields() {
        this.editTransferName = (EditText) findViewById(R.id.transfer_name);
        this.editTransferDate = (TextView) findViewById(R.id.transfer_date);
        this.editTransferFromWallet = (AppCompatSpinner) findViewById(R.id.transfer_from_wallet);
        this.editTransferToWallet = (AppCompatSpinner) findViewById(R.id.transfer_to_wallet);
        this.editTransferFromAmount = (EditText) findViewById(R.id.transfer_from_amount);
        this.editTransferToAmount = (EditText) findViewById(R.id.transfer_to_amount);
        this.officialRate = (TextView) findViewById(R.id.official_rate);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_transfer_add_edit);
        fab.setImageResource(android.R.drawable.ic_menu_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                editTransfer();
            }
        });
        setDatePickerDialog();
    }

    private void setDatePickerDialog() {
        this.dateFormatter = new SimpleDateFormat(Constants.MAIN_DATE_FORMAT, Locale.getDefault());
        final Calendar today = Calendar.getInstance();
        final DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(final DatePicker view, final int year, final int month, final int dayOfMonth) {
                final Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                editTransferDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        this.editTransferDate.setText(dateFormatter.format(today.getTime()));
        this.editTransferDate.setOnClickListener(new View.OnClickListener() {
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
        if (editTransferFromWallet.getSelectedItemPosition() == editTransferToWallet.getSelectedItemPosition()) {
            editTransferFromWallet.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            editTransferToWallet.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        } else {
            editTransferFromWallet.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
            editTransferToWallet.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
            transfer.setFromWalletId(allWallets.get(editTransferFromWallet.getSelectedItemPosition()).getId());
            transfer.setToWalletId(allWallets.get(editTransferToWallet.getSelectedItemPosition()).getId());
        }
        if (!flag) {
            return null;
        } else {
            return transfer;
        }
    }

    public void editTransfer() {

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
                final RequestAdapter<Wallet> walletRequestAdapter = new RequestAdapter<>();
                new WalletExecutor(this).execute(walletRequestAdapter.getAllToList(RequestAdapter.SELECTION_ALL));
                break;
            case WalletExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                allWallets = (List<Wallet>) result.getObject();
                final String[] walletsNames = new String[allWallets.size()];
                int i = 0;
                int fromWalletPosition = 0;
                int toWalletPosition = 0;
                for (final Wallet wallet : allWallets) {
                    if (wallet.getId() == transfer.getFromWalletId()) {
                        fromWalletPosition = i;
                    } else if (wallet.getId() == transfer.getToWalletId()) {
                        toWalletPosition = i;
                    }
                    walletsNames[i++] = wallet.getName();
                }
                final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, walletsNames);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                editTransferFromWallet.setAdapter(spinnerAdapter);
                editTransferToWallet.setAdapter(spinnerAdapter);
                editTransferFromWallet.setSelection(fromWalletPosition);
                editTransferToWallet.setSelection(toWalletPosition);
                editTransferToWallet.setOnItemSelectedListener(new TransferEditActivity.MyItemSelectedListener());
                editTransferFromWallet.setOnItemSelectedListener(new TransferEditActivity.MyItemSelectedListener());
                break;
        }
    }

    @Override
    public void onParseCompleted(final Double result) {
        if (result < 0) {
            officialRate.setText(R.string.checkInternet);

        } else {
            officialRate.setText(String.format(Locale.getDefault(), "%.4f", result));
        }
    }

    private class MyItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
            if (editTransferFromWallet.getSelectedItemPosition() == editTransferToWallet.getSelectedItemPosition()) {
                officialRate.setText("");
            } else {
                final long idFrom = (allWallets.get(editTransferFromWallet.getSelectedItemPosition()).getCurrencyId());
                final long idTo = (allWallets.get(editTransferToWallet.getSelectedItemPosition()).getCurrencyId());
                new RateJsonParser(TransferEditActivity.this).execute(idFrom, idTo);
            }
        }

        @Override
        public void onNothingSelected(final AdapterView<?> parent) {

        }
    }
}
