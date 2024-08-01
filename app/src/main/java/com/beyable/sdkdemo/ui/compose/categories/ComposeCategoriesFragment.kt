package com.beyable.sdkdemo.ui.compose.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels

/**
 *
 * Created by MarKinho on 01/08/2024.
 *
 * Wisepear Techlab
 * All rights reserved
 *
 **/

class ComposeCategoriesFragment : Fragment() {

    private val viewModel: CategoriesViewModel by viewModels()

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View? {
        return ComposeView(requireContext()).apply {
            setContent {
                // Utiliser votre composable ici
                CategoriesScreen(viewModel)
            }
        }
    }
}