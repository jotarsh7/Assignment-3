package com.example.favoritemovieapp.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoritemovieapp.adapter.MovieAdapter
import com.example.favoritemovieapp.databinding.ActivityFavoritesBinding
import com.example.favoritemovieapp.viewmodel.MovieViewModel
import android.view.View

/**
 * FavoritesActivity displays a list of favorite movies.
 * It uses a RecyclerView with a MovieAdapter to show the movies.
 * Users can navigate to view or edit details of their favorite movies,
 * as well as delete movies from their favorites list.
 */
class FavoritesActivity : AppCompatActivity() {

    // Binding instance to access views defined in the ActivityFavorites layout.
    private lateinit var binding: ActivityFavoritesBinding

    // Obtain an instance of MovieViewModel to interact with movie data.
    private val movieViewModel: MovieViewModel by viewModels()

    // Adapter for the RecyclerView that will display the list of favorite movies.
    private lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using view binding and set it as the content view.
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the adapter with an empty mutable list and define actions for edit and delete.
        adapter = MovieAdapter(mutableListOf(),
            onEdit = { movie ->
                // Navigate to MovieDetailsActivity for viewing/editing movie details.
                val intent = Intent(this, MovieDetailsActivity::class.java)
                // Pass the selected movie object to the MovieDetailsActivity.
                intent.putExtra("movie", movie)
                startActivity(intent)
            },
            onDelete = { movie ->
                // Delete the selected movie from favorites using the ViewModel.
                // After deletion, reload the list of favorites.
                movieViewModel.deleteFavorite(movie) { movieViewModel.loadFavorites() }
            }
        )

        // Set the layout manager for RecyclerView to arrange items linearly.
        binding.recyclerFavorites.layoutManager = LinearLayoutManager(this)
        // Attach the adapter to the RecyclerView.
        binding.recyclerFavorites.adapter = adapter

        // Observe changes in the favorites LiveData from the ViewModel.
        movieViewModel.favorites.observe(this) { list ->
            // Update the adapter with new data.
            adapter.updateList(list)
            // Show or hide the "empty" view message based on whether the list is empty.
            binding.textViewEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }

        // Setup click listener on a navigation button for moving back to MainActivity.
        binding.buttonSearchNav.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    // When the activity resumes, reload the list of favorite movies.
    override fun onResume() {
        super.onResume()
        movieViewModel.loadFavorites()
    }
}
