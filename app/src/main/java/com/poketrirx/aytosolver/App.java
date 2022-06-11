/*
Copyright 2022 Nathan Mentley

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.poketrirx.aytosolver;

import lombok.Builder;
import lombok.NonNull;

import com.poketrirx.aytosolver.exporters.Exporter;
import com.poketrirx.aytosolver.importers.Importer;
import com.poketrirx.aytosolver.models.Data;
import com.poketrirx.aytosolver.processors.Processor;
import com.poketrirx.aytosolver.ResultsContext;

@Builder()
/**
 * A class that contains and executes the app logic.
 */
public final class App {
    @NonNull private final Importer importer;
    @NonNull private final Exporter exporter;
    @NonNull private final Processor processor;

    /**
     * Runs the app.
     */
    public void run() {
        Data data = importer.load();

        ResultsContext context = new ResultsContext(data);

        processor.process(data, context);

        exporter.export(data, context);
    }
}
