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
import com.example.pizzaappwithfirebase.models.OrderModel
import java.text.SimpleDateFormat

class CustomOrderAdapter(private var ordersList: List<OrderModel>, private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<CustomOrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.individual_order_layout, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = ordersList[position]
        holder.bind(order)
        holder.itemView.setOnClickListener { onItemClick(order.id) }
    }

    override fun getItemCount(): Int {
        return ordersList.size
    }

    // Update the data in the adapter
    fun updateData(newData: List<OrderModel>) {
        ordersList = newData
        notifyDataSetChanged()
    }

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val orderIdTextView: TextView = itemView.findViewById(R.id.orderId)
        private val orderStatusTextView: TextView = itemView.findViewById(R.id.orderStatus)
        private val orderDateTextView: TextView = itemView.findViewById(R.id.orderDate)
        private val orderPriceTextView: TextView = itemView.findViewById(R.id.orderPrice)

        val simpleDateFormat = SimpleDateFormat("YYYY/MM/DD hh:mm:ss")

        fun bind(order: OrderModel) {
            orderIdTextView.text = order.id
            orderStatusTextView.text = order.status
            orderDateTextView.text = simpleDateFormat.format(order.orderDate.seconds * 1000L).toString()
            orderPriceTextView.text = "$ ${order.orderPrice}"
        }
    }
}
