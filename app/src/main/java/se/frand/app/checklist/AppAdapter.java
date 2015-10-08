package se.frand.app.checklist;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AppAdapter extends ArrayAdapter<ApplicationInfo> {

    private List<ApplicationInfo> appList;
    private Context context;
    private PackageManager packageManager;

    public AppAdapter(Context context, int resource, List<ApplicationInfo> objects){
        super(context, resource,objects);

        this.context = context;
        this.appList= objects;
        packageManager= context.getPackageManager();
    }

    public View getView(final int position, View convertView, ViewGroup parent){

        View view = convertView;
        if(null == view){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.activity_list_item,null);
        }

        ApplicationInfo data = appList.get(position);

        if(null != data){
            TextView appName = (TextView) view.findViewById(R.id.app_name);
            TextView packageName = (TextView) view.findViewById(R.id.app_package);
            ImageView iconView = (ImageView) view.findViewById(R.id.app_icon);

            final String pnStr = data.packageName;
            appName.setText(data.loadLabel(packageManager));
            packageName.setText(pnStr);
            iconView.setImageDrawable(data.loadIcon(packageManager));

            CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    for(String s: MainActivity.filterd) {
                        if(s.compareTo(pnStr) == 0) {
                            if(!isChecked)
                                MainActivity.filterd.remove(s);
                            else
                                return; //already there apparently
                        }
                    }
                    MainActivity.filterd.add(pnStr);
                }
            });

        }
        return view;
    }

    public int getCount(){
        return((null != appList) ? appList.size() : 0);
    }
    public ApplicationInfo getItem(int position){
        return((null != appList) ? appList.get(position): null);
    }
    public long getItemId(int position) {
        return position;
    }


}