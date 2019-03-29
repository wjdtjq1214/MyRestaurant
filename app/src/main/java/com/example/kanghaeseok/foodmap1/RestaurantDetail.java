package com.example.kanghaeseok.foodmap1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;

public class RestaurantDetail extends AppCompatActivity {
    Intent i;
    String rName, rAdd, rNum, rImage, responseString;
    int rId;
    HttpResponse response;
    ArrayList<menu> menuList = new ArrayList<>();
    TextView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        menu = (TextView)findViewById(R.id.menu);

        i = getIntent();
        rId = i.getIntExtra("rId", 0);
        rName = i.getStringExtra("rName");
        rAdd = i.getStringExtra("rAdd");
        rNum = i.getStringExtra("rNum");
        rImage = i.getStringExtra("rImage");

        select_doProcess();

        menu.append(rName + "\n" + rAdd + "\n" + rNum + "\n");

        for(int count = 0; count < menuList.size(); count++){
            menu.append(menuList.get(count).getmName() + "     " + menuList.get(count).getmPrice() + "\n");
        }
    }

    private void select_doProcess() {

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://13.209.39.126:8000/getMenuList");
        post.setHeader("Accept", "application/json");
        post.setHeader("Accept", "application/json");
        ArrayList<NameValuePair> nameValues = new ArrayList<NameValuePair>();

        try {
            //Post방식으로 넘길 값들을 각각 지정을 해주어야 한다.
            nameValues.add(new BasicNameValuePair("rId", URLDecoder.decode(String.valueOf(rId), "UTF-8")));
            //HttpPost에 넘길 값을들 Set해주기
            post.setEntity(new UrlEncodedFormEntity(nameValues, HTTP.UTF_8));
        } catch (UnsupportedEncodingException ex) {
            Log.e("Insert Log", ex.toString());
        }

        try {
            //설정한 URL을 실행시키기
            //ResponseHandler<String> responseHandler = new BasicResponseHandler();
            response = client.execute(post);
            responseString = EntityUtils.toString(response.getEntity());
            Log.i("aaaa", responseString);
            //통신 값을 받은 Log 생성. (200이 나오는지 확인할 것~) 200이 나오면 통신이 잘 되었다는 뜻!
            //Log.i("Insert Log", "response.getStatusCode:" + response.getStatusLine().getStatusCode());

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jList = new JSONArray(responseString);
            for(int i = 0; i < jList.length(); i++){
                JSONObject jsonObject = jList.getJSONObject(i);
                menu menuObj = new menu();
                menuObj.setmName(jsonObject.getString("mName"));
                menuObj.setmPrice(jsonObject.getString("mPrice"));
                menuList.add(menuObj);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
