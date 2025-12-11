package io.github.prittspadelord.application;

import io.github.prittspadelord.application.configs.ContactsAppDatabaseConfig;
import io.github.prittspadelord.application.configs.ContactsAppRootConfig;
import io.github.prittspadelord.application.configs.ContactsAppSecurityConfig;
import io.github.prittspadelord.application.configs.ContactsAppWebConfig;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class ContactsAppServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?> @Nullable [] getRootConfigClasses() {
        return new Class[] {
            ContactsAppDatabaseConfig.class,
            ContactsAppRootConfig.class,
            ContactsAppSecurityConfig.class
        };
    }

    @Override
    protected Class<?> @Nullable [] getServletConfigClasses() {
        return new Class[] {
            ContactsAppWebConfig.class
        };
    }

    @NullMarked
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}