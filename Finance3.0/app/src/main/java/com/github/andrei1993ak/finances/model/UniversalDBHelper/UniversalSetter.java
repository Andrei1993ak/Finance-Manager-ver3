package com.github.andrei1993ak.finances.model.UniversalDBHelper;

import java.lang.reflect.Method;

public class UniversalSetter {
    private final Method setter;
    private final String tableColumnName;
    private final Class type;

    public Method getSetter() {
        return setter;
    }

    public String getTableColumnName() {
        return tableColumnName;
    }

    public Class getType() {
        return type;
    }

    public UniversalSetter(final Method setter, final String tableColumnName, final Class type) {
        this.setter = setter;
        this.tableColumnName = tableColumnName;
        this.type = type;
    }
}
