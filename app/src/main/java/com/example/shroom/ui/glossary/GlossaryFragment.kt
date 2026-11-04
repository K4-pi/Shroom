package com.example.shroom.ui.glossary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shroom.R
import com.example.shroom.data.MushroomTome
import com.example.shroom.databinding.FragmentGlossaryBinding

class GlossaryFragment : Fragment() {
    private var _binding: FragmentGlossaryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MushroomAdapter

    private val backCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            childFragmentManager.popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGlossaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backCallback)

        childFragmentManager.addOnBackStackChangedListener {
            val hasDetail = childFragmentManager.backStackEntryCount > 0
            binding.listContainer.isVisible = !hasDetail
            backCallback.isEnabled = hasDetail
        }

        adapter = MushroomAdapter { mushroom ->
            binding.listContainer.isVisible = false
            backCallback.isEnabled = true
            childFragmentManager.commit {
                replace(R.id.detail_host, MushroomDetailFragment.newInstance(mushroom.id))
                addToBackStack("detail")
            }
        }
        binding.mushroomList.layoutManager = LinearLayoutManager(requireContext())
        binding.mushroomList.adapter = adapter
        adapter.submitList(MushroomTome.all)

        binding.search.doAfterTextChanged { text ->
            adapter.submitList(MushroomTome.search(text?.toString().orEmpty()))
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
