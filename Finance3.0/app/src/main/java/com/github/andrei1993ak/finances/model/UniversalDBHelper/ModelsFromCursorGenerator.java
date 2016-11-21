package com.github.andrei1993ak.finances.model.UniversalDBHelper;


import android.database.Cursor;

import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.model.annotations.types.DBDouble;
import com.github.andrei1993ak.finances.model.annotations.types.DBInteger;
import com.github.andrei1993ak.finances.model.annotations.types.DBIntegerAutoIncrement;
import com.github.andrei1993ak.finances.model.annotations.types.DBIntegerPrimaryKey;
import com.github.andrei1993ak.finances.model.annotations.types.DBString;
import com.github.andrei1993ak.finances.model.models.TableClass;
import com.github.andrei1993ak.finances.util.ContextHolder;
import com.github.andrei1993ak.finances.util.CursorUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ModelsFromCursorGenerator<Model extends TableClass> {

    private final CursorUtils cursorUtils;

    public ModelsFromCursorGenerator() {
        cursorUtils = ((App) ContextHolder.getInstance().getContext()).getCursorUtils();
    }

    public Model generateModelFromCursor(final Cursor cursor, final Class<Model> clazz) {
        try {
            final Model model = clazz.newInstance();
            final Field[] fields = clazz.getFields();
            for (final Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())) {
                    final Annotation[] annotations = field.getAnnotations();
                    if (annotations == null) {
                        continue;
                    }
                    Method setter;
                    String value;
                    try {
                        value = (String) field.get(null);
                        final String fieldName = value.equals("_id") ? "id" : value;
                        final String setterName = value.equals("_id") ? "setId" : "set" + value.substring(0, 1).toUpperCase() + value.substring(1);
                        setter = clazz.getMethod(setterName, clazz.getDeclaredField(fieldName).getType());
                    } catch (final Exception e) {
                        setter = null;
                        value = null;
                    }
                    if (setter != null) {
                        for (final Annotation annotation : annotations) {
                            if (annotation instanceof DBInteger) {
                                setter.invoke(model, cursor.getLong(cursor.getColumnIndex(value)));
                                break;
                            } else if (annotation instanceof DBDouble) {
                                setter.invoke(model, cursor.getDouble(cursor.getColumnIndex(value)));
                                break;
                            } else if (annotation instanceof DBString) {
                                setter.invoke(model, cursor.getString(cursor.getColumnIndex(value)));
                                break;
                            } else if (annotation instanceof DBIntegerPrimaryKey) {
                                setter.invoke(model, cursor.getLong(cursor.getColumnIndex(value)));
                                break;
                            } else if (annotation instanceof DBIntegerAutoIncrement) {
                                setter.invoke(model, cursor.getLong(cursor.getColumnIndex(value)));
                            }
                        }
                    }
                }
            }
            return model;
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Model> generateListOfModelsFromCursor(final Cursor cursor, final Class<Model> clazz) {
        try {
            final Field[] fields = clazz.getFields();
            final List<UniversalSetter> list = new ArrayList<>();
            for (final Field field : fields)
                if (Modifier.isStatic(field.getModifiers())) {
                    final Annotation[] annotations = field.getAnnotations();
                    if (annotations == null) {
                        continue;
                    }
                    Method setter;
                    String value;
                    try {
                        value = (String) field.get(null);
                        final String fieldName = value.equals("_id") ? "id" : value;
                        final String setterName = value.equals("_id") ? "setId" : "set" + value.substring(0, 1).toUpperCase() + value.substring(1);
                        setter = clazz.getMethod(setterName, clazz.getDeclaredField(fieldName).getType());
                    } catch (final Exception e) {
                        setter = null;
                        value = null;
                    }
                    if (setter != null) {
                        for (final Annotation annotation : annotations) {
                            if (annotation instanceof DBInteger) {
                                list.add(new UniversalSetter(setter, value, Long.class));
                                break;
                            } else if (annotation instanceof DBDouble) {
                                list.add(new UniversalSetter(setter, value, Double.class));
                                break;
                            } else if (annotation instanceof DBString) {
                                list.add(new UniversalSetter(setter, value, String.class));
                                break;
                            } else if (annotation instanceof DBIntegerPrimaryKey) {
                                list.add(new UniversalSetter(setter, value, Long.class));
                                break;
                            } else if (annotation instanceof DBIntegerAutoIncrement) {
                                list.add(new UniversalSetter(setter, value, Long.class));
                            }
                        }
                    }
                }
            if (cursor.moveToFirst()) {
                final List<Model> models = new ArrayList<>();
                do {
                    final Model model = clazz.newInstance();
                    for (final UniversalSetter setter : list) {
                        if (setter.getType().isAssignableFrom(Double.class)) {
                            setter.getSetter().invoke(model, cursorUtils.getDouble(cursor, setter.getTableColumnName()));
                        } else if (setter.getType().isAssignableFrom(Long.class)) {
                            setter.getSetter().invoke(model, cursorUtils.getLong(cursor, setter.getTableColumnName()));
                        } else {
                            setter.getSetter().invoke(model, cursorUtils.getString(cursor,setter.getTableColumnName()));
                        }
                    }
                    models.add(model);
                } while (cursor.moveToNext());
                return models;
            } else {
                return null;
            }
        } catch (final Exception e) {
            return null;
        }
    }
}
