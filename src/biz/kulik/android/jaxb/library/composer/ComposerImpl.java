package biz.kulik.android.jaxb.library.composer;

import android.util.Log;
import biz.kulik.android.jaxb.library.Annotations.XmlAttribute;
import biz.kulik.android.jaxb.library.Annotations.XmlElement;
import biz.kulik.android.jaxb.library.composer.providers.ProviderFactory;
import biz.kulik.android.jaxb.library.composer.providers.abstractProvider.UMO;
import biz.kulik.android.jaxb.library.composer.providers.abstractProvider.UMOArray;
import biz.kulik.android.jaxb.library.composer.providers.abstractProvider.UMOObject;
import biz.kulik.android.jaxb.library.composer.providers.ProviderTypes;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * User: kulik
 * Date: 10/03/12
 * Time: 23:12 PM
 */
public class ComposerImpl implements Composer {
    private static final String TAG = ComposerImpl.class.getSimpleName();
    private ProviderFactory mFactory;

    public ComposerImpl(ProviderTypes ad) {
        mFactory = new ProviderFactory(ad);
    }

    @Override
    public UMO compose(Object data) {
        UMO rootElem = null;
        rootElem = processObject(data);
        return rootElem;
    }

    protected UMO processObject(Object obj) {
        UMO myElem = null;
        Class objType = obj.getClass();
        try {
            if (obj != null) {
                //TODO need changed to Collection.class
                if (objType.isAssignableFrom(List.class)) {
                    List ourList = (List) obj;
                    myElem = mFactory.createProvider(UMOArray.class);

                    for (int i = 0, d = ourList.size(); i < d; i++) {

                        //TODO check if simple type
                        ((UMOArray) myElem).put(processObject(ourList.get(i)));
                    }
                } else if (objType.isArray()) {
                    //TODO Need to implement
                } else {
                    myElem = mFactory.createProvider(UMOObject.class);
                    processObjectContent(obj, (UMOObject) myElem);
                }
            }
        } catch (InvocationTargetException e) {
            Log.e(TAG, e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.getMessage());
        } catch (InstantiationException e) {
            Log.e(TAG, e.getMessage());
        }
        return myElem;

    }

    private void processObjectContent(Object obj, UMOObject sobj) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        processFields(obj, sobj);
        //processMethods();
    }

    private boolean processAtributeValue(Object value, String annotationName, UMOObject sobj) {

        Class<?> valueType = value.getClass();
        if (String.class.equals(valueType)) {
            sobj.putAnnotationStr(annotationName, (String) value);
            return true;
        } else if (Integer.class.equals(valueType)) {
            sobj.putAnnotationInt(annotationName, (Integer) value);
            return true;
        } else if (Long.class.equals(valueType)) {
            sobj.putAnnotationLong(annotationName, (Long) value);
            return true;
        } else if (Float.class.equals(valueType)) {
            sobj.putAnnotationFloat(annotationName, (Float) value);
            return true;
        } else if (Double.class.equals(valueType)) {
            sobj.putAnnotationDouble(annotationName, (Double) value);
            return true;
        } else if (Boolean.class.equals(valueType)) {
            sobj.putAnnotationBoolean(annotationName, (Boolean) value);
            return true;
        }
        return false;
    }

    protected boolean processSimpleValue(Object value, String valueName, UMOObject sobj) {

        Class<?> valueType = value.getClass();
        if (String.class.equals(valueType)) {
            sobj.putValueStr(valueName, (String) value);
            return true;
        } else if (Integer.class.equals(valueType)) {
            sobj.putValueInt(valueName, (Integer) value);
            return true;
        } else if (Long.class.equals(valueType)) {
            sobj.putValueLong(valueName, (Long) value);
            return true;
        } else if (Float.class.equals(valueType)) {
            sobj.putValueFloat(valueName, (Float) value);
            return true;
        } else if (Double.class.equals(valueType)) {
            sobj.putValueDouble(valueName, (Double) value);
            return true;
        } else if (Boolean.class.equals(valueType)) {
            sobj.putValueBoolean(valueName, (Boolean) value);
            return true;
        }
        return false;
    }

    protected void processFields(Object obj, UMOObject sobj) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> cl = obj.getClass();
        Field[] allFields = cl.getDeclaredFields();
        Log.d(TAG, "ProcessFields quantity:" + allFields.length);
        for (Field field : allFields) {
            Log.d(TAG, "ProcessFields field:" + field.getName() + "; AnnotationPresent:" + field.getAnnotations());
            field.setAccessible(true);
            Object value = field.get(obj);
            if (value != null) {
                if (field.isAnnotationPresent(XmlAttribute.class)) {
                    String annotationName = field.getAnnotation(XmlAttribute.class).name();
                    processAtributeValue(value, annotationName, sobj);
                } else if (field.isAnnotationPresent(XmlElement.class)) {
                    String valueName = field.getAnnotation(XmlElement.class).name();
                    boolean simpleTypeParsed = processSimpleValue(value, valueName, sobj);
                    if (simpleTypeParsed == false) {
                        processComplexValue(value, valueName, sobj);
                    }
                }
            }
        }
    }

    //TODO check for null for each object
    protected void processComplexValue(Object obj, String valueName, UMOObject sobj) throws InvocationTargetException, IllegalAccessException, InstantiationException {

        Class objType = obj.getClass();

        if (List.class.isInstance(obj)) {
            UMOArray list = mFactory.createProvider(UMOArray.class);
            sobj.put(valueName, list);
            List ourList = (List) obj;

            for (Object itemObj : ourList) {
                list.put(processObject(itemObj));
            }
        } else if (objType.isArray()) {
            //TODO Need to implement
        } else {
            sobj.put(valueName, processObject(obj));
        }

    }

}
