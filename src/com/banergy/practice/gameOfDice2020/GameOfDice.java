/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.banergy.practice.gameOfDice2020;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
	abstract protected Map<Integer, Integer> getScoresOfPlayers();
	abstract protected Map<Integer, Integer> getRanksOfPlayers();	
	abstract protected String getScoreAchievedMessage();
	abstract protected String getRankAchievementMessage();
	abstract protected String getSpecialRollMessage();
	
	public static GameOfDice newInstance(long seed) {
		return new GameOfDiceImpl(seed);
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws IOException
	{
		// TODO test/coverage for error handling
		if(args.length < 2) {
			System.out.println("USAGE: GameOfDice <number-of-players> <target-score>");
			return;
		}
		// TODO try abstracting the interaction out
		// TODO display scores & ranks in a single table
		GameOfDice game = GameOfDice.newInstance(System.currentTimeMillis());
		game.setNumPlayers(Integer.valueOf(args[0]));
		game.setScoreTarget(Integer.valueOf(args[1]));
		for(;;) {
			String nextStepMessage = game.getNextStepMessage();
			if(nextStepMessage == null)
				break;
			
			System.out.println(nextStepMessage);
			System.console().readLine();
			
			System.out.println(game.getScoreAchievedMessage());
			
			String specialRollMessage = game.getSpecialRollMessage();
			if(specialRollMessage != null)
				System.out.println(specialRollMessage);
			
			String rankAchievementMessage = game.getRankAchievementMessage();
			if(rankAchievementMessage != null)
				System.out.println(rankAchievementMessage);
			
			System.out.println("Current Scores:");
			Map<Integer, Integer> scores = game.getScoresOfPlayers();
			scores.keySet().forEach(player -> {
				System.out.println("Player-" + (player + 1) + " = " + scores.get(player));
			});
			
			System.out.println("Current Rankings:");
			Map<Integer, Integer> ranks = game.getRanksOfPlayers();
			ranks.keySet().forEach(player -> {
				System.out.println("Player-" + (player + 1) + " = " + ranks.get(player));
			});
		}
		System.out.println("End Game!");
	}
}
