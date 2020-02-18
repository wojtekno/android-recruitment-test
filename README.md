# Snowdog Android Recruitment test

### Instruction

1. Fork this repo to your github.
2. The repository contains app resources.
3. Your task is to create an app on top of this project:
	1. Your main goal is to provide data
    	* download items from [jsonplaceholder.typicode.com](https://jsonplaceholder.typicode.com):
          1. Download photos from endpoint [`/photos`](https://jsonplaceholder.typicode.com/photos). There are 5000 items, shrink the list to 100. You can use `RawPhoto` model.
          2. Download albums from endpoint [`/albums/{id}`](https://jsonplaceholder.typicode.com/albums/2) for `ids` that are in the downloaded photos (field: `albumId`). You can use `RawAlbum` model.
          3. Download users from endpoint [`/users/{id}`](https://jsonplaceholder.typicode.com/users/3) for `ids` that are in the downloaded albums (field: `userId`). You can use `RawUser` model.
        * Download all the items on `SplashActivity`. The rest of the app should use cached data. If there are some problems with connection, display dialog. You can use `showError()` in `SplashActivity`
        * Display the list with thumbnails on `RecyclerView` in `ListFragment`. You can use `ListItem` model.
        * Display details with a full photo in `DetailFragment` which appears after clicking on an item on the list. You can use `Detail` model.
        * jsonplaceholder uses [`placeholder.com`](https://placeholder.com) which returns error 410 on Android devices so you need to change `User-agent` header of request. Set `User-agent` to `Cool app` (or something different than default) for all requests.
        * Use `R.id.search` to filter data by `title` and `albumTitle` in `RecyclerView`.
    2. Your second goal is to style the app:
    	* All the necessary colors are defined in `palette.xml`, use them to style your app.
        * Use `R.drawable.ic_placeholder` for placeholders in `ImageViews`.
    3. Optional - if you want more :)
		* In `mock` flavor there are mocks for all requests that you can use while developing. You can finish mock flavor.
        * Use `R.id.banner` to show users if they are in offline mode. The app should start from scratch in offline mode for 10 minutes. After 10 minutes, when the app starts, show dialog using `showError()` in `SplashActivity`. If you want to use your http client to cache data, be aware of `Pragma` and `Age` headers.
        * Create dark mode for the app. All the variants of logos are also defined in dark mode.
        * Try to create some animations/transitions.
        * Polish your app, try to add something that will blow our minds :)
4. Remember to commit as much as you can. Don't be afraid of mess in your commits, we will not look at your commit history :)
5. **Send us link** to your repo and wait for answer. We will also be grateful for your **feedback** for this task. Send us your ideas about it, maybe we can improve it for future candidates.

### Demo:

![Demo][demo]

[demo]: art/demo.gif
