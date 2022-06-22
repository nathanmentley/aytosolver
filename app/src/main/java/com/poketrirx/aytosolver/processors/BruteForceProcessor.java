/*
Copyright 2022 Nathan Mentley

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.poketrirx.aytosolver.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.poketrirx.aytosolver.core.Processor;
import com.poketrirx.aytosolver.models.ContestantTuple;
import com.poketrirx.aytosolver.models.Data;
import com.poketrirx.aytosolver.models.KnownMatchResult;
import com.poketrirx.aytosolver.models.ResultsContext;
import com.poketrirx.aytosolver.models.ResultsContext.ResultsContextBuilder;

/**
 * A processor that'll test every possible match combination and return all possible match combinations that could still exist in the game.
 */
final class BruteForceProcessor implements Processor {
    private static final int WORKER_COUNT = 1;

	public class Worker implements Runnable {
        private final ResultsContextBuilder builder;
        private final GuessFactory guessFactory;
        private final GuessEvaluator guessEvaluator;
        private final Data data;

        private volatile static boolean stop = false;

		Worker(
            ResultsContextBuilder builder,
            GuessFactory guessFactory,
            GuessEvaluator guessEvaluator,
            Data data
        ) {
            this.builder = builder;
            this.guessFactory = guessFactory;
            this.guessEvaluator = guessEvaluator;
            this.data = data;
        }

        @Override
		public void run() {
            while(!stop) {
                List<ContestantTuple> guesses = guessFactory.build(data);
    
                if (guesses == null) {
                    break;
                }
    
                if (guessEvaluator.evaluateGuess(data, guesses)) {
                    processSuccessfulGuess(data, guesses, builder);

                    stop = true;
                }
            }
        }

        private synchronized void processSuccessfulGuess(Data data, List<ContestantTuple> guesses, ResultsContextBuilder builder) {
            //Build a list of known matches so we can correctly report what is known, and what is a guess.
            List<ContestantTuple> knownMatches = new ArrayList<ContestantTuple>();
            for(KnownMatchResult knownMatchResult : data.getKnownMatchResults()) {
                if (knownMatchResult.isMatch()) {
                    knownMatches.add(knownMatchResult.getContestants());
                }
            }

            //if our guess is possible, save our guess and continue looking for more.
            List<KnownMatchResult> solution = new ArrayList<KnownMatchResult>(); 
            for(ContestantTuple entry : guesses) {
                solution.add(
                    KnownMatchResult.builder()
                        .contestants(entry)
                        .match(true)
                        .guess(!ContestantTupleUtils.isMatchInList(knownMatches, entry))
                        .build()
                );
            }

            builder.knownMatchResult(solution);
        }
    }

    public ResultsContext process(Data data) {
        ResultsContextBuilder builder = ResultsContext.builder();

        GuessFactory guessFactory = new BasicGuessFactory(data);
        GuessEvaluator guessEvaluator = new BasicGuessEvaluator();

        ExecutorService executor = Executors.newFixedThreadPool(WORKER_COUNT);

        for (int i = 0; i < WORKER_COUNT; i++) {
			Runnable worker = new Worker(builder, guessFactory, guessEvaluator, data);

			executor.execute(worker);
		}

        executor.shutdown();
		
		while (!executor.isTerminated()) {}

        return builder.build();
    }
}