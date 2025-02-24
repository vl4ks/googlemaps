package database

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.DriverManager

class DatabaseHelper {
    private const val URL = "jdbc:postgresql://localhost:5431/two_gis"
    private const val USER = "your_username"
    private const val PASSWORD = "your_password"

    suspend fun getConnection(): Connection? {
        return withContext(Dispatchers.IO) {
            try {
                Class.forName("org.postgresql.Driver")
                DriverManager.getConnection(URL, USER, PASSWORD)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}