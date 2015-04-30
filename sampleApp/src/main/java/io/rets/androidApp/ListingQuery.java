package io.rets.androidApp;

import org.apache.http.HttpException;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewsa on 4/28/15.
 */
public class ListingQuery extends RestlyQuery<Listing> {

    public ListingQuery(){
        super("listing");
    }

    @Override
    public List<Listing> exec() throws IOException ,JSONException, HttpException {
        JSONArray jsonArray = super.executeQuery();
        List<Listing> listings = new ArrayList<Listing>();
        for (int i = 0; i < jsonArray.length(); i++) {
            listings.add(new Listing(jsonArray.getJSONObject(i)));
        }
        return listings;
    }
}
