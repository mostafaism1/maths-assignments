package com.github.mostafaism1.dfa;

import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

public interface Dfa<STATE extends Enum<STATE>, SYMBOL extends Enum<SYMBOL>> {

  public Dfa<?, ?> build(
      Set<STATE> states, 
      Set<SYMBOL> alphabet,
      BiFunction<STATE, SYMBOL, STATE> transitionFunction, 
      STATE startState,
      Set<STATE> acceptStates);

  public boolean accepts(List<SYMBOL> input);

}
