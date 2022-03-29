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

# Breakout Part 2

In this second part of the Breakout project (course project for the course Objectgericht Programmeren), you will extend the system of Part 1 with special types of blocks and special behavior of ball and paddle.
You will also implement a Facade object that will allow us to more easily test your implementation.

## Start from model solution or your own

For the second iteration, you are allowed to start from the model solution of the first iteration or your own solution of the first iteration and you are even allowed to mix and match code from both (at your own responsibility, of course).
Some of the explanations below refer to classes like `Rect` that have been added in the model solution.
You are required to start from a few extra source files provided by us that can be found [here](https://gitlab.kuleuven.be/distrinet/education/ogp/ogp-project-2021-2022-iteratie-2/-/tree/main/Breakout%20-%20opgave%202).
Additionally, the assignment (i.e.\ this text) is available online [here](https://gitlab.kuleuven.be/distrinet/education/ogp/ogp-project-2021-2022-iteratie-2/-/blob/main/README.md).
Although we strive to keep any changes to the assignment to a minimum, it is possible that we will publish changes or corrections on this git repository.
You are required to regularly check the repository for updates until the deadline. 

## Implement a Facade

To make our life easier, you must now implement a facade class `breakout.BreakoutFacade` provided by us.
This object contains various methods that allow us to more easily test and grade your implementation.
Your implementation of these methods should consist of very simple code that simply forwards the invocation to the relevant method in the other classes you've implemented.

## Game Update Frequency

In the previous assignment, the game used a very high refresh rate of 1KHz.
This was unrealistically high and meant in practice that the real refresh rate came to depend on the speed of the computer being used to play the game.
In this iteration, we will change to a more realistic refresh rate of 50Hz.
However, to ensure smooth motion, we will measure the real number of milliseconds elapsed between updates and use this to calculate the vector traveled by a ball moving at a certain velocity.
We provide a new version of the class `breakout.gui.GameView` which implements this.
It relies on `breakout.BreakoutFacade` for accessing information about the objects you've implemented.
Because of the change in game update frequency, you must modify the method `BreakoutState.tick` to accept an extra argument: `BreakoutState.tick(int paddleDir, int elapsedTime)` and take it into account where relevant.

## Blocks

In Part 1, all blocks behaved the same. In Part 2, there are different types of blocks.
Make BlockState into an abstract class that generalizes over all types of blocks in the game.
Implement the relevant methods in the facade class (see above), so that the following letters in a game description are interpreted as follows:
- 'S': a "sturdy" block, which is only destroyed after it is hit three times by a ball.
- '!': A "powerup" block: after the ball hits this block, it bounces back as usual.
       However, the ball is replaced by a "supercharged ball" (with the same position and velocity), which behaves differently for the next 10 seconds.
       If the ball hits and destroys other blocks during this period, it does not bounce back, but simply continues moving in the same direction.
       However, if the ball hits a block and the block does not disappear, the ball bounces back as usual.
- 'R': a replication block: after the ball hits this block, it is immediately destroyed, but the paddle temporarily changes into a "replicator" paddle.
       The next 3 times that a ball hits the paddle, the ball will be replicated into 4 resp. 3 resp. 2 balls.
       After that, the paddle becomes normal again.

Every different type of block must be drawn on screen in a different color.
Additionally, when the ball or paddle is temporarily in a special state, this must also be indicated to the user by drawing it in a different color.
The color of a sturdy block must differ based on how many times it still needs to be hit before it is destroyed.

The class BreakoutState must deal only with blocks generally, not with specific types of blocks specifically.

## Make BallState mutable

Furthermore, the class BallState that used to be immutable in the first iteration of this project must now be made mutable (classes BlockState and PaddleState must remain immutable).
In other words, a ball that is moved, changes velocity or otherwise changes, must now continue to be represented by the same object of type BallState, but the change must be reflected by modifications to the state of this object.
To reflect the change in meaning of the abstraction, you should rename the class `BallState` to `Ball`.
The class BallState must be abstract and there must be two subclasses representing a normal ball and a supercharged ball.
The methods in BallState must be documented with specifications that specify (in a reasonable way) the behavior that is common between all subclasses.
Specifications in subclasses need not be more precise than the parent specification, with one exception.
There must be a method `BallState.hitBlock(Rect rect)` that is invoked when the ball hits a block, and this method is responsible for changing the velocity of the ball in accordance.
The specifications of this method must be strictly more precise in subclasses than in the superclass BallState.

To accomodate this change, you should remove all invariants in `BreakoutState` which mention BallStates.

## What to submit

You have to submit all classes and interfaces of your implementation, all of which must reside in package `breakout`.

In addition to the methods specified above, you may add any additional methods you deem useful to any of these classes.
You may also add additional classes or interfaces that you find useful.

For this Part, you must write complete formal documentation for all classes, methods and interfaces you add as part of this assignment.
Make sure to respect behavioral subtyping. (Note: behavioral subtyping is not in the scope of course H02C5A; students of course H02C5A need not write any documentation at all.)

We also expect you to write a test suite for all classes you add, which tests the expected behavior of your implementations.

We also expect you to monitor the discussion forum on Toledo, where clarifications or corrections of the project assignment may be communicated.

To obtain a score of 3/6 or 4/6, your solution must compile without errors, and must pass a sufficiently high fraction of the official test cases and our own secret test suite. To obtain a score of 5/6 or 6/6, your solution must pass all of the official test cases, not use instanceof or otherwise perform explicit case analysis on the type of ball, block or paddle state; instead, it must only use dynamic binding to realize behavior that differs between the types of ball, block, or paddle.

