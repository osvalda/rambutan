package com.github.osvalda.rambutan.apitest.framework.supplementary;

import lombok.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({METHOD})
public @interface TestCaseSupplementary {
    @NonNull String[] api();
}
