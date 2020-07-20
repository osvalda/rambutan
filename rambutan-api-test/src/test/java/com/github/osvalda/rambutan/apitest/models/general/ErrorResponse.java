package com.github.osvalda.rambutan.apitest.models.general;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.osvalda.rambutan.apitest.framework.utils.CreateJSONBody;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class ErrorResponse extends CreateJSONBody {
    @JsonProperty("error")
    private String error;
}