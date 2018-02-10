package com.xrone.julis.compous.Application.Express.View;

import android.support.annotation.NonNull;

/**
 * Created by Julis on 2017/12/23.
 */

public interface IExpressView {
    void clearInput();
    void paste();
    void submit();
    void startLocation(@NonNull double desLatitude,@NonNull  double desLongitude);

}
