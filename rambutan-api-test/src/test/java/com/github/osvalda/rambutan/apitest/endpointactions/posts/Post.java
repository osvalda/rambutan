package com.github.osvalda.rambutan.apitest.endpointactions.posts;

import com.github.osvalda.rambutan.apitest.endpointactions.AbstractEndpointAction;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static io.restassured.http.Method.GET;

public class Post extends AbstractEndpointAction {

    public static final String POSTS_SLASH_ID = "/posts/{post_id}";

    @Step("GET specific post by ID {postId}")
    public static Response getPostById(String postId) {
        attachRequest(GET, POSTS_SLASH_ID);

        Response result = given()
                .contentType(ContentType.JSON)
                .get(POSTS_SLASH_ID, postId)
                .then()
                .extract().response();

        attachResult(result);
        return result;
    }
}
