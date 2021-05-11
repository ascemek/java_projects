'''
Execution:    python3 craps.py

	Note that your Python interpreter might have a different name. For example your execution
	command might look more like one of the following:
	% python craps.py
	% py craps.py
	% py3 craps.py

The casino game of craps for one player, without betting. Plays one game by default.
Commandline parameters can be used to run the game in test mode (plays until forced
to yield the dice) or to compute the odds of winning over a given number of games.

% python3 craps.py
	Simulates one game of craps: Phase I Come Out + Phase II Point.

% python3 craps.py test
	Plays until forced to yield the dice.

% python3 craps.py odds
	Computes the odds of winning craps over a default number of 10,000 games

% python3 craps.py odds [int number of games]
	Computes the odds of winning craps over the given number of games.

@author Caitrin Eaton
'''

import random	# Java hint: import java.util.Random;
import sys		# Java hint: this import is unnecessary in Java 

def roll2( verbose ):
	''' 
	Roll a pair of 6-sided dice.
	@param verbose (bool) true if the throw should be printed to the terminal
	@return sum (int) the sum of 2 rolled dice
	'''
	sides = 6
	throw1 = random.randint(1, sides)
	throw2 = random.randint(1, sides)
	sum = throw1 + throw2
	if verbose:
		print("\tThrew", throw1, "+", throw2, "=", sum)
	return sum

def comeOut( verbose ):
	''' 
	Phase I of the game of craps: The "Come Out." Roll until the "point" is established.
	@param verbose (bool) true if Phase I's progress should be printed, throw by throw
	@return point (int) the value of the die that must be rolled again before a 7
	'''
	if verbose:
		print("Phase I, the Come Out")
	target = roll2( verbose )
	if verbose:
		print("\tTarget established:", target)
	return target

def point( target, verbose ):
	''' 
	Phase II of the game of craps: The "Point." Roll until the "point" is repeated
	(the player wins) or the user sevens out (loses).
	@param target (int) the dice total established during the come out.
	@param verbose (bool) true if Phase II's progress should be printed, throw by throw
	@return win (bool) true if the player wins the game, false if they seven out
	'''
	if verbose:
		print("Phase II, the Point")
	total = roll2( verbose )
	while ((total != 7) and (total != target)):
		total = roll2( verbose )
	win = total == target
	return win

def playToReplay( verbose ):
	'''
	Play a game of craps: the "come out" (phase I) and the "point" (phase II).
	@param verbose (bool) true if the game's progress should be printed, throw by throw
	@return replay (bool) true if the player is allowed to play again, false if they must give up the dice.
	'''
	replay = False
	target = comeOut( verbose )
	if (target==7 or target==11):
		# Rolling a 7 or 11 on the first try is a "natural" that immediately wins the game.
		# But, a player who rolls a natural cannot play again. They must pass the dice.
		if verbose:
			print("A natural! You've won the game, but must yield the dice to the next player.")
		replay = False
	elif (target==2 or target==3 or target==12):
		# Rolling a 2, 3, or 12 on the first try is called "crapping out."
		# The player immediately loses, but can play again.
		if verbose:
			print("You've crapped out, but may play again.")
		replay = True
	else:
		# The target is established. The player must roll the target again before rolling a 7.
		# The player can play again if they "pass" (win), but not if they "7 out" (lose).
		replay = point( target, verbose )
		if verbose:
			if replay:
				print("A pass! You've won the game, and may play again.")
			else:
				print("You've sevened out, and must yield the dice to the next player.")
	return replay



def playToWin( verbose ):
	'''
	Play a game of craps: the "come out" (phase I) and the "point" (phase II).
	@param verbose (bool) true if the game's progress should be printed, throw by throw
	@return win (bool) true if the player won, false if they lost
	'''
	win = False
	target = comeOut( verbose )
	if (target==7 or target==11):
		# Rolling a 7 or 11 on the first try is a "natural" that immediately wins the game.
		# But, a player who rolls a natural cannot play again. They must pass the dice.
		if verbose:
			print("A natural! You've won the game, but must yield the dice to the next player.")
		win = True
	elif (target==2 or target==3 or target==12):
		# Rolling a 2, 3, or 12 on the first try is called "crapping out."
		# The player immediately loses, but can play again.
		if verbose:
			print("You've crapped out, but may play again.")
		win = False
	else:
		# The target is established. The player must roll the target again before rolling a 7.
		# The player can play again if they "pass" (win), but not if they "7 out" (lose).
		win = point( target, verbose )
		if verbose:
			if win:
				print("A pass! You've won the game, and may play again.")
			else:
				print("You've sevened out, and must yield the dice to the next player.")
	return win

def test():
	'''
	Test the game of craps by replaying until the player sevens out.
	'''
	print("Welcome to the craps table.")
	replay = playToReplay( True )
	while replay:
		print("\nPlaying again...")
		replay = playToReplay( True );

def computeOdds( numGames ):
	'''
	Compute the odds: how likely it is to win the game of craps.
	@param numGames (int) the number of craps games over which the odds will be calculated
	@return odds (float) the percent of games won: naturals + passes out of all numGames games
	'''
	wins = 0
	for game in range(numGames):
		if playToWin( False ) == True:
			wins = wins + 1
	odds = wins / numGames # Caution: potential source of a curious Java bug...
	print("\nOut of", numGames, "games:")
	print("\tWins:", wins)
	print("\tOdds:", odds)
	return odds

def main( args ):
	if len(args) > 1:
		if args[1] == "test":
			test()	# Make sure that the craps game is functioning as expected
		elif args[1] == "odds":
			numGames = 1000
			if len(args) > 2:
				numGames = int( args[2] )
			computeOdds( numGames ) # How likely is it to win the game of craps?
	else:
		playToWin( True ) # Play a normal game

if __name__=="__main__":
	main( sys.argv )