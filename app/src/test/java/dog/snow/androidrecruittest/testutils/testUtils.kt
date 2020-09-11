package dog.snow.androidrecruittest.testutils

import dog.snow.androidrecruittest.repository.model.RawAlbum
import dog.snow.androidrecruittest.repository.model.RawPhoto
import dog.snow.androidrecruittest.repository.model.RawUser
import dog.snow.androidrecruittest.ui.model.Detail
import dog.snow.androidrecruittest.ui.model.ListItem
import okhttp3.internal.format

fun createRawPhotoList(photos: Int = 100, albums: Int = 10): List<RawPhoto> {
    val list = mutableListOf<RawPhoto>()
    for (i in 1..photos) {
        val albumId = when (i % albums) {
            0 -> albums
            else -> i % albums
        }
        val photo = RawPhoto(i, albumId, "Photo title $i", "urlOfPhoto$i", "thumbNailOfPhoto$i")
        list.add(photo)
    }
    return list
}

fun createRawAlbumList(albums: Int = 3, users: Int = 2): List<RawAlbum> {
    val list = mutableListOf<RawAlbum>()
    for (i in 1..albums) {
        val userId = when (i % users) {
            0 -> users
            else -> i % users
        }
        val album = RawAlbum(i, userId, "album title $i")
        list.add(album)
    }
    return list

}

fun createRawUserList(users: Int = 2): List<RawUser> {
    val list = mutableListOf<RawUser>()
    for (i in 1..users) {
        val user = RawUser(i, "name $i", "username $i", "email $i",
                RawUser.RawAddress("street $i", "suite $i", "city $i", "zipcode $i",
                        RawUser.RawAddress.RawGeo("lat $i", "lng $i")), "phone $i", "website $i",
                RawUser.RawCompany("company name $i", "catchPhrase $i", "bs $i"))
        list.add(user)
    }
    return list

}

fun createListItems(photos: Int = 100, albums: Int = 5, albumsError: Boolean = false, users: Int = 2): List<ListItem> {
    val list = mutableListOf<ListItem>()
    val photoList = createRawPhotoList(photos, albums)
    val albumList = createRawAlbumList(albums, users)

    for (i in 0 until photos) {
        val albumId = photoList[i].albumId
        val albumTitle = if (!albumsError) albumList[albumId - 1].title else format(albumIdNetworkError, albumId)
        val element = ListItem(photoList[i].id, photoList[i].title, albumTitle, photoList[i].thumbnailUrl)
        list.add(element)
    }
    return list

}

fun createDetailList(photos: Int = 100, albums: Int = 5, albumsError: Boolean = false, users: Int = 2, usersError: Boolean = false): List<Detail> {
    val list = mutableListOf<Detail>()
    val photoList = createRawPhotoList(photos, albums)
    val albumList = createRawAlbumList(albums, users)
    val userList = createRawUserList(users)

    for (i in 0 until photos) {
        val rawPhoto = photoList[i]
        val albumId = rawPhoto.albumId
        var albumTitle: String
        var username: String
        var userEmail: String
        var phone: String

        if (albumsError) {
            albumTitle = format(albumIdNetworkError, albumId)
            username = networkErrorMessage
            userEmail = networkErrorMessage
            phone = networkErrorMessage
        } else {
            val rawAlbum = albumList[albumId - 1]
            albumTitle = rawAlbum.title
            val userId = rawAlbum.userId

            if (usersError) {
                username = format(userIdNetworkError, userId)
                userEmail = format(userIdNetworkError, userId)
                phone = format(userIdNetworkError, userId)
            } else {
                username = userList[userId - 1].username
                userEmail = userList[userId - 1].email
                phone = userList[userId - 1].phone
            }
        }
        val element = Detail(rawPhoto.id, rawPhoto.title, albumTitle, username, userEmail, phone, rawPhoto.url)
        list.add(element)
    }
    return list

}

private val networkErrorMessage = "Can\'t download necessary data!"
private val userIdNetworkError = "User id: %s - Can\'t download necessary data!"
private val albumIdNetworkError = "Album id: %s - Can\'t download necessary data!"