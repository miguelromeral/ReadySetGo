package es.miguelromeral.readysetgo.ui.history

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import es.miguelromeral.readysetgo.R
import es.miguelromeral.readysetgo.databinding.FragmentHistoryBinding
import es.miguelromeral.readysetgo.ui.database.ReadySetGoDatabase
import es.miguelromeral.readysetgo.ui.database.Start
import es.miguelromeral.readysetgo.ui.formatTime
import es.miguelromeral.readysetgo.ui.home.HomeViewModel
import es.miguelromeral.readysetgo.ui.home.HomeViewModelFactory
import es.miguelromeral.readysetgo.ui.settings.SettingsActivity
import kotlinx.android.synthetic.main.fragment_history.*

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

        val adapter = StartsAdapter(StartListener { item -> viewModel.changeSelectedRecord(item) })
        binding.historyList.adapter = adapter

        viewModel.startRecords.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.bestRecord.observe(viewLifecycleOwner, Observer {
            if(it != null){
                tvBestStart.text = formatTime(it.time)
            }else{
                tvBestStart.text = resources.getString(R.string.no_time_set_yet)
            }
        })

        viewModel.selectedRecord.observe(viewLifecycleOwner, Observer {
            it?.let{
                tvSelectedStart.text = formatTime(it.time)
                return@Observer
            }
            tvSelectedStart.text = resources.getString(R.string.time_template)
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.options_history, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item != null){
            when(item.itemId) {
                R.id.option_clearRecords -> {

                    context?.let {

                        val builder = AlertDialog.Builder(it)
                        builder.setTitle(resources.getString(R.string.clear_all_records))
                        builder.setMessage(resources.getString(R.string.option_msg_clear_starts))

                        // Set the alert dialog yes button click listener
                        builder.setPositiveButton(resources.getString(R.string.option_msg_clear_all),
                            DialogInterface.OnClickListener { dialog, which ->

                                viewModel.clearDatabase()
                            })
                        builder.setNegativeButton(resources.getString(R.string.option_msg_cancel),null)

                        val dialog = builder.create()
                        dialog.show()
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}