package com.example.collectivetrek.fragments

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.location.Geocoder
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.example.collectivetrek.BuildConfig
import com.example.collectivetrek.ItineraryRepository
import com.example.collectivetrek.ItineraryViewModel
import com.example.collectivetrek.ItineraryViewModelFactory
import com.example.collectivetrek.R
import com.example.collectivetrek.database.Event
import com.example.collectivetrek.databinding.FragmentAddEventBinding
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.PhotoMetadata
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPhotoResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchByTextRequest
import com.google.android.material.datepicker.MaterialDatePicker
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.math.max
import kotlin.math.min


class AddEventFragment : Fragment() {
    private var _binding: FragmentAddEventBinding? = null
    private val binding get() = _binding!!

    private val itineraryViewModel: ItineraryViewModel by activityViewModels() {
        ItineraryViewModelFactory(repository = ItineraryRepository())
    }

    private lateinit var placesClient: PlacesClient

    private val _bitmapSetResult = MutableLiveData<Boolean>()
    val bitmapSetResult: LiveData<Boolean> get() = _bitmapSetResult

    val args: AddEventFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiKey = BuildConfig.PLACES_API_KEY

        // Log an error if apiKey is not set.
        if (apiKey.isEmpty() || apiKey == "DEFAULT_API_KEY") {
            Log.e("Places test", "No api key")
        }else {
            // Initialize Places API client
            Places.initialize(requireContext(), apiKey)
        }


    // Continue with other initialization tasks or setup
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddEventBinding.inflate(inflater, container, false) //? attachToRoot false

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = itineraryViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        placesClient = Places.createClient(requireContext())

        val groupId = args.groupId
        Log.d("groupId received",groupId.toString())

        val navController = NavHostFragment.findNavController(this@AddEventFragment)
        Log.d("on view created","on view created")

        // TODO
        // add text watcher to place name and user leaves w/o any input, error

        //TODO
        binding.addEventDateEditText.setOnClickListener {
            Log.d("add event fragment","clicklistner")
            //showDate()
            showDate()
        }

        binding.addEventAddButton.setOnClickListener {
            // retrieve filter from edit text and validate
            var placeName = binding.addEventPlaceEditText.editText?.text.toString()
            val address = binding.addEventAddressEditText.editText?.text.toString()
            val date = binding.addEventDateTextInput.editText?.text.toString()
            val note = binding.addEventNoteEditText.editText?.text.toString()

            placeName = placeName.replaceFirstChar { c->
                c.titlecase()
            }

            val event = Event(placeName=placeName,address=address, date=date, note=note)

            // validation
            if (checkEventFields(event)){
                Log.d("check event fields result", "true")
                if (!event.bitmap.isNullOrEmpty()) {
                    bitmapSetResult.observe(viewLifecycleOwner){ result ->
                        if (result){
                            // store in database
                            addEventToDataBase(event)
                        } else {
                            Log.d("bitmapsetresult false", event.toString())
                        }
                    }
                }
                else {
                    addEventToDataBase(event)
                }

                itineraryViewModel.dataInsertionResult.observe(viewLifecycleOwner){ result ->
                    if (result){
                        // make Toast
                        Toast.makeText(context, "Event added.", Toast.LENGTH_LONG).show()
                        Log.d("add event fragment",itineraryViewModel.filter.value?.id.toString())
                        // go back to itinerary page
                        navController.popBackStack()
                    }
                    else{
                        Toast.makeText(context, "Failed.", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Log.d("check event fields result", "false")
            }
        }

        binding.addEventCancelButton.setOnClickListener {
            // make Toast
            // go back to itinerary page
            Log.d("add filter","cancel clicked")
            //findNavController().popBackStack()
            navController.popBackStack()
        }
    }

    private fun checkEventFields(event:Event) : Boolean {
        if (event.placeName!!.isEmpty()) {
            binding.addEventPlaceEditText.error = "Place name required."
            return false
        }
        else if (event.placeName.length > 200) {
            binding.addEventPlaceEditText.error = "Too long."
            return false
        }
        if (event.date!!.isNotEmpty()) {
             if (event.date!!.length != 10 && event.date!!.length != 9 && event.date!!.length != 8) {
                binding.addEventDateTextInput.error = "Invalid date length."
                return false
             }
        } //TODO add else if to validate the date is during the trip

        if (event.note != null) {
            if (event.note.length > 100000) {
                binding.addEventDateTextInput.error = "Too long."
                return false
            }
        }

        if (event.address!!.isNotEmpty()){
            Log.d("event address", event.address.toString())
            if (event.address!!.length > 400) {
                binding.addEventPlaceEditText.error = "Too long."
                return false
            }
            val coordinates = getCoordinates(event.address.toString())
            if (!coordinates.isNullOrEmpty()) { //if coordinates are not null
                event.coordinates = coordinates //save to data object
                setBitmap(event) { result ->
                    _bitmapSetResult.postValue(result)
                }
            }
        }

        Log.d("before return", "before return")
        return true
    }

    private fun addEventToDataBase(event:Event) {
        Log.d("Add event to database filterId", itineraryViewModel.filter.value?.id.toString())
        itineraryViewModel.insertEvent(event)

    }

    //TODO show a month of trip starting date
    // TODO add constraints, startdate, enddate, valid date
    // TODO change theme
    private fun showDate() {
        val calendar = Calendar.getInstance()

        // Set up the MaterialDatePicker
        val builder = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setTheme(R.style.Theme_App_DatePicker)

        // Create the MaterialDatePicker
        val materialDatePicker = builder.build()

        // Add a listener to handle date selection
        materialDatePicker.addOnPositiveButtonClickListener { selection ->
            val selectedDate = Date(selection)
            val calendar = Calendar.getInstance()
            calendar.time = selectedDate
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH) + 1

            val formattedDate = "$month/$day/$year"
            binding.addEventDateTextInput.editText?.setText(formattedDate)
        }

        // Show the MaterialDatePicker
        materialDatePicker.show(requireActivity().supportFragmentManager, "DATE_PICKER")
        // TODO change the color of positive and negative button
    }

    fun getCoordinates(eventAddress: String):String?{
        val coder = Geocoder(requireContext())
        var address = coder.getFromLocationName(eventAddress, 1)
        if (address!!.isNotEmpty()){
            val lat = address[0].latitude
            val long = address[0].longitude
            return "$lat/$long"
        } else{
            Log.d("getCoordinates fail","not able to find coordinates")
        }
        return null
    }

    //TODO !!!
    // make sure to return after getting response
    // get bitmap here too
    fun getPlaceId(coordinates: String, eventAddress: String):String?{
        //TODO autocomplete
        //https://developers.google.com/codelabs/maps-platform/places-101-android-kotlin#7
        var placeId: String? = null
        val lat = coordinates.split('/')[0].toDouble()
        val long = coordinates.split('/')[1].toDouble()

        // Define the LatLng object representing the location
        val latLng = LatLng(lat, long)

        // Initialize the Places API
        val placesClient: PlacesClient = Places.createClient(context)
        val placeFields = listOf(Place.Field.ID, Place.Field.ADDRESS, Place.Field.PHOTO_METADATAS)

        println("address: $eventAddress")
        val request = SearchByTextRequest.builder(eventAddress,placeFields)
            .setMaxResultCount(10)
            .build()
        println("request: $request")

        placesClient.searchByText(request)
            .addOnSuccessListener { response ->
                println("Response: $response")
                placeId = response.places[0].id

            }
            .addOnFailureListener{exeption ->
                println("Exeption: $exeption")
            }

        println("Place ID: $placeId")
        Log.d("getPlaceId", "Place ID: $placeId")

        return placeId
    }

    fun searchPlaces(eventAddress: String, callback: (String?, PhotoMetadata?, Exception?) -> Unit) {
        //val placesClient: PlacesClient = Places.createClient(context)
        val placeFields = listOf(Place.Field.ID, Place.Field.ADDRESS, Place.Field.PHOTO_METADATAS)
        val request = SearchByTextRequest.builder(eventAddress,placeFields)
            .setMaxResultCount(10)
            .build()
        println("request: $request")
        placesClient.searchByText(request)
            .addOnSuccessListener { response ->
                println("Response: $response")
                val placeId = response.places.firstOrNull()?.id
                val photoMetadata = response.places.firstOrNull()?.photoMetadatas?.first()
                callback(placeId,photoMetadata, null)
            }
            .addOnFailureListener { exception ->
                println("Exception: $exception")
                callback(null, null, exception)
            }
    }

    fun setBitmap(event: Event, callback: (Boolean?) -> Unit){
        searchPlaces(event.address!!){placeId, photoMetaData, exception ->
            if (placeId != null && photoMetaData != null){
                println("placeId callback: $placeId")
                //event.bitmap = getBitmap(placeId)
                getBitmap(placeId=placeId, photoMetadata = photoMetaData){ bitmap, exception ->
                    if (bitmap != null){
                        bitMapToString(bitmap){bitmapString ->
                            event.bitmap = bitmapString
                            Log.d("bitmap callback done", "done")
                            callback(true)
                        }
                    } else {
                        println("Exeption: $exception")
                        callback(false)
                    }
                }
            } else {
                println("Exeption: $exception")
            }
        }
    }

    fun bitMapToString(bitmap: Bitmap, callback: (String) -> Unit) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()

        callback(Base64.encodeToString(b, Base64.DEFAULT))
    }


    private fun getBitmap(placeId:String, photoMetadata:PhotoMetadata, callback: (Bitmap?, Exception?) -> Unit){
        println("getBitmap: $placeId, photoMetadata: $photoMetadata")

        //val photoMetadata = photoMetadata.first()
        //val placesClient = Places.createClient(requireContext())
        var bitmap: Bitmap? = null
        val originalWidth = photoMetadata.width // Width of the original photo
        val originalHeight = photoMetadata.height // Height of the original photo
        Log.d("originalWidth",originalWidth.toString())
        Log.d("originalHeight",originalHeight.toString())

        // 4. Construct the URL for the photo
        val photoRequest = FetchPhotoRequest.builder(photoMetadata)
            .setMaxWidth(500) // Set maximum width of the photo
            .setMaxHeight(500)
            .build()

        placesClient.fetchPhoto(photoRequest)
            .addOnSuccessListener { fetchPhotoResponse: FetchPhotoResponse ->
                Log.d("bitmap width", fetchPhotoResponse.bitmap.width.toString())
                Log.d("bitmap height", fetchPhotoResponse.bitmap.height.toString())
                val xy = (fetchPhotoResponse.bitmap.width - 200) / 2 //TODO change the xy to be appropriate for any image size
                //bitmap = Bitmap.createBitmap(fetchPhotoResponse.bitmap, xy, xy, 200,200)
                bitmap = getShapedBitmap(fetchPhotoResponse.bitmap,xy, 20f)
                Log.d("bitmap",bitmap.toString())
                callback(bitmap, null)
                //callback(roundedCornerImage, null)
            }
            .addOnFailureListener { exception: Exception ->
                // Handle failure to fetch photo
                Log.d("bitmap failed",exception.message.toString())
                callback(null, exception)
            }

        //https://developers.google.com/maps/documentation/places/android-sdk/photos
    }

    fun getShapedBitmap(bitmap: Bitmap, xy:Int, cornerRadius: Float): Bitmap{
        var squareSize = 200
        var x = bitmap.width
        if (x>squareSize){
            x = (bitmap.width - 200) / 2
        }
        if (x+squareSize>bitmap.width){
            x = 0
        }
        var y = bitmap.height
        if (y>squareSize){
            y = (bitmap.height - 200) / 2
        }
        if (y+squareSize>bitmap.height){
            y = 0
        }
        if (bitmap.width < squareSize || bitmap.height < squareSize){
            squareSize = min( bitmap.width,bitmap.height)
        }
        val squaredBitmap = Bitmap.createBitmap(bitmap, x, y, squareSize,squareSize)
        val roundedBitmap = Bitmap.createBitmap(squaredBitmap.width, squaredBitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(roundedBitmap)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val rect = RectF(0f, 0f, squaredBitmap.width.toFloat(), squaredBitmap.height.toFloat())
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)

        return roundedBitmap
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}