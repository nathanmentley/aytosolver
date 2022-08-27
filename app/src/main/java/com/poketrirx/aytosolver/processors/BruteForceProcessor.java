/*
Copyright 2022 Nathan Mentley

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.poketrirx.aytosolver.processors;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.NonNull;

import com.google.common.collect.ImmutableList;
import com.poketrirx.aytosolver.core.Processor;
import com.poketrirx.aytosolver.models.ContestantTuple;
import com.poketrirx.aytosolver.models.Data;
import com.poketrirx.aytosolver.models.KnownMatchResult;
import com.poketrirx.aytosolver.models.ResultsContext;
import com.poketrirx.aytosolver.models.ResultsContext.ResultsContextBuilder;
import com.poketrirx.aytosolver.models.utils.ContestantTupleUtils;
import com.poketrirx.aytosolver.processors.core.GuessEvaluator;
import com.poketrirx.aytosolver.processors.core.GuessFactory;

@Builder()
/**
 * A processor that'll test every possible match combination and return all possible match combinations that could still exist in the game.
 */
final class BruteForceProcessor implements Processor {
    @NonNull
    private final GuessFactory guessFactory;

    @NonNull
    private final GuessEvaluator guessEvaluator;

    public ResultsContext process(Data data) {
        ResultsContextBuilder resultsContextBuilder = ResultsContext.builder();

        while(true) {
            List<ContestantTuple> guess = guessFactory.build(data);

            if (guess == null) {
                break;
            }

            if (guessEvaluator.evaluateGuess(data, guess)) {
                processSuccessfulGuess(data, guess, resultsContextBuilder);

                break;
            }
        }

        return resultsContextBuilder.build();
    }

    private void processSuccessfulGuess(Data data, List<ContestantTuple> guess, ResultsContextBuilder builder) {
        //Build a list of known matches so we can correctly report what is known, and what is a guess.
        List<ContestantTuple> knownMatches = new ArrayList<ContestantTuple>();
        for(KnownMatchResult knownMatchResult : data.getKnownMatchResults()) {
            if (knownMatchResult.isMatch()) {
                knownMatches.add(knownMatchResult.getContestants());
            }
        }

        //if our guess is possible, save our guess and continue looking for more.
        List<KnownMatchResult> solution = new ArrayList<KnownMatchResult>(); 
        for(ContestantTuple entry : guess) {
            solution.add(
                KnownMatchResult.builder()
                    .contestants(entry)
                    .match(true)
                    .guess(!ContestantTupleUtils.isMatchInList(knownMatches, entry))
                    .build()
            );
        }

        builder.knownMatchResult(ImmutableList.copyOf(solution));
    }
}