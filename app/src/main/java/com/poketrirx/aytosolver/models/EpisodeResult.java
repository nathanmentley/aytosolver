/*
Copyright 2022 Nathan Mentley

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.poketrirx.aytosolver.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Builder(toBuilder=true)
@EqualsAndHashCode
@ToString(includeFieldNames=true)
@AllArgsConstructor
@RequiredArgsConstructor
/**
 * A POJO that contains results from an episode.
 */
public final class EpisodeResult {
    /**
     * All contestant matches in the result.
     */
    @NonNull @Getter private List<ContestantTuple> contestants;

    /**
     * The total number of correct matches.
     */
    @Getter private int totalCorrect;

    public void removeMatch(String id1, String id2) {
        totalCorrect--;

        contestants.remove(
            ContestantTuple.builder()
                .contestant1Id(id1)
                .contestant2Id(id2)
                .build()
        );

        contestants.remove(
            ContestantTuple.builder()
                .contestant1Id(id2)
                .contestant2Id(id1)
                .build()
        );
    }

    public void removeNonMatch(String id) {
        for(int i = 0; i < contestants.size(); i++) {
            if (
                contestants.get(i).getContestant1Id().equals(id) ||
                contestants.get(i).getContestant2Id().equals(id)
            ) {
                contestants.remove(i);
                
                break;
            }
        }
    }
}