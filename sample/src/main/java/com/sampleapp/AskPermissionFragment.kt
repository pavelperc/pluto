package com.sampleapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sampleapp.databinding.FragmentAskPermisionBinding

class AskPermissionFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAskPermisionBinding? = null
    private val binding
        get() = _binding!!

    override fun getTheme(): Int = R.style.DemoBottomSheetDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAskPermisionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
            bottomSheet?.let {
                dialog.behavior.peekHeight = requireContext().getScreen().second
                dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        binding.root.setOnClickListener {
            dismiss()
            requireContext().openOverlaySettings()
        }
    }
}
