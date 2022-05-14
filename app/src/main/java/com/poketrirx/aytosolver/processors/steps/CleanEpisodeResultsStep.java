/*
Copyright 2022 Nathan Mentley

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.poketrirx.aytosolver.processors.steps;

import java.util.ArrayList;

import com.poketrirx.aytosolver.models.ContestantTuple;
import com.poketrirx.aytosolver.models.EpisodeResult;
import com.poketrirx.aytosolver.ResultsContext;

/**
 * A step that'll clean episode results by removing known matches or non matches from the results.
 */
public final class CleanEpisodeResultsStep extends Step {
    /**
     * Gets the name of the step.
     * 
     * @return The name of the step.
     */
    @Override public String getName() {
        return "Clean Episode Results";
    }

    /**
     * Processes the step's logic.
     * 
     * @param context   The currently processed context.
     * @return A boolean that if true, means some progress was made in this step.
     */
    @Override public boolean process(ResultsContext context) {
        boolean changesMade = false;

        for(EpisodeResult episodeResult: context.getEpisodeResults()) {
            for(ContestantTuple tuple: new ArrayList<ContestantTuple>(episodeResult.getContestants())) {
                String match = context.getMatch(tuple.getContestant1Id());

                if (match != null) {
                    //if there is a match we should clean up the episode result.
                    if (match.equals(tuple.getContestant2Id())) {
                        //if the match is correct lets remove it.
                        episodeResult.removeMatch(tuple.getContestant1Id(), tuple.getContestant2Id());
                    } else {
                        //if we know the match is wrong, we can clear out both.
                        episodeResult.removeNonMatch(tuple.getContestant1Id());
                        episodeResult.removeNonMatch(tuple.getContestant2Id());
                    }

                    changesMade = true;
                }

                if (context.getMatch(tuple.getContestant2Id()) != null) {
                    //if we know the match is wrong, we can clear out both.
                    episodeResult.removeNonMatch(tuple.getContestant1Id());
                    episodeResult.removeNonMatch(tuple.getContestant2Id());

                    changesMade = true;
                }
            }
        }

        return changesMade;
    }
}