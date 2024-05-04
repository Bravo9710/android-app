import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DataAdapter(private val dataList: List<ContactsContract.Data>) :
    RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val textViewNotes: TextView = itemView.findViewById(R.id.textViewNotes)
        val textViewAmount: TextView = itemView.findViewById(R.id.textViewAmount)
        // Add references to other TextViews for other fields if needed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_data, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.textViewTitle.text = data.title
        holder.textViewNotes.text = data.notes
        holder.textViewAmount.text = data.amount.toString()
        // Bind other data fields to TextViews if needed
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
