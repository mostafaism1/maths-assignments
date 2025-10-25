# DFA (Deterministic Finite Automaton) Library

A generic Java implementation of a Deterministic Finite Automaton that provides a type-safe API for building and testing finite state machines. This library uses Java enums for states and symbols, ensuring compile-time type safety and clean, readable code.

## Features

- **Generic Implementation**: Supports any enum-based states and symbols
- **Type-Safe API**: Uses Java generics for compile-time safety
- **Simple Builder Pattern**: Easy DFA construction with fluent interface

## Requirements

- Java 21
- Maven 3.x

## Installation

Extract the project files and build using Maven:

```bash
mvn clean install
```

## Usage Examples

### Example 1: Even-Length String Recognizer

This example demonstrates how to create a DFA that accepts strings of even length:

```java
// Define states and symbols as enums
enum ParityState { EVEN, ODD }
enum BinarySymbol { ZERO, ONE }

// Create transition function
BiFunction<ParityState, BinarySymbol, ParityState> transitionFunction = 
    (state, symbol) -> {
        switch (state) {
            case EVEN: return ParityState.ODD;
            case ODD: return ParityState.EVEN;
            default: throw new IllegalArgumentException("Invalid state: " + state);
        }
    };

// Build the DFA
Dfa<ParityState, BinarySymbol> dfa = new DfaImpl<ParityState, BinarySymbol>().build(
    Set.of(ParityState.EVEN, ParityState.ODD),  // states
    Set.of(BinarySymbol.ZERO, BinarySymbol.ONE), // alphabet
    transitionFunction,                          // transition function
    ParityState.EVEN,                           // start state
    Set.of(ParityState.EVEN)                    // accept states
);

// Test the DFA
List<BinarySymbol> input = Arrays.asList(BinarySymbol.ZERO, BinarySymbol.ONE);
boolean accepts = dfa.accepts(input); // returns true (even length)
```

### Example 2: String Ending with "01" Pattern

This example shows a DFA that accepts strings ending with the pattern "01":

```java
// Define states for pattern matching
enum PatternState { 
    START,      // Initial state
    SEEN_ZERO,  // Seen '0'
    SEEN_ONE    // Seen '01' pattern
}

enum InputSymbol { ZERO, ONE }

// Create transition function for "01" pattern
BiFunction<PatternState, InputSymbol, PatternState> patternTransition = 
    (state, symbol) -> {
        switch (state) {
            case START:
                return symbol == InputSymbol.ZERO ? PatternState.SEEN_ZERO : PatternState.START;
            case SEEN_ZERO:
                return symbol == InputSymbol.ONE ? PatternState.SEEN_ONE : 
                       symbol == InputSymbol.ZERO ? PatternState.SEEN_ZERO : PatternState.START;
            case SEEN_ONE:
                return symbol == InputSymbol.ZERO ? PatternState.SEEN_ZERO : PatternState.START;
            default:
                throw new IllegalArgumentException("Invalid state: " + state);
        }
    };

// Build the DFA
Dfa<PatternState, InputSymbol> patternDfa = new DfaImpl<PatternState, InputSymbol>().build(
    Set.of(PatternState.START, PatternState.SEEN_ZERO, PatternState.SEEN_ONE),
    Set.of(InputSymbol.ZERO, InputSymbol.ONE),
    patternTransition,
    PatternState.START,
    Set.of(PatternState.SEEN_ONE) // Accept when we've seen "01"
);

// Test the DFA
List<InputSymbol> testInput = Arrays.asList(InputSymbol.ZERO, InputSymbol.ONE);
boolean accepts = patternDfa.accepts(testInput); // returns true
```

## API Reference

### Dfa Interface

The main interface for working with DFAs:

```java
public interface Dfa<STATE extends Enum<STATE>, SYMBOL extends Enum<SYMBOL>> {
    Dfa<?, ?> build(
        Set<STATE> states, 
        Set<SYMBOL> alphabet,
        BiFunction<STATE, SYMBOL, STATE> transitionFunction, 
        STATE startState,
        Set<STATE> acceptStates
    );
    
    boolean accepts(List<SYMBOL> input);
}
```

### Key Methods

- **`build(...)`**: Constructs a DFA with the specified components
  - `states`: Set of all possible states
  - `alphabet`: Set of all possible input symbols
  - `transitionFunction`: Function defining state transitions
  - `startState`: Initial state
  - `acceptStates`: Set of accepting states

- **`accepts(List<SYMBOL> input)`**: Tests whether the DFA accepts the given input sequence

## Project Structure

```
src/
├── main/java/com/github/mostafaism1/
│   └── dfa/
│       ├── Dfa.java          # Main interface
│       └── DfaImpl.java      # Implementation
└── test/java/com/github/mostafaism1/
    └── dfa/
        └── DfaTest.java      # Test examples
```

## Testing

Run the test suite using Maven:

```bash
mvn test
```

The project uses JUnit 5 for testing. See `DfaTest.java` for comprehensive test examples including the even-length string recognizer and additional test cases.
