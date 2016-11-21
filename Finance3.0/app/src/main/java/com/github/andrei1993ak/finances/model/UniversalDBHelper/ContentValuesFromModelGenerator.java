package com.github.andrei1993ak.finances.model.UniversalDBHelper;

import android.content.ContentValues;

import com.github.andrei1993ak.finances.model.annotations.types.DBDouble;
import com.github.andrei1993ak.finances.model.annotations.types.DBInteger;
import com.github.andrei1993ak.finances.model.annotations.types.DBString;
import com.github.andrei1993ak.finances.model.models.TableClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ContentValuesFromModelGenerator<Model extends TableClass> {

    public ContentValues generateCVFromModel(final Model model) {
        final Class clazz = model.getClass();
        final Field[] fields = clazz.getFields();
        final ContentValues cv = new ContentValues();
        for (final Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                final Annotation[] annotations = field.getAnnotations();
                if (annotations == null) {
                    continue;
                }
                Method getter;
                String value;
                try {
                    value = (String) field.get(null);
                    if (value.equals("_id")) {
                        continue;
                    }
                    final String getterName = "get" + value.substring(0, 1).toUpperCase() + value.substring(1);
                    getter = clazz.getMethod(getterName);
                } catch (final Exception e) {
                    getter = null;
                    value = null;
                }
                if (getter != null) {
                    try {
                        for (final Annotation annotation : annotations) {
                            if (annotation instanceof DBInteger) {
                                cv.put(value, (Long) getter.invoke(model));
                                break;
                            } else if (annotation instanceof DBDouble) {
                                cv.put(value, (Double) getter.invoke(model));
                                break;
                            } else if (annotation instanceof DBString) {
                                cv.put(value, (String) getter.invoke(model));
                            }
                        }
                    } catch (final IllegalAccessException e) {
                        return null;
                    } catch (final InvocationTargetException e) {
                        return null;
                    }
                }
            }
        }
        return cv;
    }
}
