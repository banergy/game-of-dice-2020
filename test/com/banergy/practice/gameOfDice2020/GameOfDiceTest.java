/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.banergy.practice.gameOfDice2020;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author user
 */
public class GameOfDiceTest
{
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
	
	@Test
	public void testPlayerSequences() throws InterruptedException {
		_testPlayerSequence(2);
		_testPlayerSequence(3);
		_testPlayerSequence(5);
		_testPlayerSequence(8);
		_testPlayerSequence(13);
		_testPlayerSequence(21);
		_testPlayerSequence(GameOfDice.MAX_PLAYERS);
	}
	
	@Test
	public void testDiceRandomness() throws InterruptedException {
		int timesEachFaceAppearsPerRoll[][] = new int[7][10];
		boolean randomEnough = false;
		long seed = System.currentTimeMillis();
		String failureStr = "Failure with seed " + seed;
		for(int retries = 0; retries < 5 && !randomEnough; retries++) {
			for(int trial = 0; trial < 20; trial++) {
				GameOfDice game = GameOfDice.newInstance(seed + trial + retries * 20);
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
			for(int roll = 0; roll < 6000; roll++) {
				faceCounts[game.rollDice()]++;
			}
			//System.out.println(Arrays.toString(faceCounts));
			for(int face = 1; face <= 6; face++) {
				int count = faceCounts[face];
				assertTrue(failureStr + ": count of face " + face + " not within reasonably random range: " + count, 
					800 <= count && count <= 1200);
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
			facePerRoll[roll] = game.rollDice();
		}
		game = GameOfDice.newInstance(seed);
		for(int roll = 0; roll < 100; roll++) {
			assertEquals(facePerRoll[roll], game.rollDice());
		}
	}
	
	@Test
	public void testGames() {
		_testGameLogic(3, 5);
		_testGameLogic(5, 8);
		_testGameLogic(3, 13);
		_testGameLogic(5, 21);
	}
	
	@Test
	public void testMain() {
		GameOfDice.main(new String[0]);
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
			assertEquals(failureStr, expectedMsg, msg);
			
			Map<Integer, Integer> scores = new HashMap(game.getScoresOfPlayers());
			Map<Integer, Integer> ranks = new HashMap(game.getRanksOfPlayers());
			
			int face = game.rollDice();
			assertEquals(failureStr, "Achieved score points of " + face, game.getScoreAchievedMessage());

			// checking correctness of score update...
			Map<Integer, Integer> newScores = game.getScoresOfPlayers();
			for(int player = 0; player < numPlayers; player++) {
				if(player == currentPlayer)
					assertEquals(failureStr, (int)(scores.get(currentPlayer) + face), (int)(newScores.get(currentPlayer)));
				else
					assertEquals(failureStr, (int)(scores.get(player)), (int)(newScores.get(player)));
			}

			// checking correctness of rank update...
			Map<Integer, Integer> newRanks = game.getRanksOfPlayers();
			if(newScores.get(currentPlayer) > scoreTarget) {
				int rankAchieved = ranks.size() + 1;
				assertEquals(failureStr, playerName + " just achieved rank " + rankAchieved + '!', game.getRankAchievementMessage());
				ranks.put(currentPlayer, rankAchieved);
			}
			else
				assertNull(failureStr, game.getRankAchievementMessage());
			
			assertEquals(failureStr, ranks, newRanks);
			
			// checking what's the next step...
			String newMsg = game.getNextStepMessage();
			if(ranks.size() == numPlayers) {
				assertNull(newMsg);
				break;
			}
			boolean gotOne = false;
			switch(face) {
				case 6:
					assertEquals(failureStr, expectedMsg, newMsg);
					assertEquals(failureStr, playerName + " got 6, so gets a chance to roll again", game.getSpecialRollMessage());
					break;

				case 1:
					player2bSkipped[currentPlayer] = true;
					assertEquals(failureStr, playerName + " got 1, so has to skip the next turn", game.getSpecialRollMessage());
					gotOne = true;
				default:
					if(!gotOne)
						assertNull(failureStr, game.getSpecialRollMessage());
					
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
			assertEquals("Score target must be between 1 and " + GameOfDice.MAX_SCORE_TARGET + ". Invalid score target specified: " + scoreTarget, ex.getMessage());
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
		long seed = System.currentTimeMillis();//1605384073173L; //1605387478444L;
		//for(int i = 0; i < 100; i++)
			//System.out.println(new Random(0 + i).nextInt(2));
		String failureStr = "Failure with seed " + seed;
		_testPlayerSequencePredictabilityBySeed(seed);
		int[][] timesEachPlayerInEachPosition = new int[numPlayers][numPlayers];
		for(int trial = 0; trial < numPlayers * numPlayers * 3; trial++) {
			GameOfDice game = GameOfDice.newInstance(seed + trial);
			game.setNumPlayers(numPlayers);
			List<Integer> seq = game.getPlayerSequence();
			//System.out.println(seq);
			_testPlayerSequenceToBeValid(failureStr, numPlayers, seq);
			
			for(int i = 0; i < seq.size(); i++) {
				timesEachPlayerInEachPosition[seq.get(i)][i]++;
			}
			
			List<Integer> seqAgain = game.getPlayerSequence();
			assertEquals(failureStr, seq, seqAgain);
		}
		//System.out.println(Arrays.deepToString(timesEachPlayerInEachPosition));
		int unvisitedCount = 0;
		for(int player = 0; player < numPlayers; player++)
			for(int pos = 0; pos < numPlayers; pos++)
				if(timesEachPlayerInEachPosition[player][pos] < 1)
					unvisitedCount++;
		assertTrue(failureStr, unvisitedCount < 2);
	}
	
	private void _testPlayerSequencePredictabilityBySeed(long seed) {
		GameOfDice game;
		String failureStr = "Failure with seed " + seed;
		game = GameOfDice.newInstance(seed);
		List<Integer> seq1 = game.getPlayerSequence();
		game = GameOfDice.newInstance(seed);
		List<Integer> seq2 = game.getPlayerSequence();
		assertEquals(failureStr, seq1, seq2);
	}
	
	private void _testPlayerSequenceToBeValid(String failureStr, int numPlayers, List<Integer> sequence) {
		assertEquals(failureStr, numPlayers, sequence.size());
		boolean[] encountered = new boolean[numPlayers];
		sequence.forEach(player -> {
			assertTrue(failureStr + ": player " + player, 0 <= player && player < numPlayers);
			assertFalse(failureStr, encountered[player]);
			encountered[player] = true;
		});
	}
}
