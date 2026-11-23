package io.aparok.app.core_ui.features.sets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.aparok.app.core_ui.databinding.ItemViewerSortBinding
import io.aparok.app.core_ui.extensions.text
import io.aparok.app.core_utils.diff.DefaultDiffUtil
import io.aparok.app.core_utils.ext.gone
import io.aparok.app.core_utils.ext.visible
import io.aparok.app.presentation.sets.sort.ViewerSortViewModel.ScreenState
import io.aparok.app.presentation.sets.sort.ViewerSortViewModel.ViewerSortView

class ViewerSortAdapter(
    private val onViewerSortClicked: (ViewerSortView) -> Unit,
    private val onRemoveViewerSortClicked: (ViewerSortView) -> Unit
) : RecyclerView.Adapter<ViewerSortAdapter.ViewHolder>() {

    private var views = emptyList<ViewerSortView>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder.Sort(
            binding = ItemViewerSortBinding.inflate(
                inflater, parent, false
            )
        ).apply {
            itemView.setOnClickListener {
                onViewerSortClicked(views[bindingAdapterPosition])
            }
            binding.ivRemove.setOnClickListener {
                onRemoveViewerSortClicked(views[bindingAdapterPosition])
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is ViewHolder.Sort) holder.bind(views[position])
    }

    override fun getItemCount(): Int = views.size

    fun update(update: List<ViewerSortView>) {
        val differ = DefaultDiffUtil(old = views, new = update)
        val result = DiffUtil.calculateDiff(differ, false)
        views = update
        result.dispatchUpdatesTo(this)
    }

    sealed class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        class Sort(val binding: ItemViewerSortBinding) : ViewHolder(binding.root) {
            fun bind(item: ViewerSortView) = with(binding) {
                if (item.mode == ScreenState.READ) {
                    ivRemove.gone()
                    ivGo.visible()
                } else {
                    ivRemove.visible()
                    ivGo.gone()
                }
                tvTitle.text = item.name
                tvSubtitle.setText(item.type.text(item.format))
                ivRelation.bind(item.format)
            }
        }
    }
}