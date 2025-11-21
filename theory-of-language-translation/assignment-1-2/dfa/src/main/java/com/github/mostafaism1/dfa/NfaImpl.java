package com.github.mostafaism1.dfa;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import com.google.common.collect.Sets;

public class NfaImpl<STATE, SYMBOL extends Enum<SYMBOL>> implements Nfa<STATE, SYMBOL> {

  private Set<STATE> states;
  private Set<SYMBOL> alphabet;
  private STATE startState;
  private BiFunction<STATE, Optional<SYMBOL>, Set<STATE>> transitionFunction;
  private Set<STATE> acceptStates;

  @Override
  public Nfa<STATE, SYMBOL> build(
      Set<STATE> states,
      Set<SYMBOL> alphabet,
      BiFunction<STATE, Optional<SYMBOL>, Set<STATE>> transitionFunction,
      STATE startState,
      Set<STATE> acceptStates) {
    this.states = states;
    this.alphabet = alphabet;
    this.transitionFunction = transitionFunction;
    this.startState = startState;
    this.acceptStates = acceptStates;
    return this;
  }

  @Override
  public boolean accepts(List<SYMBOL> input) {
    return equivalentDfa().accepts(input);
  }

  @Override
  public Dfa<Set<STATE>, SYMBOL> equivalentDfa() {
    Dfa<Set<STATE>, SYMBOL> dfa = new DfaImpl<Set<STATE>, SYMBOL>().build(
        equivalentDfaStates(),
        equivalentDfaAlphabet(),
        equivalentDfaTransitionFunction(),
        equivalentDfaStartState(),
        equivalentDfaAcceptStates());
    return dfa;
  }

  private Set<Set<STATE>> equivalentDfaStates() {
    return Sets.powerSet(states);
  }

  private Set<SYMBOL> equivalentDfaAlphabet() {
    return alphabet;
  }

  private BiFunction<Set<STATE>, SYMBOL, Set<STATE>> equivalentDfaTransitionFunction() {
    return (Set<STATE> state, SYMBOL symbol) -> state.stream()
        .flatMap(innerState -> transitionFunction.apply(innerState, Optional.of(symbol)).stream())
        .flatMap(innerState -> epsilonClosure(innerState).stream())
        .collect(Collectors.toSet());
  }

  private Set<STATE> equivalentDfaStartState() {
    return epsilonClosure(startState);
  }

  private Set<Set<STATE>> equivalentDfaAcceptStates() {
    return Sets.filter(equivalentDfaStates(), subset -> intersects(subset, acceptStates));
  }

  private Set<STATE> epsilonClosure(STATE q) {
    Set<STATE> closure = new HashSet<>();
    Deque<STATE> stack = new ArrayDeque<>();

    closure.add(q);
    stack.push(q);

    while (!stack.isEmpty()) {
      STATE current = stack.pop();

      // All states reachable via epsilon (Optional.empty())
      Set<STATE> epsNext = transitionFunction.apply(current, Optional.empty());

      for (STATE nxt : epsNext) {
        if (!closure.contains(nxt)) {
          closure.add(nxt);
          stack.push(nxt);
        }
      }
    }

    return closure;
  }

  private static <S> boolean intersects(Set<S> a, Set<S> b) {
    return !Collections.disjoint(a, b);
  }

}
