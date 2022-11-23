package com.example.examination

import android.util.Log
import java.sql.*
import kotlin.reflect.typeOf

class MySQL {
    companion object {
        var cn: Connection? = null
        var ps: Statement? = null
    }


    fun disconnect() {
        ps?.close()
        cn?.close()
    }

    fun connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver")
            cn = DriverManager.getConnection(
                "jdbc:mysql://zlaa-mysql.mysql.database.azure.com:3306/android?useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true&allowPublicKeyRetrieval=true",
                "zlaa",
                "531531mqy."
            )
            ps = cn?.createStatement()
            Log.e("MySQL", "succeed")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: SQLException) {
            Log.e("MySQL", "ERROR")
            e.printStackTrace()
        } catch (e: java.net.SocketException) {
            Log.e("MySQL", "java.net.SocketException")
            e.printStackTrace()
        }
    }
}