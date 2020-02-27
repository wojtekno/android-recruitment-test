package dog.snow.androidrecruittest.repository.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RawAlbum(
    val id: Int,
    val userId: Int,
    val title: String
) : Parcelable
