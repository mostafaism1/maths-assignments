package com.github.mostafaism1.dfa;

import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

public class DfaImpl<STATE, SYMBOL extends Enum<SYMBOL>>
    implements Dfa<STATE, SYMBOL> {

  private STATE startState;
  private BiFunction<STATE, SYMBOL, STATE> transitionFunction;
  private Set<STATE> acceptStates;

  @Override
  public Dfa<STATE, SYMBOL> build(
      Set<STATE> states, 
      Set<SYMBOL> alphabet,
      BiFunction<STATE, SYMBOL, STATE> transitionFunction, 
      STATE startState,
      Set<STATE> acceptStates) {
    this.transitionFunction = transitionFunction;
    this.startState = startState;
    this.acceptStates = acceptStates;
    return this;
  }

  @Override
  public boolean accepts(List<SYMBOL> input) {
    STATE currentState = startState;
    for (SYMBOL symbol : input) {
      currentState = transitionFunction.apply(currentState, symbol);
    }
    return acceptStates.contains(currentState);
  }


}
