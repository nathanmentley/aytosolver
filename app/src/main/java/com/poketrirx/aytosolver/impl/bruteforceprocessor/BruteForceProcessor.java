/*
Copyright 2022 Nathan Mentley

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.poketrirx.aytosolver.impl.bruteforceprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;

import com.google.common.collect.ImmutableList;

import com.poketrirx.aytosolver.core.GuessEvaluator;
import com.poketrirx.aytosolver.core.GuessFactory;
import com.poketrirx.aytosolver.core.Processor;
import com.poketrirx.aytosolver.models.ContestantTuple;
import com.poketrirx.aytosolver.models.Data;
import com.poketrirx.aytosolver.models.KnownMatchResult;
import com.poketrirx.aytosolver.models.ResultsContext;
import com.poketrirx.aytosolver.models.ResultsContext.ResultsContextBuilder;
import com.poketrirx.aytosolver.models.utils.ContestantTupleUtils;

/**
 * A processor that'll test every possible match combination and return all possible match combinations that could still exist in the game.
 */
public final class BruteForceProcessor implements Processor {
    private final GuessFactory guessFactory;
    private final GuessEvaluator guessEvaluator;

    @Inject
    public BruteForceProcessor(
        GuessFactory guessFactory,
        GuessEvaluator guessEvaluator
    ) {
        this.guessFactory = Objects.requireNonNull(guessFactory);
        this.guessEvaluator = Objects.requireNonNull(guessEvaluator);
    }

    public ResultsContext process(Data data) {
        ResultsContextBuilder resultsContextBuilder = ResultsContext.builder();

        while(true) { //While we still have guesses
            //Use the factory to select the next guess
            List<ContestantTuple> guess = guessFactory.build(data);

            if (guess == null) { // if we get null back we're out of guesses.
                break;  //so lets bail and report what we found.
            }

            if (guessEvaluator.evaluateGuess(data, guess)) { //if we have a guess lets see if it's valid
                processSuccessfulGuess(data, guess, resultsContextBuilder); //If it is lets store the result.

                break; //Lets bail on the first guess. No reason to find all right now.
            }
        }

        //Build the results with all possible guesses we found.
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