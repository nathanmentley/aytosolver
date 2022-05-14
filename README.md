# Are you the one solver

Takes in data from a ridiculous reality tv show, and attempts to solve the correct matches.

## running

update the data in src/main/resources/data.json

run: gradle run

## Updating the solving logic.

Add classes that inherit from com.poketrirx.aytosolver.processors.steps.Step.

Include those new step classes in the constructor of com.poketrirx.aytosolver.processors.StepProcessor.

## Updating the input data.

The input data is pulled from a bundled json file located at src/main/resources/data.json