package com.poketrirx.aytosolver.cli;

import java.util.Objects;

import com.google.inject.Guice;
import com.google.inject.Injector;
import picocli.CommandLine.IFactory;

import com.poketrirx.aytosolver.impl.Module;

final class Factory implements IFactory {
    private static final Injector INJECTOR = Guice.createInjector(new Module());

    @Override
    public <Type> Type create(Class<Type> aClass) throws Exception {
        Objects.requireNonNull(aClass);

        return INJECTOR.getInstance(aClass);
    }
}