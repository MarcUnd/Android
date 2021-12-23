package com.example.sensoren;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private OkHttpClient client = null;
    final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    String appKey = "";
    String baseURl = "";
    String thing = "";
    String datatable = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        client = new OkHttpClient();
        appKey = getString(R.string.app_key);
        baseURl = getString(R.string.url);
        thing = getString(R.string.thing);
        datatable = getString(R.string.data_table_sensor);
        CallServiceAsyncTask task = new CallServiceAsyncTask();
        task.execute();
        Button bt = findViewById(R.id.button);
        bt.setOnClickListener(this::onCLickRequest);


    }
    public void onCLickRequest(View view){
        CallQueryAsyncTask task2 = new CallQueryAsyncTask();
        task2.execute();
    }

    private class CallServiceAsyncTask extends AsyncTask<String, Void, String> {

        private JSONArray generateDataJSON() throws JSONException
        {

            JSONArray rows = new JSONArray();
            SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
            List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ALL);

            for (Sensor s : sensors)
            {
                String name = s.getName();
                String type = s.getStringType();
                String vendor = s.getVendor();
                rows.put(new JSONObject()
                        .put("type", type)
                        .put("name", name)
                        .put("vendor", vendor));

            }
            return rows;

        }

        private JSONObject generateSendData() throws JSONException
        {
            JSONObject fieldDefs = new JSONObject();
            JSONObject fieldDefsContent = new JSONObject();
            fieldDefsContent.put("key", generateFieldDefinition("key", "STRING"));
            fieldDefsContent.put("sensor", generateFieldDefinition("sensor", "JSON"));

            fieldDefs.put("fieldDefinitions", fieldDefsContent);

            JSONObject dataShape = new JSONObject();
            dataShape.put("dataShape", fieldDefs);

            JSONObject values = new JSONObject();
            values.put("values", dataShape);

            JSONArray rows = new JSONArray();

            rows.put(new JSONObject()
                    .put("key", "mUnderrain")
                    .put("sensor", generateDataJSON()));

            dataShape.put("rows", rows);
            return values;
        }
        private JSONObject generateFieldDefinition(String name, String baseType) throws JSONException
        {

            JSONObject def = new JSONObject();
            def.put("name", name);
            def.put("baseType", baseType);
            return def;
        }



        String url = String.format("%s/Thingworx/Things/%s/Services/AddOrUpdateDataTableEntry", baseURl, datatable); //Add the parameter to the url to fetch value
        RequestBody body = null;
        protected String doInBackground(String... urls) {

            try {
                String jsonStr = generateSendData().toString();
                body = RequestBody.create(jsonStr, JSON);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Accept", "application/json")
                    .addHeader("appkey", appKey)
                    .post(body)
                    .build();
            Log.d("ThingsRequest", request.toString());
            String responseStr = "";
            try  {
                Response response = client.newCall(request).execute();
                //responseStr =  response.body().string();
                responseStr =  response.body().string();
                Log.d("ThingWorxActivity", responseStr);


                JsonObject obj = new Gson().fromJson(responseStr, JsonObject.class);

                //responseStr = obj.getAsJsonArray("rows").get(0).getAsJsonObject().get(sendKey).getAsString();
            } catch (Exception e)
            {
                Log.e("ThingWorxActivity", String.valueOf(e));
            }

            return responseStr;
        }

        /**
         * Used to public progress, just call publishProgress((int) from doInBackground
         * @param values
         */
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("result",result);
        }
    }

    private class CallQueryAsyncTask extends AsyncTask<String, Void, String> {

        private JSONObject generateDataJSON() throws JSONException
        {
            TextView t = findViewById(R.id.textView2);

            JSONObject query = new JSONObject().put("filters", new JSONObject()
                    .put("type", "EQ")
                    .put("fieldName", "key")
                    .put("value", t.getText()));

            return new JSONObject().put("query",query);
        }

        protected String doInBackground(String... urls) {
            String url = String.format("%s/Thingworx/Things/%s/Services/QueryDataTableEntries", baseURl, datatable); //Add the parameter to the url to fetch value
            RequestBody body = null;
            try {
                String jsonStr = generateDataJSON().toString();
                Log.d("ThingWorxActivity", jsonStr);
                body = RequestBody.create(jsonStr, JSON);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Accept", "application/json")
                    .addHeader("appkey", appKey)
                    .post(body)
                    .build();
            Log.d("ThingWorxActivity", request.toString());
            String responseStr = "";
            StringBuilder res = new StringBuilder();
            try  {
                Response response = client.newCall(request).execute();
                responseStr =  response.body().string();
                Log.d("ThingWorxActivity", responseStr);


                JsonObject obj = new Gson().fromJson(responseStr, JsonObject.class);

                JsonArray rows = obj.getAsJsonArray("rows");

                for (JsonElement row : rows)
                {
                    JsonObject rowO = row.getAsJsonObject();
                    res.append(String.format(rowO.get("sensor").toString()));
                }
            } catch (Exception e)
            {
                Log.e("ThingWorxActivity", String.valueOf(e));
            }
            return res.toString();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            TextView t = findViewById(R.id.textView);
            t.setMovementMethod(new ScrollingMovementMethod());
            t.setText(result);
            Log.d("result1",result);
        }
    }
}
