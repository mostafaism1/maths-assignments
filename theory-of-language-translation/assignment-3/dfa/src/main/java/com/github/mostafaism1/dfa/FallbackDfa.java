package com.github.mostafaism1.dfa;

import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

public interface FallbackDfa <STATE, SYMBOL extends Enum<SYMBOL>> {

  public FallbackDfa<STATE, SYMBOL> build(
      Set<STATE> states, 
      Set<SYMBOL> alphabet,
      BiFunction<STATE, SYMBOL, STATE> transitionFunction, 
      STATE startState,
      Set<STATE> acceptStates,
      BiFunction<STATE, List<SYMBOL>, Void> actions);

  public boolean accepts(List<SYMBOL> input);
}
