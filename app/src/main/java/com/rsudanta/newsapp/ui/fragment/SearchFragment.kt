package com.rsudanta.newsapp.ui.fragment

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rsudanta.newsapp.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        showKeyboard()
        editTextSetup()

        binding.ivClear.setOnClickListener {
            binding.etSearch.text.clear()
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun editTextSetup() {
        binding.etSearch.setOnEditorActionListener { _, id, _ ->
            return@setOnEditorActionListener when (id) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    Toast.makeText(activity, "Cek", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }

        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            if (text.toString().trim().isEmpty()) {
                binding.ivClear.visibility = View.GONE
            } else {
                binding.ivClear.visibility = View.VISIBLE
            }
        }
    }

    private fun showKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        binding.etSearch.postDelayed({
            binding.etSearch.requestFocus()
            imm?.showSoftInput(binding.etSearch, 0)
        }, 100)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}