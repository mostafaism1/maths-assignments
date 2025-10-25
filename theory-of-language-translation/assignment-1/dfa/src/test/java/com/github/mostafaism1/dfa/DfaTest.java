package com.github.mostafaism1.dfa;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DfaTest {

  private Dfa<ParityState, BinarySymbol> dfa;

  @BeforeEach
  public void setUp() {
    Set<ParityState> states = new HashSet<ParityState>(Arrays.asList(ParityState.values()));
    Set<BinarySymbol> alphabet = new HashSet<BinarySymbol>(Arrays.asList(BinarySymbol.values()));
    dfa = new DfaImpl<ParityState, BinarySymbol>().build(
        states,
        alphabet,
        transitionFunction,
        ParityState.EVEN, 
        Set.of(ParityState.EVEN));
  }

  @Test
  public void givenAnEvenStringDfa_whenInputLengthIsTwo_thenAccepts() {
    // Given.
    List<BinarySymbol> inputString = Arrays.asList(BinarySymbol.ZERO, BinarySymbol.ONE);

    // When.
    boolean accepts = dfa.accepts(inputString);

    // Then.
    assertTrue(accepts);
  }

  @Test
  public void givenAnEvenStringDfa_whenInputLengthIsThree_thenRejects() {
    // Given.
    List<BinarySymbol> inputString = Arrays.asList(BinarySymbol.ZERO, BinarySymbol.ONE, BinarySymbol.ZERO);

    // When.
    boolean accepts = dfa.accepts(inputString);

    // Then.
    assertFalse(accepts);
  }


  // Helpers.
  private static enum ParityState {
    EVEN, ODD
  }

  private static enum BinarySymbol {
    ZERO, ONE;
  }

  private static BiFunction<ParityState, BinarySymbol, ParityState> transitionFunction =
      (state, symbol) -> {
        switch (state) {
          case EVEN:
            return ParityState.ODD;
          case ODD:
            return ParityState.EVEN;
          default:
            throw new IllegalArgumentException("Invalid state: " + state);
        }
      };
}
