package com.raul.rsd.android.smanager.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.raul.rsd.android.smanager.R;
import com.raul.rsd.android.smanager.domain.Location;
import com.raul.rsd.android.smanager.utils.UIUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomItem extends AbstractItem<CustomItem, CustomItem.ViewHolder> {

    // ------------------------- ATTRIBUTES --------------------------

    private final int TASK = 0, RESOURCE = 1;
    private int itemType;

    // Task Item
    public long id;
    private String description, type;
    private int time;
    private Context context;

    // Server Item
    private String farmName, farmerId, phone, zipCode;
    private Location location;

    // ------------------------- CONSTRUCTOR -------------------------

    @Inject
    CustomItem(Context context) {
        this.context = context;
    }

    public CustomItem withTask(long id, String description, String skillName, int time) {
        this.id = id;
        this.description = description;
        this.type = skillName;
        this.time = time;

        itemType = TASK;
        return this;
    }

    public CustomItem withResource(String farmName, String farmerId, String phone, String zipCode,
                                   Location location) {
        this.farmName = farmName;
        this.farmerId = farmerId;
        this.phone = phone;
        this.zipCode = zipCode;
        this.location = location;

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
            viewHolder.time.setText(UIUtils.getCustomDurationString(context, time));
        } else {
            viewHolder.coordinates.setText(location.getLatitude() + "," + location.getLongitude());
            viewHolder.zipCode.setText(zipCode);
            viewHolder.farmName.setText(farmName);
            viewHolder.phone.setText(phone);
            viewHolder.farmerId.setText(farmerId);
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
