/*
Copyright 2022 Nathan Mentley

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.poketrirx.aytosolver.impl.basicguessevaluator;

import java.util.List;
import javax.inject.Inject;

import com.poketrirx.aytosolver.core.GuessEvaluator;
import com.poketrirx.aytosolver.models.ContestantTuple;
import com.poketrirx.aytosolver.models.Data;
import com.poketrirx.aytosolver.models.EpisodeResult;
import com.poketrirx.aytosolver.models.KnownMatchResult;
import com.poketrirx.aytosolver.models.utils.ContestantTupleUtils;

public final class BasicGuessEvaluator implements GuessEvaluator {
    @Inject
    public BasicGuessEvaluator() {}

    //Process the guess data to see if it's actually valid or not.
    public boolean evaluateGuess(Data data, List<ContestantTuple> guess) {
        return doesGuessFitKnownMatches(data, guess) && doesGuessFitEpisodeResults(data, guess);
    }

    private static boolean doesGuessFitKnownMatches(Data data, List<ContestantTuple> guess) {
        //foreach of our known results
        for (KnownMatchResult knownMatchResult : data.getKnownMatchResults()) {
            //if our guess doesn't fit our known matches and mismatches, we can bail out early.
            boolean isInList = ContestantTupleUtils.isMatchInList(guess, knownMatchResult.getContestants());

            if (knownMatchResult.isMatch() != isInList) {
                return false;
            }
        }

        //If we make it here, we know the guesses matches all of our already known results.
        return true;
    }

    private static boolean doesGuessFitEpisodeResults(Data data, List<ContestantTuple> guess) {
        //foreach episode result
        for (EpisodeResult episodeResult : data.getEpisodeResults()) {
            //count how many couples are a match if our guess was correct.
            int targetCorrect = episodeResult.getTotalCorrect();

            for(ContestantTuple guessEntry: guess) {
                if (ContestantTupleUtils.isMatchInList(episodeResult.getContestants(), guessEntry)) {
                    //for each correct decrement the target correct count.
                    if (--targetCorrect < 0) {
                        break; // if We're below zero we can exit early
                    }
                }
            }

            //if our guess doesn't match how many correct guesses we expect, bail out as a failure.
            if (targetCorrect != 0) {
                return false;
            }
        }

        return true;
    }
}
