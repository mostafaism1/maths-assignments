package com.github.mostafaism1.dfa;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Acceptance tests for NfaImpl based on Figure 1.27 (N_1) [Sipser, page 48].
 *
 * The NFA has:
 * - States: q1, q2, q3, q4
 * - Start state: q1
 * - Accept state: q4
 * - Transitions:
 *   - q1 on 0,1 -> q1 (self-loop)
 *   - q1 on 1 -> q2
 *   - q2 on 0 -> q3
 *   - q2 on ϵ -> q3
 *   - q3 on 1 -> q4
 *   - q4 on 0,1 -> q4 (self-loop)
 */
public class NfaTest {

  private Nfa<State, BinarySymbol> nfa;

  @BeforeEach
  public void setUp() {
    Set<State> states = new HashSet<>(Arrays.asList(State.values()));
    Set<BinarySymbol> alphabet = new HashSet<>(Arrays.asList(BinarySymbol.values()));
    nfa = new NfaImpl<State, BinarySymbol>().build(
        states,
        alphabet,
        transitionFunction,
        State.Q1,
        Set.of(State.Q4));
  }

  // Tests for strings that should be accepted

  @Test
  public void testAccepts_101() {
    // Contains "101" substring
    List<BinarySymbol> input = Arrays.asList(BinarySymbol.ONE, BinarySymbol.ZERO, BinarySymbol.ONE);
    assertTrue(nfa.accepts(input));
  }

  @Test
  public void testAccepts_11() {
    // Contains "11" substring (using epsilon transition)
    List<BinarySymbol> input = Arrays.asList(BinarySymbol.ONE, BinarySymbol.ONE);
    assertTrue(nfa.accepts(input));
  }

  @Test
  public void testAccepts_1011() {
    // Contains "101" followed by additional symbols
    List<BinarySymbol> input = Arrays.asList(BinarySymbol.ONE, BinarySymbol.ZERO,
        BinarySymbol.ONE, BinarySymbol.ONE);
    assertTrue(nfa.accepts(input));
  }

  @Test
  public void testAccepts_110() {
    // Contains "11" followed by 0
    List<BinarySymbol> input = Arrays.asList(BinarySymbol.ONE, BinarySymbol.ONE, BinarySymbol.ZERO);
    assertTrue(nfa.accepts(input));
  }

  @Test
  public void testAccepts_00101() {
    // Prefix followed by "101"
    List<BinarySymbol> input = Arrays.asList(BinarySymbol.ZERO, BinarySymbol.ZERO,
        BinarySymbol.ONE, BinarySymbol.ZERO, BinarySymbol.ONE);
    assertTrue(nfa.accepts(input));
  }

  @Test
  public void testAccepts_0011() {
    // Prefix followed by "11"
    List<BinarySymbol> input = Arrays.asList(BinarySymbol.ZERO, BinarySymbol.ZERO,
        BinarySymbol.ONE, BinarySymbol.ONE);
    assertTrue(nfa.accepts(input));
  }

  @Test
  public void testAccepts_10100() {
    // "101" followed by "00"
    List<BinarySymbol> input = Arrays.asList(BinarySymbol.ONE, BinarySymbol.ZERO,
        BinarySymbol.ONE, BinarySymbol.ZERO, BinarySymbol.ZERO);
    assertTrue(nfa.accepts(input));
  }

  @Test
  public void testAccepts_111() {
    // Multiple 1s (contains "11")
    List<BinarySymbol> input = Arrays.asList(BinarySymbol.ONE, BinarySymbol.ONE, BinarySymbol.ONE);
    assertTrue(nfa.accepts(input));
  }

  @Test
  public void testAccepts_01011() {
    // Contains "101" in the middle
    List<BinarySymbol> input = Arrays.asList(BinarySymbol.ZERO, BinarySymbol.ONE,
        BinarySymbol.ZERO, BinarySymbol.ONE, BinarySymbol.ONE);
    assertTrue(nfa.accepts(input));
  }

  // Tests for strings that should be rejected

  @Test
  public void testRejects_emptyString() {
    List<BinarySymbol> input = Collections.emptyList();
    assertFalse(nfa.accepts(input));
  }

  @Test
  public void testRejects_0() {
    List<BinarySymbol> input = Arrays.asList(BinarySymbol.ZERO);
    assertFalse(nfa.accepts(input));
  }

  @Test
  public void testRejects_1() {
    List<BinarySymbol> input = Arrays.asList(BinarySymbol.ONE);
    assertFalse(nfa.accepts(input));
  }

  @Test
  public void testRejects_10() {
    List<BinarySymbol> input = Arrays.asList(BinarySymbol.ONE, BinarySymbol.ZERO);
    assertFalse(nfa.accepts(input));
  }

  @Test
  public void testRejects_00() {
    List<BinarySymbol> input = Arrays.asList(BinarySymbol.ZERO, BinarySymbol.ZERO);
    assertFalse(nfa.accepts(input));
  }

  @Test
  public void testRejects_000() {
    List<BinarySymbol> input = Arrays.asList(BinarySymbol.ZERO, BinarySymbol.ZERO, BinarySymbol.ZERO);
    assertFalse(nfa.accepts(input));
  }

  @Test
  public void testRejects_010() {
    List<BinarySymbol> input = Arrays.asList(BinarySymbol.ZERO, BinarySymbol.ONE, BinarySymbol.ZERO);
    assertFalse(nfa.accepts(input));
  }

  // Helpers

  private static enum State {
    Q1, Q2, Q3, Q4
  }

  private static enum BinarySymbol {
    ZERO, ONE
  }

  /**
   * Transition function for the NFA in Figure 1.27.
   *
   * Takes a state and an optional symbol (Optional.empty() represents epsilon).
   * Returns the set of states reachable from the given state on the given symbol.
   */
  private static BiFunction<State, Optional<BinarySymbol>, Set<State>> transitionFunction =
      (state, symbol) -> {
        Set<State> result = new HashSet<>();

        switch (state) {
          case Q1:
            if (symbol.isPresent()) {
              // Self-loop on both 0 and 1
              result.add(State.Q1);
              // Transition to Q2 on 1
              if (symbol.get() == BinarySymbol.ONE) {
                result.add(State.Q2);
              }
            }
            break;

          case Q2:
            if (symbol.isEmpty()) {
              // Epsilon transition to Q3
              result.add(State.Q3);
            } else if (symbol.get() == BinarySymbol.ZERO) {
              // Transition to Q3 on 0
              result.add(State.Q3);
            }
            break;

          case Q3:
            if (symbol.isPresent() && symbol.get() == BinarySymbol.ONE) {
              // Transition to Q4 on 1
              result.add(State.Q4);
            }
            break;

          case Q4:
            if (symbol.isPresent()) {
              // Self-loop on both 0 and 1
              result.add(State.Q4);
            }
            break;

          default:
            throw new IllegalArgumentException("Invalid state: " + state);
        }

        return result;
      };
}
