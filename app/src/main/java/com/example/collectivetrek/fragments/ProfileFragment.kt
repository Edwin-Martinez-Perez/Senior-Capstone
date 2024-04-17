package com.example.collectivetrek.fragments

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.collectivetrek.R
import com.example.collectivetrek.database.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {

    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var profilePictureImageView: ImageView
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button
    private lateinit var logoutButton: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser

    private val PICK_IMAGE_REQUEST = 71
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance()
        // Get current user
        currentUser = auth.currentUser!!

        // Initialize views
        firstNameEditText = view.findViewById(R.id.editTextFirstName)
        lastNameEditText = view.findViewById(R.id.editTextLastName)
        emailEditText = view.findViewById(R.id.editTextEmail)
        profilePictureImageView = view.findViewById(R.id.imageViewProfilePicture)
        saveButton = view.findViewById(R.id.buttonSave)
        deleteButton = view.findViewById(R.id.buttonDelete)
        logoutButton = view.findViewById(R.id.buttonLogOut)

        // Set initial values from current user's profile
        emailEditText.setText(currentUser.email)

        // Disable editing of email field
        emailEditText.isEnabled = false

        // Fetch and display user's first name and last name from the database
        val userReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.uid)
        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                if (user != null) {
                    firstNameEditText.setText(user.firstName)
                    lastNameEditText.setText(user.lastName)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Failed to read user data", databaseError.toException())
            }
        })

        // Set click listener for save button
        saveButton.setOnClickListener { saveUserProfile() }

        // Set click listener for delete button
        deleteButton.setOnClickListener { showDeleteConfirmationDialog() }

        // Set click listener for logout button
        logoutButton.setOnClickListener { logoutUser() }

        return view
    }

    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirm Deletion")
        builder.setMessage("Are you sure you want to delete your account? This action cannot be undone.")

        builder.setPositiveButton("Yes") { dialog, _ ->
            deleteAccount()
            dialog.dismiss()
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun deleteAccount() {
        val userReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.uid)
        userReference.removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // User data deleted successfully
                    Log.d(TAG, "User data deleted from database")

                    // Delete the user account
                    currentUser.delete()
                        .addOnCompleteListener { deletionTask ->
                            if (deletionTask.isSuccessful) {
                                // Account deleted successfully
                                // Navigate to the welcome page
                                val action = ProfileFragmentDirections.actionProfileFragmentToWelcomeFragment()
                                findNavController().navigate(action)
                            } else {
                                // Account deletion failed
                                Log.e(TAG, "Error deleting account", deletionTask.exception)
                                // Show error message if needed
                            }
                        }
                } else {
                    // User data deletion failed
                    Log.e(TAG, "Error deleting user data from database", task.exception)
                    // Show error message if needed
                }
            }
    }


    private fun logoutUser() {
        auth.signOut()
        // Navigate to the welcome page
        val action = ProfileFragmentDirections.actionProfileFragmentToWelcomeFragment()
        findNavController().navigate(action)
    }

    private fun saveUserProfile() {
        // Get updated values from EditText fields
        val firstName = firstNameEditText.text.toString()
        val lastName = lastNameEditText.text.toString()

        // Update display name (first name + last name)
        val updatedDisplayName = "$firstName $lastName"
        val profileUpdates = userProfileChangeRequest {
            displayName = updatedDisplayName
        }

        // Update display name in Firebase Auth
        currentUser.updateProfile(profileUpdates)
            .addOnCompleteListener { profileTask ->
                if (profileTask.isSuccessful) {
                    // Show success message for profile update
                    Log.d(TAG, "User profile updated")

                    // Update first name and last name in Firebase Realtime Database
                    val userReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.uid)
                    val userUpdates = HashMap<String, Any>()
                    userUpdates["firstName"] = firstName
                    userUpdates["lastName"] = lastName

                    userReference.updateChildren(userUpdates)
                        .addOnCompleteListener { databaseTask ->
                            if (databaseTask.isSuccessful) {
                                // Show success message for database update
                                Log.d(TAG, "User data updated in database")
                            } else {
                                // Database update failed
                                Log.e(TAG, "Error updating user data in database", databaseTask.exception)
                                // Show error message if needed
                            }
                        }
                } else {
                    // Profile update failed
                    Log.e(TAG, "Error updating profile", profileTask.exception)
                    // Show error message if needed
                }
            }
    }

    companion object {
        private const val TAG = "ProfileFragment"
    }
}
