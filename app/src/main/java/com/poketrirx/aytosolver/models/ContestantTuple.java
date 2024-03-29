/*
Copyright 2022 Nathan Mentley

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.poketrirx.aytosolver.models;

import java.util.Objects;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Builder(toBuilder=true)
@EqualsAndHashCode
@ToString(includeFieldNames=true)
/**
 * A POJO that contains a link between two Contestants
 */
public final class ContestantTuple {
    /**
     * The unique identifier of the first contestant.
     */
    @NonNull
    @Getter
    private final String contestant1Id;

    /**
     * The unique identifier of the second contestant.
     */
    @NonNull
    @Getter
    private final String contestant2Id;

    /**
     * Checks if this ContestantTuple is a match for another ContestantTuple
     * 
     * @param other The other ContestantTuple to evaluate against
     * @return True if the two Tuples match. If the two contestants are flipped it is still a match.
     */
    public boolean isMatch(ContestantTuple other) {
        if (Objects.isNull(other)) {
            return false;
        }

        return (
            (
                getContestant1Id().equals(other.getContestant1Id()) &&
                getContestant2Id().equals(other.getContestant2Id())
            ) || (
                getContestant1Id().equals(other.getContestant2Id()) &&
                getContestant2Id().equals(other.getContestant1Id())
            )
        );
    }
}