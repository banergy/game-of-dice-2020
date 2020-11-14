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
public abstract class GameOfDice
{
	public static final int MAX_PLAYERS = 100;
	public static final int MAX_SCORE_TARGET = 1000;

	abstract public void setNumPlayers(int numPlayers);
	abstract public void setScoreTarget(int scoreTarget);
	
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
