/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.banergy.practice.gameOfDice2020;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
	private int _scoreTarget;
	private List<Integer> _playerSequence;
	int[] _consecutiveOnesByPlayer;
	private int _iNextPlayer;
	private Map<Integer, Integer> _scoresByPlayer;
	private Map<Integer, Integer> _ranksByPlayer;
	private String _scoreAchievedMessage;
	private String _specialRollMessage;
	private String _rankAchievementMessage;
	
	protected GameOfDiceImpl(long seed) {
		_random = new Random(seed);
		_random.nextInt();
	}
	
	@Override
	public void setNumPlayers(int numPlayers) {
		if(numPlayers < 2 || MAX_PLAYERS < numPlayers)
			throw new GameOfDiceException("Number of players must between 2 and " + MAX_PLAYERS + ". Invalid number of players specified: " + numPlayers);
		
		_numPlayers = numPlayers;
		_scoresByPlayer = new LinkedHashMap();
		for(int player = 0; player < _numPlayers; player++)
			_scoresByPlayer.put(player, 0);
		_ranksByPlayer = new HashMap();
		_consecutiveOnesByPlayer = new int[_numPlayers];
	}
	
	@Override
	protected void setScoreTarget(int scoreTarget) {
		if(scoreTarget < 2 || MAX_SCORE_TARGET < scoreTarget)
			throw new GameOfDiceException("Score target must be between 1 and " + MAX_SCORE_TARGET + ". Invalid score target specified: " + scoreTarget);
		
		_scoreTarget = scoreTarget;
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
		_rankAchievementMessage = null;
		int face = 1 + _random.nextInt(6);
		if(_playerSequence != null) {
			int nextPlayer = _playerSequence.get(_iNextPlayer);
			String playerStr = "Player-" + (nextPlayer + 1);
			int newScore = _scoresByPlayer.get(nextPlayer) + face;
			_scoresByPlayer.put(nextPlayer, newScore);
			_scoreAchievedMessage = "Achieved score points of " + face;
			
			_rankAchievementMessage = null;
			if(newScore > _scoreTarget) {
				int rank = _ranksByPlayer.size() + 1;
				_ranksByPlayer.put(nextPlayer, rank);
				_rankAchievementMessage = playerStr + " just achieved rank " + rank + '!';
				if(_ranksByPlayer.size() == _numPlayers) {
					_iNextPlayer = -1;
					return face;
				}
			}
			_specialRollMessage = null;
			boolean needIncrementPlayer;
			boolean gotOne = false;
			switch(face)
			{
				case 6:
					if(_rankAchievementMessage != null) {
						needIncrementPlayer = true;
					}
					else {
						_specialRollMessage = playerStr + " got 6, so gets a chance to roll again";
						needIncrementPlayer = false;
					}
					break;
			
				case 1:
					gotOne = true;
					_consecutiveOnesByPlayer[nextPlayer]++;
					if(_consecutiveOnesByPlayer[nextPlayer] > 1) {
						_specialRollMessage = playerStr + " got 1 twice in a row, so has to skip the next turn";
					}
				default:
					needIncrementPlayer = true;
					break;
			}
			if(!gotOne) {
				_consecutiveOnesByPlayer[nextPlayer] = 0;
			}
			while(needIncrementPlayer) {
				_iNextPlayer = (_iNextPlayer + 1) % _numPlayers;
				needIncrementPlayer = false;

				nextPlayer = _playerSequence.get(_iNextPlayer);
				if(_ranksByPlayer.containsKey(nextPlayer)) {
					needIncrementPlayer = true;
				}
				else if(_consecutiveOnesByPlayer[nextPlayer] > 1) {
					_consecutiveOnesByPlayer[nextPlayer] = 0;
					needIncrementPlayer = true;
				}
			}
		}
		return face;
	}
	
	@Override
	protected String getNextStepMessage() {
		if(_iNextPlayer < 0)
			return null;
		
		return "Player-" + (getPlayerSequence().get(_iNextPlayer) + 1) + ", it's your turn (press 'r' to roll the dice)";
	}
	
	@Override
	protected Map<Integer, Integer> getScoresOfPlayers() {
		return _scoresByPlayer;
	}
	
	@Override
	protected Map<Integer, Integer> getRanksOfPlayers() {
		return _ranksByPlayer;
	}
	
	@Override
	protected String getScoreAchievedMessage() {
		return _scoreAchievedMessage;
	}
	@Override
	protected String getRankAchievementMessage() {
		return _rankAchievementMessage;
	}
	@Override
	protected String getSpecialRollMessage() {
		return _specialRollMessage;
	}

}
