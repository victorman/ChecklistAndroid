package se.frand.app.checklist;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by victorfrandsen on 9/30/15.
 */
public class FilteredAdapter extends ArrayAdapter<ApplicationInfo> {

    private List<ApplicationInfo> appList;
    private Context context;
    private PackageManager packageManager;

    public FilteredAdapter(Context context, int resource, List<ApplicationInfo> objects) {
        super(context, resource, objects);

        this.context = context;
        this.appList = objects;
        packageManager = context.getPackageManager();

    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.filtered_list_item, null);

        final ApplicationInfo data = appList.get(position);

        if (null != data) {
            view.setVisibility(View.VISIBLE);
            TextView appName = (TextView) view.findViewById(R.id.app_name);
            TextView packageName = (TextView) view.findViewById(R.id.app_package);
            ImageView iconView = (ImageView) view.findViewById(R.id.app_icon);

            appName.setText(data.loadLabel(packageManager));
            packageName.setText(data.packageName);
            final String pnStr = data.packageName;
            iconView.setImageDrawable(data.loadIcon(packageManager));

            for(String s: MainActivity.filterd) {
                if(s.compareTo(pnStr) == 0) {
                    return new RelativeLayout(getContext());
                }
            }

        }
        return view;
    }

    public int getCount() {
        return ((null != appList) ? appList.size() : 0);
    }

    public ApplicationInfo getItem(int position) {
        return ((null != appList) ? appList.get(position) : null);
    }

    public long getItemId(int position) {
        return position;
    }

}
