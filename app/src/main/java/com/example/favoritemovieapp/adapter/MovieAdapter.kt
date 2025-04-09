package com.example.favoritemovieapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.favoritemovieapp.databinding.ItemMovieBinding
import com.example.favoritemovieapp.model.Movie
import com.bumptech.glide.Glide

/**
 * MovieAdapter is a RecyclerView adapter responsible for binding movie data to the list view.
 * It uses the layout defined in ItemMovieBinding for each movie item.
 *
 * @param movies A mutable list of Movie objects representing the data source.
 * @param onEdit A lambda function that is invoked when a movie item is edited.
 * @param onDelete A lambda function that is invoked when a movie item is deleted.
 */
class MovieAdapter(
    private val movies: MutableList<Movie>,
    private val onEdit: (Movie) -> Unit,
    private val onDelete: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    /**
     * Inner class that represents the ViewHolder for a movie item.
     * It holds an instance of the binding class for the movie item layout.
     *
     * @param binding The binding object for the movie item layout.
     */
    inner class MovieViewHolder(val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * Called when RecyclerView needs a new ViewHolder for a movie item.
     * It inflates the movie item layout and wraps it into a MovieViewHolder.
     *
     * @param parent The parent ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new instance of MovieViewHolder with the inflated layout.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        // Inflate the movie item layout using the parent's context
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    /**
     * Called by RecyclerView to display data at the specified position.
     * This method binds the movie data to the views in the layout.
     *
     * @param holder The ViewHolder which should be updated.
     * @param position The position of the movie item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.binding.apply {
            // Set the movie title to the titleText view.
            titleText.text = movie.title
            // Set the studio name to the studioText view.
            studioText.text = movie.studio
            // Set the critics rating to the ratingText view converting it to a String.
            ratingText.text = movie.criticsRating.toString()

            // Load the movie poster image using Glide library.
            Glide.with(posterImage.context)
                .load(movie.imageUrl)
                .into(posterImage)

            // Set click listener on the editButton to invoke the onEdit lambda.
            editButton.setOnClickListener { onEdit(movie) }

            // Set click listener on the deleteButton to invoke the onDelete lambda.
            deleteButton.setOnClickListener { onDelete(movie) }
        }
    }

    /**
     * Returns the total number of movie items in the data source.
     *
     * @return The size of the movies list.
     */
    override fun getItemCount(): Int = movies.size

    /**
     * Updates the adapter's movie list with new data.
     * Clears the old list, adds the new items, and notifies RecyclerView to rebind the views.
     *
     * @param newList The new list of Movie objects.
     */
    fun updateList(newList: List<Movie>) {
        movies.clear()
        movies.addAll(newList)
        notifyDataSetChanged()
    }
}
