package com.github.andrei1993ak.finances.model.reportDbHelpers;


import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCategoryCost;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCost;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperIncome;
import com.github.andrei1993ak.finances.model.models.Cost;
import com.github.andrei1993ak.finances.model.models.CostCategory;
import com.github.andrei1993ak.finances.model.models.Income;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCategoryIncome;
import com.github.andrei1993ak.finances.model.models.IncomeCategory;
import com.github.andrei1993ak.finances.model.reportModels.PieChartItem;
import com.github.andrei1993ak.finances.util.ContextHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class PieChartItemHelper {

    private final DBHelperCost dbHelperCost;
    private final DBHelperIncome dbHelperIncome;
    private final DBHelperCategoryIncome dbHelperCategoryIncome;
    private final DBHelperCategoryCost dbHelperCategoryCost;

    public PieChartItemHelper(){
        dbHelperIncome = ((DBHelperIncome) ((App) ContextHolder.getInstance().getContext()).getDbHelper(Income.class));
        dbHelperCost = ((DBHelperCost) ((App) ContextHolder.getInstance().getContext()).getDbHelper(Cost.class));
        dbHelperCategoryCost = ((DBHelperCategoryCost) ((App) ContextHolder.getInstance().getContext()).getDbHelper(CostCategory.class));
        dbHelperCategoryIncome = ((DBHelperCategoryIncome) ((App) ContextHolder.getInstance().getContext()).getDbHelper(IncomeCategory.class));
    }

    public ArrayList<PieChartItem> gRepInfoIncome(final long walletId, final long categoryId) {
        final ArrayList<PieChartItem> list = new ArrayList<>();
        final List<IncomeCategory> allParents = dbHelperCategoryIncome.getAllToListByParentId(categoryId);
        if (categoryId != -1) {
            allParents.add(dbHelperCategoryIncome.get(categoryId));
        }
        HashMap<Long, Double> sums = new HashMap<>();
        final HashMap<Long, String> names = new HashMap<>();
        for (final IncomeCategory parent : allParents) {
            sums.put(parent.getId(), 0.0);
            names.put(parent.getId(), parent.getName());
        }
        final List<Income> allIncomes = dbHelperIncome.getAllToListByWalletId(walletId);
        for (final Income income : allIncomes) {
            if (sums.containsKey(income.getCategoryId())) {
                sums.put(income.getCategoryId(), (sums.get(income.getCategoryId()) + income.getAmount()));
            } else if (categoryId == -1) {
                final Long parentId = dbHelperCategoryIncome.get(income.getCategoryId()).getParentId();
                sums.put(parentId, (sums.get(parentId) + income.getAmount()));
            }
        }
        sums.values().removeAll(Collections.singleton(0.0));
        sums = sortByValue(sums);
        for (final Map.Entry<Long, Double> entry : sums.entrySet()) {
            list.add(new PieChartItem(entry.getKey(), names.get(entry.getKey()), entry.getValue()));
        }
        return list;
    }

    public ArrayList<PieChartItem> gRepInfoCost(final long walletId, final long categoryId) {
        final ArrayList<PieChartItem> list = new ArrayList<>();
        final List<CostCategory> allParents = dbHelperCategoryCost.getAllToListByParentId(categoryId);
        if (categoryId != -1) {
            allParents.add(dbHelperCategoryCost.get(categoryId));
        }
        HashMap<Long, Double> sums = new HashMap<>();
        final HashMap<Long, String> names = new HashMap<>();
        for (final CostCategory parent : allParents) {
            sums.put(parent.getId(), 0.0);
            names.put(parent.getId(), parent.getName());
        }
        final List<Cost> allCosts =   dbHelperCost.getAllToListByWalletId(walletId);
        for (final Cost cost : allCosts) {
            if (sums.containsKey(cost.getCategoryId())) {
                sums.put(cost.getCategoryId(), (sums.get(cost.getCategoryId()) + cost.getAmount()));
            } else if (categoryId == -1) {
                final Long parentId = dbHelperCategoryCost.get(cost.getCategoryId()).getParentId();
                sums.put(parentId, (sums.get(parentId) + cost.getAmount()));
            }
        }
        sums.values().removeAll(Collections.singleton(0.0));
        sums = sortByValue(sums);
        for (final Map.Entry<Long, Double> entry : sums.entrySet()) {
            list.add(new PieChartItem(entry.getKey(), names.get(entry.getKey()), entry.getValue()));
        }
        return list;
    }

    private static <K, V extends Comparable<? super V>> HashMap<K, V> sortByValue(final Map<K, V> map) {
        final List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(final Map.Entry<K, V> o1, final Map.Entry<K, V> o2) {
                return -(o1.getValue().compareTo(o2.getValue()));
            }
        });
        final HashMap<K, V> result = new LinkedHashMap<>();
        for (final Map.Entry<K, V> kvEntry : list) {
            result.put(kvEntry.getKey(), kvEntry.getValue());
        }
        return result;
    }
}
