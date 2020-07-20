package com.github.osvalda.rambutan.apitest.models.post;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.osvalda.rambutan.apitest.framework.utils.CreateJSONBody;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
@Builder
public class PostModel extends CreateJSONBody {
    @JsonProperty("userId")
    private Integer userId;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("body")
    private String body;
}
