package com.gmail.a93ak.andrei19.finance30.view.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gmail.a93ak.andrei19.finance30.R;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_activity);
//        DBHelperCategoryIncome helperCategoryIncome = DBHelperCategoryIncome.getInstance(DBHelper.getInstance(this));
//        helperCategoryIncome.deleteAll();
//       long id = helperCategoryIncome.add(new IncomeCategory("зарплата13",-1));
//       long id2 =  helperCategoryIncome.add(new IncomeCategory("Аренда14",-1));
//       long id3 = helperCategoryIncome.add(new IncomeCategory("Дивиденды15",-1));
//
//        helperCategoryIncome.add(new IncomeCategory("ЗП2",1));
//        helperCategoryIncome.add(new IncomeCategory("Ар1",1));
//        helperCategoryIncome.add(new IncomeCategory("Ар2",1));
//        helperCategoryIncome.add(new IncomeCategory("Див1",2));
//        helperCategoryIncome.add(new IncomeCategory("Диe",2));
//        helperCategoryIncome.add(new IncomeCategory("Диer1",2));
//        helperCategoryIncome.add(new IncomeCategory("Дивer2",2));
//        helperCategoryIncome.add(new IncomeCategory("Див2",3));
//        helperCategoryIncome.add(new IncomeCategory("Див3",3));
//        helperCategoryIncome.add(new IncomeCategory("Див4",3));
//        helperCategoryIncome.add(new IncomeCategory("Див5",3));
    }


    public void selectCategory(View view) {
        switch (view.getId()) {
            case R.id.tvIncome:
                startActivity(new Intent(this, IncomeCategoryActivity.class));
                break;
            case R.id.tvCosts:
                startActivity(new Intent(this, IncomeCategoryActivity.class));
                break;
        }
    }
}
