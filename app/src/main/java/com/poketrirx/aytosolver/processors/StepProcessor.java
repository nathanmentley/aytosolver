/*
Copyright 2022 Nathan Mentley

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.poketrirx.aytosolver.processors;

import java.util.List;

import com.poketrirx.aytosolver.processors.steps.Step;
import com.poketrirx.aytosolver.ResultsContext;

/**
 * A processor that'll use a list of steps to attempt to solve the matches.
 */
public class StepProcessor implements Processor {
    private List<Step> steps;

    /**
     * Constructor
     * 
     * 
     */
    public StepProcessor(List<Step> steps) {
        this.steps = steps;
    }

    /**
     * Analyzes a result context to attempt to solve all the matches.
     */
    public void process(ResultsContext context) {
        boolean changesMade;

        do {
            changesMade = false;

            for(Step step : steps) {
                changesMade |= step.process(context);
            }
        }
        while (changesMade);
    }
}