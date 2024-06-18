package com.tapyou.myPointsGraph.ui.mainscreen.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tapyou.myPointsGraph.R
import com.tapyou.myPointsGraph.entities.Point

class PointsAdapter : ListAdapter<Point, PointsAdapter.PointViewHolder>(PointsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_point, parent, false)
        return PointViewHolder(view)
    }

    override fun onBindViewHolder(holder: PointViewHolder, position: Int) {
        val point = getItem(position)
        holder.bind(point)
    }

    class PointViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textX: TextView = itemView.findViewById(R.id.textX)
        private val textY: TextView = itemView.findViewById(R.id.textY)
        private val context = itemView.context

        fun bind(point: Point) {
            textX.text = context.getString(R.string.point_x, point.x)
            textY.text = context.getString(R.string.point_y, point.y)
        }
    }

    class PointsDiffCallback : DiffUtil.ItemCallback<Point>() {
        override fun areItemsTheSame(oldItem: Point, newItem: Point): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Point, newItem: Point): Boolean {
            return oldItem.x == newItem.x && oldItem.y == newItem.y
        }
    }
}
