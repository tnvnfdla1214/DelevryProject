package com.example.delevryproject.ui.profile

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.delevryproject.R
import com.example.delevryproject.databinding.FragmentFavoriteBinding
import com.example.delevryproject.databinding.FragmentProfileBinding
import com.example.delevryproject.ui.base.BaseFragment
import com.example.delevryproject.ui.favorite.FavoriteViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class ProfileFragment: BaseFragment<ProfileViewModel, FragmentProfileBinding>() {
    override val viewModel by viewModels<ProfileViewModel>()

    override fun getViewBinding(): FragmentProfileBinding = FragmentProfileBinding.inflate(layoutInflater)

    override fun initViews() = with(binding) {
        Log.d("민규","1")
        loginButton.setOnClickListener {
            signInGoogle()
        }
    }

    override fun observeData() = viewModel.myStateLiveData.observe(this) {
        when (it) {
            is ProfileState.Uninitialized -> initViews()
            is ProfileState.Loading -> handleLoadingState()
            is ProfileState.Login -> handleLoginState(it)
            is ProfileState.Success -> handleSuccessState(it)
            is ProfileState.Error -> handleErrorState(it)
        }
    }
    private fun handleLoadingState() = with(binding) {
        Log.d("민규","2")
        progressBar.isVisible = true
    }
    private fun handleLoginState(state: ProfileState.Login) = with(binding) {
        Log.d("민규","3")
        loginButton.isGone = true
        val credential = GoogleAuthProvider.getCredential(state.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    viewModel.setUserInfo(user)
                }
            }
    }
    private fun handleSuccessState(state: ProfileState.Success) = with(binding) {
        Log.d("민규","4")
        progressBar.isGone = true
        when (state) {
            is ProfileState.Success.Registered -> {
                Log.d("민규","5")
                scrollview.isVisible = true
            }
            is ProfileState.Success.NotRegistered -> {
                Log.d("민규","6")
                //등록되어 있지 않을때-> 지금은 무조건 등록되어 있다고 함
            }
        }
    }
    private fun handleErrorState(state: ProfileState.Error) {
        binding.loginButton.isVisible = true
        Toast.makeText(requireContext(), state.messageId, Toast.LENGTH_SHORT).show()
    }


    private val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    private val gsc by lazy { GoogleSignIn.getClient(requireActivity(), gso) }

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val loginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                task.getResult(ApiException::class.java)?.let { account ->
                    Log.e(TAG, "firebaseAuthWithGoogle: ${account.id}")
                    viewModel.saveToken(account.idToken ?: throw Exception())
                } ?: throw Exception()
            } catch (e: Exception) {
                e.printStackTrace()

            }
        }
    }
    private fun signInGoogle() {
        val signInIntent = gsc.signInIntent
        loginLauncher.launch(signInIntent)
    }


    companion object {

        fun newInstance() = ProfileFragment()

        const val TAG = "ProfileFragment"

    }
}