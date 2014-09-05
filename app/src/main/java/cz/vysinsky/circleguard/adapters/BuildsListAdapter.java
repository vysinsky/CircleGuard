package cz.vysinsky.circleguard.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cz.vysinsky.circleguard.R;
import cz.vysinsky.circleguard.model.entities.Build;

public class BuildsListAdapter extends ArrayAdapter<Build> {

    private final Context context;

    private final ArrayList<Build> items;
    private ViewHolder holder = new ViewHolder();



    public BuildsListAdapter(Context context, ArrayList<Build> items) {
        super(context, R.layout.build_list_item, items);
        this.context = context;
        this.items = items;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.build_list_item, parent, false);
        }
        holder.indicatorView = convertView.findViewById(R.id.status_indicator);
        holder.titleView = (TextView) convertView.findViewById(R.id.title);
        holder.branchNameView = (TextView) convertView.findViewById(R.id.branch_name);
        holder.buildTimeView = (TextView) convertView.findViewById(R.id.build_time);
        convertView.setTag(holder);

        final Build build = items.get(position);
        holder.indicatorView.getBackground().setColorFilter(getColorFilterByStatus(build.getStatus()));
        holder.titleView.setText("#" + build.getId() + " " + build.getUsername() + "/" + build.getRepositoryName());
        holder.branchNameView.setText(build.getBranch());
        holder.buildTimeView.setText(build.getElapsedTime());
        return convertView;
    }



    private PorterDuffColorFilter getColorFilterByStatus(int status) {
        int color;

        switch (status) {
            case Build.STATUS_GREEN:
                color = Color.GREEN;
                break;
            case Build.STATUS_BLUE:
                color = Color.BLUE;
                break;
            case Build.STATUS_ORANGE:
                color = Color.YELLOW;
                break;
            case Build.STATUS_RED:
                color = Color.RED;
                break;
            case Build.STATUS_GRAY:
            default:
                color = Color.GRAY;
        }


        return new PorterDuffColorFilter(color, PorterDuff.Mode.SRC);
    }



    static class ViewHolder {
        View indicatorView;
        TextView titleView;
        TextView branchNameView;
        TextView buildTimeView;
    }
}
