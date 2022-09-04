package com.poketrirx.aytosolver.impl;

import com.google.inject.AbstractModule;

import com.poketrirx.aytosolver.core.Exporter;
import com.poketrirx.aytosolver.core.GuessEvaluator;
import com.poketrirx.aytosolver.core.GuessFactory;
import com.poketrirx.aytosolver.core.Importer;
import com.poketrirx.aytosolver.core.Processor;
import com.poketrirx.aytosolver.impl.basicguessevaluator.BasicGuessEvaluator;
import com.poketrirx.aytosolver.impl.basicguessfactory.BasicGuessFactory;
import com.poketrirx.aytosolver.impl.bruteforceprocessor.BruteForceProcessor;
import com.poketrirx.aytosolver.impl.resourcejsonimporter.ResourceJsonImporter;
import com.poketrirx.aytosolver.impl.stdoutexporter.StdOutExporter;

public class Module extends AbstractModule {
    @Override
    protected void configure() {
        bind(GuessEvaluator.class).to(BasicGuessEvaluator.class);
        bind(GuessFactory.class).to(BasicGuessFactory.class);
        bind(Processor.class).to(BruteForceProcessor.class);
        bind(Importer.class).to(ResourceJsonImporter.class);
        bind(Exporter.class).to(StdOutExporter.class);
    }
}
