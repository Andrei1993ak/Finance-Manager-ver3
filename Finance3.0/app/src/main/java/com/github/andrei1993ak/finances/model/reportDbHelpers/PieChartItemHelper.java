package com.github.andrei1993ak.finances.model.reportDbHelpers;


import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCategoryCost;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCost;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperIncome;
import com.github.andrei1993ak.finances.model.models.Cost;
import com.github.andrei1993ak.finances.model.models.CostCategory;
import com.github.andrei1993ak.finances.model.models.Income;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCategoryIncome;
import com.github.andrei1993ak.finances.model.models.IncomeCategory;
import com.github.andrei1993ak.finances.model.reportModels.PieChartItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PieChartItemHelper {

    private static PieChartItemHelper instance;

    public static PieChartItemHelper getInstance() {
        if (instance == null)
            instance = new PieChartItemHelper();
        return instance;
    }

    public ArrayList<PieChartItem> gRepInfoIncome(final long purseId, final long categoryId) {
        final ArrayList<PieChartItem> list = new ArrayList<>();
        final List<IncomeCategory> allParents = DBHelperCategoryIncome.getInstance().getAllToListByParentId(categoryId);
        if (categoryId != -1) {
            allParents.add(DBHelperCategoryIncome.getInstance().get(categoryId));
        }
        HashMap<Long, Double> sums = new HashMap<>();
        final HashMap<Long, String> names = new HashMap<>();
        for (final IncomeCategory parent : allParents) {
            sums.put(parent.getId(), 0.0);
            names.put(parent.getId(), parent.getName());
        }
        final List<Income> allIncomes = DBHelperIncome.getInstance().getAllToListByPurseId(purseId);
        for (final Income income : allIncomes) {
            if (sums.containsKey(income.getCategoryId())) {
                sums.put(income.getCategoryId(), (sums.get(income.getCategoryId()) + income.getAmount()));
            } else if (categoryId == -1) {
                final Long parentId = DBHelperCategoryIncome.getInstance().get(income.getCategoryId()).getParent_id();
                sums.put(parentId, (sums.get(parentId) + income.getAmount()));
            }
        }
        sums.values().removeAll(Collections.singleton(0.0));
        sums = (HashMap) sortByValue(sums);
        for (final Map.Entry<Long, Double> entry : sums.entrySet()) {
            list.add(new PieChartItem(entry.getKey(), names.get(entry.getKey()), entry.getValue()));
        }
        return list;
    }

    public ArrayList<PieChartItem> gRepInfoCost(final long purseId, final long categoryId) {
        final ArrayList<PieChartItem> list = new ArrayList<>();
        final List<CostCategory> allParents = DBHelperCategoryCost.getInstance().getAllToListByParentId(categoryId);
        if (categoryId != -1) {
            allParents.add(DBHelperCategoryCost.getInstance().get(categoryId));
        }
        HashMap<Long, Double> sums = new HashMap<>();
        final HashMap<Long, String> names = new HashMap<>();
        for (final CostCategory parent : allParents) {
            sums.put(parent.getId(), 0.0);
            names.put(parent.getId(), parent.getName());
        }
        final List<Cost> allCosts = DBHelperCost.getInstance().getAllToListByPurseId(purseId);
        for (final Cost cost : allCosts) {
            if (sums.containsKey(cost.getCategoryId())) {
                sums.put(cost.getCategoryId(), (sums.get(cost.getCategoryId()) + cost.getAmount()));
            } else if (categoryId == -1) {
                final Long parentId = DBHelperCategoryCost.getInstance().get(cost.getCategoryId()).getParent_id();
                sums.put(parentId, (sums.get(parentId) + cost.getAmount()));
            }
        }
        sums.values().removeAll(Collections.singleton(0.0));
        sums = (HashMap) sortByValue(sums);
        for (final Map.Entry<Long, Double> entry : sums.entrySet()) {
            list.add(new PieChartItem(entry.getKey(), names.get(entry.getKey()), entry.getValue()));
        }
        return list;
    }

    private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(final Map<K, V> map) {
        final List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(final Map.Entry<K, V> o1, final Map.Entry<K, V> o2) {
                return -(o1.getValue().compareTo(o2.getValue()));
            }
        });
        final Map<K, V> result = new LinkedHashMap<>();
        for (final Map.Entry<K, V> kvEntry : list) {
            result.put(kvEntry.getKey(), kvEntry.getValue());
        }
        return result;
    }
}
