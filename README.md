# ethereum-bytecode-analyzer
Find contracts in the Ethereum blockchain, analyze its bytecode and filter by JSON custom rules

## ~~This project is not ready yet. Work in progress...~~
### Discontinued at the moment. There are already similar tools that I cannot catch up

### It supports (or ~~will~~):
#### Done
* Bytecode disassembling
* Control Flow Graph (static)
* Control Flow Graph (dynamic/symbolic)
* Keeps a trace of every word used as output/input of any opcode involved in the execution of a function
#### ~~In progress~~ / partial
* Blockchain navigation
* Ethereum Virtual Machine implemented
* Symbolic execution
* Execution and analysis of all possible paths changing the values of the configured inputs
#### ~~Future/~~ Wanted
* Process engine to filter via custom rules
* Can check if there are stack elements at the end of every execution and try to remove the unnecessary operations that introduced them, reducing the gas of deploying and execution
* Reproduce a given transaction from the blockchain and draws a graph with the EVM state in every step, allowing debugging in one go
