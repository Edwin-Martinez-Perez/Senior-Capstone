package com.example.collectivetrek.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collectivetrek.GroupFragmentListener
import com.example.collectivetrek.MyAdapter
import com.example.collectivetrek.R
import com.example.collectivetrek.SharedViewModel
import com.example.collectivetrek.database.Group
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class GroupFragment : Fragment() {

    private lateinit var dbref : DatabaseReference
    private lateinit var userRecyclerview : RecyclerView
    private lateinit var userArrayList : ArrayList<Group>


    private val groupIdViewModel: SharedViewModel by viewModels({ requireActivity() })


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,

        ): View? {
        // Inflate the layout
        val view = inflater.inflate(R.layout.group, container, false)


        view.findViewById<View>(R.id.add_event_button)?.setOnClickListener{
            findNavController().navigate(R.id.action_group_to_createGroup)

        }

        userRecyclerview = view.findViewById(R.id.events_recycler_view)
        userRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        userRecyclerview.setHasFixedSize(true)

        userArrayList = arrayListOf()
        getUserData()

        return view
    }


    private fun getUserData(){

        dbref = FirebaseDatabase.getInstance().getReference("groups")

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {

                    for (userSnapshot in snapshot.children) {


                        val groups = userSnapshot.getValue(Group::class.java)
                        userArrayList.add(groups!!)

                    }

                    userRecyclerview.adapter = MyAdapter(GroupFragmentListener {group ->
                        Log.d("groupId", "${group.groupID}, ${group.groupName}")
                        groupIdViewModel.setGroupId(group.groupID!!)
                        groupIdViewModel.groupIdSetResult.observe(viewLifecycleOwner){result->
                            if (result){
                                findNavController().popBackStack(R.id.group, false)
                                findNavController().navigate(R.id.action_group_to_itineraryFragment)
                            }

                        }

                        Log.d("group", "group id${groupIdViewModel.sharedGroupId.value.toString()} , groupName ${group.groupName}") }, userArrayList)

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }





    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}