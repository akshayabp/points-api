package com.fetchrewards.pointsapi.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fetchrewards.pointsapi.constants.ErrorCodes;
import com.fetchrewards.pointsapi.model.PointsTracker;
import com.fetchrewards.pointsapi.model.Transaction;
import com.fetchrewards.pointsapi.model.User;

@Service
public class PointsServiceImpl implements PointsService{

	@Autowired
	private UserService userService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Override
	public Transaction addPoints(String userId, Transaction transaction) {

		User user = this.userService.get(userId);

		if (user == null) {
			throw new RuntimeException(ErrorCodes.USER_DOES_NOT_EXIST);
		}

		String payer = transaction.getPayer();
		long points = transaction.getPoints();

		PriorityQueue<Transaction> transactions = user.getTransactions();
		Map<String, Long> payerPointsMap = user.getPayerPointsMap();

		if (points > 0) {
			transactions.add(transaction);
		} else if (points < 0) {
			// get all payer transactions

			if (!payerPointsMap.containsKey(payer)) {
				throw new RuntimeException(ErrorCodes.PAYER_DOES_NOT_EXIST);				
			}

			long pointsToSpend = -1 * points;

			long totalPayerPoints = payerPointsMap.get(payer);

			if (pointsToSpend > totalPayerPoints) {
				throw new RuntimeException(ErrorCodes.PAYER_INSUFFICENT_BALANCE);
			}

			transactions.add(transaction);
		}

		long totalPayerPoints = payerPointsMap.getOrDefault(payer, 0l);
		totalPayerPoints = totalPayerPoints + points;

		payerPointsMap.put(payer, totalPayerPoints);
		
		long totalPoints = user.getTotalPoints();
		totalPoints=totalPoints+points;
		user.setTotalPoints(totalPoints);

		return transaction;

	}
	
	@Override
	public Map<String, Long> getPayerBalances(String userId){
		User user = this.userService.get(userId);

		if (user == null) {
			throw new RuntimeException(ErrorCodes.USER_DOES_NOT_EXIST);
		}
		
		return user.getPayerPointsMap();
	}

	@Override
	public List<PointsTracker> spendPoints(String userId, long totalPointsToSpend) {
		List<PointsTracker> pointsTracker = new ArrayList<>();

		User user = this.userService.get(userId);

		if (user == null) {
			throw new RuntimeException(ErrorCodes.USER_DOES_NOT_EXIST);
		}

		long totalPoints = user.getTotalPoints();

		if (totalPointsToSpend > totalPoints) {
			throw new RuntimeException(ErrorCodes.USER_INSUFFICENT_BALANCE);
		}

		long pointsToSpend = totalPointsToSpend;

		PriorityQueue<Transaction> transactions = user.getTransactions();
		Map<String, Long> payerPointsMap = user.getPayerPointsMap();

		Map<String, Long> payerPointsSpentMap = new HashMap<>();

		Map<String, Queue<Transaction>> payerPositiveTransactionsQueueMap = new HashMap<>();
		Map<String, Queue<Transaction>> payerNegativeTransactionsQueueMap = new HashMap<>();
		
		while (pointsToSpend > 0 && !transactions.isEmpty()) {
			Transaction currentTransaction = transactions.poll();
			String currentPayer = currentTransaction.getPayer();
			long currentTransactionPoints = currentTransaction.getPoints();

			long currentPayerPointsSpent = payerPointsSpentMap.getOrDefault(currentPayer, 0l);

			// check if there are any pending negative transactions for payer
			Queue<Transaction> payerNegativeTransactionsQueue = payerNegativeTransactionsQueueMap
					.getOrDefault(currentPayer, new LinkedList<>());
			payerNegativeTransactionsQueueMap.put(currentPayer, payerNegativeTransactionsQueue);
			
			
			
			Queue<Transaction> payerPositiveTransactionsQueue = payerPositiveTransactionsQueueMap
					.getOrDefault(currentPayer, new LinkedList<>());
			payerPositiveTransactionsQueueMap.put(currentPayer, payerPositiveTransactionsQueue);

			if (currentTransactionPoints > 0) {

				long currentTransactionPendingPoints = currentTransactionPoints;

				while (!payerNegativeTransactionsQueue.isEmpty() && currentTransactionPendingPoints > 0l) {

					Transaction negativeTransaction = payerNegativeTransactionsQueue.peek();

					if (currentTransactionPendingPoints + negativeTransaction.getPoints() > 0) {
						currentTransactionPendingPoints = currentTransactionPendingPoints
								+ negativeTransaction.getPoints();
						payerNegativeTransactionsQueue.poll();
					} else if (currentTransactionPendingPoints + negativeTransaction.getPoints() < 0) {
						negativeTransaction
								.setPoints(currentTransactionPendingPoints + negativeTransaction.getPoints());
						currentTransactionPendingPoints = 0l;
						break;
					} else if (currentTransactionPendingPoints + negativeTransaction.getPoints() == 0) {
						currentTransactionPendingPoints = currentTransactionPendingPoints
								+ negativeTransaction.getPoints();
						payerNegativeTransactionsQueue.poll();
						break;
					}

				}

				if (currentTransactionPendingPoints > 0l) {
					if (pointsToSpend < currentTransactionPendingPoints) {
						currentPayerPointsSpent = currentPayerPointsSpent + pointsToSpend;
						pointsToSpend = 0l;
					} else if (pointsToSpend > currentTransactionPendingPoints) {
						currentPayerPointsSpent = currentPayerPointsSpent + currentTransactionPendingPoints;
						pointsToSpend = pointsToSpend - currentTransactionPendingPoints;
					} else if (pointsToSpend == currentTransactionPendingPoints) {
						currentPayerPointsSpent = currentPayerPointsSpent + currentTransactionPendingPoints;
						pointsToSpend = 0l;
					}
					payerPointsSpentMap.put(currentPayer, currentPayerPointsSpent);

					currentTransaction.setPoints(currentTransactionPendingPoints);
					payerPositiveTransactionsQueue.add(currentTransaction);
				}

			} else if (currentTransactionPoints < 0l) {

				long currentTransactionPendingPoints = currentTransactionPoints;

				while (!payerPositiveTransactionsQueue.isEmpty() && currentTransactionPendingPoints < 0l) {
					Transaction positiveTransaction = payerPositiveTransactionsQueue.peek();

					if (currentTransactionPendingPoints + positiveTransaction.getPoints() < 0l) {
						currentTransactionPendingPoints = currentTransactionPendingPoints
								+ positiveTransaction.getPoints();
						payerPositiveTransactionsQueue.poll();
						
						// all the positions contributed by positive transaction need to be reverted from pointsToSpend
						currentPayerPointsSpent = currentPayerPointsSpent - positiveTransaction.getPoints();
						pointsToSpend = pointsToSpend + positiveTransaction.getPoints();
						
					} else if (currentTransactionPendingPoints + positiveTransaction.getPoints() > 0l) {
						positiveTransaction
								.setPoints(currentTransactionPendingPoints + positiveTransaction.getPoints());
						
						//only currentTransactionPendingPoints need to reverted from pointsToSpend
						
						pointsToSpend = pointsToSpend - currentTransactionPendingPoints;
						currentPayerPointsSpent = currentPayerPointsSpent + currentTransactionPendingPoints;
						
						currentTransactionPendingPoints = 0l;
						break;
					} else if (currentTransactionPendingPoints + positiveTransaction.getPoints() == 0l) {
						currentTransactionPendingPoints = currentTransactionPendingPoints
								+ positiveTransaction.getPoints();
						payerPositiveTransactionsQueue.poll();
						
						// all the positions contributed by positive transaction need to be reverted from pointsToSpend						
						currentPayerPointsSpent = currentPayerPointsSpent - positiveTransaction.getPoints();
						pointsToSpend = pointsToSpend + positiveTransaction.getPoints();
						
						break;
					}
				}
				
				if(currentTransactionPendingPoints<0l) {
					currentTransaction.setPoints(currentTransactionPendingPoints);
					payerNegativeTransactionsQueue.add(currentTransaction);
				}
				
				payerPointsSpentMap.put(currentPayer, currentPayerPointsSpent);
			}

		}
		
		//add all pending negative transactions back
		for (Map.Entry<String, Queue<Transaction>> payerPointsSpentEntry : payerNegativeTransactionsQueueMap.entrySet()) {
			Queue<Transaction> currentNegativeTransactions = payerPointsSpentEntry.getValue();
			if(!currentNegativeTransactions.isEmpty()) {
				transactions.addAll(currentNegativeTransactions);
			}
		}

		for (Map.Entry<String, Long> payerPointsSpentEntry : payerPointsSpentMap.entrySet()) {
			String currentPayer = payerPointsSpentEntry.getKey();
			long currentTransactionPoints = payerPointsSpentEntry.getValue();

			PointsTracker currentPayerPointsTracker = new PointsTracker();
			currentPayerPointsTracker.setPayer(currentPayer);
			currentPayerPointsTracker.setPoints(-1 * currentTransactionPoints);
			pointsTracker.add(currentPayerPointsTracker);

			payerPointsMap.put(currentPayer, payerPointsMap.get(currentPayer) - currentTransactionPoints);
		}
		
		totalPoints = totalPoints - pointsToSpend;
		user.setTotalPoints(totalPoints);

		return pointsTracker;
	}

}
