package com.example.sqlitekotlinexample.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.sqlitekotlinexample.AddOrEditActivity
import com.example.sqlitekotlinexample.R
import com.example.sqlitekotlinexample.models.Tasks


class TaskRecyclerAdapter(tasksList: List<Tasks>, internal var context: Context) : RecyclerView.Adapter<TaskRecyclerAdapter.TaskViewHolder>() {

    var tasksList: List<Tasks> = ArrayList()

    init {
        this.tasksList = tasksList

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_tasks, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val tasks = tasksList[position]
        holder.name.text = tasks.name
        holder.desc.text = tasks.desc
        holder.developer.text = tasks.developer
        holder.time.text = tasks.time
        holder.category.text = tasks.catagory.toString()
        if (tasks.completed == "Y")
            holder.list_item.background = ContextCompat.getDrawable(context,
                R.color.colorPrimaryDark
            )
        else
            holder.list_item.background = ContextCompat.getDrawable(context, R.color.colorUnSuccess)


        if(tasks.catagory == 0){
            holder.category.text= "Low"
            holder.category.setTextColor(ContextCompat.getColor(context,R.color.yellow))

        }
        if(tasks.catagory == 1){
            holder.category.text= "Medium"
            holder.category.setTextColor(ContextCompat.getColor(context,R.color.orange))

        }
        if(tasks.catagory == 2){
            holder.category.text= "High"
            holder.category.setTextColor(ContextCompat.getColor(context,R.color.red))

        }

        holder.itemView.setOnClickListener {
            val i = Intent(context, AddOrEditActivity::class.java)
            i.putExtra("Mode", "Edit")
            i.putExtra("Id", tasks.id)

            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    fun filterList(filterlist: List<Tasks>) {
        tasksList = filterlist
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.tvName) as TextView
        var desc: TextView = view.findViewById(R.id.tvDesc) as TextView
        var list_item: LinearLayout = view.findViewById(R.id.list_item) as LinearLayout
        val developer : TextView = view.findViewById(R.id.tvDeveloper)as TextView
        val time : TextView = view.findViewById(R.id.tvTime)as TextView
        val category : TextView = view.findViewById(R.id.tvCategory)as TextView
        val listBackground : LinearLayout = view.findViewById((R.id.listBackground)) as LinearLayout
    }
}
