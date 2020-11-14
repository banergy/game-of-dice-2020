/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.banergy.practice.gameOfDice2020;

/**
 *
 * @author user
 */
public class GameOfDiceImpl
	extends GameOfDice
{
	protected GameOfDiceImpl(long seed) {
		
	}
	
	@Override
	public void setNumPlayers(int numPlayers) {
		//if(numPlayers < 2 || MAX_PLAYERS < numPlayers)
			throw new GameOfDiceException("Number of players must between 2 and " + MAX_PLAYERS + ". Invalid number of players specified: " + numPlayers);
	}
	
	@Override
	public void setScoreTarget(int scoreTarget) {
		//if(scoreTarget < 2 || MAX_SCORE_TARGET < scoreTarget)
			throw new GameOfDiceException("Score target must be between 1 and " + MAX_SCORE_TARGET + ". Invalid score target specified: " + scoreTarget);
	}
	
	@Override
	public int rollDice() {
		return 1 + (int) (6 * Math.random());
	}
}
