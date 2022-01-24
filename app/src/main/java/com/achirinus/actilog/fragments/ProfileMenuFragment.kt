package com.achirinus.actilog.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.achirinus.actilog.activities.MainActivity
import com.achirinus.actilog.R
import com.achirinus.actilog.activities.LoginActivity
import com.achirinus.actilog.adapters.EntryTypeAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ProfileMenuFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_profile_menu, container, false)

        val profileNameTextView : TextView = view.findViewById(R.id.profileNameTextView)
        val profileImageView : ImageView = view.findViewById(R.id.profileImageView)
        val logoutButton : Button = view.findViewById(R.id.logoutButton)

        logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        val user = auth.currentUser
        if(user != null)
        {
            profileNameTextView.text = user.email
            profileImageView.setImageURI(user.photoUrl)
            profileImageView.setOnClickListener {

            }
        }



        return view
    }

}