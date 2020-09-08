package dog.snow.androidrecruittest.repository

import dog.snow.androidrecruittest.network.JsonPlaceholderApi
import dog.snow.androidrecruittest.repository.model.RawPhoto
import io.reactivex.rxjava3.core.Observable


class PlaceholderRepository(placeholderApi: JsonPlaceholderApi) {

    val photosObs: Observable<List<RawPhoto>> = placeholderApi.getPhotos()

}