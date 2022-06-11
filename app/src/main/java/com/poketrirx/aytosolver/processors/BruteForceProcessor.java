/*
Copyright 2022 Nathan Mentley

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.poketrirx.aytosolver.processors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.poketrirx.aytosolver.models.ContestantTuple;
import com.poketrirx.aytosolver.models.Data;
import com.poketrirx.aytosolver.models.EpisodeResult;
import com.poketrirx.aytosolver.models.KnownMatchResult;
import com.poketrirx.aytosolver.ResultsContext;

/**
 * A processor that'll use a list of steps to attempt to solve the matches.
 */
public final class BruteForceProcessor implements Processor {
   private static final List<String> men = Arrays.asList(
        "asaf",
        "cam",
        "cameron",
        "gio",
        "john",
        "morgan",
        "prosper",
        "sam",
        "stephen",
        "tyler"
    );
    private static final List<String> women = Arrays.asList(
        "alyssa",
        "camile",
        "emma",
        "francesca",
        "julia",
        "kaylen",
        "mikala",
        "nicole",
        "tori",
        "victoria"
    );

    public void process(Data data, ResultsContext context) {
        boolean solved = false;

        long guess = 0;
        while(true) {
            List<ContestantTuple> guesses = buildNextGuess(data, guess++);

            if (guesses == null) {
                break;
            }
            if (guesses.size() == 0) {
                continue;
            }

            solved = evaluateGuess(data, guesses);

            if (solved) {
                //if our guess is possible, save our guess and exit the loop.
                for(ContestantTuple entry : guesses) {
                    context.addKnownMatchResult(
                        KnownMatchResult.builder()
                            .contestants(entry)
                            .match(true)
                            .build()
                    );
                }

                break;
            }
        }
    }

    private List<ContestantTuple> buildNextGuess(Data data, long guess) {
        if (guess > 9999999999L) {
            return null;
        }
        if (guess % 100000000 == 0) {
            System.out.println(String.format("working %d", guess));
        }

        List<ContestantTuple> result = new ArrayList<ContestantTuple>();
        List<String> matchedWomen = new ArrayList<String>();

        int manIndex = 0;
        for(String man : men) {
            String woman = women.get(getWomanIndex(guess, manIndex));

            if (matchedWomen.contains(woman)) {
                //If we double match someone, we should just skip this run.
                return new ArrayList<ContestantTuple>();
            }

            matchedWomen.add(woman);

            result.add(
                ContestantTuple.builder()
                    .contestant1Id(man)
                    .contestant2Id(woman)
                    .build()
            );

            manIndex++;
        }

        return result;
    }

    private int getWomanIndex(long guess, int manIndex) {
        int divider = (int)Math.pow(10, manIndex);

        return (int)((guess / divider) % 10);
    }

    private boolean evaluateGuess(Data data, List<ContestantTuple> guess) {
        //foreach of our known results
        for (KnownMatchResult knownMatchResult : data.getKnownMatchResults()) {
            //if our guess doesn't match these, we can bail out early.

            if (knownMatchResult.isMatch()) {
                if (!isMatchInList(guess, knownMatchResult.getContestants())) {
                    //System.out.println("A known match wasn't guessed.");

                    return false;
                }
            } else {
                if (isMatchInList(guess, knownMatchResult.getContestants())) {
                    //System.out.println("A known mismatch was guessed.");

                    return false;
                }
            }
        }

        //foreach episode result
        for (EpisodeResult episodeResult : data.getEpisodeResults()) {
            //count how many couples are a match if our guess was correct.
            int targetCorrect = episodeResult.getTotalCorrect();
            for(ContestantTuple guessEntry: guess) {
                if (isMatchInList(episodeResult.getContestants(), guessEntry)) {
                    targetCorrect--; //for each correct decrement the target correct count.
                }
            }

            //if our guess doesn't match how many correct guesses we expect, bail out as a failure.
            if (targetCorrect != 0) {
                //System.out.println("An episode result doesn't have the expected correct count.");

                return false;
            }
        }

        return true;
    }

    private boolean isMatchInList(List<ContestantTuple> episodeGuesses, ContestantTuple correctMatch) {
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