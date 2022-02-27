package com.rashed.donofood.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.rashed.donofood.Models.FoodDonation;
import com.rashed.donofood.R;
import java.util.List;

public class DonationAdapter extends ArrayAdapter<FoodDonation> {

    public DonationAdapter(@NonNull Context context, @NonNull List<FoodDonation> objects) {
        super(context, 0, objects);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;

        if(listItemView == null)
            listItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item, parent, false);

        // Get the object located at this position in the list
        FoodDonation currentDonation = getItem(position);

        if(currentDonation != null) {
            //Update listView with current Donation object
            TextView donateItemName = (TextView) listItemView.findViewById(R.id.donateItemName);
            donateItemName.setText(currentDonation.getFoodName());

            TextView donateItemAddress = (TextView) listItemView.findViewById(R.id.donateItemAddress);
            donateItemAddress.setText(currentDonation.getLocation());

            TextView donateItemQty = (TextView) listItemView.findViewById(R.id.donateItemQty);
            donateItemQty.setText(currentDonation.getQuantity().toString());
        }

        // Return the whole list item layout
        // so that it can be shown in the ListView
        return listItemView;
    }
}

