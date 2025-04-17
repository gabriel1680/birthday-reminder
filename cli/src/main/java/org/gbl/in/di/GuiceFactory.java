package org.gbl.in.di;

import com.google.inject.ConfigurationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import picocli.CommandLine;
import picocli.CommandLine.IFactory;

public class GuiceFactory implements IFactory {
    private final Injector injector = Guice.createInjector(new ContactsModule());

    @Override
    public <K> K create(Class<K> aClass) throws Exception {
        try {
            return injector.getInstance(aClass);
        } catch (ConfigurationException ex) {
            return CommandLine.defaultFactory().create(aClass);
        }
    }
}

