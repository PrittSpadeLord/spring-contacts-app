package io.github.prittspadelord.application.inializer;

import io.github.prittspadelord.application.configs.ContactsAppWebConfig;

import org.jspecify.annotations.Nullable;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class ContactsAppServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?> @Nullable [] getRootConfigClasses() {
        return new Class[] {};
    }

    @Override
    protected Class<?> @Nullable [] getServletConfigClasses() {
        return new Class[] {
            ContactsAppWebConfig.class
        };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}
