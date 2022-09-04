/*
Copyright 2022 Nathan Mentley

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.poketrirx.aytosolver.impl.stdoutexporter;

import java.util.List;
import javax.inject.Inject;

import com.poketrirx.aytosolver.core.Exporter;
import com.poketrirx.aytosolver.models.Contestant;
import com.poketrirx.aytosolver.models.Data;
import com.poketrirx.aytosolver.models.KnownMatchResult;
import com.poketrirx.aytosolver.models.ResultsContext;

/**
* An exporter that'll simply output the results to the console using standard out.
*/
public final class StdOutExporter implements Exporter {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";

    @Inject
    public StdOutExporter() {}

    /**
    * Exports the results based on the raw input data and the current state of the context. 
    *
    * @param  data      The raw input data that was processed.
    * @param  context   The final resulting context after processing.
    */
    public void export(Data data, ResultsContext context) {
        StringBuilder builder = new StringBuilder();

        populateHeader(builder);

        int counter = 1;

        for (List<KnownMatchResult> results : context.getKnownMatchResults()) {
            populateSolution(builder, counter, data, results);

            counter++;
        }

        System.out.print(builder.toString());
    }

    private static void populateHeader(StringBuilder builder) {
        builder.append(System.lineSeparator());
        builder.append(System.lineSeparator());

        builder.append(ANSI_PURPLE);
        builder.append("Final Results:");
        builder.append(ANSI_RESET);

        builder.append(System.lineSeparator());
        builder.append(System.lineSeparator());
    }

    private static void populateSolution(StringBuilder builder, int counter, Data data, List<KnownMatchResult> results) {
        builder.append(ANSI_CYAN);
        builder.append("Solution: ");
        builder.append(counter);
        builder.append(ANSI_RESET);
        builder.append(System.lineSeparator());

        populateSolutionMatches(builder, data, results);

        builder.append(System.lineSeparator());
        builder.append(System.lineSeparator());
    }

    /**
    * Exports the results based on the raw input data and the current state of the context. 
    *
    * @param  buildder  The StringBuilder to populate data against.
    * @param  data      The raw input data that was processed.
    * @param  results   The collection of known match results to print.
    */
    private static void populateSolutionMatches(StringBuilder builder, Data data, List<KnownMatchResult> results) {
        for (KnownMatchResult result : results) {
            builder.append(ANSI_BLUE);
            builder.append(getContestantName(data.getContestants(), result.getContestants().getContestant1Id()));
            builder.append(ANSI_RESET);

            builder.append(ANSI_BLACK);
            builder.append(" - ");
            builder.append(ANSI_RESET);

            if (!result.isMatch()) {
                builder.append(ANSI_RED);
                builder.append("Unknown Match");
                builder.append(ANSI_RESET);
            } else {
                builder.append(ANSI_GREEN);
                builder.append(getContestantName(data.getContestants(), result.getContestants().getContestant2Id()));
                builder.append(ANSI_RESET);
            }

            if (result.isGuess()) {
                builder.append(ANSI_YELLOW);
                builder.append(" ***GUESS***");
                builder.append(ANSI_RESET);
            }

            builder.append(System.lineSeparator());
        }
    }

    private static String getContestantName(List<Contestant> contestants, String id) {
        return contestants.stream()
            .filter(contestant -> contestant != null && contestant.getId().equals(id))
            .map(contestant -> contestant.getName())
            .findFirst()
            .orElse("Unknown Contestant");
    }
}
