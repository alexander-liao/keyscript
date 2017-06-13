`SLEEP`: Sleeps for $1 milliseconds  
`DELAY`: Delays the execution for $1 milliseconds of the next $2 lines and skips the next $2 lines. Works in parallel.  
`PRESS`: Presses the key with code $2...$N with a delay of $1, including at the end  
`RELEASE`: Releases the key with code $N...$2 with a delay of $1  
`SEQUENCE`: Performs PRESS on $3...$N, using $1 as a pause factor, and RELEASE on $N...$3, using $2 as a pause factor  
`TYPE`: Attempts to type the string $3, with down delay $1 and up delay $2. Unknown characters will be skipped.  
`MOVE`: Moves the mouse to ($1, $2)  
`DRAG`: Drags the mouse to ($1, $2)  
`RIGHT-DRAG`: Drags the mouse with button 2 to ($1, $2)  
`ANIMATE-MOVE`: Moves the mouse to ($1, $2) in $3 steps, delaying by $4 milliseconds each step  
`ANIMATE-DRAG`: Drags the mouse to ($1, $2) in $3 steps, delaying by $4 milliseconds each step, waiting $5 milliseconds after pressing and $6 milliseconds after releasing  
`ANIMATE-RIGHT-DRAG`: ANIMATE-DRAG except with RIGHT-DRAG  
`MOUSEDOWN`: Presses button $1  
`MOUSEUP`: Releases button $1  
`MOUSECLICK`: MOUSEDOWN $1; SLEEP $2; MOUSEUP $1  
`LEFT-CLICK`: MOUSECLICK 1 $1  
`RIGHT-CLICK`: MOUSECLICK 2 $1  
`WHEEL-CLICK`: MOUSECLICK 3 $1  
`LOCATION`: Gets the pointer location and sets it on the stack; first Y then X  
`GOTO`: Goes to line $1  
`INPUT`: Gets the input and puts it on the stack as a string  
`RAW-INPUT`: Gets the input and unescapes backslash escape codes  
`INT`: Parses $1 to an integer and pushes it onto the stack  
`LONG`: Parses $1 to a long and pushes it onto the stack  
`DOUBLE`: Parses $1 to a double and pushes it onto the stack  
`FLOAT`: Parses $1 to a float and pushes it onto the stack  
`SHORT`: Parses $1 to a short and pushes it onto the stack  
`BYTE`: Parses $1 to a byte and pushes it onto the stack  
`STRING`: Turns $1 to a string and pushes it onto the stack  
`CHAR`: Parses $1 into a char; if it is a number type, floor, take absolute value, and cast.  
`CHARS`: Splits $1 into its individual characters  
`CODE`: Gets the keycode of $1. Throws `RuntimeException` if the top is not a character. Integers are treated as characters.  
`BURY`: Pops the top of the stack and rotates it to the bottom.  
`DIG`: Pulls the bottom of the stack off and pushes it.  
`FLIP`: Flips the stack around.  
`QUEUE`: The interpreter will treat the array as a queue instead.  
`STACK`: The interpreter will treat the array as a stack instead.  
`IS-STACK`: Pushes 1 onto the stack or 0 onto the queue  
`IS-QUEUE`: Pushes 0 onto the stack or 1 onto the queue  
`POP`: Pops the top of the stack off.  
`PUSH`: Pushes $1 to the stack. Note that `PUSH` with no arguments will do nothing because it takes the top of the stack and pushes it  
`COPY`: Copies the top of the stack  
`STRETCH`: Stretches the stack by $1 times. Essentially, each element is repeated in-place, so `[1, 2, 3]` stretched 4 times would become `[1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3]`.  
`ADD`: $1 + $2  
`SUBTRACT`: $1 - $2  
`MULTIPLY`: $2 * $2. String multiplcation by a negative number reverses. Fractional string multiplication works by concatenating part of the string.  
`DIVIDE`: $1 / $2  
`EXPONENTIATE`: $1 ** $2  
`DIFFERENCE`: Pushes the absolute difference of the top two elements of the stack  
`ABSOLUTE-VALUE`: Takes the absolute value of the top of the stack  
`PRINT`: Prints $1 or `command[6:]`.  
`PRINT-RAW`: Same as PRINT except it doesn't unescape  
`REVERSE`: Reverses $1 and pushes it onto the stack. Automatically converts to a string.  
`QUIT`: Terminates the program  
`IF`: If $1, then execute the next $2 lines; otherwise, skip them  
`UNESCAPE`: Unescapes $1  
  
Note: If not enough arguments are provided, elements will be pulled off the stack.  
Note: If the argument given is `\\`, then it will pull something off the stack and use it instead.  