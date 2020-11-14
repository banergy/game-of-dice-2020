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
		if(numPlayers < 2 || MAX_PLAYERS < numPlayers)
			throw new GameOfDiceException("Number of players must between 2 and " + MAX_PLAYERS + ". Invalid number of players specified: " + numPlayers);
	}
}
