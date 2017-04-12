package com.raul.rsd.android.smanager.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.raul.rsd.android.smanager.R;
import com.raul.rsd.android.smanager.domain.Location;
import com.raul.rsd.android.smanager.domain.Resource;
import com.raul.rsd.android.smanager.domain.Task;
import com.raul.rsd.android.smanager.utils.UIUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomItem extends AbstractItem<CustomItem, CustomItem.ViewHolder> {

    // ------------------------- ATTRIBUTES --------------------------

    public static final int TASK = 0;
    private static final int RESOURCE = 1;
    public int itemType;

    // Task Item
    public long id;
    private String description, type;
    private int time;
    private Context context;
    public boolean completed;

    // Server Item
    private String farmName, farmerId, phone, zipCode;
    private Location location;

    // ------------------------- CONSTRUCTOR -------------------------

    @Inject
    CustomItem(Context context) {
        this.context = context;
    }

    public CustomItem withTask(Task task) {
        this.id = task.getId();
        this.description = task.getDescription();
        this.type = task.getRequiredSkill().getName();
        this.time = task.getDuration();
        this.completed = task.isCompleted();

        itemType = TASK;
        return this;
    }

    public CustomItem withResource(Resource resource) {
        this.farmName = resource.getFarm_name();
        this.farmerId = resource.getFarmer_id();
        this.phone = resource.getPhone1();
        this.zipCode = resource.getZipcode();
        this.location = resource.getLocation();

        itemType = RESOURCE;
        return this;
    }

    @Override
    public int getLayoutRes() {
        return itemType == TASK ? R.layout.item_task : R.layout.item_resource;
    }

    // -------------------------- AUXILIARY --------------------------

    @Override
    public int getType() { return itemType == RESOURCE ? R.id.menu_server : R.id.menu_tasks; }

    // -------------------------- USE CASES --------------------------


    @Override @SuppressWarnings("all")
    public void bindView(ViewHolder viewHolder, List<Object> payloads) {
        super.bindView(viewHolder, payloads);

        if(itemType == TASK) {
            viewHolder.description.setText(description);
            viewHolder.type.setText(type);

            CharSequence timeStr;
            if(completed)
                timeStr = context.getString(R.string.done);
            else
                timeStr = UIUtils.getCustomDurationString(context, time);
            viewHolder.time.setText(timeStr);
        } else {
            String notAvailable = context.getString(R.string.not_available);

            String aux = location.getLatitude() + "," + location.getLongitude();
            viewHolder.coordinates.setText(aux != null ? aux : notAvailable);
            aux = context.getString(R.string.zip) + " " + zipCode;
            viewHolder.zipCode.setText(aux != null ? aux : notAvailable);
            viewHolder.farmName.setText(farmName != null ? farmName : notAvailable);
            viewHolder.phone.setText(phone != null ? phone : notAvailable);
            viewHolder.farmerId.setText(farmerId != null ? farmerId : notAvailable);
        }
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    // ------------------------- VIEW HOLDER -------------------------

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable @BindView(R.id.description_tv) TextView description;
        @Nullable @BindView(R.id.type_tv) TextView type;
        @Nullable @BindView(R.id.time_tv) TextView time;
        @Nullable @BindView(R.id.coordinates_tv) TextView coordinates;
        @Nullable @BindView(R.id.zip_code_tv) TextView zipCode;
        @Nullable @BindView(R.id.farm_name_tv) TextView farmName;
        @Nullable @BindView(R.id.farmer_id_tv) TextView farmerId;
        @Nullable @BindView(R.id.phone_tv) TextView phone;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
