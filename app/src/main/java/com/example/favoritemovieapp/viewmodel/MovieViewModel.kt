package com.example.favoritemovieapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.favoritemovieapp.model.Movie
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * MovieViewModel handles operations related to movies and favorites,
 * interacting with both the Firebase Firestore database and Firebase Authentication.
 * It provides LiveData for both the main movies list and the favorites list.
 */
class MovieViewModel : ViewModel() {

    // Firebase Firestore instance to perform database operations.
    private val db = FirebaseFirestore.getInstance()
    // Firebase Authentication instance to get the current user's UID.
    private val auth = FirebaseAuth.getInstance()

    // Backing property for the movies list from the "movies" collection.
    // Only movies belonging to the current user are fetched.
    private val _movies = MutableLiveData<List<Movie>>()
    // Public immutable LiveData for observing changes to the movies list.
    val movies: LiveData<List<Movie>> get() = _movies

    // Backing property for the favorites list from the "favorites" collection.
    private val _favorites = MutableLiveData<List<Movie>>()
    // Public immutable LiveData for observing changes to the favorites list.
    val favorites: LiveData<List<Movie>> get() = _favorites

    // Initialization block that loads both the movies and favorites for the current user.
    init {
        loadMovies()    // Load the main list of movies for the current user.
        loadFavorites() // Load the current user's list of favorite movies.
    }

    /**
     * Loads movies from the "movies" collection in Firestore for the current user.
     * Filters the movies by comparing the document's "uid" field with the current user's UID.
     * Upon successful retrieval, updates the _movies LiveData with the list of movies.
     */
    fun loadMovies() {
        // Get the current user's UID. If not logged in, exit the function.
        val uid = auth.currentUser?.uid ?: return
        db.collection("movies")
            .whereEqualTo("uid", uid) // Filter documents where "uid" matches.
            .get()
            .addOnSuccessListener { snapshot ->
                // Map each document to a Movie object and assign the document ID to movie.id.
                val list = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Movie::class.java)?.copy(id = doc.id)
                }
                // Update the LiveData with the retrieved list of movies.
                _movies.value = list
            }
    }

    /**
     * Adds a new movie to the "movies" collection in Firestore.
     * Adds the current user's UID to the movie object before saving.
     *
     * @param movie The movie object to be added.
     * @param onComplete Callback function that is called upon successful addition.
     */
    fun addMovie(movie: Movie, onComplete: () -> Unit) {
        // Ensure the current user is logged in by retrieving the UID.
        val uid = auth.currentUser?.uid ?: return
        // Create a new movie object including the current user's UID.
        val movieWithUid = movie.copy(uid = uid)
        db.collection("movies").add(movieWithUid)
            .addOnSuccessListener { onComplete() }
    }

    /**
     * Updates an existing movie in the "movies" collection.
     *
     * @param movie The movie object with updated data.
     * @param onComplete Callback function that is called upon successful update.
     */
    fun updateMovie(movie: Movie, onComplete: () -> Unit) {
        db.collection("movies").document(movie.id)
            .set(movie)
            .addOnSuccessListener { onComplete() }
    }

    /**
     * Deletes a movie from the "movies" collection.
     *
     * @param movie The movie object to be deleted.
     * @param onComplete Callback function that is called upon successful deletion.
     */
    fun deleteMovie(movie: Movie, onComplete: () -> Unit) {
        db.collection("movies").document(movie.id)
            .delete()
            .addOnSuccessListener { onComplete() }
    }

    /**
     * Loads favorite movies from the "favorites" collection in Firestore for the current user.
     * Filters the favorites by the current user's UID.
     * Upon successful retrieval, updates the _favorites LiveData with the list of favorite movies.
     */
    fun loadFavorites() {
        // Ensure the current user is logged in by retrieving the UID.
        val uid = auth.currentUser?.uid ?: return
        db.collection("favorites")
            .whereEqualTo("uid", uid) // Filter documents where "uid" matches the current user.
            .get()
            .addOnSuccessListener { snapshot ->
                // Map each document to a Movie object and assign the document ID to movie.id.
                val list = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Movie::class.java)?.copy(id = doc.id)
                }
                // Update the LiveData with the retrieved list of favorite movies.
                _favorites.value = list
            }
    }

    /**
     * Adds a movie to the favorites list in the "favorites" collection.
     * The movie is copied with the current user's UID to ensure the favorite is user-specific.
     *
     * @param movie The movie object to be added to favorites.
     * @param onComplete Callback function that is called upon successful addition.
     */
    fun addToFavorites(movie: Movie, onComplete: () -> Unit) {
        // Get the current user's UID. If not logged in, exit the function.
        val uid = auth.currentUser?.uid ?: return
        // Create a copy of the movie with the current user's UID for proper association.
        val favMovie = movie.copy(uid = uid)
        db.collection("favorites").add(favMovie)
            .addOnSuccessListener { onComplete() }
    }

    /**
     * Updates an existing favorite movie in the "favorites" collection.
     *
     * @param movie The favorite movie object with updated data.
     * @param onComplete Callback function that is called upon successful update.
     */
    fun updateFavorite(movie: Movie, onComplete: () -> Unit) {
        db.collection("favorites").document(movie.id)
            .set(movie)
            .addOnSuccessListener { onComplete() }
    }

    /**
     * Deletes a movie from the favorites list in the "favorites" collection.
     *
     * @param movie The favorite movie object to be deleted.
     * @param onComplete Callback function that is called upon successful deletion.
     */
    fun deleteFavorite(movie: Movie, onComplete: () -> Unit) {
        db.collection("favorites").document(movie.id)
            .delete()
            .addOnSuccessListener { onComplete() }
    }
}
