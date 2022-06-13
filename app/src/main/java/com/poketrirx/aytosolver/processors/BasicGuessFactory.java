/*
Copyright 2022 Nathan Mentley

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.poketrirx.aytosolver.processors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.poketrirx.aytosolver.models.Contestant;
import com.poketrirx.aytosolver.models.ContestantTuple;
import com.poketrirx.aytosolver.models.Data;

/* TODO: THIS CURRENTLY ONLY SUPPORTS EXACTLY GROUPS OF 10 CONTESTANTS */
final class BasicGuessFactory implements GuessFactory {
    private final List<String> men;
    private final List<String> women;

    private long guess = 0L; //Pretty hacky, but we're gonna use a ten digit number.
        // the index of each number represents the index of the man in our men array
        // the value of the digit at that index represents the matching women.
        // If we process 0 through 10 billion that'll be every possible guess
        // including invalid guessses where we match the same person multiple times.
        // So, we'll need to make sure we filter those out.

    public BasicGuessFactory(Data data) {
        List<String> men = new ArrayList<String>();
        List<String> women = new ArrayList<String>();

        for(Contestant contestant : data.getContestants()) {
            if ("M".equals(contestant.getGroup())) {
                men.add(contestant.getId());
            } else {
                women.add(contestant.getId());
            }
        }

        if (women.size() != 10 || men.size() != 10) {
            throw new RuntimeException("Both contestant groups must be 10 in size.");
        }

        this.men = men;
        this.women = women;
    }

    /**
     * takes our 10 digit number representing a guess, and builds a list of contestant tuples that represents our matches.
     * 
     * If the guess number is too large, we'll return null, and exit the processor.
     * If the guess would be invalid because of a single person being matched to multiple people, we'll return an empty
     *  list, and continue on to the next guess.
     */
    public List<ContestantTuple> build(Data data) {
        while(true) {
            guess++;

            if (guess > 9999999999L) {
                return null;
            }

            if (countRepeatingDigits(guess) > 1) {
                continue;
            }

            List<ContestantTuple> result = new ArrayList<ContestantTuple>();

            int manIndex = 0;
            for(String man : men) {
                result.add(
                    ContestantTuple.builder()
                        .contestant1Id(man)
                        .contestant2Id(women.get(getWomanIndex(guess, manIndex)))
                        .build()
                );

                manIndex++;
            }

            return result;
        }
    }

    private int getWomanIndex(long guess, int manIndex) {
        int divider = (int)Math.pow(10, manIndex);

        return (int)((guess / divider) % 10);
    }

    private static int countRepeatingDigits(long N)
    {
        // Initialize a variable to store
        // count of Repeating digits
        int res = 0;
    
        // Initialize cnt array to
        // store digit count
    
        int cnt[] = new int[10];
    
        // Iterate through the digits of N
        while (N > 0)
        {
        
            // Retrieve the last digit of N
            int rem = (int)(N % 10);
        
            // Increase the count of digit
            cnt[rem]++;
        
            // Remove the last digit of N
            N = N / 10;
        }
    
        // Iterate through the cnt array
        for (int i = 0; i < 10; i++)
        {
        
            // If frequency of digit
            // is greater than 1
            if (cnt[i] > 1)
            {
            
                // Increment the count
                // of Repeating digits
                res++;
            }
        }
    
        // Return count of repeating digit
        return res;
    }
}
