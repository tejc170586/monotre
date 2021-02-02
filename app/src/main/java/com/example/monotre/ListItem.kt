package com.example.monotre

import android.graphics.Bitmap

data class ListItem(var thumbnail: Bitmap? = null,
                    var itemName : String,
                    var beaconId : String,
                    var distance : String)