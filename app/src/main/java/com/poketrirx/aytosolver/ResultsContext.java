/*
Copyright 2022 Nathan Mentley

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.poketrirx.aytosolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import com.poketrirx.aytosolver.models.Contestant;
import com.poketrirx.aytosolver.models.ContestantTuple;
import com.poketrirx.aytosolver.models.Data;
import com.poketrirx.aytosolver.models.EpisodeResult;
import com.poketrirx.aytosolver.models.KnownMatchResult;

@EqualsAndHashCode
@ToString(includeFieldNames=true)
/**
 * A POJO that holds the current state and progress of the solution.
 */
public class ResultsContext {
    @NonNull @Getter private Map<String, Map<String, Boolean>> data;
    @NonNull @Getter private List<EpisodeResult> episodeResults;

    /**
     * Constructor
     */
    public ResultsContext(Data rawData) {
        episodeResults = new ArrayList<EpisodeResult>();
        data = new HashMap<String, Map<String, Boolean>>();

        for (Contestant contestant : rawData.getContestants()) {
            data.put(contestant.getId(), new HashMap<String, Boolean>());

            for (Contestant matchContestant : rawData.getContestants()) {
                Boolean initialValue = matchContestant.getId().equals(contestant.getId()) ? false: null;

                data.get(contestant.getId()).put(matchContestant.getId(), initialValue);
            }
        }

        for (KnownMatchResult result : rawData.getKnownMatchResults()) {
            addKnownMatchResult(result);
        }

        for (EpisodeResult result: rawData.getEpisodeResults()) {
            episodeResults.add(cloneEpisodeResult(result));
        }
    }

    public void addKnownMatchResult(KnownMatchResult result) {
        data.get(result.getContestants().getContestant1Id())
            .put(result.getContestants().getContestant2Id(), result.isMatch());

        data.get(result.getContestants().getContestant2Id())
            .put(result.getContestants().getContestant1Id(), result.isMatch());
    }

    /**
     * Fetches the id of a contestant that is a match of the passed contestant id. If no match is known null is returned.
     */
    public String getMatch(String contestantId) {
        for (Map.Entry<String, Boolean> entry : data.get(contestantId).entrySet()) {
            Boolean isMatch = entry.getValue();

            if (isMatch != null && isMatch == true) {
                return entry.getKey();
            }
        }

        return null;
    }

    /**
     * Checks if two contestants are matched or not. If matched, returns true, if known not to be matched false, if unkonwn null.
     */
    public Boolean isMatch(String contestant1Id, String contestant2Id) {
        return data.get(contestant1Id).get(contestant2Id);
    }

    private EpisodeResult cloneEpisodeResult(EpisodeResult result) {
        ArrayList<ContestantTuple> clone = new ArrayList<>();

        Iterator<ContestantTuple> iterator = result.getContestants().iterator();

        while(iterator.hasNext())
        {
            clone.add(iterator.next().toBuilder().build());  
        }

        return result.toBuilder().contestants(clone).build();
    }
}