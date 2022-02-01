package com.example.delevryproject.data.local.db.dao

import androidx.room.*
import com.example.delevryproject.data.entitiy.locaion.LocationLatLngEntity


@Dao
interface LocationDao {

    @Query("SELECT * FROM LocationLatLngEntity WHERE id=:id")
    suspend fun get(id: Long): LocationLatLngEntity?

    //LocationLatLngEntity가 들어왔을 때 아이디가 기존의 것과 같으면 들어온 것으로 대체한다.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(locationLatLngEntity: LocationLatLngEntity)

    @Query("DELETE FROM LocationLatLngEntity WHERE id=:id")
    suspend fun delete(id: Long)

}