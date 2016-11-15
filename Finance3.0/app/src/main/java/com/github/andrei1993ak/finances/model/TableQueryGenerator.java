package com.github.andrei1993ak.finances.model;


import android.support.annotation.Nullable;

import com.github.andrei1993ak.finances.model.annotations.Table;
import com.github.andrei1993ak.finances.model.annotations.types.DBDouble;
import com.github.andrei1993ak.finances.model.annotations.types.DBInteger;
import com.github.andrei1993ak.finances.model.annotations.types.DBIntegerAutoIncrement;
import com.github.andrei1993ak.finances.model.annotations.types.DBIntegerPrimaryKey;
import com.github.andrei1993ak.finances.model.annotations.types.DBString;
import com.github.andrei1993ak.finances.model.models.TableClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Locale;

public class TableQueryGenerator {

    private static final String SQL_TABLE_CREATE_TEMPLATE = "CREATE TABLE IF NOT EXISTS %s (%s)";
    private static final String SQL_TABLE_DELETE_TEMPLATE = "DROP TABLE IF EXISTS %s";
    private static final String SQL_TABLE_CREATE_FIELD_TEMPLATE = "%s %s";

//    Package local
    @Nullable
    static String getTableCreateQuery(final Class<? extends TableClass> clazz) {

        final Table table = clazz.getAnnotation(Table.class);

        if (table != null) {
            try {
                final String name = table.name();

                final StringBuilder builder = new StringBuilder();
                final Field[] fields = clazz.getFields();
                boolean firstField = true;
                for (final Field field : fields) {
                    final Annotation[] annotations = field.getAnnotations();

                    if (annotations == null) {
                        continue;
                    }

                    String type = null;

                    for (final Annotation annotation : annotations) {
                        if (annotation instanceof DBInteger) {
                            type = ((DBInteger) annotation).value();
                        } else if (annotation instanceof DBDouble) {
                            type = ((DBDouble) annotation).value();
                        } else if (annotation instanceof DBString) {
                            type = ((DBString) annotation).value();
                        } else if (annotation instanceof DBIntegerPrimaryKey) {
                            type = ((DBIntegerPrimaryKey) annotation).value();
                        } else if (annotation instanceof DBIntegerAutoIncrement) {
                            type = ((DBIntegerAutoIncrement) annotation).value();
                        }
                    }

                    if (type == null) {
                        continue;
                    }

                    final String value = (String) field.get(null);

                    if (firstField) {
                        firstField = false;
                    } else {
                        builder.append(", ");
                    }

                    builder.append(String.format(Locale.US, SQL_TABLE_CREATE_FIELD_TEMPLATE, value, type));

                }

                return String.format(Locale.US, SQL_TABLE_CREATE_TEMPLATE, name, builder);
            } catch (final Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }
//    Package local
    @Nullable
    static String getTableDeleteQuery(final Class<? extends TableClass> clazz) {

        final Table table = clazz.getAnnotation(Table.class);

        if (table != null) {
            return String.format(Locale.US, SQL_TABLE_DELETE_TEMPLATE, table.name());
        } else {
            return null;
        }
    }

    @Nullable
    public static String getTableName(final Class<? extends TableClass> clazz) {
        final Table table = clazz.getAnnotation(Table.class);
        if (table != null) {
            return table.name();
        } else {
            return null;
        }
    }
}
