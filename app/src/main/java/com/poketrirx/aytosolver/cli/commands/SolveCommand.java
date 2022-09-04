/*
Copyright 2022 Nathan Mentley

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.poketrirx.aytosolver.cli.commands;

import java.util.concurrent.Callable;
import java.util.Objects;
import javax.inject.Inject;

import picocli.CommandLine.Command;

import com.poketrirx.aytosolver.core.Exporter;
import com.poketrirx.aytosolver.core.Importer;
import com.poketrirx.aytosolver.core.Processor;
import com.poketrirx.aytosolver.models.Data;
import com.poketrirx.aytosolver.models.ResultsContext;

@Command(name = "solve")
final class SolveCommand implements Callable<Integer> {
    private final Importer importer;

    private final Exporter exporter;

    private final Processor processor;

    @Inject
    public SolveCommand(Importer importer, Exporter exporter, Processor processor) {
        this.importer = Objects.requireNonNull(importer);
        this.exporter = Objects.requireNonNull(exporter);
        this.processor = Objects.requireNonNull(processor);
    }

    @Override
    public Integer call() throws Exception {
        Data data = importer.load();

        ResultsContext context = processor.process(data);

        exporter.export(data, context);

        return 0;
    }
}