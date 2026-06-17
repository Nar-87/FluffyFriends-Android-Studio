package com.example.fluffyfriends;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBDao extends SQLiteOpenHelper {
    private static final String DB_NAME = "fluffyfriends.db";
    private static final int DB_VERSION = 3; //subida de versión por haber cambiado la foto_uri a foto_path (o borrar el emulador, es lo mismo)
    public static final String TABLE_USUARIOS = "usuarios";
    public static final String TABLE_MASCOTAS = "mascotas";
    public static final String TABLE_SERVICIOS = "servicios";

    public DBDao(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE usuarios (" +
                        "id TEXT PRIMARY KEY," +
                        "nombre TEXT," +
                        "email TEXT," +
                        "password TEXT," +
                        "telefono TEXT," +
                        "foto_path TEXT)"
        );
        db.execSQL(
                "CREATE TABLE mascotas (" +
                        "id TEXT PRIMARY KEY," +
                        "usuario_id TEXT," +
                        "nombre TEXT," +
                        "raza TEXT," +
                        "sexo TEXT," +
                        "tipo TEXT," +
                        "color TEXT," +
                        "descripcion TEXT," +
                        "observaciones TEXT," +
                        "foto_path TEXT)"
        );
        db.execSQL(
                "CREATE TABLE servicios (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "mascota_id TEXT," +
                        "tipo TEXT," +
                        "descripcion TEXT," +
                        "fecha TEXT," +
                        "precio REAL," +
                        "estado TEXT)"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS servicios");
        db.execSQL("DROP TABLE IF EXISTS mascotas");
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(db);
    }
    // Insertar mascotas
    public void insertarMascota(Mascota m, String usuarioId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();

        v.put("id", m.getId());
        v.put("usuario_id", usuarioId);
        v.put("nombre", m.getNombre());
        v.put("raza", m.getRaza());
        v.put("sexo", m.getSexo());
        v.put("tipo", m.getTipo());
        v.put("color", m.getColor());
        v.put("descripcion", m.getDescripcion());
        v.put("observaciones", m.getObservaciones());
        v.put("foto_path", m.getFotoPath());

        db.insert(TABLE_MASCOTAS, null, v);
    }
    //Actualizar mascota
    public void actualizarMascota(Mascota m) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put("nombre", m.getNombre());
        v.put("raza", m.getRaza());
        v.put("sexo", m.getSexo());
        v.put("tipo", m.getTipo());
        v.put("color", m.getColor());
        v.put("descripcion", m.getDescripcion());
        v.put("observaciones", m.getObservaciones());
        v.put("foto_path", m.getFotoPath());

        db.update(TABLE_MASCOTAS, v, "id=?", new String[]{m.getId()});
    }
    //Otener mascota
    public List<Mascota> obtenerMascotas(String usuarioId) {
        SQLiteDatabase db = getReadableDatabase();
        List<Mascota> lista = new ArrayList<>();

        Cursor c = db.rawQuery(
                "SELECT * FROM mascotas WHERE usuario_id=?",
                new String[]{usuarioId}
        );

        while (c.moveToNext()) {
            Mascota m = new Mascota();
            m.setId(c.getString(c.getColumnIndexOrThrow("id")));
            m.setNombre(c.getString(c.getColumnIndexOrThrow("nombre")));
            m.setRaza(c.getString(c.getColumnIndexOrThrow("raza")));
            m.setSexo(c.getString(c.getColumnIndexOrThrow("sexo")));
            m.setTipo(c.getString(c.getColumnIndexOrThrow("tipo")));
            m.setColor(c.getString(c.getColumnIndexOrThrow("color")));
            m.setDescripcion(c.getString(c.getColumnIndexOrThrow("descripcion")));
            m.setObservaciones(c.getString(c.getColumnIndexOrThrow("observaciones")));
            m.setFotoPath(c.getString(c.getColumnIndexOrThrow("foto_path")));

            lista.add(m);
        }

        c.close();
        return lista;
    }
    //Eliminar mascota
    public void eliminarMascota(String mascotaId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("servicios", "mascota_id=?", new String[]{mascotaId});
        db.delete("mascotas", "id=?", new String[]{mascotaId});
    }
    // Servicios
    public void eliminarServicio(int servicioId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_SERVICIOS, "id = ?", new String[]{String.valueOf(servicioId)});
    }
    // Login usuarios
    public Usuario login(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT * FROM " + TABLE_USUARIOS + " WHERE email = ? AND password = ?",
                new String[]{email, password}
        );
        if (c.moveToFirst()) {
            Usuario u = new Usuario();
            u.setId(c.getString(c.getColumnIndexOrThrow("id")));
            u.setNombre(c.getString(c.getColumnIndexOrThrow("nombre")));
            u.setEmail(c.getString(c.getColumnIndexOrThrow("email")));
            u.setPassword(c.getString(c.getColumnIndexOrThrow("password")));
            u.setTelefono(c.getString(c.getColumnIndexOrThrow("telefono")));
            u.setFotoPath(c.getString(c.getColumnIndexOrThrow("foto_path")));
            c.close();
            return u;
        }
        c.close();
        return null;
    }
    // Obtener servicios
    public List<ServicioMascota> obtenerServicios(String mascotaId) {
        SQLiteDatabase db = getReadableDatabase();
        List<ServicioMascota> lista = new ArrayList<>();

        Cursor c = db.rawQuery(
                "SELECT * FROM " + TABLE_SERVICIOS + " WHERE mascota_id = ?",
                new String[]{mascotaId}
        );

        while (c.moveToNext()) {
            ServicioMascota s = new ServicioMascota();
            s.setId(c.getInt(c.getColumnIndexOrThrow("id")));
            s.setMascotaId(mascotaId);
            s.setTipo(c.getString(c.getColumnIndexOrThrow("tipo")));
            s.setDescripcion(c.getString(c.getColumnIndexOrThrow("descripcion")));
            s.setFecha(c.getString(c.getColumnIndexOrThrow("fecha")));
            s.setPrecio(c.getDouble(c.getColumnIndexOrThrow("precio")));
            s.setEstado(c.getString(c.getColumnIndexOrThrow("estado")));
            lista.add(s);
        }

        c.close();
        return lista;
    }
    // Insertar usuario
    public boolean insertarUsuario(Usuario u) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put("id", u.getId());
        v.put("nombre", u.getNombre());
        v.put("email", u.getEmail());
        v.put("password", u.getPassword());
        v.put("telefono", u.getTelefono());
        v.put("foto_path", u.getFotoPath());

        return db.insert(TABLE_USUARIOS, null, v) != -1;
    }
    // Actualizar servicio
    public void actualizarServicio(ServicioMascota s) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("tipo", s.getTipo());
        v.put("descripcion", s.getDescripcion());
        v.put("fecha", s.getFecha());
        v.put("precio", s.getPrecio());
        v.put("estado", s.getEstado());

        db.update(
                TABLE_SERVICIOS,
                v,
                "id = ?",
                new String[]{String.valueOf(s.getId())}
        );
    }
    // Insertar servicio
    public long insertarServicio(String mascotaId, ServicioMascota s) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues v = new ContentValues();
        v.put("mascota_id", mascotaId);
        v.put("tipo", s.getTipo());
        v.put("descripcion", s.getDescripcion());
        v.put("fecha", s.getFecha());
        v.put("precio", s.getPrecio());
        v.put("estado", s.getEstado());

        return db.insert(TABLE_SERVICIOS, null, v);
    }
    public void actualizarUsuario(Usuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", usuario.getNombre());
        values.put("email", usuario.getEmail());
        values.put("telefono", usuario.getTelefono());
        values.put("foto_path", usuario.getFotoPath());

        db.update("usuarios", values, "id = ?", new String[]{usuario.getId()});
    }

}
