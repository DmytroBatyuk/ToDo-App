package ua.batyuk.dmytro.todoapp.common


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import atownsend.swipeopenhelper.BaseSwipeOpenViewHolder

typealias OnItemClick<T> = (position: Int, item: T) -> Unit

class GenericAdapter<BindingT : ViewDataBinding, T>(
    private val bindingCreator: (inflater: LayoutInflater, viewGroup: ViewGroup, viewType: Int) -> BindingT,
    private val binder: (binding: BindingT, item: T, position: Int) -> Unit,
    private val getSwipeView:(binding: BindingT) -> View,
    private val getEndHiddenViewSize:(binding: BindingT) -> Float
) : RecyclerView.Adapter<GenericAdapter<BindingT, T>.ViewHolder>() {

    private val list = arrayListOf<T>()

    var onItemClick: OnItemClick<T>? = null

    fun updateList(list: List<T>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            bindingCreator(LayoutInflater.from(parent.context), parent, viewType),
            binder
        )

    override fun getItemCount(): Int =
        list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }


    inner class ViewHolder(
        private val binding: BindingT,
        private val binder: (binding: BindingT, item: T, position: Int) -> Unit
    ) : BaseSwipeOpenViewHolder(binding.root) {
        private var item: T? = null

        fun bind(item: T, position: Int) {
            this.item = item
            binder(binding, item, position)
            binding.root.setOnClickListener { onItemClick?.invoke(position, item) }
        }

        override fun getSwipeView(): View {
            return getSwipeView(binding)
        }

        override fun getEndHiddenViewSize(): Float {
            return getEndHiddenViewSize(binding)
        }

        override fun getStartHiddenViewSize(): Float =
            0f
    }
}