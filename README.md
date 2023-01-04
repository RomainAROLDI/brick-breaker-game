# Brick-breaker-game

As part of my Full-Stack Developer Bachelor's degree (RNCP title "Software and Network Project Manager"
Web Engineering specialty) at Metz Numeric School, I chose to create a brick breaker game for 
the evaluation of the Java course. _Thanks to Franck Bansept for his knowledge_ :)

## Installation

Clone the repository on your machine.  
`git clone https://github.com/RomainAROLDI/brick-breaker-game.git`

Quickly start a game (make sure you are in the cloned folder).  
`java -jar out/artifacts/brick_breaker_game_jar/brick-breaker-game.jar`

You are free to configure the game as you wish in the Game class.

## What is a brick breaker game?

A brick breaker game is a type of arcade game in which the player controls a paddle at the bottom 
of the screen and bounces a ball back and forth against a wall of bricks at the top of the screen.

## How does my game work?

The objective of the game is to destroy all the bricks by hitting them with the ball and to keep the ball from falling 
off the bottom of the screen. You lose a life if the ball falls. If your life balance reaches 0, you lose the game!

**Warning**, the speed of the ball increases by dint of breaking bricks and some bricks must be touched several times before breaking.

### Bonuses

Bonuses help you win the game. They sometimes appear when you break a brick. If that is the case,
a colored cube goes down and you must catch it with the paddle to activate the bonus, otherwise it is lost. 

Each color represents a different bonus:
- **MAGENTA** spawns an extra ball
- **RED** extends paddle size by 1.25x
- **BLUE** slows down all balls currently in play