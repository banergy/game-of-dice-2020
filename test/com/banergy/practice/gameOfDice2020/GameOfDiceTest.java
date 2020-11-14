/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.banergy.practice.gameOfDice2020;

import java.util.Arrays;
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
		int timesEachFaceAppearsPerRoll[][] = new int[7][10];
		boolean randomEnough = false;
		long seed = System.currentTimeMillis();
		String failureStr = "Failure with seed " + seed;
		for(int retries = 0; retries < 3 && !randomEnough; retries++) {
			for(int trial = 0; trial < 20; trial++) {
				GameOfDice game = GameOfDice.newInstance(seed + retries);
				for(int roll = 0; roll < 10; roll++) {
					int face = game.rollDice();
					timesEachFaceAppearsPerRoll[face][roll]++;
				}
			}
			randomEnough = true;
			for(int roll = 0; roll < 10 && randomEnough; roll++) {
				for(int face = 1; face <= 6 && randomEnough; face++) {
					if(timesEachFaceAppearsPerRoll[face][roll] < 1) {
						randomEnough = false;
						//System.out.println(Arrays.deepToString(timesEachFaceAppearsPerRoll));
					}
				}
			}
		}
		assertTrue(failureStr, randomEnough);
	}
	
	@Test
	public void testDiceNormalDistribution() {
		long seed = System.currentTimeMillis();
		String failureStr = "Failure with seed " + seed;
		GameOfDice game = GameOfDice.newInstance(seed);
		for(int pass = 0; pass < 2; pass++) {
			int[] faceCounts = new int[7];
			for(int roll = 0; roll < 600; roll++) {
				faceCounts[game.rollDice()]++;
			}
			//System.out.println(Arrays.toString(faceCounts));
			for(int face = 1; face <= 6; face++) {
				int count = faceCounts[face];
				assertTrue(failureStr + ": count of face " + face + " not within reasonably random range: " + count, 
					80 <= count && count <= 120);
			}
		}
	}
	
	@Test
	public void testDicePredictabilityBySeed() {
		long seed = System.currentTimeMillis();
		GameOfDice game;
		game = GameOfDice.newInstance(seed);
		int[] facePerRoll = new int[100];
		for(int roll = 0; roll < 100; roll++) {
			//TODO facePerRoll[roll] = game.rollDice();
		}
		game = GameOfDice.newInstance(seed);
		for(int roll = 0; roll < 100; roll++) {
			//TODO assertEquals(game.rollDice(), facePerRoll[roll]);
		}
	}
	
	@Test
	public void testExceptionUponInvalidNumPlayers() {
		_testExceptionUponInvalidNumPlayer(-1);
		_testExceptionUponInvalidNumPlayer(0);
		_testExceptionUponInvalidNumPlayer(1);
		_testExceptionUponInvalidNumPlayer(GameOfDice.MAX_PLAYERS + 1);
	}
	
	@Test
	public void testExceptionUponInvalidScoreTargets() {
		_testExceptionUponInvalidScoreTarget(-1);
		_testExceptionUponInvalidScoreTarget(0);
		_testExceptionUponInvalidScoreTarget(GameOfDice.MAX_SCORE_TARGET + 1);
	}
	
	private void _testGameLogic(int numPlayers, int scoreTarget, int... randomNumbers) {
		long seed = System.currentTimeMillis();
		String failureStr = "Failure with seed " + seed;
		GameOfDice game = GameOfDice.newInstance(seed);
		game.setNumPlayers(numPlayers);
		game.setScoreTarget(scoreTarget);
		List<Integer> seq = game.getPlayerSequence();
		boolean player2bSkipped[] = new boolean[numPlayers];
		for(int iPlayer = 0; ;) {
			int currentPlayer = seq.get(iPlayer);
			String msg = game.getNextStepMessage();
			String playerName = "Player-" + (currentPlayer + 1);
			String expectedMsg = playerName + ", it's your turn (press 'r' to roll the dice)";
			//System.out.println(msg + " -vs- " + expectedMsg);
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
					
					iPlayer = (iPlayer + 1) % numPlayers;
					while(player2bSkipped[seq.get(iPlayer)]) {
						player2bSkipped[seq.get(iPlayer)] = false;
						iPlayer = (iPlayer + 1) % numPlayers;
					}
					break;
			}
		}
	}
	
	private void _testExceptionUponInvalidScoreTarget(int scoreTarget) {
		GameOfDice game = GameOfDice.newInstance(System.currentTimeMillis());
		try {
			game.setScoreTarget(scoreTarget);
			fail();
		}
		catch(GameOfDiceException ex) {
			assertEquals(ex.getMessage(), "Score target must be between 1 and " + GameOfDice.MAX_SCORE_TARGET + ". Invalid score target specified: " + scoreTarget);
		}
	}
	
	private void _testExceptionUponInvalidNumPlayer(int num) {
		GameOfDice game = GameOfDice.newInstance(System.currentTimeMillis());
		try {
			game.setNumPlayers(num);
			fail();
		}
		catch(GameOfDiceException ex) {
			assertEquals(ex.getMessage(), "Number of players must between 2 and " + GameOfDice.MAX_PLAYERS + ". Invalid number of players specified: " + num);
		}
	}
	
	private void _testPlayerSequence(int numPlayers) throws InterruptedException {
		Thread.sleep((long) (1000 * Math.random()));
		long seed = System.currentTimeMillis();
		String failureStr = "Failure with seed " + seed;
		_testPlayerSequencePredictabilityBySeed(seed);
		int[][] timesEachPlayerInEachPosition = new int[numPlayers][numPlayers];
		for(int trial = 0; trial < numPlayers * numPlayers; trial++) {
			GameOfDice game = GameOfDice.newInstance(seed + trial);
			game.setNumPlayers(numPlayers);
			List<Integer> seq = game.getPlayerSequence();
			_testPlayerSequenceToBeValid(failureStr, numPlayers, seq);
			for(int i = 0; i < seq.size(); i++) {
				timesEachPlayerInEachPosition[seq.get(i)][i]++;
			}
		}
		boolean randomEnough = true;
		for(int player = 0; player < numPlayers && randomEnough; player++)
			for(int pos = 0; pos < numPlayers && randomEnough; pos++)
				if(timesEachPlayerInEachPosition[player][pos] < 1)
					randomEnough = false;
		assertTrue(failureStr, randomEnough);
	}
	
	private void _testPlayerSequencePredictabilityBySeed(long seed) {
		GameOfDice game;
		String failureStr = "Failure with seed " + seed;
		game = GameOfDice.newInstance(seed);
		List<Integer> seq1 = null;//TODO game.getPlayerSequence();
		game = GameOfDice.newInstance(seed);
		List<Integer> seq2 = null;//TODO game.getPlayerSequence();
		assertEquals(failureStr, seq1, seq2);
	}
	
	private void _testPlayerSequenceToBeValid(String failureStr, int numPlayers, List<Integer> sequence) {
		assertEquals(failureStr, sequence.size(), numPlayers);
		boolean[] encountered = new boolean[numPlayers];
		sequence.forEach(player -> {
			assertTrue(failureStr + ": player " + player, 0 <= player && player < numPlayers);
			assertFalse(failureStr, encountered[player]);
			encountered[player] = true;
		});
	}
}
