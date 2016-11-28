package com.github.andrei1993ak.finances.app.addEditActivities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.app.BaseActivity;
import com.github.andrei1993ak.finances.control.base.IOnTaskCompleted;
import com.github.andrei1993ak.finances.control.base.RequestAdapter;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.control.executors.CostCategoryExecutor;
import com.github.andrei1993ak.finances.control.executors.WalletExecutor;
import com.github.andrei1993ak.finances.model.DBHelper;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Cost;
import com.github.andrei1993ak.finances.model.models.CostCategory;
import com.github.andrei1993ak.finances.model.models.Wallet;
import com.github.andrei1993ak.finances.util.Constants;
import com.github.andrei1993ak.finances.util.universalLoader.ImageNameGenerator;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CostAddActivity extends BaseActivity implements IOnTaskCompleted {

    private static final int CAMERA_REQUEST = 1;

    private EditText newCostName;
    private EditText newCostAmount;
    private TextView newCostDate;
    private AppCompatSpinner newCostWallets;
    private AppCompatSpinner newCostCategory;
    private AppCompatSpinner newCostSubCategory;
    private List<Wallet> wallets;
    private List<CostCategory> categoriesList;
    private List<CostCategory> subCategoriesList;
    private SimpleDateFormat dateFormatter;
    private Bitmap photo;
    private ArrayAdapter<String> spinnerSubCategoriesAdapter;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cost_add_edit_activity);
        setTitle(R.string.newCost);

        initFields();

        new WalletExecutor(this).execute(new RequestAdapter<Wallet>().getAllToList(RequestAdapter.SELECTION_ALL));
        new CostCategoryExecutor(this).execute(new RequestAdapter<CostCategory>().getAllToList(RequestAdapter.SELECTION_PARENT_CATEGORIES));
    }

    private void initFields() {
        this.newCostName = (EditText) findViewById(R.id.cost_name);
        this.newCostAmount = (EditText) findViewById(R.id.cost_amount);
        this.newCostDate = (TextView) findViewById(R.id.cost_date);
        this.newCostWallets = (AppCompatSpinner) findViewById(R.id.cost_wallet);
        this.newCostCategory = (AppCompatSpinner) findViewById(R.id.cost_category);
        this.newCostSubCategory = (AppCompatSpinner) findViewById(R.id.cost_subCategory);

        final PackageManager pm = getApplicationContext().getPackageManager();
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            findViewById(R.id.add_edit_photo_button).setVisibility(View.INVISIBLE);
        }

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_cost_add_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                addNewCost();
            }
        });
        initDatePickerDialog();
    }

    private void initDatePickerDialog() {
        this.dateFormatter = new SimpleDateFormat(Constants.MAIN_DATE_FORMAT, Locale.getDefault());
        final Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(final DatePicker view, final int year, final int month, final int dayOfMonth) {
                final Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                newCostDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        this.newCostDate.setText(dateFormatter.format(newCalendar.getTime()));
        this.newCostDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialog.show();
            }
        });
    }

    public void addNewCost() {
        final Cost cost = checkFields();
        if (cost != null) {
            final Intent intent = new Intent();
            intent.putExtra(TableQueryGenerator.getTableName(Cost.class), cost);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Nullable
    private Cost checkFields() {
        final Cost cost = new Cost();
        boolean flag = true;
        if (newCostName.getText().toString().length() == 0) {
            newCostName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        } else {
            newCostName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
            cost.setName(newCostName.getText().toString());
        }
        try {
            cost.setDate(dateFormatter.parse(newCostDate.getText().toString()).getTime());
            final Double amount = Double.parseDouble(newCostAmount.getText().toString());
            if (amount > 0) {
                cost.setAmount(amount);
                newCostAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
            } else {
                newCostAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
                flag = false;
            }
        } catch (final NumberFormatException e) {
            newCostAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        } catch (final ParseException e) {
            flag = false;
        }
        if (newCostAmount.getText().length() == 0) {
            newCostAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        } else {
            newCostAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
        }
        cost.setWalletId(wallets.get(newCostWallets.getSelectedItemPosition()).getId());
        if (newCostSubCategory.getVisibility() == View.GONE) {
            cost.setCategoryId(categoriesList.get(newCostCategory.getSelectedItemPosition()).getId());
        } else {
            if (newCostSubCategory.getSelectedItemPosition() != spinnerSubCategoriesAdapter.getCount() - 1) {
                cost.setCategoryId(subCategoriesList.get(newCostSubCategory.getSelectedItemPosition()).getId());
            } else {
                cost.setCategoryId(categoriesList.get(newCostCategory.getSelectedItemPosition()).getId());
            }
        }
        if (photo == null) {
            cost.setPhoto(Constants.COST_HAS_NOT_PHOTO);
        } else {
            cost.setPhoto(Constants.COST_HAS_PHOTO);
        }
        if (!flag) {
            return null;
        } else {
            return cost;
        }
    }

    public void addEditPhoto(final View view) {
        final Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final Context context = this;
        final File imagePath = new File(context.getFilesDir(), Constants.SHARE_FOLDER);
        if (!imagePath.exists()) {
            imagePath.mkdirs();
        }
        final File file = new File(imagePath, Constants.TEMP_PHOTO_NAME);
        final Uri outputFileUri = FileProvider.getUriForFile(context, Constants.AUTHORITY, file);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    public void onTaskCompleted(final Result result) {
        switch (result.getId()) {
            case WalletExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                wallets = (List<Wallet>) result.getObject();
                final String[] walletsNames = new String[wallets.size()];
                int i = 0;
                for (final Wallet wallet : wallets) {
                    walletsNames[i++] = wallet.getName();
                }
                final ArrayAdapter<String> spinnerWalletsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, walletsNames);
                spinnerWalletsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                newCostWallets.setAdapter(spinnerWalletsAdapter);
                break;
            case CostCategoryExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                categoriesList = (List<CostCategory>) result.getObject();
                final String[] categoriesNames = new String[categoriesList.size()];
                int j = 0;
                for (final CostCategory costCategory : categoriesList) {
                    categoriesNames[j++] = costCategory.getName();
                }
                final ArrayAdapter<String> spinnerCategoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriesNames);
                spinnerCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                newCostCategory.setAdapter(spinnerCategoriesAdapter);
                newCostCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                        final RequestAdapter<CostCategory> categoryRequestAdapter = new RequestAdapter<>();
                        new CostCategoryExecutor(CostAddActivity.this).execute(
                                categoryRequestAdapter.getAllToListByCategory(categoriesList.get(position).getId()));
                    }

                    @Override
                    public void onNothingSelected(final AdapterView<?> parent) {

                    }
                });
                break;
            case CostCategoryExecutor.KEY_RESULT_GET_ALL_TO_LIST_BY_PARENT_ID:
                subCategoriesList = (List<CostCategory>) result.getObject();
                final int length = subCategoriesList.size();
                if (length == 0) {
                    newCostSubCategory.setVisibility(View.GONE);
                } else {
                    newCostSubCategory.setVisibility(View.VISIBLE);
                    final String[] subCategoriesNames = new String[subCategoriesList.size() + 1];
                    int k = 0;
                    for (final CostCategory costCategory : subCategoriesList) {
                        subCategoriesNames[k++] = costCategory.getName();
                    }
                    subCategoriesNames[k] = "-";
                    spinnerSubCategoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subCategoriesNames);
                    spinnerSubCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    newCostSubCategory.setAdapter(spinnerSubCategoriesAdapter);
                }
                break;
        }
    }

    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            final File imagePath = new File(this.getFilesDir(), Constants.SHARE_FOLDER);
            if (!imagePath.exists()) {
                imagePath.mkdirs();
            }
            final File file = new File(imagePath, Constants.TEMP_PHOTO_NAME);
            photo = BitmapFactory.decodeFile(file.getPath());
            final ImageView view = (ImageView) findViewById(R.id.cost_photo);
            view.setImageBitmap(photo);
            final long nextId = DBHelper.getInstance(this).getNextCostId();
            final String path = ImageNameGenerator.getImagePath(nextId);
            final File toFile = new File(path);
            if (toFile.exists()) {
                toFile.delete();
            }
            try {
                Files.move(file, toFile);
            } catch (final IOException e) {
                photo = null;
            }
        }
    }
}
