package com.example.madtodolist

import android.os.Bundle
import android.widget.Toast // Add this import statement
import androidx.appcompat.app.AppCompatActivity

import com.example.madtodolist.databinding.ActivityUpdateNoteBinding

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateNoteBinding
    private lateinit var db: NotesDatabaseHelper

    private var noteId: Int = -1 // Corrected variable name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NotesDatabaseHelper(this)

        noteId = intent.getIntExtra("note_id", -1) // Corrected variable name

        if (noteId == -1) {
            finish()
            return
        }

        val note = db.getNoteByID(noteId)
        binding.updateTitleEditText.setText(note.title)
        binding.updateContentEditText.setText(note.content)

        binding.updateSaveButton.setOnClickListener {
            val newTitle = binding.updateTitleEditText.text.toString()
            val newContent = binding.updateContentEditText.text.toString()
            val updatedNote = Note(noteId, newTitle, newContent)

            db.updateNote(updatedNote)
            finish()

            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show()
        }

        // The rest of the code is likely related to updating the note
    }
}
