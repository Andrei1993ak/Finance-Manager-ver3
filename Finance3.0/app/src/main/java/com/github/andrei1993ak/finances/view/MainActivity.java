package com.github.andrei1993ak.finances.view;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.control.adapters.PursesRecycleViewAdapter;
import com.github.andrei1993ak.finances.control.loaders.PurseCursorLoader;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCurrencyOfficial;
import com.github.andrei1993ak.finances.model.models.CurrencyOfficial;
import com.github.andrei1993ak.finances.view.activities.CategoryStartingActivity;
import com.github.andrei1993ak.finances.view.activities.CostActivity;
import com.github.andrei1993ak.finances.view.activities.CurrencyActivity;
import com.github.andrei1993ak.finances.view.activities.IncomeActivity;
import com.github.andrei1993ak.finances.view.activities.PurseActivity;
import com.github.andrei1993ak.finances.view.activities.ReportsActivity;
import com.github.andrei1993ak.finances.view.activities.SettingsActivity;
import com.github.andrei1993ak.finances.view.activities.TransferActivity;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    //TODO move to Constants.class
    public static final int LOADER_ID = 0;
    public static final int REQUEST_CODE_SETTING = 0;

    private RecyclerView recyclerView;
    private PursesRecycleViewAdapter adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        if (getSharedPreferences(App.PREFS, Context.MODE_PRIVATE).getBoolean(App.THEME, false)) {
            setTheme(R.style.Dark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecViewPursesSwissHelper(this, LOADER_ID));
//        itemTouchHelper.attachToRecyclerView(recyclerView);
//        new HelloEndpoint().execute(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO move to local final var
        if (getSupportLoaderManager().getLoader(LOADER_ID) != null) {
            getSupportLoaderManager().getLoader(LOADER_ID).forceLoad();
        }
    }

    public void selectCategory(final View view) {
        switch (view.getId()) {
            case R.id.tvCurrency:
                startActivity(new Intent(this, CurrencyActivity.class));
                break;
            case R.id.tvPurse:
                startActivity(new Intent(this, PurseActivity.class));
                break;
            case R.id.tvCategories:
                startActivity(new Intent(this, CategoryStartingActivity.class));
                break;
            case R.id.tvIncomes:
                startActivity(new Intent(this, IncomeActivity.class));
                break;
            case R.id.tvCosts:
                startActivity(new Intent(this, CostActivity.class));
                break;
            case R.id.tvTransfers:
                startActivity(new Intent(this, TransferActivity.class));
                break;
            case R.id.tvReports:
                startActivity(new Intent(this, ReportsActivity.class));
                break;
            case R.id.tvSettings:

                startActivityForResult(new Intent(this, SettingsActivity.class), REQUEST_CODE_SETTING);
                break;
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == REQUEST_CODE_SETTING) {
            recreate();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        return new PurseCursorLoader(this);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
        adapter = new PursesRecycleViewAdapter(data, this);
        recyclerView.swapAdapter(adapter, true);

    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        recyclerView.swapAdapter(null, true);
    }

    private void fill() {
        DBHelperCurrencyOfficial helperCurrencyFromWeb = DBHelperCurrencyOfficial.getInstance();
        helperCurrencyFromWeb.add(new CurrencyOfficial("HNL", "Hondurian Lempira"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("MMK", "Myanma Kyat"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("BHD", "Bahrain Dinar"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("PEN", "Reruvian Nuevo Sol"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("CZK", "Czech Koruna"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("AMD", "Armenia Dram"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("ISK", "Icelandic Krona"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("MDL", "Moldova Lei"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("ILS", "Israeli New Sheqel"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("UZS", "Uzbekistan Sum"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("KWD", "Kuwaiti Dinar"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("JOD", "Jordanian Dinar"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("MUR", "Mauritian Rupee"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("UYU", "Uruguayan Peso"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("NOK", "Norwegian Krone"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("NIO", "Nicaraguan Cordoba"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("PLN", "Polish Zloty"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("SAR", "Saudi Riyal"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("LKR", "Sri Lanka Rupee"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("THB", "Thai Baht"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("AED", "U.A.E Dihram"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("BRL", "Brazilian Real"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("RON", "Romanian New Leu"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("HKD", "Hong Kong Dollar"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("XAF", "Central African CFA Franc"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("VND", "Vietnamese Dong"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("ARS", "Argentine Peso"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("XCD", "East Caribbean Dollar"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("CNY", "Chinese Yuan"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("GTQ", "Guatemalan Quetzal"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("MAD", "Moroccan Dihram"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("PAB", "Panamanian Balboa"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("BND", "Brunei Dollar"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("COP", "Colombian Peso"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("AZN", "Azerbaijan Manat"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("HUF", "Hungarian Forint"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("KGS", "Kyrgyzstan Som"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("IDR", "Indonesian Rupiah"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("TJS", "Tajikistan Ruble"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("KRW", "South Korean Won"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("MYR", "Malaysian Ringgit"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("EGP", "Egyptian Pound"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("PYG", "Paraguayan Guaran"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("NZD", "New Zealand Dollar"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("CRC", "Costa Rican Col"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("PKR", "Pakistani Rupee"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("RUB", "Russian Ruble"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("USD", "U.S. Dollar"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("EUR", "Euro"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("GBP", "U.K. Pound Sterling"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("AUD", "Australian Dollar"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("CHF", "Swiss Franc"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("JPY", "Japanese Yen"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("CAD", "Canadian Dollar"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("ZAR", "South African Rand"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("TND", "Tunisian Dinar"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("BBD", "Barbadian Dollar"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("BGN", "Bulgarian Lev"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("TRY", "Turkish Lira"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("PHP", "Philippine Peso"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("TWD", "New Taiwan Dollar "));
        helperCurrencyFromWeb.add(new CurrencyOfficial("NGN", "Nigerian Naira"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("XPF", "CFP Franc"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("GHS", "Ghanaian Cedi"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("JMD", "Jamaican Dollar"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("DZD", "Algerian Dinar"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("ANG", "Neth. Antillean Guilder"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("BWP", "Botswana Pula"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("RSD", "Serbian Dinar"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("CLP", "Chilean Peso"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("BYN", "Belarussian Ruble"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("DKK", "Danish Krone"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("TMT", "New Turkmenistan Manat"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("INR", "Indian Rupee"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("UAH", "Ukrainian Hryvnia"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("KZT", "Kazakhstani Tenge"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("LBP", "Lebanese Pound"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("LYD", "Libyan Dinar"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("BOB", "Bolivian Boliviano"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("NPR", "Nepalese Rupee"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("OMR", "Omani Rial"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("QAR", "Qatari Rial"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("SGD", "Singapore Dollar"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("SEK", "Swedish Krona"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("TTD", "Trinidad Tobago Dollar"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("VEF", "Venezuelan Bolivar"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("DOP", "Dominican Peso"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("HRK", "Croatian Kuna"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("MXN", "Mexican Peso"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("XOF", "West African CFA Franc"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("PGK", "Papua New Guinean kina"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("BSD", "Bahamian Dollar"));
        helperCurrencyFromWeb.add(new CurrencyOfficial("FJD", "Fiji Dollar"));
    }
}
