package com.rashed.donofood.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.rashed.donofood.Models.ClothDonation;
import com.rashed.donofood.Models.FoodDonation;
import com.rashed.donofood.R;
import java.util.List;

public class ClothDonationAdapter extends ArrayAdapter<ClothDonation> {

    public ClothDonationAdapter(@NonNull Context context, @NonNull List<ClothDonation> objects) {
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
        ClothDonation currentDonation = getItem(position);

        if(currentDonation != null) {
            //Update listView with current Donation object
            TextView donateItemName = listItemView.findViewById(R.id.donateItemName);
            donateItemName.setText(currentDonation.getClothName());

            TextView donateItemAddress = listItemView.findViewById(R.id.donateItemAddress);
            donateItemAddress.setText(currentDonation.getLocation());

            TextView donateItemQty = listItemView.findViewById(R.id.donateItemQty);
            donateItemQty.setText(Integer.toString(currentDonation.getQuantity()));
        }

        // Return the whole list item layout
        // so that it can be shown in the ListView
        return listItemView;
    }
}