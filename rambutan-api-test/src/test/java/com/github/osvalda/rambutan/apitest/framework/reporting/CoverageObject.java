package com.github.osvalda.rambutan.apitest.framework.reporting;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@AllArgsConstructor
public class CoverageObject {

    private static final String SEPARATOR = " ";
    private String area;
    private String endpoint = "NoN";
    private String method = "NoN";
    private int covered;

    public CoverageObject(String area, String endpoint) {
        this.area = area;
        if(!endpoint.isEmpty()) {
            this.endpoint = StringUtils.substringAfter(endpoint, SEPARATOR);
            this.method = StringUtils.substringBefore(endpoint, SEPARATOR);
        }
    }
}
