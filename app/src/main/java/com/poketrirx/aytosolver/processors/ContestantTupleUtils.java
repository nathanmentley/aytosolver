package com.poketrirx.aytosolver.processors;

import java.util.List;

import com.poketrirx.aytosolver.models.ContestantTuple;

final class ContestantTupleUtils {
    //A helper method to just check if a specific match exists in a list of matches.
    public static boolean isMatchInList(List<ContestantTuple> episodeGuesses, ContestantTuple correctMatch) {
        for(ContestantTuple episodeGuess : episodeGuesses) {
            if (
                episodeGuess.getContestant1Id().equals(correctMatch.getContestant1Id()) &&
                episodeGuess.getContestant2Id().equals(correctMatch.getContestant2Id())
            ) {
                return true;
            }

            if (
                episodeGuess.getContestant1Id().equals(correctMatch.getContestant2Id()) &&
                episodeGuess.getContestant2Id().equals(correctMatch.getContestant1Id())
            ) {
                return true;
            }
        }

        return false;
    }
}
