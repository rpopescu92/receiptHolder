package com.example.receiptholder.springbootandroid;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.example.receiptholder.model.User;
import com.example.receiptholder.springootandroid.service.ReceiptService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getName();
    private ReceiptService receiptService;
    private Button loginButton;
    private Button registerButton;
    private TextView loggedUser;


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            receiptService = ((ReceiptService.ServiceBinder) binder).getService();
            Log.d(TAG, "service connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            receiptService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = (Button)findViewById(R.id.login_button);
        registerButton = (Button) findViewById(R.id.register_button);
        loggedUser = (TextView) findViewById(R.id.logged_user);

        bindService(new Intent(this,ReceiptService.class), serviceConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new HttpRequestTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            new HttpRequestTask().execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_button:
                break;
            case R.id.register_button:
                break;
        }
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, ResponseEntity<User>> {
        @Override
        protected ResponseEntity<User> doInBackground(Void... params) {
            try {
                final String url = "http://10.0.2.2:8080/login";
                User user = new User();
                user.setPassword("1234");
                user.setUsername("gigi");
                RestTemplate restTemplate = new RestTemplate();

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                //User userReceived = restTemplate.getForObject(url, User.class);
                ResponseEntity<User> responseEntity =  restTemplate.postForEntity(url,user, User.class);

                return responseEntity;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(ResponseEntity<User> userResponseEntity) {
            TextView greetingIdText = (TextView) findViewById(R.id.id_value);
            TextView greetingContentText = (TextView) findViewById(R.id.content_value);
            //greetingIdText.setText(userResponseEntity.getBody().getId());
            Log.d(TAG,userResponseEntity.getBody().getUsername());
            greetingContentText.setText(userResponseEntity.getBody().getUsername());
        }

    }


}
