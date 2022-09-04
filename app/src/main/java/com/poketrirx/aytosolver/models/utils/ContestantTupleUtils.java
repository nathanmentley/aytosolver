/*
Copyright 2022 Nathan Mentley

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.poketrirx.aytosolver.models.utils;

import java.util.List;
import java.util.Objects;

import com.poketrirx.aytosolver.models.ContestantTuple;

/**
 * A support class to help with operating on ContestantTuples
 */
public final class ContestantTupleUtils {
    /**
     * A helper method to just check if a specific match exists in a list of matches.
     * 
     * @param haystack A collection of ContestantTuples to evaluate
     * @param needle A ContestantTuple to search for.
     * @return True, if the tuple exists in the haystack collection.
     */
    public static boolean isMatchInList(List<ContestantTuple> haystack, ContestantTuple needle) {
        Objects.requireNonNull(haystack);
        Objects.requireNonNull(needle);

        return haystack.stream().anyMatch(entry -> entry.isMatch(needle));
    }
}
