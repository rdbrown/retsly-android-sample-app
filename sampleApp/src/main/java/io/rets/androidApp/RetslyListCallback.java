package io.rets.androidApp;

import java.util.List;

/**
 * Created by matthewsa on 4/24/15.
 */
public abstract class RetslyListCallback<T> {
    public abstract void getDataList(List<T> data);
}
