/*
Copyright 2022 Nathan Mentley

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.poketrirx.aytosolver.processors.steps;

import com.poketrirx.aytosolver.models.Data;
import com.poketrirx.aytosolver.ResultsContext;

/**
 * An atomic step that can be used to solve the matches.
 */
public abstract class Step {
    /**
     * Gets the name of the step.
     * 
     * @return The name of the step.
     */
    public abstract String getName();

    /**
     * Processes the step's logic.
     * 
     * @param data      The raw immutable input data for inspection.
     * @param context   The ResultsContext to inspect and mutate in the step.
     * @return          A boolean that if true, means some progress was made in this step.
     */
    public abstract boolean process(Data data, ResultsContext context);
}