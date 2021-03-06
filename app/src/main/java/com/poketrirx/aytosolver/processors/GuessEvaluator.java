package com.poketrirx.aytosolver.processors;

import java.util.List;

import com.poketrirx.aytosolver.models.ContestantTuple;
import com.poketrirx.aytosolver.models.Data;

interface GuessEvaluator {
    boolean evaluateGuess(Data data, List<ContestantTuple> guess);
}
