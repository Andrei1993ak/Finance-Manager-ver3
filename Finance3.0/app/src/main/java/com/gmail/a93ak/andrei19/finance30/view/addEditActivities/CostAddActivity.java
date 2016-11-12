package com.gmail.a93ak.andrei19.finance30.view.addEditActivities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.a93ak.andrei19.finance30.App;
import com.gmail.a93ak.andrei19.finance30.R;
import com.gmail.a93ak.andrei19.finance30.control.executors.CostCategoryExecutor;
import com.gmail.a93ak.andrei19.finance30.control.executors.PurseExecutor;
import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.RequestHolder;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.TableQueryGenerator;
import com.gmail.a93ak.andrei19.finance30.model.models.Cost;
import com.gmail.a93ak.andrei19.finance30.model.models.CostCategory;
import com.gmail.a93ak.andrei19.finance30.model.models.Purse;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
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
    private ArrayAdapter<String> spinnerSubCategoriesAdapter;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        if (getSharedPreferences(App.PREFS, Context.MODE_PRIVATE).getBoolean(App.THEME, false)) {
            setTheme(R.style.Dark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cost_add_activity);
        findViewsBuId();
        new PurseExecutor(this).execute(new RequestHolder<Purse>().getAllToList(RequestHolder.SELECTION_ALL));
        new CostCategoryExecutor(this).execute(new RequestHolder<CostCategory>().getAllToList(RequestHolder.SELECTION_PARENT_CATEGORIES));
        setDatePickerDialog();
    }

    private void setDatePickerDialog() {
        dateFormatter = new SimpleDateFormat(getResources().getString(R.string.dateFormat), Locale.US);
        final Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(final DatePicker view, final int year, final int month, final int dayOfMonth) {
                final Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                newCostDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        newCostDate.setText(dateFormatter.format(newCalendar.getTime()));
        newCostDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialog.show();
            }
        });
    }

    private void findViewsBuId() {
        newCostName = (EditText) findViewById(R.id.new_cost_name);
        newCostAmount = (EditText) findViewById(R.id.new_cost_amount);
        newCostDate = (TextView) findViewById(R.id.new_cost_date);
        newCostPurse = (AppCompatSpinner) findViewById(R.id.new_cost_purse);
        newCostCategory = (AppCompatSpinner) findViewById(R.id.new_cost_category);
        newCostSubCategory = (AppCompatSpinner) findViewById(R.id.new_cost_subCategory);
    }

    public void addNewCost(final View view) {
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
        cost.setPurseId(pursesList.get(newCostPurse.getSelectedItemPosition()).getId());
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

    public void addPhoto(final View view) {
        final File file = new File(App.getTempImagePath());
        final Uri outputFileUri = Uri.fromFile(file);
        final Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    public void onTaskCompleted(final Result result) {
        switch (result.getId()) {
            case PurseExecutor.KEY_RESULT_GET_ALL_TO_LIST:
                pursesList = (List<Purse>) result.getObject();
                final String[] pursesNames = new String[pursesList.size()];
                int i = 0;
                for (final Purse purse : pursesList) {
                    pursesNames[i++] = purse.getName();
                }
                final ArrayAdapter<String> spinnerPursesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pursesNames);
                spinnerPursesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                newCostPurse.setAdapter(spinnerPursesAdapter);
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
                        final RequestHolder<CostCategory> categoryRequestHolder = new RequestHolder<>();
                        new CostCategoryExecutor(CostAddActivity.this).execute(
                                categoryRequestHolder.getAllToListByCategory(categoriesList.get(position).getId()));
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
            photo = BitmapFactory.decodeFile(App.getTempImagePath());
            final ImageView view = (ImageView) findViewById(R.id.new_cost_photo);
            view.setImageBitmap(photo);
            final String path = App.getImagePath(DBHelper.getInstance(this).getNextId());
            final File toFile = new File(path);
            try {
                Files.move(new File(App.getTempImagePath()), toFile);
            } catch (final IOException e) {
                photo = null;
            }
        }
    }
}
