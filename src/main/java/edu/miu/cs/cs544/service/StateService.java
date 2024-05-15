package edu.miu.cs.cs544.service;

import edu.miu.cs.cs544.domain.State;

import java.util.List;

public interface StateService {

    State addState(State state) throws Exception;

    State updateState(Long stateId, State state) throws Exception;

    State getStateById(Long stateId);

    List<State> getAllState();

}
