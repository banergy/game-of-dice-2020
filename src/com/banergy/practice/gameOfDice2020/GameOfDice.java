/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.banergy.practice.gameOfDice2020;

import java.util.List;

/**
 *
 * @author user
 */
public abstract class GameOfDice
{
	public static final int MAX_PLAYERS = 100;
	public static final int MAX_SCORE_TARGET = 1000;

	abstract protected void setNumPlayers(int numPlayers);
	abstract protected void setScoreTarget(int scoreTarget);
	
	abstract protected List<Integer> getPlayerSequence();
	abstract protected int rollDice();
	abstract protected String getNextStepMessage();
	
	public static GameOfDice newInstance(long seed) {
		return new GameOfDiceImpl(seed);
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args)
	{
		// TODO code application logic here
	}
}
