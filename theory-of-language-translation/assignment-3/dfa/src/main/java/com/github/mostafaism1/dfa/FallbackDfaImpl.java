package com.github.mostafaism1.dfa;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.function.BiFunction;

public class FallbackDfaImpl<STATE, SYMBOL extends Enum<SYMBOL>> implements FallbackDfa<STATE, SYMBOL> {

  private STATE startState;
  private BiFunction<STATE, SYMBOL, STATE> transitionFunction;
  private Set<STATE> acceptStates;
  private BiFunction<STATE, List<SYMBOL>, Void> actions;

  @Override
  public FallbackDfa<STATE, SYMBOL> build(
    Set<STATE> states, 
    Set<SYMBOL> alphabet,
    BiFunction<STATE, SYMBOL, STATE> transitionFunction, 
    STATE startState,
    Set<STATE> acceptStates, 
    BiFunction<STATE, List<SYMBOL>, Void> actions) {

    this.transitionFunction = transitionFunction;
    this.startState = startState;
    this.acceptStates = acceptStates;
    this.actions = actions;
    return this;

  }

  @Override
  public boolean accepts(List<SYMBOL> input) {
    // Initialization.
    List<SYMBOL> lexeme = new ArrayList<>();
    int leftHead = 0;
    int rightHead = 0;
    STATE currentState = startState;
    Stack<STATE> stack = new Stack<>();
    stack.push(currentState);

    // On every transition, push the new state on the stack and move leftHead one step to the right.
    for (SYMBOL symbol : input) {
      currentState = transitionFunction.apply(currentState, symbol);
      stack.push(currentState);
      leftHead++;
    }

    // When the input string is consumed, check whether $currentState \in  acceptStates$.
    // If true, run the corresponding action for currentState and return.
    if (acceptStates.contains(currentState)) {
      lexeme = input.subList(rightHead, leftHead);
      actions.apply(currentState, lexeme);
      return true;
    }

    // If false (i.e. case 1 or 2 in the slides), backtrack.
    STATE backTrackState = currentState;
    while (!stack.empty() && !acceptStates.contains(backTrackState)) {
      backTrackState = stack.pop();
      leftHead--;
    }

    // If case 1 (i.e. empty stack), run the corresponding action for currentState (i.e. q_r in the slides) and return.
    if (stack.empty()) {
      lexeme = input;
      actions.apply(currentState, lexeme);
      return false;
    }

    // If case 2 (i.e. $backtrackState \in acceptStates$), run the corresponding action for backtrackState, move leftHead one position to the right, move rightHead to leftHead, and recurse on the remaining input string.
    lexeme = input.subList(rightHead, leftHead);
    actions.apply(backTrackState, lexeme);
    leftHead++;
    rightHead = leftHead;
    return this.accepts(input.subList(rightHead, input.size()));
  }
  
}
