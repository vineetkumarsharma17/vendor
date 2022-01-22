package com.mbl.zaikavendor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import com.mbl.zaikavendor.Fragment.About;
import com.mbl.zaikavendor.Fragment.Category;
import com.mbl.zaikavendor.Fragment.ContactUs;
import com.mbl.zaikavendor.Fragment.Facility;
import com.mbl.zaikavendor.Fragment.Main;
import com.mbl.zaikavendor.Fragment.Myoffers;
import com.mbl.zaikavendor.Fragment.Password;
import com.mbl.zaikavendor.Fragment.Products;
import com.mbl.zaikavendor.Fragment.Producttype;
import com.mbl.zaikavendor.Fragment.Profile;
import com.mbl.zaikavendor.Fragment.Profilelist;
import com.mbl.zaikavendor.Fragment.Terms;
import com.mbl.zaikavendor.Fragment.TotalOrders;
import com.mbl.zaikavendor.Fragment.Workhour;
import com.mbl.zaikavendor.Fragment.helpandsupport;
import com.mbl.zaikavendor.Fragment.helpcenter;
import com.mbl.zaikavendor.app.EndPoints;
import com.mbl.zaikavendor.constant.Config;
import com.mbl.zaikavendor.helper.MyPreferenceManager;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;


public class Welcome extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;

    String mytoken="";
    SharedPreferences pref;
    String userid="";

    MenuItem item;
    public String title;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    MyPreferenceManager session2;
    public static BottomNavigationView navigation;

    TextView topbarhead;
    ImageView tv_header_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(R.string.app_name);
            setSupportActionBar(toolbar);
            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        topbarhead=(TextView)findViewById(R.id.topbarhead);
        topbarhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment home = new Main();
                FragmentManager FM = getSupportFragmentManager();
                FM
                        .beginTransaction()
                        .replace(R.id.container_body, home)
                        .addToBackStack(null)
                        .addToBackStack("Home")
                        .commit();
            }
        });
        tv_header_title = (ImageView)findViewById(R.id.tv_header_title);
        tv_header_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment home = new Main();
                FragmentManager FM = getSupportFragmentManager();
                FM
                        .beginTransaction()
                        .replace(R.id.container_body, home)
                        .addToBackStack(null)
                        .addToBackStack("Home")
                        .commit();
            }
        });



        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.removeShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        session2 = new MyPreferenceManager(getApplicationContext());

      /*  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState(); */

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        pref = getSharedPreferences(EndPoints.sharedpref, 0);
        if (pref.getString(EndPoints.prefname, "").length() > 0) {

        } else{
                 session2.logoutUser();
        }


        Fragment home = new Main();
        FragmentManager FM = getSupportFragmentManager();
        FM
                .beginTransaction()
                .replace(R.id.container_body, home)
                .addToBackStack(null)
                .addToBackStack("Home")
                .commit();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    // displayFirebaseRegId();
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    String message = intent.getStringExtra("message");
                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                    //txtMessage.setText(message);
                }
            }
        };

        try {
            FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
            Log.d("kumarmanglam", "Subscribed to " + Config.TOPIC_GLOBAL + " topic");
        } catch (Exception e){}

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        mytoken=regId;
        new JSONAsyncTask().execute(EndPoints.UPDATETOKEN);

    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportFragmentManager().popBackStack();
        return true;
    }


    public void showUpButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void hideUpButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }


    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Bundle bundle = new Bundle();
            bundle.putString("logid", "1");
            title = "Hans Zaika";
            Fragment home = new Main();
            FragmentManager FM = getSupportFragmentManager();

            switch (item.getItemId()) {
                case R.id.navigation_cart:
                    home = new Main();
                    break;
                case R.id.navigation_gifts:
                    home = new helpcenter();
                    break;
                case R.id.navigation_order:
                    home = new Producttype();
                    break;
                case R.id.navigation_profile:
                    home = new Profilelist();
                    break;
                default:
                    break;
            }

            FM
                    .beginTransaction()
                    .replace(R.id.container_body, home)
                    .addToBackStack(null)
                    .addToBackStack("Home")
                    .commit();

            return false;
        }
    };



    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } */
        int index = getSupportFragmentManager().getBackStackEntryCount();
        if (index > 1) {
            FragmentManager.BackStackEntry fragment = getSupportFragmentManager().getBackStackEntryAt(index - 1);
            String tag = fragment.getName();
            Fragment fragments = getSupportFragmentManager().findFragmentByTag(tag);
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //  Toast.makeText(this, "Item added to cart.", Toast.LENGTH_SHORT).show();
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu); //your file name
        item = menu.findItem(R.id.action_notifications);
        //NotificationCountSetClass.setAddToCart(Mainactivity2.this, item, notificationCountCart);
        //  return super.onCreateOptionsMenu(menu);
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        FragmentManager fm = getSupportFragmentManager();
        title=getString(R.string.app_name);

      /*  Fragment newFragment = new About();
        switch (item.getItemId()) {
            case R.id.action_notifications:
                newFragment = new About();
                break;

            default:
                break;
        }
        if (newFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, newFragment);
            fragmentTransaction.addToBackStack(title);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        } */
        return super.onOptionsItemSelected(item);

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment newFragment = new About();
        FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("logid", "1");
        title = getString(R.string.app_name);
        switch (item.getItemId()) {
            case R.id.Home:
                Intent intent = new Intent(Welcome.this, Welcome.class);
                startActivity(intent);
                break;


            case R.id.Empanelments:
                newFragment = new TotalOrders();
                break;
            case R.id.category:
                newFragment = new Category();
                break;
            case R.id.products:
                newFragment = new Products();
                break;



            case R.id.doctors:
                newFragment = new Profile();
                break;
            case R.id.technology:
                newFragment = new Facility();
                break;
            case R.id.hours:
                newFragment = new Workhour();
                break;
            case R.id.password:
                newFragment = new Password();
                break;


            case R.id.logout:
                session2.logoutUser();
                finish();
                break;



         /*   case R.id.About_us:
                newFragment = new About();
                break;

            case R.id.contact_us:
                newFragment = new ContactUs();
                break;

            case R.id.faq:
                newFragment = new helpandsupport();
                break;

            case R.id.help:
                newFragment = new Terms();
                break; */

            default:
                break;

        }
        //     FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //     ft.replace(R.id.view_pager, newFragment);
        //     ft.commit();

        newFragment.setArguments(bundle);
      //  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        // item.setChecked(true);
        getSupportActionBar().setTitle(title);
        //assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        fm
                .beginTransaction()
                .replace(R.id.container_body, newFragment)
                .addToBackStack(null)
                .addToBackStack(title)
                .commit();

        return true;
    }



    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            pref= getSharedPreferences(EndPoints.sharedpref, 0);
            userid = pref.getString(EndPoints.prefuname,"");

            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;


                HttpClient httpclient = new DefaultHttpClient();

                SchemeRegistry registry = new SchemeRegistry();
                org.apache.http.conn.ssl.SSLSocketFactory socketFactory = org.apache.http.conn.ssl.SSLSocketFactory.getSocketFactory();
                socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
                registry.register(new Scheme("https", socketFactory, 443));
                SingleClientConnManager mgr = new SingleClientConnManager(httpclient.getParams(), registry);
                DefaultHttpClient httpClient = new DefaultHttpClient(mgr, httpclient.getParams());

                // Set verifier
                HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

                // Example send http request
                final String url = urls[0];

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("fullname", mytoken));
                params.add(new BasicNameValuePair("password", userid));

                HttpPost httppost = new HttpPost(url);
                httppost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse response = httpclient.execute(httppost);
                String result = EntityUtils.toString(response.getEntity());

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    System.out.println("saaaaaa"+result.toString());
                    if(result.toString().equals("success")){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
                else{
                    return false;
                }
                //------------------>>
            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

        }
    }

}
