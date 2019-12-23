package io.github.cerveme.crud_kotlin.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import io.github.cerveme.crud_kotlin.model.Cliente
import kotlinx.android.synthetic.main.activity_configuracao.*

class DatabaseHelper(context: Context,
                     factory: SQLiteDatabase.CursorFactory?) :
        SQLiteOpenHelper(context, DATABASE_NAME,
                factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE " +
                TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME
                + " TEXT" + ")")
        db.execSQL(CREATE_PRODUCTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    public fun dropDatabase() {
        val db = this.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun addCliente(name: Cliente) {
        val values = ContentValues()
        values.put(COLUMN_NAME, name.codigo_cliente)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllClientes(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun getCodigoCliente(): String {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        if(cursor!!.count > 0) {
            cursor!!.moveToFirst()
            do {
                return cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME))
            } while (cursor.moveToNext())
        }else{
            return "0"
        }
        cursor.close()
    }

    fun deleteAllClientes() {
        val values = ContentValues()
        //  values.put(COLUMN_NAME, name.codigo_cliente)
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close()
    }



    fun hasCodigoCadastrado(): Boolean {
        val cursor = getAllClientes()
        return cursor!!.count > 0

    }

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "cerveme.db"
        val TABLE_NAME = "cliente"
        val COLUMN_ID = "pk_cliente"
        val COLUMN_NAME = "codigo_cliente"
    }
}