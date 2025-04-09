package com.example.favoritemovieapp.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import com.example.favoritemovieapp.databinding.ActivityAddEditBinding
import com.example.favoritemovieapp.model.Movie
import com.example.favoritemovieapp.viewmodel.MovieViewModel

/**
 * AddEditActivity allows users to add a new movie or edit an existing movie.
 *
 * In add mode, the user can enter all details about a movie.
 * In edit mode, the user can only update the description; all other details are displayed as read-only.
 */
class AddEditActivity : AppCompatActivity() {

    // Binding object to access the views defined in the activity's XML layout.
    private lateinit var binding: ActivityAddEditBinding

    // ViewModel instance to handle movie-related operations and data management.
    private val movieViewModel: MovieViewModel by viewModels()

    // Holds the current movie to be edited; null if we're adding a new movie.
    private var currentMovie: Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using view binding.
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the passed movie object from the intent if it exists.
        currentMovie = intent.getSerializableExtra("movie") as? Movie
        if (currentMovie != null) {
            // In edit mode: pre-fill the form with the movie's existing details.
            binding.titleEdit.setText(currentMovie!!.title)
            binding.studioEdit.setText(currentMovie!!.studio)
            binding.descEdit.setText(currentMovie!!.description)
            binding.posterEdit.setText(currentMovie!!.imageUrl)
            binding.ratingEdit.setText(currentMovie!!.criticsRating.toString())

            // Disable fields other than the description so that only the description can be edited.
            binding.titleEdit.isEnabled = false
            binding.studioEdit.isEnabled = false
            binding.posterEdit.isEnabled = false
            binding.ratingEdit.isEnabled = false

            // Change the submit button text to indicate an update operation.
            binding.submitButton.text = "Update"
        } else {
            // In add mode: set the submit button text to indicate adding a new movie.
            binding.submitButton.text = "Add Movie"
        }

        // Set up the submit button click listener.
        binding.submitButton.setOnClickListener {
            if (currentMovie != null) {
                // Edit mode: only the description is editable.
                val updatedDescription = binding.descEdit.text.toString()
                // Validate that the description is not empty.
                if (updatedDescription.isEmpty()) {
                    Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                // Create an updated copy of the current movie with the new description.
                val updatedMovie = currentMovie!!.copy(
                    description = updatedDescription
                )
                // Update the movie in the ViewModel. Once completed, return the updated movie as the result.
                movieViewModel.updateMovie(updatedMovie) {
                    // Prepare an Intent with the updated movie as extra data.
                    val resultIntent = Intent().apply { putExtra("updatedMovie", updatedMovie) }
                    // Set the result to be returned to the caller activity.
                    setResult(Activity.RESULT_OK, resultIntent)
                    // Finish the activity.
                    finish()
                }
            } else {
                // Add mode: collect all inputs from the user.
                val title = binding.titleEdit.text.toString()
                val studio = binding.studioEdit.text.toString()
                val desc = binding.descEdit.text.toString()
                val imageUrl = binding.posterEdit.text.toString()
                // Attempt to convert the rating input to a Double; if conversion fails, default to 0.0.
                val rating = binding.ratingEdit.text.toString().toDoubleOrNull() ?: 0.0

                // Validate that all required fields (for adding a movie) are filled.
                if (title.isEmpty() || studio.isEmpty() || imageUrl.isEmpty()) {
                    Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Create a new Movie object with the provided details.
                val newMovie = Movie(
                    id = "", // ID will be generated/set in the ViewModel or backend.
                    title = title,
                    studio = studio,
                    description = desc,
                    imageUrl = imageUrl,
                    criticsRating = rating,
                    uid = "" // UID will be set in the ViewModel based on the current user.
                )
                // Add the new movie using the ViewModel.
                movieViewModel.addMovie(newMovie) {
                    Toast.makeText(this, "Movie added", Toast.LENGTH_SHORT).show()
                    // Finish the activity after adding the new movie.
                    finish()
                }
            }
        }

        // Set up the cancel button to simply finish the activity without saving any changes.
        binding.cancelButton.setOnClickListener { finish() }
    }
}
