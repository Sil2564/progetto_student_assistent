package com.silvianikikarim.studentassistant.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.silvianikikarim.studentassistant.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        view.findViewById<Button>(R.id.btnOrarioLezioni).setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_orarioFragment2)
        }

        view.findViewById<Button>(R.id.btnAppunti).setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_appuntiFragment)
        }

        view.findViewById<Button>(R.id.btnCalendario).setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_calendarioFragment)
        }

        view.findViewById<Button>(R.id.btnVoti).setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_andamentoFragment)
        }

        view.findViewById<Button>(R.id.btnConsigli).setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_consigliFragment)
        }

        view.findViewById<Button>(R.id.btnImpostazioni).setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_impostazioniFragment)
        }
    }
}
