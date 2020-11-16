/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.banergy.practice.gameOfDice2020;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author user
 */
public abstract class GameOfDice
{
	public static final int MAX_PLAYERS = 100;
	public static final int MAX_SCORE_TARGET = 1000;
	
	private static InputStream sInteractionInputStream = System.in;

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
	
	protected static void setInteractionInputStream(InputStream in) {
		sInteractionInputStream = in;
	}
	
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
		long seed = System.currentTimeMillis();
		GameOfDice game = GameOfDice.newInstance(seed);
		System.out.println("Initialized game, with seed " + seed + '.');
		game.setNumPlayers(Integer.valueOf(args[0]));
		game.setScoreTarget(Integer.valueOf(args[1]));
		for(Scanner in = new Scanner(sInteractionInputStream); ;) {
			String nextStepMessage = game.getNextStepMessage();
			if(nextStepMessage == null)
				break;
			
			System.out.println(nextStepMessage);
			String s = in.nextLine();
			if("q".equals(s))
				break;

			game.rollDice();
			System.out.println(game.getScoreAchievedMessage());
			
			String specialRollMessage = game.getSpecialRollMessage();
			if(specialRollMessage != null)
				System.out.println(specialRollMessage);
			
			String rankAchievementMessage = game.getRankAchievementMessage();
			if(rankAchievementMessage != null)
				System.out.println(rankAchievementMessage);
			
			System.out.println("\nCurrent Scores:");
			Map<Integer, Integer> scores = game.getScoresOfPlayers();
			scores.keySet().forEach(player -> {
				System.out.println("Player-" + (player + 1) + " = " + scores.get(player));
			});
			
			System.out.println("\nCurrent Rankings:");
			Map<Integer, Integer> ranks = game.getRanksOfPlayers();
			ranks.keySet().forEach(player -> {
				System.out.println("Player-" + (player + 1) + " = " + ranks.get(player));
			});
			System.out.println();
		}
		System.out.println("End Game!");
	}
}
