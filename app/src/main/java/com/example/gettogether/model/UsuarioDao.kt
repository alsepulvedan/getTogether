package com.example.gettogether.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarSesion(usuario: UsuarioEntity)

    @Query("SELECT * FROM usuarios LIMIT 1")
    fun obtenerUsuarioActivo(): Flow<UsuarioEntity?>

    @Query("DELETE FROM usuarios")
    suspend fun cerrarSesion()
}