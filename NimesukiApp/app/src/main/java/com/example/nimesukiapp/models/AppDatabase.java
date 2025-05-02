package com.example.nimesukiapp.models;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.content.Context;

import com.example.nimesukiapp.models.dao.FavoritosDAO;
import com.example.nimesukiapp.models.dao.UsuarioDAO;
import com.example.nimesukiapp.models.entities.AnimeEntity;
import com.example.nimesukiapp.models.entities.FavoritosEntity;
import com.example.nimesukiapp.models.entities.UsuarioEntity;

@Database(entities = {UsuarioEntity.class, AnimeEntity.class, FavoritosEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UsuarioDAO usuarioDAO();
    public abstract AnimeDAO animeDAO();
    public abstract FavoritosDAO favoritosDAO();

    public static AppDatabase getDatabase(Context context) {
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "bd_nimesuki").allowMainThreadQueries().build();
        return db;
    }
}
