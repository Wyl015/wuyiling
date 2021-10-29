package com.test.utils.cryptology;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestQuery {
    private String name;
    private String password;

}
