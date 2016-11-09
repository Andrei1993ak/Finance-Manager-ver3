package com.gmail.a93ak.andrei19.finance30.model.reportDbHelpers;


import com.gmail.a93ak.andrei19.finance30.model.dbHelpers.DBHelperCategoryIncome;
import com.gmail.a93ak.andrei19.finance30.model.dbHelpers.DBHelperIncome;
import com.gmail.a93ak.andrei19.finance30.model.models.Income;
import com.gmail.a93ak.andrei19.finance30.model.models.IncomeCategory;
import com.gmail.a93ak.andrei19.finance30.model.reportModels.IncomePieCategory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class IncomePieReportHelper {

    private static IncomePieReportHelper instance;

    public static IncomePieReportHelper getInstance() {
        if (instance == null)
            instance = new IncomePieReportHelper();
        return instance;
    }

    public ArrayList<IncomePieCategory> gRepInfo(final long purseId) {
        final ArrayList<IncomePieCategory> list = new ArrayList<>();
        final List<IncomeCategory> allParents = DBHelperCategoryIncome.getInstance().getAllToListByParentId(-1);
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
            } else {
                final Long parentId = DBHelperCategoryIncome.getInstance().get(income.getCategoryId()).getParent_id();
                sums.put(parentId, (sums.get(parentId) + income.getAmount()));
            }
        }
        sums.values().removeAll(Collections.singleton(0.0));
        sums = (HashMap) sortByValue(sums);
        for (final Map.Entry<Long, Double> entry : sums.entrySet()) {
            list.add(new IncomePieCategory(entry.getKey(), names.get(entry.getKey()), entry.getValue()));
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
