package es.miguelromeral.readysetgo.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import es.miguelromeral.readysetgo.R
import es.miguelromeral.readysetgo.databinding.FragmentHistoryBinding
import es.miguelromeral.readysetgo.ui.database.ReadySetGoDatabase
import es.miguelromeral.readysetgo.ui.database.Start
import es.miguelromeral.readysetgo.ui.home.HomeViewModel
import es.miguelromeral.readysetgo.ui.home.HomeViewModelFactory

class HistoryFragment : Fragment() {

    private lateinit var viewModel: HistoryViewModel

    private lateinit var binding : FragmentHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(this.activity).application
        val dataSource = ReadySetGoDatabase.getInstance(application).database
        val viewModelFactory = HistoryViewModelFactory(dataSource, application)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HistoryViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)

        binding.historyViewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = StartsAdapter()
        binding.historyList.adapter = adapter

        viewModel.startRecords.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }
}