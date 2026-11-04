package com.example.shroom.ui.glossary

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.shroom.R
import com.example.shroom.data.MushroomTome
import com.example.shroom.databinding.FragmentMushroomDetailBinding

class MushroomDetailFragment : Fragment() {
    private var _binding: FragmentMushroomDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMushroomDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val id = requireArguments().getString(ARG_ID).orEmpty()
        val mushroom = MushroomTome.byId(id) ?: run {
            parentFragmentManager.popBackStack()
            return
        }

        binding.detailImageview.setImageResource(mushroom.imageRes)
        binding.detailCommonName.text = mushroom.commonName
        binding.detailLatinName.text = mushroom.latinName
        binding.detailHabitat.text = getString(R.string.habitat_label) + " · " + mushroom.habitat
        binding.detailSeason.text = getString(R.string.season_label) + " · " + mushroom.season
        binding.detailDescription.text = mushroom.description
        binding.detailAlchemist.text = mushroom.alchemistNote
        binding.detailLookalikes.text = mushroom.lookalikes
        binding.detailEdibility.text = edibilityLabel(requireContext(), mushroom.edibility)
        val seal = ContextCompat.getDrawable(requireContext(), R.drawable.bg_wax_seal)
            ?.mutate() as GradientDrawable
        seal.setColor(
            ContextCompat.getColor(requireContext(), edibilityColor(mushroom.edibility)),
        )
        binding.detailEdibility.background = seal
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val ARG_ID = "mushroom_id"

        fun newInstance(id: String) = MushroomDetailFragment().apply {
            arguments = Bundle().apply { putString(ARG_ID, id) }
        }
    }
}
