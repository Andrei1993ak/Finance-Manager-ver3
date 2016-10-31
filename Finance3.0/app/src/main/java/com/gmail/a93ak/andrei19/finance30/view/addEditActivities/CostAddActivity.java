package com.gmail.a93ak.andrei19.finance30.view.addEditActivities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.gmail.a93ak.andrei19.finance30.control.Executors.PurseExecutor;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.pojos.CostCategory;
import com.gmail.a93ak.andrei19.finance30.model.pojos.Purse;
import com.gmail.a93ak.andrei19.finance30.util.ImageUploader;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CostAddActivity extends AppCompatActivity implements OnTaskCompleted {

    private static final int CAMERA_REQUEST = 1;
    private EditText newCostName;
    private EditText newCostAmount;
    private TextView newCostDate;
    private AppCompatSpinner newCostPurse;
    private AppCompatSpinner newCostCategory;
    private AppCompatSpinner newCostSubCategory;
    private List<Purse> pursesList;
    private List<CostCategory> categoriesList;
    private List<CostCategory> subCategoriesList;
    private SimpleDateFormat dateFormatter;
    private Bitmap photo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cost_add_activity);
        findViewsBuId();
        RequestHolder<Purse> purseRequestHolder = new RequestHolder<>();
        purseRequestHolder.setGetAllToListRequest(0);
        new PurseExecutor(this).execute(purseRequestHolder.getGetAllToListRequest());
        RequestHolder<CostCategory> categoryRequestHolder = new RequestHolder<>();
        categoryRequestHolder.setGetAllToListRequest(1);
        new CostCategoryExecutor(this).execute(categoryRequestHolder.getGetAllToListRequest());
        setDatePickerDialog();
    }

    private void setDatePickerDialog() {
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        final Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                newCostDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        newCostDate.setText(dateFormatter.format(newCalendar.getTime()));
        newCostDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    @Override
    public void onTaskCompleted(Result result) {
        switch (result.getId()) {
            case PurseExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                pursesList = (List<Purse>) result.getT();
                String[] pursesNames = new String[pursesList.size()];
                int i = 0;
                for (Purse purse : pursesList) {
                    pursesNames[i++] = purse.getName();
                }
                ArrayAdapter<String> spinnerPursesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pursesNames);
                spinnerPursesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                newCostPurse.setAdapter(spinnerPursesAdapter);
                break;
            case CostCategoryExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                categoriesList = (List<CostCategory>) result.getT();
                String[] categoriesNames = new String[categoriesList.size()];
                int j = 0;
                for (CostCategory costCategory : categoriesList) {
                    categoriesNames[j++] = costCategory.getName();
                }
                ArrayAdapter<String> spinnerCategoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriesNames);
                spinnerCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                newCostCategory.setAdapter(spinnerCategoriesAdapter);
                newCostCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        RequestHolder<CostCategory> categoryRequestHolder = new RequestHolder<>();
                        categoryRequestHolder.setGetAllToListByCategoryId(categoriesList.get(position).getId());
                        new CostCategoryExecutor(CostAddActivity.this).execute(categoryRequestHolder.getGetAllToListByCategoryId());
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
                    newCostSubCategory.setVisibility(View.GONE);
                } else {
                    newCostSubCategory.setVisibility(View.VISIBLE);
                    String[] subCategoriesNames = new String[subCategoriesList.size()];
                    int k = 0;
                    for (CostCategory costCategory : subCategoriesList) {
                        subCategoriesNames[k++] = costCategory.getName();
                    }
                    ArrayAdapter<String> spinnerSubCategoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subCategoriesNames);
                    spinnerSubCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    newCostSubCategory.setAdapter(spinnerSubCategoriesAdapter);
                }
                break;
        }

    }

    private void findViewsBuId() {
        newCostName = (EditText) findViewById(R.id.new_cost_name);
        newCostAmount = (EditText) findViewById(R.id.new_cost_amount);
        newCostDate = (TextView) findViewById(R.id.new_cost_date);
        newCostPurse = (AppCompatSpinner) findViewById(R.id.new_cost_purse);
        newCostCategory = (AppCompatSpinner) findViewById(R.id.new_cost_category);
        newCostSubCategory = (AppCompatSpinner) findViewById(R.id.new_cost_subCategory);
    }

    public void addNewCost(View view) {
        if (checkFields()) {
            try {
                Intent intent = new Intent();
                intent.putExtra(DBHelper.COST_KEY_NAME, newCostName.getText().toString());
                intent.putExtra(DBHelper.COST_KEY_AMOUNT, Double.parseDouble(newCostAmount.getText().toString()));
                intent.putExtra(DBHelper.COST_KEY_DATE, dateFormatter.parse(newCostDate.getText().toString()).getTime());
                intent.putExtra(DBHelper.COST_KEY_PURSE_ID, pursesList.get(newCostPurse.getSelectedItemPosition()).getId());
                if (newCostSubCategory.getVisibility() == View.GONE) {
                    intent.putExtra(DBHelper.COST_KEY_CATEGORY_ID, categoriesList.get(newCostCategory.getSelectedItemPosition()).getId());
                } else {
                    intent.putExtra(DBHelper.COST_KEY_CATEGORY_ID, subCategoriesList.get(newCostSubCategory.getSelectedItemPosition()).getId());
                }
                if (photo == null) {
                    intent.putExtra(DBHelper.COST_KEY_PHOTO, 0);
                }
                else {
                    intent.putExtra(DBHelper.COST_KEY_PHOTO,1);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG,100,stream);
                    byte[] byteArray = stream.toByteArray();
                    intent.putExtra("photoArray",byteArray);
                }
                setResult(RESULT_OK, intent);
                finish();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkFields() {
        boolean flag = true;
        if (newCostName.getText().toString().length() == 0) {
            newCostName.setBackground(getResources().getDrawable(R.drawable.shape_red_field));
            flag = false;
        } else {
            newCostName.setBackground(getResources().getDrawable(R.drawable.shape_green_field));

        }
        if (newCostAmount.getText().length() == 0) {
            newCostAmount.setBackground(getResources().getDrawable(R.drawable.shape_red_field));
            flag = false;
        } else {
            newCostAmount.setBackground(getResources().getDrawable(R.drawable.shape_green_field));

        }
        return flag;
    }

    public void add_photo(View view) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            ImageView view = (ImageView) findViewById(R.id.new_cost_photo);
            view.setImageBitmap(photo);
        }
    }
}
