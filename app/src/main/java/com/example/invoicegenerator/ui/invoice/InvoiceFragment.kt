package com.example.invoicegenerator.ui.invoice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.invoicegenerator.MainActivity
import com.example.invoicegenerator.R
import com.example.invoicegenerator.databinding.FragmentInvoiceBinding
import com.example.invoicegenerator.databinding.FragmentItemBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton


class InvoiceFragment : Fragment() {

    // Direct reference to the components on the layout that have an ID.
    private var _binding: FragmentInvoiceBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

//         Bring the main button to the front of everything.
//        accessMainButton().bringToFront()


        // Inflate the layout for this fragment.
        _binding = FragmentInvoiceBinding.inflate(inflater, container, false)

        // Return the root for this fragment.
        return _binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

//        accessMainButton().setOnClickListener {
//            Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
//        }
    }


//     Access the button in the MainActivity.
    private fun accessMainButton(): FloatingActionButton {
        return (requireActivity() as MainActivity).fab
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}