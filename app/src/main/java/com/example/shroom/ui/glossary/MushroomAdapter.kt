package com.example.shroom.ui.glossary

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shroom.R
import com.example.shroom.data.Edibility
import com.example.shroom.data.Mushroom
import com.example.shroom.databinding.ItemMushroomBinding

class MushroomAdapter(
    private val onClick: (Mushroom) -> Unit,
) : ListAdapter<Mushroom, MushroomAdapter.Holder>(Diff) {

    object Diff : DiffUtil.ItemCallback<Mushroom>() {
        override fun areItemsTheSame(oldItem: Mushroom, newItem: Mushroom) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Mushroom, newItem: Mushroom) = oldItem == newItem
    }

    class Holder(val binding: ItemMushroomBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemMushroomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val mushroom = getItem(position)
        val context = holder.itemView.context
        holder.binding.mushroomView.setImageResource(mushroom.imageRes)
        holder.binding.commonName.text = mushroom.commonName
        holder.binding.latinName.text = mushroom.latinName
        holder.binding.description.text = mushroom.description
        holder.binding.edibilitySeal.text = edibilityLabel(context, mushroom.edibility)
        val seal = ContextCompat.getDrawable(context, R.drawable.bg_wax_seal)?.mutate() as GradientDrawable
        seal.setColor(ContextCompat.getColor(context, edibilityColor(mushroom.edibility)))
        holder.binding.edibilitySeal.background = seal
        holder.binding.root.setOnClickListener { onClick(mushroom) }
    }
}

fun edibilityLabel(context: android.content.Context, edibility: Edibility): String =
    when (edibility) {
        Edibility.Edible -> context.getString(R.string.edible)
        Edibility.Caution -> context.getString(R.string.caution)
        Edibility.Poisonous -> context.getString(R.string.poisonous)
    }

fun edibilityColor(edibility: Edibility): Int =
    when (edibility) {
        Edibility.Edible -> R.color.forest_moss
        Edibility.Caution -> R.color.caution_amber
        Edibility.Poisonous -> R.color.seal_wax
    }
