package com.github.osvalda.rambutan.apitest.endpointactions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.github.osvalda.rambutan.apitest.framework.utils.CreateJSONBody;
import io.qameta.allure.Allure;
import io.restassured.http.Header;
import io.restassured.http.Method;
import io.restassured.response.Response;

public abstract class AbstractEndpointAction {

    protected static final String CASE_ID = "caseid";
    protected static final String ASSET_ID = "assetid";

    private static final String REQUEST_PATH = "Request path";
    private static final String REQUEST_BODY = "Request body";
    private static final String RESPONSE_BODY = "Response Body";
    private static final String STATUS_CODE = "Status Code";
    private static final String RESPONSE_HEADERS = "Response headers";
    private static final String RESPONSE_COOKIES = "Response cookies";
    private static final String TEXT_JSON = "text/json";

    protected static void attachResult(Response result) {
        Allure.addAttachment(RESPONSE_BODY, TEXT_JSON, formattJson(result.getBody().asString()));
        Allure.addAttachment(STATUS_CODE, String.valueOf(result.getStatusCode()));
        Allure.addAttachment(RESPONSE_HEADERS, result.getHeaders().toString());
        Allure.addAttachment(RESPONSE_COOKIES, result.getCookies().toString());
    }

    private static String formattJson(String jsonString) {
        JsonParser parser = new JsonParser();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            JsonElement el = parser.parse(jsonString);
            jsonString = gson.toJson(el);
            return jsonString;
        } catch (JsonParseException e) {
            return jsonString;
        }
    }

    protected static void attachRequest(CreateJSONBody payload, Method method, String endpoint) {
        Allure.addAttachment(REQUEST_PATH, method.name().concat(" ").concat(endpoint));
        String payloadAsString = "null";
        if(payload != null)
            payloadAsString = payload.createJSONBodyWithNulls();
        Allure.addAttachment(REQUEST_BODY, TEXT_JSON, payloadAsString);
    }

    protected static void attachRequest(Method method, String endpoint) {
        Allure.addAttachment(REQUEST_PATH, method.name().concat(" ").concat(endpoint));
    }

    protected static Header getAnAuthorizationHeader(String accessToken) {
        return new Header("authorization", accessToken);
    }
}
