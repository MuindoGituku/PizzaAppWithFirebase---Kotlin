/**
 * @Group 1
 * @author Muindo Gituku (301372521)
 * @author Emre Deniz (301371047)
 * @date Nov 24, 2023
 * @description: Android Assignment 4
 */


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pizzaappwithfirebase.R
import com.example.pizzaappwithfirebase.models.PizzaModel

class CustomPizzasAdapter(private var pizzasList: List<PizzaModel>, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<CustomPizzasAdapter.PizzaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PizzaViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.individual_pizza_layout, parent, false)
        return PizzaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PizzaViewHolder, position: Int) {
        val pizza = pizzasList[position]
        holder.bind(pizza)
        holder.itemView.setOnClickListener { onItemClick(pizza.id) }
    }

    override fun getItemCount(): Int {
        return pizzasList.size
    }

    // Update the data in the adapter
    fun updateData(newData: List<PizzaModel>) {
        pizzasList = newData
        notifyDataSetChanged()
    }

    class PizzaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pizzaNameTextView: TextView = itemView.findViewById(R.id.singlePizzaName)
        private val priceTextView: TextView = itemView.findViewById(R.id.singlePizzaPrice)
        private val categoryTextView: TextView = itemView.findViewById(R.id.singlePizzaCategory)

        fun bind(pizza: PizzaModel) {
            pizzaNameTextView.text = pizza.pizzaName
            priceTextView.text = "$ ${pizza.price}"
            categoryTextView.text = pizza.category
        }
    }
}
