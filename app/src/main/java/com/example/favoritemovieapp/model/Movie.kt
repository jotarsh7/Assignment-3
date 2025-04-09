// Package declaration for the Movie data model.
package com.example.favoritemovieapp.model

// Import Serializable to allow Movie objects to be serialized.
// Serialization is useful for transferring objects between activities or saving state.
import java.io.Serializable

/**
 * Data class representing a Movie entity.
 *
 * This class encapsulates all the attributes for a movie and implements Serializable,
 * which allows instances of Movie to be passed between Android components.
 *
 * @property id Unique identifier for the movie.
 * @property title The title of the movie.
 * @property studio The studio or production company that produced the movie.
 * @property description A brief description or synopsis of the movie.
 * @property imageUrl URL string for the movie poster or cover image.
 * @property criticsRating The rating assigned to the movie by critics, represented as a Double.
 * @property uid Unique identifier for the user who owns or added the movie.
 *              Used for filtering movies specific to a user in the main list.
 */
data class Movie(
    var id: String = "",
    var title: String = "",
    var studio: String = "",
    var description: String = "",
    var imageUrl: String = "",
    var criticsRating: Double = 0.0,
    var uid: String = ""
) : Serializable
