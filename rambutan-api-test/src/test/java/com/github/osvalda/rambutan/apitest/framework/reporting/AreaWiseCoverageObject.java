package com.github.osvalda.rambutan.apitest.framework.reporting;

import lombok.Data;

@Data
public class AreaWiseCoverageObject {

    private int coveredEndpoints;
    private int allEndpoints;

    public AreaWiseCoverageObject(int covered) {
        allEndpoints = 1;
        if(covered > 0)
            this.coveredEndpoints = 1;
    }

    public void increaseCoverage(int covered) {
        allEndpoints += 1;
        if(covered > 0)
            this.coveredEndpoints += 1;
    }

    public int getUncoveredEndpointNum() {
        return allEndpoints - coveredEndpoints;
    }
}
