package com.example.madtodolist

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NotesDatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_TITLE TEXT, " +
                "$COLUMN_CONTENT TEXT)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "notesapp.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "allnotes"
        const val COLUMN_ID = "_id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_CONTENT = "content"
    }
    fun insertNote(note: Note){

        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
        }

        db.insert(TABLE_NAME, null, values)

        db.close()

    }
    fun getAllNotes(): List<Note> {

        val notesList = mutableListOf<Note>() // Creates an empty mutable list of notes

        val db = readableDatabase // Assuming db is initialized elsewhere to connect to the database

        val query = "SELECT * FROM $TABLE_NAME" // SQL query to select all records from the table "TABLE_NAME"

        val cursor = db.rawQuery(query, null) // Executes the query and stores the results in a cursor

        while (cursor.moveToNext()) { // Loops through each row of the results

            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)) // Gets the value of the "COLUMN_ID" column and converts it to an int

            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)) // Gets the value of the "COLUMN_TITLE" column and converts it to a string

            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)) // Gets the value of the "COLUMN_CONTENT" column and converts it to a string

            val note = Note(id, title, content) // Creates a new Note object with the retrieved data

            notesList.add(note) // Adds the new Note object to the list
        }

        cursor.close() // Closes the cursor to release resources

        db.close() // Closes the database connection

        return notesList // Returns the list of all notes
    }
    fun updateNote (note: Note){
        val db = writableDatabase

        val values = ContentValues().apply { this.put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)

        }
        val whereClause = "$COLUMN_ID = ?"

        val whereArgs = arrayOf(note.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)

        db.close()
    }

    fun getNoteByID (noteId: Int): Note{
        val db = readableDatabase

        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $noteId"
        val cursor = db.rawQuery(query,  null)

        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow (COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow (COLUMN_CONTENT))

        cursor.close()
        db.close()

        return Note(id, title, content)

    }

    fun deleteNote (noteId: Int){
        val db = writableDatabase

        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(noteId.toString())

        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()

    }




}
