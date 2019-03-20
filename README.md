# TicTacTix
A 3D Tic-Tac Toe game written in Java

----

## Requirements

1. Your program should start by asking the user whether they would like to go first or
not. You should use proper input validation so that only “y”, “Y”, “n”, or “N” are
accepted as valid responses.

2. The Tic-Tactix board should be neatly displayed as three separate 3x3 grids
(horizontally, side-by-side) using ASCII characters to divide the cells. Include numbers
for each grid level, column, and row to assist the user in selecting where to put their
next “X”.

3. The game should alternate turns between the user (“X”) and computer (“O”). After
each makes a move, check if there is a winner or a stalemate. To win, the user or
computer must have 3 in a row -- either horizontally, vertically, or diagonally on the
same layer or between layers. A stalemate occurs when all of the cells are filled, but
no one has won.

4. When getting the user’s move (i.e., layer, row, and column), use input validation and
exception handling techniques. If a grid cell is already full, the user should not be
allowed to put their “X” into it, and asked for a different cell.
5. Use random number generation to make the computer’s move. If a grid cell is already
full, the computer should randomly pick another location until an empty cell is found.

6. When a winner is detected, or there is a stalemate, the game should display an
appropriate message and end the program.

7. Your program should include a classes called TicTacTix, and TicTacTixTest with an
efficient main() method. Model it after the ConnectFourGame exercise in U3-3 Q2.

8. Your program should demonstrate your mastery of the style rules we have discussed so
far this year including JavaDoc comments, code comments, and good variable names.

9. After you have successfully completed requirements 1 through 8, enhance your game
so that whenever the user wins the game asks for their name and appends it to a text
file called “HallOfFame.txt”. Before requirement #1, your program should display the
names stored in the Hall Of Fame, number each name starting with 1. If the
“HallOfFame.txt” file does not exist, then the message “No Human Has Ever Beat Me..
mwah-ha-ha-ha!” should be displayed instead.

10. In Tic-Tac-Toe (and Tic-Tactics) the first player can always win (or at least stalemate)
by selecting the center grid cell and then mirroring their opponent’s moves. After you
have successfully completed requirements 1 through 8, enhance your game so that the
center cell of the middle layer is reserved; that is, neither the user nor computer can
put an “X” or “O” into it. This will make the game more challenging. Be sure to
indicate that the center grid cell in the middle layer is not selectable when you
display the layers.
