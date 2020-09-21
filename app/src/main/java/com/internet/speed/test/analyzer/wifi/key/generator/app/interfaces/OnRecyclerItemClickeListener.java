package com.internet.speed.test.analyzer.wifi.key.generator.app.interfaces;

import android.view.View;

public interface OnRecyclerItemClickeListener {

    void onItemClicked(int position);

    void onItemLongClicked(int position);

    void onItemCheckBoxClicked(View view, int position);
}
