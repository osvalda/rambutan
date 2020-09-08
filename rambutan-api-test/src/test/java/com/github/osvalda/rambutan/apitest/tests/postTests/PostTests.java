package com.github.osvalda.rambutan.apitest.tests.postTests;

import com.github.osvalda.rambutan.apitest.framework.BaseTest;
import com.github.osvalda.rambutan.apitest.models.post.PostModel;
import io.github.osvalda.pitaya.annotation.TestCaseSupplementary;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.restassured.response.Response;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.annotations.Test;

import static com.github.osvalda.rambutan.apitest.endpointactions.posts.Post.POSTS_SLASH_ID;
import static com.github.osvalda.rambutan.apitest.endpointactions.posts.Post.getPostById;
import static io.qameta.allure.SeverityLevel.BLOCKER;
import static org.assertj.core.api.Assertions.assertThat;

public class PostTests extends BaseTest {

    @Test(groups = "smoke")
    @TestCaseSupplementary(api = {GET + POSTS_SLASH_ID})
    @Severity(BLOCKER)
    @Description(value = "Get a specific post by id")
    public void postGetTest() throws JSONException {
        PostModel expectedResult = PostModel.builder()
                .userId(2)
                .id(12)
                .title("in quibusdam tempore odit est dolorem")
                .body("itaque id aut magnam\npraesentium quia et ea odit et ea voluptas et\nsapiente quia nihil " +
                        "amet occaecati quia id voluptatem\nincidunt ea est distinctio odio")
                .build();

        Response resp = getPostById("12");
        assertThat(resp.statusCode())
                .as("The request should be successful!")
                .isEqualTo(200);
        JSONAssert.assertEquals(expectedResult.createJSONBody(), resp.body().asString(), true);
    }
}
