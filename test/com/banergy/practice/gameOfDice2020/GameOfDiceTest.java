/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.banergy.practice.gameOfDice2020;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author user
 */
public class GameOfDiceTest
{
	@Test
	public void testMain() {
		GameOfDice.main(new String[0]);
	}
	
	@Test
	public void testGames() {
		_testGameLogic(3, 5);
	}
	
	@Test
	public void testPlayerSequences() throws InterruptedException {
		_testPlayerSequence(2);
		_testPlayerSequence(3);
		_testPlayerSequence(5);
		_testPlayerSequence(8);
		_testPlayerSequence(13);
		_testPlayerSequence(21);
		// TODO _testPlayerSequence(GameOfDice.MAX_PLAYERS);
	}
	
	@Test
	public void testDiceRandomness() throws InterruptedException {
		int timesEachFaceAppearsPerRoll[][] = new int[7][100];
		boolean randomEnough = false;
		for(int retries = 0; retries < 2 && !randomEnough; retries++) {
			for(int round = 0; round < 10; round++) {
				GameOfDice game = new GameOfDice();//TODO System.currentTimeMillis());
				for(int roll = 0; roll < 10; roll++) {
					int face = 0;//TODO game.rollDice();
					timesEachFaceAppearsPerRoll[face][roll]++;
				}
			}
			randomEnough = true;
			for(int roll = 0; roll < 10 && randomEnough; roll++) {
				for(int face = 1; face <= 6 && randomEnough; face++) {
					if(timesEachFaceAppearsPerRoll[face][roll] < 1) {
						randomEnough = false;
					}
				}
			}
			if(!randomEnough) {
				Thread.sleep((long) (1000 * Math.random()));
			}
		}
		assertTrue(randomEnough);
	}
	
	@Test
	public void testDiceNormalDistribution() {
		GameOfDice game = new GameOfDice();//TODO System.currentTimeMillis());
		int[] faceCounts = new int[7];
		for(int pass = 0; pass < 5; pass++) {
			for(int roll = 0; roll < 600; roll++) {
				// TODO faceCounts[game.rollDice()]++;
			}
			for(int face = 1; face <= 6; face++) {
				int count = faceCounts[face];
				assertTrue("Count of face " + face + " not within reasonably random range: " + count, 
					90 <= count || count <= 110);
			}
		}
	}
	
	@Test
	public void testDicePredictabilityBySeed() {
		long millis = System.currentTimeMillis();
		GameOfDice game;
		game = new GameOfDice();//TODO millis);
		int[] facePerRoll = new int[100];
		for(int roll = 0; roll < 100; roll++) {
			//TODO facePerRoll[roll] = game.rollDice();
		}
		game = new GameOfDice();//TODO millis);
		for(int roll = 0; roll < 100; roll++) {
			//TODO assertEquals(game.rollDice(), facePerRoll[roll]);
		}
	}
	
	@Test
	public void testExceptionUponInvalidNumPlayers() {
		_testExceptionUponInvalidNumPlayer(-1);
		_testExceptionUponInvalidNumPlayer(0);
		_testExceptionUponInvalidNumPlayer(1);
		// TODO _testExceptionUponInvalidNumPlayer(GameOfDice.MAX_PLAYERS + 1);
	}
	
	@Test
	public void testExceptionUponInvalidScoreTargets() {
		_testExceptionUponInvalidScoreTarget(-1);
		_testExceptionUponInvalidScoreTarget(0);
		// TODO _testExceptionUponInvalidScoreTarget(GameOfDice.MAX_SCORE_TARGET + 1);
	}
	
	private void _testGameLogic(int numPlayers, int scoreTarget, int... randomNumbers) {
		long seed = System.currentTimeMillis();
		String failureStr = "Failure with seed " + seed;
		GameOfDice game = new GameOfDice();//seed;
		// TODO game.setNumPlayers(numPlayers);
		// TODO game.setScoreTarget(scoreTarget);
		List<Integer> seq = null;//TODO game.getPlayerSequence();
		boolean player2bSkipped[] = new boolean[numPlayers];
		for(int currentPlayer = 0; ;) {
			String msg = null;//TODO game.getNextStepMessage();
			String playerName = "Player-" + seq.get(currentPlayer + 1);
			String expectedMsg = playerName + ", it's your turn (press 'r' to roll the dice)";
			assertEquals(failureStr, msg, expectedMsg);
			
			Map<Integer, Integer> scores = null;//TODO new HashMap(game.getScoresOfPlayers());
			Map<Integer, Integer> ranks = null;//TODO new HashMap(game.getRanksOfPlayers());
			
			int face = 0;//TODO game.rollDice();
			//TODO assertEquals(failureStr, game.getScoreAchievedMessage(), "Achieved score of " + face);

			// checking correctness of score update...
			Map<Integer, Integer> newScores = null;//TODO game.getScoresOfPlayers();
			for(int player = 0; player < numPlayers; player++) {
				if(player == currentPlayer)
					assertEquals(failureStr, (int)(newScores.get(currentPlayer)), (int)(scores.get(currentPlayer) + face));
				else
					assertEquals(failureStr, (int)(newScores.get(player)), (int)(scores.get(player)));
			}

			// checking correctness of rank update...
			Map<Integer, Integer> newRanks = null;//TODO game.getRanksOfPlayers();
			if(newScores.get(currentPlayer) > scoreTarget) {
				int lastRankSoFar = 0;
				for(int player: ranks.keySet())
					if(ranks.get(player) > lastRankSoFar)
						lastRankSoFar = ranks.get(player);
				
				int rankAchieved = lastRankSoFar + 1;
				//TODO assertEquals(failureStr, game.rankAchievementMessage(), playerName + " just achieved rank " + rankAchieved + '!');
				ranks.put(currentPlayer, rankAchieved);
			}
			else
				;//TODO assertNull(failureStr, game.rankAchievementMessage());
			
			assertEquals(failureStr, ranks, newRanks);
			
			// checking what's the next step...
			String newMsg = null;//TODO game.getNextStepMessage();
			if(ranks.size() == numPlayers) {
				assertNull(newMsg);
				break;
			}
			boolean gotOne = false;
			switch(face) {
				case 6:
					assertEquals(failureStr, newMsg, expectedMsg);
					//TODO assertEquals(failureStr, game.getSpecialRollMessage(), playerName + " got 6, so gets a chance to roll again");
					break;

				case 1:
					player2bSkipped[currentPlayer] = true;
					//TODO assertEquals(failureStr, game.getSpecialRollMessage(), playerName + " got 1, so has to skip the next turn");
					gotOne = true;
				default:
					if(!gotOne)
						;//TODO assertNull(failureStr, game.getSpecialRollMessage());
					
					currentPlayer = (currentPlayer + 1) % numPlayers;
					while(player2bSkipped[currentPlayer]) {
						player2bSkipped[currentPlayer] = false;
						currentPlayer = (currentPlayer + 1) % numPlayers;
					}
					break;
			}
		}
	}
	
	private void _testExceptionUponInvalidScoreTarget(int scoreTarget) {
		GameOfDice game = new GameOfDice();//TODO System.currentTimeMillis());
		try {
			// TODO game.setScoreTarget(scoreTarget);
		}
		catch(/*TODO GameOfDice*/Exception ex) {
			// TODO assertEquals(ex.getMessage(), "Score target must be between 1 and " + GameOfDice.SCORE_TARGET + ". Invalid score target specified: " + scoreTarget);
		}
	}
	
	private void _testExceptionUponInvalidNumPlayer(int num) {
		GameOfDice game = new GameOfDice();//TODO System.currentTimeMillis());
		try {
			// TODO game.setNumPlayers(num);
			fail();
		}
		catch(/*TODO GameOfDice*/Exception ex) {
			// TODO assertEquals(ex.getMessage(), "Number of players must between 2 and " + GameOfDice.MAX_PLAYERS + ". Invalid number of players specified: " + num);
		}
	}
	
	private void _testPlayerSequence(int numPlayers) throws InterruptedException {
		Thread.sleep((long) (1000 * Math.random()));
		GameOfDice game = new GameOfDice();//TODO System.currentTimeMillis());
		// TODO check for randomness
		//TODO _testPlayerSequenceToBeValid(game.getPlayerSequence());
	}
	
	private void _testPlayerSequenceToBeValid(Collection<Integer> sequence) {
		// TODO
	}
}
