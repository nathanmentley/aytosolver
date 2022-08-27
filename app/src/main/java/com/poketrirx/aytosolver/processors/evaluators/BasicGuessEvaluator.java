package com.poketrirx.aytosolver.processors.evaluators;

import java.util.List;

import com.poketrirx.aytosolver.models.ContestantTuple;
import com.poketrirx.aytosolver.models.Data;
import com.poketrirx.aytosolver.models.EpisodeResult;
import com.poketrirx.aytosolver.models.KnownMatchResult;
import com.poketrirx.aytosolver.models.utils.ContestantTupleUtils;
import com.poketrirx.aytosolver.processors.core.GuessEvaluator;

final class BasicGuessEvaluator implements GuessEvaluator {
    //Process the guess data to see if it's actually valid or not.
    public boolean evaluateGuess(Data data, List<ContestantTuple> guess) {
        if (!doesGuessFitKnownMatches(data, guess)) {
            return false;
        }
        
        if (!doesGuessFitEpisodeResults(data, guess)) {
            return false;
        }
        
        return true;
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
