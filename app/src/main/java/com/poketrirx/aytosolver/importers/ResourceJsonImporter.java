/*
Copyright 2022 Nathan Mentley

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.poketrirx.aytosolver.importers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.google.common.io.Resources;
import com.google.gson.GsonBuilder;

import com.poketrirx.aytosolver.core.Importer;
import com.poketrirx.aytosolver.models.Data;

/**
 * An Importer that'll load data from a json file that's bundled as a resource named data.json
 */
final class ResourceJsonImporter implements Importer {
    /**
     * Loads the input data from a data source.
     * 
     * @Returns The input data.
     */
    public Data load() {
        try {
            String json = Resources.toString(Resources.getResource("data.json"), StandardCharsets.UTF_8);

            return new GsonBuilder().create().fromJson(json, Data.class);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}