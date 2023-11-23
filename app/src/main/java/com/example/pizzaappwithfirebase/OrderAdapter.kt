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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pizzaappwithfirebase.R
import com.example.pizzaappwithfirebase.models.CustomerModel
import com.example.pizzaappwithfirebase.models.OrderModel
import com.example.pizzaappwithfirebase.models.PizzaModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import java.security.AccessController.getContext
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
        private val pizzaOrderedName = itemView.findViewById<TextView>(R.id.pizzaOrderedNameText)
        private val orderCustomerFullName = itemView.findViewById<TextView>(R.id.orderCustomerFullNameText)
        private val orderDateTextView = itemView.findViewById<TextView>(R.id.orderDate)
        private val orderPriceTextView = itemView.findViewById<TextView>(R.id.orderPrice)
        private val orderStatus = itemView.findViewById<TextView>(R.id.orderStatusText)

        val simpleDateFormat = SimpleDateFormat("YYYY/MM/DD hh:mm:ss")

        fun bind(order: OrderModel) {
            val firebaseFirestore = FirebaseFirestore.getInstance()

            firebaseFirestore.collection("pizzas").document(order.productID).get().addOnSuccessListener {
                if (it.exists()){
                    val pizzaObject = it.toObject<PizzaModel>()
                    pizzaOrderedName.text = pizzaObject!!.pizzaName
                }
                else{
                    pizzaOrderedName.text = "Pizza Not Found"
                }
            }

            firebaseFirestore.collection("customers").document(order.customerID).get().addOnSuccessListener {
                if (it.exists()){
                    val customerObject = it.toObject<CustomerModel>()
                    orderCustomerFullName.text = "${customerObject!!.firstname} ${customerObject!!.lastname}"
                }
                else{
                    orderCustomerFullName.text = "Customer Not Found"
                }
            }
            orderDateTextView.text = simpleDateFormat.format(order.orderDate.seconds * 1000L).toString()
            orderPriceTextView.text = "$ ${order.orderPrice}"
            orderStatus.text = order.status
        }
    }
}
