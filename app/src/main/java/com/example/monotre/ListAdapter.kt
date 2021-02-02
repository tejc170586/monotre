package com.example.monotre

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class ListAdapter(context: Context, resource: Int, items: List<ListItem>): ArrayAdapter<ListItem>(
        context,
        resource,
        items
) {
    private val mResource: Int = resource
    private val mItems: List<ListItem> = items
    private val mInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: mInflater.inflate(mResource, null)
        
        val item: ListItem = mItems[position]
        
        val thumbnail: ImageView = view.findViewById(R.id.thumbnail)
        thumbnail.setImageBitmap(item.thumbnail)
        
        val name: TextView = view.findViewById(R.id.teName)
        name.text = item.itemName
        
        val beaconId: TextView = view.findViewById(R.id.teBeacon)
        beaconId.text = item.beaconId
        
        val distance: TextView = view.findViewById(R.id.teDistance)
//        val insertText = "Distance: ${item.distance}"
//        Log.d("insertText", insertText)
        distance.text = item.distance//insertText
        
        return view
    }
    
}