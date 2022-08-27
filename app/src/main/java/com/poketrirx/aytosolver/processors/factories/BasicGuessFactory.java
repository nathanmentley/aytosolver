/*
Copyright 2022 Nathan Mentley

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.poketrirx.aytosolver.processors.factories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.poketrirx.aytosolver.models.Contestant;
import com.poketrirx.aytosolver.models.ContestantTuple;
import com.poketrirx.aytosolver.models.Data;
import com.poketrirx.aytosolver.models.KnownMatchResult;
import com.poketrirx.aytosolver.processors.core.GuessFactory;

/* TODO: THIS CURRENTLY ONLY SUPPORTS EXACTLY GROUPS OF 10 CONTESTANTS */
final class BasicGuessFactory implements GuessFactory {
    private boolean isDataSet = false;
    private List<String> men;
    private List<String> women;

    private static final int CONTESTANTS_COUNT = 10;
    private volatile int[] guessArray = new int[CONTESTANTS_COUNT];
    private Map<Integer, Integer> knownMatches = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> knownMismatches = new HashMap<Integer, Integer>();

    /**
     * takes our 10 digit number representing a guess, and builds a list of contestant tuples that represents our matches.
     * 
     * If the guess number is too large, we'll return null, and exit the processor.
     * If the guess would be invalid because of a single person being matched to multiple people, we'll return an empty
     *  list, and continue on to the next guess.
     */
    public List<ContestantTuple> build(Data data) {
        setData(data);

        while(true) {
            int[] thisGuess = incrementGuess();

            if (thisGuess == null) {
                return null;
            }

            if (doesGuessHaveDupes(thisGuess) || doesGuessNotMatchKnownMatchResults(thisGuess)) {
                continue;
            }

            List<ContestantTuple> result = new ArrayList<ContestantTuple>();

            int manIndex = 0;
            for(String man : men) {
                result.add(
                    ContestantTuple.builder()
                        .contestant1Id(man)
                        .contestant2Id(women.get(thisGuess[manIndex]))
                        .build()
                );

                manIndex++;
            }

            return result;
        }
    }

    private synchronized void setData(Data data) {
        if (isDataSet) {
            return;
        }

        List<String> men = new ArrayList<String>();
        List<String> women = new ArrayList<String>();

        for(Contestant contestant : data.getContestants()) {
            if ("M".equals(contestant.getGroup())) {
                men.add(contestant.getId());
            } else {
                women.add(contestant.getId());
            }
        }

        if (women.size() != CONTESTANTS_COUNT || men.size() != CONTESTANTS_COUNT) {
            throw new RuntimeException("Both contestant groups must be 10 in size.");
        }

        this.men = men;
        this.women = women;

        for(KnownMatchResult knownMatchResult : data.getKnownMatchResults()) {
            int maleId = getMaleId(knownMatchResult.getContestants());
            int femaleId = getFemaleId(knownMatchResult.getContestants());

            if (knownMatchResult.isMatch()) {
                knownMatches.put(maleId, femaleId);
            } else {
                knownMismatches.put(maleId, femaleId);
            }
        }

        isDataSet = true;
    }

    private synchronized int[] incrementGuess() {
        for(int i = 0; i < CONTESTANTS_COUNT; i++) {
            guessArray[i]++;

            if (guessArray[i] < CONTESTANTS_COUNT) {
                break;
            } else {
                guessArray[i] = 0;                   
            }
        }

        if (
            guessArray[0] == 0 &&
            guessArray[1] == 0 &&
            guessArray[2] == 0 &&
            guessArray[3] == 0 &&
            guessArray[4] == 0 &&
            guessArray[5] == 0 &&
            guessArray[6] == 0 &&
            guessArray[7] == 0 &&
            guessArray[8] == 0 &&
            guessArray[9] == 0
        ) {
            return null;
        }

        int[] ret = new int[CONTESTANTS_COUNT];

        for(int i = 0; i < CONTESTANTS_COUNT; i++) {
            ret[i] = guessArray[i];
        }

        return ret;
    }

    private static boolean doesGuessHaveDupes(int[] thisGuess) {
        HashSet<Integer> set = new HashSet<Integer>();
        for (int name : thisGuess) {
            if (set.add(name) == false) {
                return true;
            }
        }

        return false;
    }

    private boolean doesGuessNotMatchKnownMatchResults(int[] thisGuess) {
        for (Map.Entry<Integer, Integer> entry : knownMatches.entrySet()) {
            if (thisGuess[entry.getKey()] != entry.getValue()) {
                return true;
            }
        }

        for (Map.Entry<Integer, Integer> entry : knownMismatches.entrySet()) {
            if (thisGuess[entry.getKey()] == entry.getValue()) {
                return true;
            }
        }

        return false;
    }

    private int getMaleId(ContestantTuple contestantTuple) {
        if (men.contains(contestantTuple.getContestant1Id())) {
            return men.indexOf(contestantTuple.getContestant1Id());
        } else if (men.contains(contestantTuple.getContestant2Id())) {
            return men.indexOf(contestantTuple.getContestant2Id());
        }

        return 1;
    }

    private int getFemaleId(ContestantTuple contestantTuple) {
        if (women.contains(contestantTuple.getContestant1Id())) {
            return women.indexOf(contestantTuple.getContestant1Id());
        } else if (women.contains(contestantTuple.getContestant2Id())) {
            return women.indexOf(contestantTuple.getContestant2Id());
        }

        return 1;
    }
}
