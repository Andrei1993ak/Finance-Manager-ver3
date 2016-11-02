package com.gmail.a93ak.andrei19.finance30.view.addEditActivities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.a93ak.andrei19.finance30.R;

import com.gmail.a93ak.andrei19.finance30.control.Executors.CostCategoryExecutor;
import com.gmail.a93ak.andrei19.finance30.control.Executors.CostExecutor;
import com.gmail.a93ak.andrei19.finance30.control.Executors.PurseExecutor;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.pojos.Cost;
import com.gmail.a93ak.andrei19.finance30.model.pojos.CostCategory;
import com.gmail.a93ak.andrei19.finance30.model.pojos.Purse;
import com.gmail.a93ak.andrei19.finance30.util.UniversalLoader.Loaders.BitmapLoader;
import com.gmail.a93ak.andrei19.finance30.view.Activities.CostActivity;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CostEditActivity extends AppCompatActivity implements OnTaskCompleted {

    private Cost cost;
    private EditText editCostName;
    private EditText editCostAmount;
    private TextView editCostDate;
    private AppCompatSpinner editCostPurse;
    private AppCompatSpinner editCostCategory;
    private AppCompatSpinner editCostSubCategory;
    private List<Purse> pursesList;
    private List<CostCategory> categoriesList;
    private List<CostCategory> subCategoriesList;
    private ImageView imageView;
    private SimpleDateFormat dateFormatter;
    private long parentId;
    private long id;
    private Bitmap photo;
    public static final int CAMERA_REQUEST = 1;
    private boolean isPhotoChange = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cost_edit_activity);

        findViewsBuId();
        RequestHolder<Cost> requestHolder = new RequestHolder<>();
        id = getIntent().getLongExtra(DBHelper.COST_KEY_ID, -1);
        requestHolder.setGetRequest(id);
        new CostExecutor(this).execute(requestHolder.getGetRequest());
        setDatePicekDialog();
    }

    private void setDatePicekDialog() {
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        final Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                editCostDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        editCostDate.setText(dateFormatter.format(newCalendar.getTime()));
        editCostDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    @Override
    public void onTaskCompleted(Result result) {
        switch (result.getId()) {

            case CostExecutor.KEY_RESULT_GET:
                cost = (Cost) result.getT();
                editCostName.setText(cost.getName());
                editCostAmount.setText(String.valueOf(cost.getAmount()));
                editCostDate.setText(dateFormatter.format(cost.getDate()));
                if (cost.getPhoto() == 1) {
                    String url = CostActivity.FTP_PATH + String.valueOf(id) + ".jpg";
                    BitmapLoader bitmapLoader = BitmapLoader.getInstance(this);
                    bitmapLoader.load(url, imageView);
                }
                RequestHolder<Purse> purseRequestHolder = new RequestHolder<>();
                purseRequestHolder.setGetAllToListRequest(0);
                new PurseExecutor(this).execute(purseRequestHolder.getGetAllToListRequest());
                RequestHolder<CostCategory> categoryRequestHolder = new RequestHolder<>();
                categoryRequestHolder.setGetRequest(cost.getCategory_id());
                new CostCategoryExecutor(this).execute(categoryRequestHolder.getGetRequest());
                break;

            case PurseExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                pursesList = (List<Purse>) result.getT();
                String[] pursesNames = new String[pursesList.size()];
                int i = 0;
                int position = -1;
                for (Purse purse : pursesList) {
                    pursesNames[i++] = purse.getName();
                    if (purse.getId() == cost.getPurse_id()) {
                        position = i - 1;
                    }
                }
                ArrayAdapter<String> spinnerPursesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pursesNames);
                spinnerPursesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                editCostPurse.setAdapter(spinnerPursesAdapter);
                editCostPurse.setSelection(position);
                break;

            case CostCategoryExecutor.KEY_RESULT_GET:
                CostCategory category = (CostCategory) result.getT();
                parentId = category.getParent_id();
                RequestHolder<CostCategory> costCategoryRequestHolder = new RequestHolder<>();
                costCategoryRequestHolder.setGetAllToListRequest(1);
                new CostCategoryExecutor(this).execute(costCategoryRequestHolder.getGetAllToListRequest());
                break;

            case CostCategoryExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                categoriesList = (List<CostCategory>) result.getT();
                String[] categoriesNames = new String[categoriesList.size()];
                int j = 0;
                int categoryPosition = -1;
                for (CostCategory costCategory : categoriesList) {
                    categoriesNames[j++] = costCategory.getName();
                    if (parentId == -1) {
                        if (costCategory.getId() == cost.getCategory_id()) {
                            categoryPosition = j - 1;
                        }
                    } else {
                        if (costCategory.getId() == parentId) {
                            categoryPosition = j - 1;
                        }
                    }
                }
                ArrayAdapter<String> spinnerCategoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriesNames);
                spinnerCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                editCostCategory.setAdapter(spinnerCategoriesAdapter);
                editCostCategory.setSelection(categoryPosition);
                editCostCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        RequestHolder<CostCategory> categoryRequestHolder = new RequestHolder<>();
                        categoryRequestHolder.setGetAllToListByCategoryId(categoriesList.get(position).getId());
                        new CostCategoryExecutor(CostEditActivity.this).execute(categoryRequestHolder.getGetAllToListByCategoryId());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;


            case CostCategoryExecutor.KEY_RESULT_GET_ALL_TO_LIST_BY_PARENT_ID:
                subCategoriesList = (List<CostCategory>) result.getT();
                int length = subCategoriesList.size();
                if (length == 0) {
                    editCostSubCategory.setVisibility(View.GONE);
                } else {
                    editCostSubCategory.setVisibility(View.VISIBLE);
                    String[] subCategoriesNames = new String[subCategoriesList.size()];
                    int k = 0;
                    int subCatPosition = 0;
                    for (CostCategory costCategory : subCategoriesList) {
                        subCategoriesNames[k++] = costCategory.getName();
                        if (costCategory.getId() == cost.getCategory_id()) {
                            subCatPosition = k - 1;
                        }
                    }
                    ArrayAdapter<String> spinnerSubCategoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subCategoriesNames);
                    spinnerSubCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    editCostSubCategory.setAdapter(spinnerSubCategoriesAdapter);
                    editCostSubCategory.setSelection(subCatPosition);
                }
                break;

        }

    }

    private void findViewsBuId() {
        editCostName = (EditText) findViewById(R.id.edit_cost_name);
        editCostAmount = (EditText) findViewById(R.id.edit_cost_amount);
        editCostDate = (TextView) findViewById(R.id.edit_cost_date);
        editCostPurse = (AppCompatSpinner) findViewById(R.id.edit_cost_purse);
        editCostCategory = (AppCompatSpinner) findViewById(R.id.edit_cost_category);
        editCostSubCategory = (AppCompatSpinner) findViewById(R.id.edit_cost_subCategory);
        imageView = (ImageView) findViewById(R.id.edit_cost_photo);
    }

    public void editCost(View view) {
        if (checkFields()) {
            try {
                Intent intent = new Intent();
                intent.putExtra(DBHelper.COST_KEY_ID, id);
                intent.putExtra(DBHelper.COST_KEY_NAME, editCostName.getText().toString());
                intent.putExtra(DBHelper.COST_KEY_AMOUNT, Double.parseDouble(editCostAmount.getText().toString()));
                intent.putExtra(DBHelper.COST_KEY_DATE, dateFormatter.parse(editCostDate.getText().toString()).getTime());
                intent.putExtra(DBHelper.COST_KEY_PURSE_ID, pursesList.get(editCostPurse.getSelectedItemPosition()).getId());
                if (editCostSubCategory.getVisibility() == View.GONE) {
                    intent.putExtra(DBHelper.COST_KEY_CATEGORY_ID, categoriesList.get(editCostCategory.getSelectedItemPosition()).getId());
                } else {
                    intent.putExtra(DBHelper.COST_KEY_CATEGORY_ID, subCategoriesList.get(editCostSubCategory.getSelectedItemPosition()).getId());
                }
                if (photo == null) {
                    intent.putExtra(DBHelper.COST_KEY_PHOTO, 0);
                } else {
                    intent.putExtra(DBHelper.COST_KEY_PHOTO, 1);
                    intent.putExtra("isChanged",isPhotoChange);
                }
                setResult(RESULT_OK, intent);
                finish();
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    private boolean checkFields() {
        boolean flag = true;
        if (editCostName.getText().toString().length() == 0) {
            editCostName.setBackground(getResources().getDrawable(R.drawable.shape_red_field));
            flag = false;
        } else {
            editCostName.setBackground(getResources().getDrawable(R.drawable.shape_green_field));

        }
        if (editCostAmount.getText().length() == 0) {
            editCostAmount.setBackground(getResources().getDrawable(R.drawable.shape_red_field));
            flag = false;
        } else {
            editCostAmount.setBackground(getResources().getDrawable(R.drawable.shape_green_field));

        }
        return flag;
    }

    public void edit_photo(View view) {
        File file = new File(CostActivity.TEMP_PATH);
        Uri outputFileUri = Uri.fromFile(file);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            photo = (Bitmap) BitmapFactory.decodeFile(CostActivity.TEMP_PATH);
            ImageView view = (ImageView) findViewById(R.id.edit_cost_photo);
            view.setImageBitmap(photo);
            isPhotoChange = true;
        }
    }
}
