package com.thanhuy.tiki.hometest.interfaces;

import com.thanhuy.tiki.hometest.data.response.Deal;

import java.util.List;

/**
 *
 * author:uy.daonguyen@gmail.com
 */
public interface MainAsyncResponse {
    void processStart();
    void processFinish(List<Deal> listDeal);
}
