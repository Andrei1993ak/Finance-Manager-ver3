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
import com.github.andrei1993ak.finances.control.executors.CostExecutor;
import com.github.andrei1993ak.finances.control.executors.WalletExecutor;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Cost;
import com.github.andrei1993ak.finances.model.models.CostCategory;
import com.github.andrei1993ak.finances.model.models.Wallet;
import com.github.andrei1993ak.finances.util.Constants;
import com.github.andrei1993ak.finances.util.universalLoader.ImageNameGenerator;
import com.github.andrei1993ak.finances.util.universalLoader.loaders.BitmapLoader;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CostEditActivity extends BaseActivity implements IOnTaskCompleted {

    private Cost cost;
    private EditText editCostName;
    private EditText editCostAmount;
    private TextView editCostDate;
    private AppCompatSpinner editCostWallet;
    private AppCompatSpinner editCostCategory;
    private AppCompatSpinner editCostSubCategory;
    private List<Wallet> wallets;
    private List<CostCategory> categoriesList;
    private List<CostCategory> subCategoriesList;
    private ImageView imageView;
    private SimpleDateFormat dateFormatter;
    private long parentId;
    private long editCostId;
    private Bitmap photo;
    public static final int CAMERA_REQUEST = 1;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cost_add_edit_activity);
        setTitle(R.string.editing);

        initFields();

        editCostId = getIntent().getLongExtra(Cost.ID, -1);
        new CostExecutor(this).execute(new RequestAdapter<Cost>().get(editCostId));
    }

    private void initFields() {
        this.editCostName = (EditText) findViewById(R.id.cost_name);
        this.editCostAmount = (EditText) findViewById(R.id.cost_amount);
        this.editCostDate = (TextView) findViewById(R.id.cost_date);
        this.editCostWallet = (AppCompatSpinner) findViewById(R.id.cost_wallet);
        this.editCostCategory = (AppCompatSpinner) findViewById(R.id.cost_category);
        this.editCostSubCategory = (AppCompatSpinner) findViewById(R.id.cost_subCategory);
        this.imageView = (ImageView) findViewById(R.id.cost_photo);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_cost_add_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                editCost();
            }
        });
        fab.setImageResource(android.R.drawable.ic_menu_edit);

        final PackageManager pm = getApplicationContext().getPackageManager();
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            findViewById(R.id.add_edit_photo_button).setVisibility(View.INVISIBLE);
        }

        setDatePickerDialog();
    }

    private void setDatePickerDialog() {
        this.dateFormatter = new SimpleDateFormat(Constants.MAIN_DATE_FORMAT, Locale.getDefault());
        final Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(final DatePicker view, final int year, final int month, final int dayOfMonth) {
                final Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                editCostDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        editCostDate.setText(dateFormatter.format(newCalendar.getTime()));
        editCostDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialog.show();
            }
        });
    }

    public void editCost() {
        final Cost cost = checkFields();
        if (cost != null) {
            cost.setId(editCostId);
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
        if (editCostName.getText().toString().length() == 0) {
            editCostName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        } else {
            editCostName.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
            cost.setName(editCostName.getText().toString());
        }
        try {
            cost.setDate(dateFormatter.parse(editCostDate.getText().toString()).getTime());
            final Double amount = Double.parseDouble(editCostAmount.getText().toString());
            if (amount > 0) {
                cost.setAmount(amount);
                editCostAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
            } else {
                editCostAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
                flag = false;
            }
        } catch (final NumberFormatException e) {
            editCostAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        } catch (final ParseException e) {
            flag = false;
        }
        if (editCostAmount.getText().length() == 0) {
            editCostAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_red_field));
            flag = false;
        } else {
            editCostAmount.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_green_field));
        }
        cost.setWalletId(wallets.get(editCostWallet.getSelectedItemPosition()).getId());
        if (editCostSubCategory.getVisibility() == View.GONE) {
            cost.setCategoryId(categoriesList.get(editCostCategory.getSelectedItemPosition()).getId());
        } else {
            cost.setCategoryId(subCategoriesList.get(editCostSubCategory.getSelectedItemPosition()).getId());
        }
        if (photo == null) {
            cost.setPhoto(0);
        } else {
            cost.setPhoto(1);
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
            case CostExecutor.KEY_RESULT_GET:
                cost = (Cost) result.getObject();
                editCostName.setText(cost.getName());
                editCostAmount.setText(String.valueOf(cost.getAmount()));
                editCostDate.setText(dateFormatter.format(cost.getDate()));
                if (cost.getPhoto() == Constants.COST_HAS_PHOTO) {
                    final String filePath = ImageNameGenerator.getImagePath(editCostId);
                    final File file = new File(filePath);
                    final BitmapLoader bitmapLoader = BitmapLoader.getInstance(this);
                    try {
                        bitmapLoader.load(file.toURI().toURL().toString(), imageView);
                    } catch (final MalformedURLException e) {
                        e.printStackTrace();
                        //TODO demo image
                    }
                }
                final RequestAdapter<Wallet> walletRequestAdapter = new RequestAdapter<>();
                new WalletExecutor(this).execute(walletRequestAdapter.getAllToList(0));
                final RequestAdapter<CostCategory> categoryRequestAdapter = new RequestAdapter<>();
                new CostCategoryExecutor(this).execute(categoryRequestAdapter.get(cost.getCategoryId()));
                break;
            case WalletExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                wallets = (List<Wallet>) result.getObject();
                final String[] walletsNames = new String[wallets.size()];
                int i = 0;
                int position = -1;
                for (final Wallet wallet : wallets) {
                    walletsNames[i++] = wallet.getName();
                    if (wallet.getId() == cost.getWalletId()) {
                        position = i - 1;
                    }
                }
                final ArrayAdapter<String> spinnerWalletsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, walletsNames);
                spinnerWalletsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                editCostWallet.setAdapter(spinnerWalletsAdapter);
                editCostWallet.setSelection(position);
                break;
            case CostCategoryExecutor.KEY_RESULT_GET:
                final CostCategory category = (CostCategory) result.getObject();
                parentId = category.getParentId();
                final RequestAdapter<CostCategory> costCategoryRequestAdapter = new RequestAdapter<>();
                new CostCategoryExecutor(this).execute(costCategoryRequestAdapter.getAllToList(1));
                break;
            case CostCategoryExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                categoriesList = (List<CostCategory>) result.getObject();
                final String[] categoriesNames = new String[categoriesList.size()];
                int j = 0;
                int categoryPosition = -1;
                for (final CostCategory costCategory : categoriesList) {
                    categoriesNames[j++] = costCategory.getName();
                    if (parentId == -1) {
                        if (costCategory.getId() == cost.getCategoryId()) {
                            categoryPosition = j - 1;
                        }
                    } else {
                        if (costCategory.getId() == parentId) {
                            categoryPosition = j - 1;
                        }
                    }
                }
                final ArrayAdapter<String> spinnerCategoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriesNames);
                spinnerCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                editCostCategory.setAdapter(spinnerCategoriesAdapter);
                editCostCategory.setSelection(categoryPosition);
                editCostCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                        final RequestAdapter<CostCategory> categoryRequestAdapter = new RequestAdapter<>();
                        new CostCategoryExecutor(CostEditActivity.this).execute(categoryRequestAdapter.getAllToListByCategory(categoriesList.get(position).getId()));
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
                    editCostSubCategory.setVisibility(View.GONE);
                } else {
                    editCostSubCategory.setVisibility(View.VISIBLE);
                    final String[] subCategoriesNames = new String[subCategoriesList.size()];
                    int k = 0;
                    int subCatPosition = 0;
                    for (final CostCategory costCategory : subCategoriesList) {
                        subCategoriesNames[k++] = costCategory.getName();
                        if (costCategory.getId() == cost.getCategoryId()) {
                            subCatPosition = k - 1;
                        }
                    }
                    final ArrayAdapter<String> spinnerSubCategoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subCategoriesNames);
                    spinnerSubCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    editCostSubCategory.setAdapter(spinnerSubCategoriesAdapter);
                    editCostSubCategory.setSelection(subCatPosition);
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
            final ImageView view = (ImageView) findViewById(R.id.cost_photo);
            final String path = ImageNameGenerator.getImagePath(editCostId);
            final File toFile = new File(path);
            try {
                Files.move(file, toFile);
                photo = BitmapFactory.decodeFile(toFile.getPath());
                BitmapLoader.getInstance(this).clearCashes(toFile.toURI().toURL().toString());
                view.setImageBitmap(photo);
            } catch (final IOException e) {
                photo = null;
            }
        }
    }
}
