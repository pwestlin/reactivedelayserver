package nu.westlin.reactivedelayserver

import java.io.File

fun main() {
    LogAnalyzer().analyze("Pantamera")
}

class LogAnalyzer {

    data class LogRow(val timestamp: String, val message: String)

    fun analyze(applicationName: String) {
        val logRows = File("logs/application.log").readLines()
            .filter { it.contains("DelayController") }
            .filter { it.contains(applicationName) }
            .sorted()
            .map { row ->
                val split = row.split(": ")
                LogRow(split[0].substring(11, 23), split.last())
            }

        logRows.forEach { println("${it.timestamp} : ${it.message}") }

        val timestamps = logRows.map { it.timestamp }
        timestamps.forEach { timestamp ->
            println("$timestamp : ${noOfActive(timestamp, logRows)}")
        }
    }

    private fun noOfActive(timestamp: String, logRows: List<LogRow>): Int {
        val indexOfFirst = logRows.indexOfFirst { it.timestamp == timestamp }
        val list = logRows.filterIndexed { index, row -> index <= indexOfFirst }
        return list.count { it.message.endsWith("start") } - list.count { it.message.endsWith("done") }
    }
}