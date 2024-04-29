package com.example.collectivetrek.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.findNavController
import com.example.collectivetrek.R
import com.example.collectivetrek.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CreateGroupFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private val dbRef: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val filterRef = dbRef.getReference("date")
    private val userRef = dbRef.getReference("groupName")
    private val eventRef = dbRef.getReference("destination")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,

        ): View? {
        // Inflate the layout
        val view = inflater.inflate(R.layout.create_group, container, false)

        // Set onClickListener for the create button to lead to the group fragment
        view.findViewById<View>(R.id.create_button)?.setOnClickListener{
            findNavController().navigate(R.id.action_createGroup_to_group)
        }


        // Set onClickListener for the back button to lead to the group fragment
        view.findViewById<View>(R.id.back_button)?.setOnClickListener{
            findNavController().navigate(R.id.action_createGroup_to_group)
        }

        val createGroupName = view.findViewById<TextInputLayout>(R.id.group_name_edit_text)
        val destination = view.findViewById<TextInputLayout>(R.id.destination_edit_text)
        val date = view.findViewById<TextInputLayout>(R.id.date_edit_text)
        val createGroupButton = view.findViewById<Button>(R.id.create_button)

        createGroupButton.setOnClickListener {
            val destination = destination.editText?.text.toString()
            val date = date.editText?.text.toString()
            val groupName = createGroupName.editText?.text.toString()

            val database = Firebase.database
            val groupsRef = database.getReference("groups")
            val groupID = groupsRef.push().key.toString()


            // Upload data to Firebase
            uploadGroupToFirebase(destination, date, groupName, groupID)

            findNavController().navigate(R.id.action_createGroup_to_group)
        }





        return view
    }
    private fun uploadGroupToFirebase(destination: String, date: String, groupName: String, groupId: String) {
        val database = Firebase.database
        val groupsRef = database.getReference("groups")

        //val groupId = groupsRef.push().key
        if (groupId != null) {
            val groupData = mapOf(
                "destination" to destination,
                "date" to date,
                "groupName" to groupName,
                "groupID" to groupId
            )
            groupsRef.child(groupId).setValue(groupData)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Group created successfully!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "Group Creation Failed!", Toast.LENGTH_SHORT).show()
                }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateGroupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}