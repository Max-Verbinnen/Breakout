# Breakout
The submission deadline for Part I is Friday, 25 March 2022, 15h30.

As part of this assignment, you are provided with a Java program that implements a (simplified) Breakout game, except that the implementations of classes `BallState`, `BlockState`, `PaddleState`, `BreakoutState` are not yet complete and there are some remaining TODOs in `GameView` and `GameMap`.
You must complete this implementation.

`BallState`, `BlockState` and `PaddleState` must be implemented as @immutable classes.
They must keep account of the objects' current position and size.
Additionally, balls must keep track of their current velocity, i.e. the direction and speed they are currently moving.

The most difficult method to implement is the `BreakoutState.tick()` method.
This method must do the following (in this order):
- Move all balls one step forward according to their current velocity.
- Check whether any balls hit the walls on the left, right and top side of the game area, in which case they must bounce back.
- Check whether any balls hit the bottom of the field, in which case they must be removed from the game.
- Check whether any ball hit any block, in which case the block must be removed from the game and the ball must bounce back.
- Check whether any ball hit the paddle, in which case it must bounce back.
  Additionally, the ball must speed up by one fifth of the current velocity of the paddle.

You must also implement the `BreakoutState.isWon()`, `BreakoutState.isDead()`, `BreakoutState.movePaddleLeft()` and `BreakoutState.movePaddleRight()` methods:
- the game is won if there is still at least one ball and no blocks left.
- the player is dead if there is no ball left.
- the paddle must move left/right by de-/increasing the x coordinate by 10 for every time step.

Some hints and clarifications:
- It is not necessary to specify the precise behavior of `BreakoutState.tick()`, `BreakoutState.movePaddleLeft()` and `BreakoutState.movePaddleRight()`.
- We have provided classes `Vector` and `Point`, which provide some useful methods.
- The whole game should be implemented without the use of floating point numbers, because this makes it easier to document the code correctly.
- When a ball moving with velocity v hits a flat, hard surface with its outermost point in direction d, the velocity after the bounce v' can be computed by mirroring v over direction d.
  In this formula, (v . d) represents the dot product of two vectors and ||d|| represents the length of vector d.
  If d is normalized, i.e. ||d|| = 1, then dividing by ||d|| is of course not necessary.
  You may use the method `Vector.mirrorOver` which implements this computation already.
- To avoid weird effects, it is important that a ball only bounces on an object when the direction from the ball (vector d) to that object is at a blunt angle from the ball's current velocity (vector v).
  You can check this easily by verifying that the dot product of the two vectors (d . b) is positive.

# Requirements

For Part I of the Objectgericht Programmeren project, you shall complete the implementations of these classes, in accordance with the provided documentation and the above requirements and clarifications, such that a working (simplified) Breakout game is obtained. You shall ensure that these classes are properly encapsulated.

You shall also insert complete public and internal formal documentation for these classes.

You shall deal with invalid constructor or method calls defensively.

You shall also write a test suite for these classes that tests all of the code you wrote, except for code that runs only in cases where the client performed an invalid method call or an invalid constructor call.

The solution you submit for Part I counts for 4 out of the 20 points for the project.

# Minimum requirements

To obtain 2 out of 4 points for Part I, the following conditions must be met:
- The code you submit must compile without errors when compiled with FSC4J (i.e. there must be no errors reported in Eclipse's Problems view for this program, after installing the FSC4J plugin).
- You did not make any changes to the provided code; you only added the requested missing implementations and documentation.
- It must include a test suite that tests the correctness of all of the code you wrote, and there must be no test failures or errors when running your project with JUnit in an installation of Eclipse that has the FSC4J plugin installed.
- The constructor(s) of `BreakoutState` must apply defensive programming to deal with invalid parameters.
  Constructors of `BallState`, `BlockState` and `PaddleState`  must apply contractual programming to deal with invalid parameters.
- Your code must pass our own (secret) test suite.
- It must be clear from the formal documentation you write that you have a basic understanding of how to properly document single-object abstractions.

When you submit your solution, an automatic check will be performed that checks some of these requirements (including compiling your code and running both your test suite and our own test suite, in the presence of FSC4J).
If the check fails, you will have an opportunity to submit a corrected solution, provided the submission deadline has not yet passed.
The automated tests that we perform are far from complete and it is important that you also implement an additional test suite of your own, to validate all the intended behavior of your code.

Detailed submission instructions and the automatic check will be available by Friday, 11 March 2022, 16h00.

# Extra requirements

To obtain 3 out of 4 points for Part I, additionally, your implementation and your test suite must be complete and correct, and your formal documentation must be reasonably complete as regards @throws, @post, @invar, @immutable, and @representationObject clauses.
Additionally, you must not duplicate code for detecting collisions between (1) balls and blocks, (2) balls and paddles and (3) balls and walls, by introducing and using a new well-documented abstraction for representing rectangles.
Your solution must implement all the requirements
Furthermore, `BreakoutState` must have an invariant that specifies that the paddle is located entirely within the game field and this invariant must be enforced correctly.

To obtain 4 out of 4 points, additionally, your formal documentation must be complete and correct, not just with respect to the tags mentioned above, but with respect to the other tags used in the course as well (including @inspects and @mutates).
Additionally, `BreakoutState` must have invariants that specify that all blocks and balls are located entirely within the game field and these invariants must be enforced correctly.
