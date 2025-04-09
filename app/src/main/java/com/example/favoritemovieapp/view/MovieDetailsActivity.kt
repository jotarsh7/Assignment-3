package com.example.favoritemovieapp.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.favoritemovieapp.databinding.ActivityMovieDetailsBinding
import com.example.favoritemovieapp.model.Movie
import com.example.favoritemovieapp.viewmodel.MovieViewModel

/**
 * MovieDetailsActivity displays the details of a specific movie.
 * Users can view movie details, add the movie to favorites, or edit the movie.
 *
 * The activity uses an ActivityResultLauncher to receive updates from the AddEditActivity.
 */
class MovieDetailsActivity : AppCompatActivity() {

    // View binding instance to access views in the layout defined by ActivityMovieDetails.
    private lateinit var binding: ActivityMovieDetailsBinding

    // Obtain an instance of MovieViewModel to perform operations on movie data.
    private val movieViewModel: MovieViewModel by viewModels()

    // Holds the currently displayed movie. Initialized either from the Intent or after an edit.
    private var currentMovie: Movie? = null

    // Register an ActivityResultLauncher to start the AddEditActivity and receive a result.
    private val editMovieLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // This block is executed when the launched activity returns a result.
        if (result.resultCode == Activity.RESULT_OK) {
            // Retrieve the updated movie object from the returned data.
            val updatedMovie = result.data?.getSerializableExtra("updatedMovie") as? Movie
            if (updatedMovie != null) {
                // Update the current movie with the updated data.
                currentMovie = updatedMovie
                // Refresh the UI to display the updated movie details.
                updateUI(updatedMovie)
                // Notify the user that the movie has been updated.
                Toast.makeText(this, "Movie updated", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using view binding.
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the movie passed to this activity from the previous screen.
        currentMovie = intent.getSerializableExtra("movie") as? Movie
        // If a movie was provided, update the UI with its details.
        currentMovie?.let { movie ->
            updateUI(movie)
        }

        // Set up the "Add to Favorites" button click listener.
        binding.addToFavButton.setOnClickListener {
            currentMovie?.let { movie ->
                // Call the ViewModel to add the current movie to the favorites list.
                movieViewModel.addToFavorites(movie) {
                    Toast.makeText(this, "Movie added to favorites", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Set up the "Edit" button click listener to launch the AddEditActivity for editing.
        binding.editButton.setOnClickListener {
            currentMovie?.let { movie ->
                // Create an Intent to navigate to AddEditActivity.
                val intent = Intent(this, AddEditActivity::class.java)
                // Pass the current movie data so it can be edited.
                intent.putExtra("movie", movie)
                // Launch AddEditActivity and wait for the updated movie result.
                editMovieLauncher.launch(intent)
            }
        }

        // Set up the "Back" button click listener to finish the current activity.
        binding.backButton.setOnClickListener { finish() }
    }

    /**
     * Updates the UI elements with the movie details.
     *
     * @param movie The movie object whose details are to be displayed.
     */
    private fun updateUI(movie: Movie) {
        // Display the movie title.
        binding.movieTitleText.text = movie.title
        // Display the movie studio.
        binding.movieStudioText.text = movie.studio
        // Display the movie description.
        binding.movieDescText.text = movie.description
        // Display the movie's critics rating.
        binding.movieRatingText.text = movie.criticsRating.toString()
        // Load and display the movie image using the Glide library.
        Glide.with(this)
            .load(movie.imageUrl)
            .into(binding.movieImage)
    }
}
