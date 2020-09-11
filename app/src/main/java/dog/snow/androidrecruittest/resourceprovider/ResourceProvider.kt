package dog.snow.androidrecruittest.resourceprovider

interface ResourceProvider {
    fun getString(resId: Int): String
}