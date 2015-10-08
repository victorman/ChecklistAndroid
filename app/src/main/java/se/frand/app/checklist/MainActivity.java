package se.frand.app.checklist;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {

    private boolean isFiltered = false;
    private PackageManager packageManager = null;
    private List<ApplicationInfo> applist = null;
    private ArrayAdapter listadapter = null;

    public static final List<String> filterd = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        packageManager = getPackageManager();
        //ListView listview= getListView();
        new LoadApplications().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            //toggle filter
            if(isFiltered) {
                new LoadApplications().execute();
                isFiltered = false;
            } else {
                new FilterApplications().execute();
                isFiltered = true;
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onListItemClick(ListView l, View v, int position, long id){
        super.onListItemClick(l, v, position, id);
        ApplicationInfo app = applist.get(position);
        try{
            Intent intent = packageManager.getLaunchIntentForPackage(app.packageName);

            if(intent != null){
                startActivity(intent);
            }
        }catch(ActivityNotFoundException e){
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }catch(Exception e){
            Toast.makeText(MainActivity.this,e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list){
        ArrayList<ApplicationInfo> appList = new ArrayList<ApplicationInfo>();
        for(ApplicationInfo info : list){
            try{
                if(packageManager.getLaunchIntentForPackage(info.packageName)!=null){
                    appList.add(info);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return appList;
    }

    private class LoadApplications extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;
        protected Void doInBackground(Void... params){
            applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
            listadapter = new AppAdapter(MainActivity.this, R.layout.activity_list_item, applist);
            return null;
        }
        protected void onPostExecute(Void result){
            setListAdapter(listadapter);
            progress.dismiss();
            super.onPostExecute(result);
        }
        protected void onPreExecute(){
            progress = ProgressDialog.show(MainActivity.this, null, "Loading apps info...");
            super.onPreExecute();
        }
    }

    private class FilterApplications extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;
        protected Void doInBackground(Void... params){
            applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
            listadapter = new FilteredAdapter(MainActivity.this, R.layout.filtered_list_item, applist);
            return null;
        }
        protected void onPostExecute(Void result){
            setListAdapter(listadapter);
            progress.dismiss();
            super.onPostExecute(result);
        }
        protected void onPreExecute(){
            progress = ProgressDialog.show(MainActivity.this, null, "Loading apps info...");
            super.onPreExecute();
        }
    }
}