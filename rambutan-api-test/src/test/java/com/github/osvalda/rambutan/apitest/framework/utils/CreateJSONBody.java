package com.github.osvalda.rambutan.apitest.framework.utils;

import com.google.gson.GsonBuilder;

public abstract class CreateJSONBody {

    protected CreateJSONBody() {
    }

    public String createJSONBody() {
        return (new GsonBuilder()).setPrettyPrinting().create().toJson(this);
    }

    public String createJSONBodyWithNulls() {
        return (new GsonBuilder()).serializeNulls().setPrettyPrinting().create().toJson(this);
    }

}
