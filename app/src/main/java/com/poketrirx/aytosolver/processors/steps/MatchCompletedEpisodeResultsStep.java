/*
Copyright 2022 Nathan Mentley

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.poketrirx.aytosolver.processors.steps;

import com.poketrirx.aytosolver.models.Data;
import com.poketrirx.aytosolver.models.ContestantTuple;
import com.poketrirx.aytosolver.models.EpisodeResult;
import com.poketrirx.aytosolver.models.KnownMatchResult;
import com.poketrirx.aytosolver.ResultsContext;

/**
 * A step that'll check each episode result, and mark all matches if all remaining tuples must be correct.
 */
public final class MatchCompletedEpisodeResultsStep extends Step {
    /**
     * Gets the name of the step.
     * 
     * @return The name of the step.
     */
    @Override public String getName() {
        return "Match Completed Episode Results";
    }

    /**
     * Processes the step's logic.
     * 
     * @param context   The currently processed context.
     * @return A boolean that if true, means some progress was made in this step.
     */
    @Override public boolean process(Data data, ResultsContext context) {
        boolean changesMade = false;

        for(EpisodeResult episodeResult: context.getEpisodeResults()) {
            if (episodeResult.getTotalCorrect() == episodeResult.getContestants().size()) {
                for(ContestantTuple tuple: episodeResult.getContestants()) {
                    Boolean isMatch = context.isMatch(tuple.getContestant1Id(), tuple.getContestant2Id());

                    if (isMatch == null) {
                        context.addKnownMatchResult(
                            KnownMatchResult.builder()
                                .contestants(tuple)
                                .match(true)
                                .build()
                        );

                        changesMade = true;
                    } else if (isMatch == false) {
                        throw new RuntimeException("impossible operation");
                    }
                }
            }
        }

        return changesMade;
    }
}