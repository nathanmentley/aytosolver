# Are you the one solver

Takes in data from a ridiculous reality tv show, and attempts to solve the correct matches.

## Running

### Dependancies

Install openjdk: https://formulae.brew.sh/formula/openjdk
Install gradle: https://formulae.brew.sh/formula/gradle

### Executing

update the data in src/main/resources/data.json

run: gradle run in the repository root.

## Updating the solving logic.

The current solving algorithm is extremely simple. This program is designed to make it easy for anyone to expand and extend.

You can add more intellegence to it by:

1. Adding classes that inherit from com.poketrirx.aytosolver.processors.steps.Step. These classes should contain any new logic you want to run to help solve the problem.
2. Include those new step classes in the constructor of com.poketrirx.aytosolver.processors.StepProcessor, so they'll be executed when running the processing loop.