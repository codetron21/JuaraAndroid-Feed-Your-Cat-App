package com.codetron.feedyourcat.features.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.codetron.feedyourcat.R
import com.codetron.feedyourcat.databinding.FragmentSplashBinding
import com.codetron.feedyourcat.utils.SPLASH_DELAY
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            delay(SPLASH_DELAY)
            findNavController().navigate(R.id.nav_splash_to_home)
            requireActivity().finish()
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}