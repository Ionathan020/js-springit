package com.jstronkhorst.springit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

@SpringBootTest
class SpringitApplicationTests {

    ApplicationModules modules = ApplicationModules.of(SpringitApplication.class);

    @Test
    void springModulithVerification() {
        modules.verify();
    }

    @Test
    void springModulithDocumentation() {
        new Documenter(modules).writeDocumentation();
    }

}
