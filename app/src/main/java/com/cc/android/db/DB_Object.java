package com.cc.android.db;

import android.content.Context;
import com.google.gson.Gson;

/**
 * Created by ilisin on 16/6/22.
 */
public class DB_Object<T> extends DB_Base {

    private Class<T> objCls;

    public DB_Object(Context context, Class<T> objCls){
        super(context);
        this.objCls = objCls;
        String clsName = objCls.getName();
        setName(clsName);
        getSettings();
    }

    public void setObject(T data){
        Gson gson = new Gson();
        String jsonString = gson.toJson(data);
        setSaveString("jsonstring", jsonString);
    }

    public T getObject() {
        Gson gson = new Gson();
        String jsonString = getSaveString("jsonstring", "");
        Object t = gson.fromJson(jsonString, this.objCls);
        return (T) t;
    }

    public void reset(){
        if (settings == null) {
            settings = getSettings();
        }
        settings.edit().clear().commit();

    }
}
