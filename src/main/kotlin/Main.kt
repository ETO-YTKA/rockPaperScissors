data class Player(val name: String, var item: String) {
    override fun toString(): String = "Player $name: $item"
    fun itemGeneration() = listOf("rock", "paper", "scissors").random().also { item = it }
    fun isHuman() = name.contains("Human")
}

fun main() = newGame()

fun newGame() {
    println("New game\n")
    val playersList = mutableListOf(Player("",""))
    userItem(playersList)
    action(botsNumber(playersList))
}

fun userItem(playersList: MutableList<Player>) {
    println("Rock, paper, scissors?")
    return when (val userChoice = readln().lowercase()) {
        "rock", "paper", "scissors" -> playersList[0] = Player("Human", userChoice)
        else -> {
            println("Invalid choice\n")
            userItem(playersList)
        }
    }
}

fun botsNumber(playersList: MutableList<Player>): MutableList<Player> {
    return try {
        println("How many bots?")
        val numberOfBots = readln().toInt()

        return if (numberOfBots <= 0) {
            println("Number must be greater than zero")
            botsNumber(playersList)
        } else {
            repeat(numberOfBots) {
                playersList.add(Player("${it + 2}", listOf("rock", "paper", "scissors").random()))
            }
            playersList
        }
    } catch (e: NumberFormatException) {
        println("The number must consist only of digits\n")
        botsNumber(playersList)
    }
}

fun itemsGen(playersList: MutableList<Player>): MutableList<Player> {
    for (i in 0..<playersList.size) {
        if (playersList[i].isHuman()) continue
        else playersList[i].itemGeneration()
    }
    return playersList
}

fun action(playersList: MutableList<Player>) {
    // only unique elements in the order in which they beat each other (paper, rock, scissors)
    val items = playersList.asSequence().map { it.item }.toMutableList().distinct().sorted().toMutableList()

    if (items.size == 3) nextRound(playersList, "Draw")
    else if (items.size == 1) nextRound(playersList, "everyone chose one item")
    else {
        // if (paper, rock) swap them to correct work
        if (items.contains("paper") && items.contains("scissors")) items[0] = "scissors".also { items[1] = "paper" }

        playersListPrint(playersList)
        val newPlayersList = playersList.filter { it.item == items[0] }.toMutableList() // only the winning players

        if (newPlayersList.size == 1) {
            println("Player ${newPlayersList.first().name} wins\n")
            newGame()
        } else {
            println("Round result:")
            nextRound(newPlayersList, "Ok")
        }
    }
}

fun playersListPrint(playersList: MutableList<Player>) = println(buildString { playersList.forEach { append("$it\n") } })

fun nextRound(playersList: MutableList<Player>, reason: String) {
    playersListPrint(playersList)
    println("$reason. New round:\n")
    if (playersList[0].isHuman()) {
        userItem(playersList)
    }
    action(itemsGen(playersList))
}

