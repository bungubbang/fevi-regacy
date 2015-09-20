package com.app.fevir.interfaces.web.model;

import android.text.TextUtils;

public class OpenModel {
    public boolean containHashUrl(String lastPath) {

        return !TextUtils.isEmpty(lastPath);
    }

}
