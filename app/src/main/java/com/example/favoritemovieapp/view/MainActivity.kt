package com.example.favoritemovieapp.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoritemovieapp.adapter.MovieAdapter
import com.example.favoritemovieapp.databinding.ActivityMainBinding
import com.example.favoritemovieapp.model.Movie
import com.example.favoritemovieapp.viewmodel.MovieViewModel

/**
 * MainActivity displays the list of movies and serves as the primary interface for interacting
 * with the movie collection. It provides functionalities such as searching, viewing details,
 * editing, deleting movies, and navigating to the favorites and add/edit screens.
 */
class MainActivity : AppCompatActivity() {

    // View binding instance to access views from the layout.
    private lateinit var binding: ActivityMainBinding

    // Obtain the MovieViewModel instance using the activity's scope.
    private val movieViewModel: MovieViewModel by viewModels()

    // Adapter for RecyclerView to manage and display movie items.
    private lateinit var adapter: MovieAdapter

    // Local copy of movies list used for filtering operations.
    private var moviesList: List<Movie> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the activity layout using view binding.
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the MovieAdapter with an empty mutable list.
        // Define actions for editing and deleting movies.
        adapter = MovieAdapter(mutableListOf(),
            onEdit = { movie ->
                // On edit, navigate to MovieDetailsActivity to view details and possibly add to favorites.
                val intent = Intent(this, MovieDetailsActivity::class.java)
                intent.putExtra("movie", movie) // Pass the selected movie to the details screen.
                startActivity(intent)
            },
            onDelete = { movie ->
                // On delete, call the ViewModel to delete the movie.
                // Once deletion is complete, reload the movies.
                movieViewModel.deleteMovie(movie) { movieViewModel.loadMovies() }
            }
        )

        // Configure the RecyclerView with a linear layout manager and the initialized adapter.
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Observe changes to the list of movies from the ViewModel.
        movieViewModel.movies.observe(this) { list ->
            moviesList = list // Store the current list for filtering.
            adapter.updateList(list) // Update the adapter with the new list of movies.
            // Display a message if the movie list is empty.
            binding.textViewEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }

        // Set click listener for the "Add Movie" button to launch the Add/Edit screen.
        binding.addButton.setOnClickListener {
            startActivity(Intent(this, AddEditActivity::class.java))
        }

        // Add a TextWatcher to the search EditText to enable dynamic filtering.
        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            // After text is changed, call the filter function with the new query string.
            override fun afterTextChanged(s: Editable?) {
                filterMovies(s.toString())
            }
            // No action needed before the text changes.
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            // No action needed during the text change.
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })

        // Set click listener for the "Favorites" button to navigate to FavoritesActivity.
        binding.buttonFavorites.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }
    }

    /**
     * Filters the movie list based on the provided query.
     * If the query is empty, the original list is restored. Otherwise,
     * movies are filtered by checking if the title, studio, or description contains
     * the query string (case-insensitive).
     *
     * @param query The search query input by the user.
     */
    private fun filterMovies(query: String) {
        val filtered = if (query.isEmpty()) {
            moviesList // If query is empty, do not filter.
        } else {
            // Filter movies where the title, studio, or description contains the query string.
            moviesList.filter { movie ->
                movie.title.toLowerCase().contains(query.toLowerCase()) ||
                        movie.studio.toLowerCase().contains(query.toLowerCase()) ||
                        movie.description.toLowerCase().contains(query.toLowerCase())
            }
        }
        // Update the adapter with the filtered list.
        adapter.updateList(filtered)
        // Update the empty state visibility based on whether any movies match the query.
        binding.textViewEmpty.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
    }

    /**
     * Reloads the movies from the ViewModel when the activity resumes.
     * This ensures the displayed list is always up to date.
     */
    override fun onResume() {
        super.onResume()
        movieViewModel.loadMovies()
    }
}
