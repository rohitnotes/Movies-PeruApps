package peruapps.movies.ui.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import peruapps.movies.databinding.ItemMovieBinding

class ListMoviesAdapter(private val onClickItemListener: OnClickItemListener) :
    RecyclerView.Adapter<ListMoviesAdapter.ListMoviesViewHolder>() {

    private var list: MutableList<MovieModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ListMoviesViewHolder(
        ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = list?.size ?: 0

    override fun onBindViewHolder(holder: ListMoviesViewHolder, position: Int) {
        list?.get(position)?.run { holder.bind(this) }
    }

    fun addMovies(movies: MutableList<MovieModel>) {
        if (list == null) {
            list = mutableListOf()
        }
        list?.addAll(movies)
        notifyItemRangeInserted(list?.size ?: 0, movies.size)
    }

    fun onClickItem(view: View, movieModel: MovieModel) {
        onClickItemListener.onClickItem(view, movieModel)
    }

    inner class ListMoviesViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movieModel: MovieModel) {
            binding.movie = movieModel
            binding.adapter = this@ListMoviesAdapter
            binding.executePendingBindings()
        }
    }

    interface OnClickItemListener {
        fun onClickItem(view: View, movie: MovieModel)
    }
}