package io.aparok.app.sample.design_system

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.aparok.app.sample.R
import io.aparok.app.sample.databinding.FragmentContentStyleMultilineBinding

class ContentStyleMultiline: Fragment(R.layout.fragment_content_style_multiline) {

    private var fragmentBinding: FragmentContentStyleMultilineBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val _binding = FragmentContentStyleMultilineBinding.bind(view)
        fragmentBinding = _binding
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentBinding = null
    }
}