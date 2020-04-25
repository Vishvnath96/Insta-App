package com.example.vps.ui.util.common;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Collections;

public class GsonUtils {
    private static final String UTF_ENCODING = "UTF-8";
    private static volatile GsonUtils sInstance;
    private static final String TAG = "GsonUtils";

    private GsonBuilder mGsonBuilder;
    private Gson mGson;

    private GsonUtils() {
        mGsonBuilder = new GsonBuilder();
        mGson = mGsonBuilder.create();
    }

    public static GsonUtils getInstance() {
        if (sInstance == null) {
            synchronized (GsonUtils.class) {
                if (sInstance == null) {
                    sInstance = new GsonUtils();
                }
            }
        }
        return sInstance;
    }

    /**
     * Deserializes the JSON object and returns an object of the the type specified in <i>classPath</i>
     * <p>
     * This method expects the classPath to refer to a non generic type.
     *
     * @param  json      the String representation of incoming JSON object
     * @param  classPath the non generic type of the return object
     * @return           the deserialized object of type classPath if the class exists and json represents an object of the type classPath, null otherwise
     */
    public <T> T deserializeJSON(String json, String classPath)throws ClassNotFoundException {
        T queryResult = null;

        if (json != null && classPath != null) {
            try {
                queryResult = (T) mGson.fromJson(json, Class.forName(classPath));
            } catch (JsonSyntaxException e) {
                Log.e(TAG,"JsonSyntaxException: " + e.toString(),e);
            } catch (JsonIOException e) {
                Log.e(TAG,"JsonIOException " + e.toString(),e);
            }catch(Exception e){
                Log.e(TAG, e.toString(),e);
            }catch (IncompatibleClassChangeError e){
                Log.e(TAG, "IncompatibleClassChangeError ::"+e.toString(),e);
            }
        }
        return queryResult;
    }

    /**
     * Deserializes the JSON object and returns an object of the the type specified in <i>classPath</i>
     * <p>
     * This method expects the classPath to refer to a non generic type.
     *
     * @param  json     the String representation of incoming JSON object
     * @param  classPath the non generic type of the return object
     * @return           the deserialized object of type classPath if the class exists and json represents an object of the type classPath     * @return           the deserialized object of type classPath if the class exists and json represents an object of the type classPath, null otherwise
     */
    public <T> T deserializeJSON(String json, Class classPath) {
        T queryResult = null;

        if (json != null && classPath != null) {
            try {
                queryResult = (T) mGson.fromJson(json, classPath);
            } catch (JsonSyntaxException e) {
                Log.e(TAG,"JsonSyntaxException: " + e.toString(),e);
            } catch (JsonIOException e) {
                Log.e(TAG,"JsonIOException " + e.toString(),e);
            }catch(Exception e){
                Log.e(TAG, e.toString(),e);
            }catch (IncompatibleClassChangeError e){
                Log.e(TAG, "IncompatibleClassChangeError:: "+e.toString(),e);
            }
        }
        return queryResult;
    }

    /**
     * Deserializes the JSON object and returns an object of the the type specified in <i>classPath</i>
     * <p>
     * This method expects the classPath to refer to a non generic type.
     *
     * @param  inputStream the InputStream to read the incoming JSON object
     * @param  classPath   the non generic type of the return object
     * @return             the deserialized object of type classPath if the class exists and json represents an object of the type classPath     * @return           the deserialized object of type classPath if the class exists and json represents an object of the type classPath, null otherwise
     */
    public <T> T deserializeJSON(InputStream inputStream, Class classPath) {
        T queryResult = null;

        if (inputStream != null && classPath != null) {
            Reader reader = new InputStreamReader(inputStream, Charset.forName(UTF_ENCODING));
            try {
                queryResult = (T) mGson.fromJson(reader, classPath);
            } catch (JsonSyntaxException e) {
                Log.e(TAG,"JsonSyntaxException: " + e.toString(),e);
            } catch (JsonIOException e) {
                Log.e(TAG,"JsonIOException " + e.toString(),e);
            } catch(Exception e){
                Log.e(TAG, e.toString(),e);
            }catch (IncompatibleClassChangeError e){
                Log.e(TAG, "IncompatibleClassChangeError :: "+e.toString(),e);
            }
            finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG,"IOException " + e.toString(),e);
                }
            }
        }
        return queryResult;
    }

    public <T> T deserializeJsonThrowsException(InputStream inputStream, Class classPath) throws GsonUtilException {
        if(inputStream == null){
            throw new IllegalArgumentException("inputstream is null");
        }
        if(classPath == null){
            throw new IllegalArgumentException("classpath is null");
        }
        T queryResult = null;

        Reader reader = new InputStreamReader(inputStream, Charset.forName(UTF_ENCODING));
        try {
            queryResult = (T) mGson.fromJson(reader, classPath);
        } catch (JsonSyntaxException e) {
            throw new GsonUtilException("json syntax exception", e);
        } catch (JsonIOException e) {
            throw new GsonUtilException("JsonIOException", e);
        } catch (Exception e) {
            throw new GsonUtilException("generic exception", e);
        } catch (IncompatibleClassChangeError e) {
            Log.e(TAG, "IncompatibleClassChangeError :: " + e.toString(), e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                Log.e(TAG, "IOException " + e.toString(), e);
            }
        }
        return queryResult;
    }

    /**
     * Deserializes the JSON object passed as String and returns an object of the the type specified
     * <p>
     * This method expects the classPath to refer to a non generic type.
     *
     * @param  json the InputStream to read the incoming JSON object
     * @param  type   typeOfT = new TypeToken&lt;Collection&lt;Foo&gt;&gt;(){}.getType();
     * @return            the deserialized object of type classPath if the class exists and json represents an object of the type classPath     * @return           the deserialized object of type classPath if the class exists and json represents an object of the type classPath, null otherwise
     */
    public <T> T deserializeJSON(String  json, Type type) {
        T queryResult = null;

        if (json.length()>0 && type != null) {
            try {
                queryResult = mGson.fromJson(json, type);
            } catch (JsonSyntaxException e) {
                Log.e(TAG, "JsonSyntaxException: " + e.toString(), e);
            } catch (JsonIOException e) {
                Log.e(TAG, "JsonIOException " + e.toString(), e);
            } catch (Exception e) {
                Log.e(TAG, e.toString(), e);
            }catch (IncompatibleClassChangeError e){
                Log.e(TAG, "IncompatibleClassChangeError :: "+e.toString(),e);
            }
        }
        return queryResult;
    }

    /**
     * Deserializes the JSON object passed as JsonElement and returns an object of the the type specified in <i>classPath</i>
     * <p>
     * This method expects the classPath to refer to a non generic type.
     *
     * @param  json the JsonElement to read the incoming JSON object
     * @param  classPath   the non generic type of the return object
     * @return             the deserialized object of type classPath if the class exists and json represents an object of the type classPath     * @return           the deserialized object of type classPath if the class exists and json represents an object of the type classPath, null otherwise
     */

    public <T> T deserializeJSON(JsonElement json, Class classPath){
        T queryResult = null;

        if(json != null && classPath != null){
            try{
                queryResult = (T) mGson.fromJson(json, classPath);
            } catch (JsonSyntaxException e) {
                Log.e(TAG, "JsonSyntaxException: " + e.toString(), e);
            } catch (JsonIOException e) {
                Log.e(TAG, "JsonIOException " + e.toString(), e);
            } catch (Exception e) {
                Log.e(TAG, e.toString(), e);
            }catch (IncompatibleClassChangeError e){
                Log.e(TAG, "IncompatibleClassChangeError :: "+e.toString(),e);
            }
        }
        return queryResult;
    }

    public <T> T deserializeJSON(Reader reader, Class classPath) {
        T queryResult = null;
        if(reader != null && classPath != null){
            try{
                queryResult = (T) mGson.fromJson(reader, classPath);
            } catch (JsonSyntaxException e) {
                Log.e(TAG, "JsonSyntaxException: " + e.toString(), e);
            } catch (JsonIOException e) {
                Log.e(TAG, "JsonIOException " + e.toString(), e);
            } catch (Exception e) {
                Log.e(TAG, e.toString(), e);
            }catch (IncompatibleClassChangeError e){
                Log.e(TAG, "IncompatibleClassChangeError :: "+e.toString(),e);
            }
        }
        return queryResult;
    }



    public <T> T deserializeJSONByType(InputStream inputStream, Type type) {
        T queryResult = null;

        if (inputStream != null && type != null) {
            Reader reader = new InputStreamReader(inputStream,Charset.forName(UTF_ENCODING));
            try {
                queryResult = mGson.fromJson(reader, type);
            } catch (JsonSyntaxException e) {
                Log.e(TAG,"JsonSyntaxException: " + e.toString(),e);
            } catch (JsonIOException e) {
                Log.e(TAG,"JsonIOException " + e.toString(),e);
            }catch(Exception e){
                Log.e(TAG, e.toString(),e);
            } catch (IncompatibleClassChangeError e) {
                Log.e(TAG, "IncompatibleClassChangeError"+e.toString(),e);
            }finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG,"IOException " + e.toString(),e);
                }
            }
        }
        return queryResult;
    }

    /**
     * @param listJson - json string of mobile number
     * @return - list  - of MobileNumber class
     */
    public  <T> T deserializeJSON(String listJson, TypeToken token) {
        if (listJson.length()>0 || token==null) {
            return (T) Collections.EMPTY_LIST;
        }
        return deserializeJSON(listJson, token.getType());
    }


    /**
     * Serializes an object to its JSON representation
     * <p>
     *
     * This method should be used only with non generic objects.
     * @param  object the InputStream to read the incoming JSON object
     * @return        the  JSON representation in form of a String
     */
    public String serializeToJson(Object object) {
        try {
            return mGson.toJson(object);
        }catch (Exception e){
            Log.e(TAG ,e.toString());
        }catch (IncompatibleClassChangeError e){
            Log.e(TAG ,e.toString());
        }
        return null;
    }

    /**
     * Serializes an object to its JSON representation
     * <p>
     * This method should be used only with non generic objects.
     *
     * @param  object the InputStream to read the incoming JSON object
     * @return        the  JSON representation in form of a JsonElement
     */
    public JsonElement serializeToJsonElement(Object object) {
        try {
            return mGson.toJsonTree(object);
        }catch (Exception e){
            Log.e(TAG ,e.getMessage());
        }catch (IncompatibleClassChangeError e){
            Log.e(TAG ,e.toString());
        }
        return null;
    }

    /**
     * Serializes an object to its JSON representation
     * <p>
     * This method should be used only with non generic objects.
     *
     * @param  object the InputStream to read the incoming JSON object
     * @return        the  JSON representation in form of a String
     */
    public String serializeToJson(Object object, Type type) {
        try {
            return mGson.toJson(object,type);
        }catch (Exception e){
            Log.e(TAG ,e.toString());
        }catch (IncompatibleClassChangeError e){
            Log.e(TAG ,e.toString());
        }
        return null;
    }

    //do not use this method in production
    @Deprecated
    public String prettyPrintObject(Object object){
        return mGsonBuilder.setPrettyPrinting().create().toJson(object);
    }

    public Gson getGson() {
        return mGson;
    }

}
