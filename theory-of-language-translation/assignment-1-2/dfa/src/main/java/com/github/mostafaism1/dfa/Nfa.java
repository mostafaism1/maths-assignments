package com.github.mostafaism1.dfa;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;

public interface Nfa<STATE, SYMBOL extends Enum<SYMBOL>> {
  public Nfa<?, ?> build(
      Set<STATE> states,
      Set<SYMBOL> alphabet,
      BiFunction<STATE, Optional<SYMBOL>, Set<STATE>> transitionFunction,
      STATE startState,
      Set<STATE> acceptStates);

  public boolean accepts(List<SYMBOL> input);

  public Dfa<Set<STATE>, SYMBOL> equivalentDfa();
}
