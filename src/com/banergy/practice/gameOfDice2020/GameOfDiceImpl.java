/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.banergy.practice.gameOfDice2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author user
 */
public class GameOfDiceImpl
	extends GameOfDice
{
	private int _numPlayers;
	private List<Integer> _playerSequence;
	private int _nextPlayer;
	
	protected GameOfDiceImpl(long seed) {
		
	}
	
	@Override
	public void setNumPlayers(int numPlayers) {
		if(numPlayers < 2 || MAX_PLAYERS < numPlayers)
			throw new GameOfDiceException("Number of players must between 2 and " + MAX_PLAYERS + ". Invalid number of players specified: " + numPlayers);
		
		_numPlayers = numPlayers;
		_nextPlayer = getPlayerSequence().get(0);
	}
	
	@Override
	protected void setScoreTarget(int scoreTarget) {
		if(scoreTarget < 2 || MAX_SCORE_TARGET < scoreTarget)
			throw new GameOfDiceException("Score target must be between 1 and " + MAX_SCORE_TARGET + ". Invalid score target specified: " + scoreTarget);
	}
	
	@Override
	protected List<Integer> getPlayerSequence() {
		if(_playerSequence == null) {
			List<Integer> seq = new ArrayList(_numPlayers);
			for(int i = 0; i < _numPlayers; i++)
				seq.add(i);

			// https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle#The_modern_algorithm
			for(int i = 0; i < _numPlayers; i++) {
				int j = i + (int) ((_numPlayers - i) * Math.random());
				int swap = seq.get(j);
				seq.set(j, seq.get(i));
				seq.set(i, swap);
			}
			_playerSequence = seq;
		}
		return _playerSequence;
	}
		
	@Override
	protected int rollDice() {
		return 1 + (int) (6 * Math.random());
	}
	
	@Override
	protected String getNextStepMessage() {
		return "Player-" + (_nextPlayer + 1) + ", it's your turn (press 'r' to roll the dice)";
	}
}
