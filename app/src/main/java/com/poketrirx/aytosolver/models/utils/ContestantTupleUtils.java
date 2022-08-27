package com.poketrirx.aytosolver.models.utils;

import java.util.List;

import com.poketrirx.aytosolver.models.ContestantTuple;

public final class ContestantTupleUtils {
    //A helper method to just check if a specific match exists in a list of matches.
    public static boolean isMatchInList(List<ContestantTuple> haystack, ContestantTuple needle) {
        return haystack.stream().anyMatch(entry -> entry.isMatch(needle));
    }
}
