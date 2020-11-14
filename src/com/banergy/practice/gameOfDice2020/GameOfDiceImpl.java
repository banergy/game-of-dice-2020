/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.banergy.practice.gameOfDice2020;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author user
 */
public class GameOfDiceImpl
	extends GameOfDice
{
	private Random _random;
	private int _numPlayers;
	private List<Integer> _playerSequence;
	private int _nextPlayer;
	private Map<Integer, Integer> _scoresByPlayer;
	private Map<Integer, Integer> _ranksByPlayer;
	
	protected GameOfDiceImpl(long seed) {
		_random = new Random(seed);
		_random.nextInt();
	}
	
	@Override
	public void setNumPlayers(int numPlayers) {
		if(numPlayers < 2 || MAX_PLAYERS < numPlayers)
			throw new GameOfDiceException("Number of players must between 2 and " + MAX_PLAYERS + ". Invalid number of players specified: " + numPlayers);
		
		_numPlayers = numPlayers;
		_nextPlayer = getPlayerSequence().get(0);
		_scoresByPlayer = new HashMap();
		for(int player = 0; player < _numPlayers; player++)
			_scoresByPlayer.put(player, 0);
		_ranksByPlayer = new HashMap();
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
				int j = i + _random.nextInt(_numPlayers - i);
				//System.out.println("swapping " + i + "&" + j);
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
		int face = 1 + _random.nextInt(6);
		if(_playerSequence != null)
			_scoresByPlayer.put(_nextPlayer, _scoresByPlayer.get(_nextPlayer) + face);
		return face;
	}
	
	@Override
	protected String getNextStepMessage() {
		return "Player-" + (_nextPlayer + 1) + ", it's your turn (press 'r' to roll the dice)";
	}
	
	@Override
	protected Map<Integer, Integer> getScoresOfPlayers() {
		return _scoresByPlayer;
	}
	
	@Override
	protected Map<Integer, Integer> getRanksOfPlayers() {
		return _ranksByPlayer;
	}
}
