package com.mianbrothersbooksellerandstationers.android.utils;

import android.widget.Filter;

import com.mianbrothersbooksellerandstationers.android.adapters.CustomerProductAdapter;
import com.mianbrothersbooksellerandstationers.android.adapters.ProductAdapter;
import com.mianbrothersbooksellerandstationers.android.models.ProductModel;

import java.util.ArrayList;

public class FilterProducts extends Filter {

    private CustomerProductAdapter adapter;
    private ArrayList<ProductModel> filterList;

    public FilterProducts(CustomerProductAdapter adapter, ArrayList<ProductModel> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        //validate data for search query
        if(constraint !=null && constraint.length() > 0)
        {
            //search field not empty, searching something, perform search

            //change to upper case, to make case insensitive
            constraint = constraint.toString();
            //store our filtered list
            ArrayList<ProductModel> filteredModels = new ArrayList<>();
            for(int i=0;i<filterList.size();i++)
            {
                //check search by category
                if(filterList.get(i).getName().toUpperCase().contains(constraint.toString().toUpperCase()))
                {
                    //add filtered data to list
                    filteredModels.add(filterList.get(i));
                }
            }
            results.count = filteredModels.size();
            results.values = filteredModels;

        }
        else{
            //search filed empty, not searching return original/all/complete list
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapter.productList = (ArrayList<ProductModel>) filterResults.values;
        //refresh adapter
        adapter.notifyDataSetChanged();
    }
}
