package es.miguelromeral.readysetgo.ui.home

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import es.miguelromeral.readysetgo.R
import es.miguelromeral.readysetgo.ui.settings.SettingsActivity

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(this, Observer {
            textView.text = it
        })

        setHasOptionsMenu(true)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        //when(item!!.itemId){
        //    R.id.settings -> {
                val intent = Intent(context, SettingsActivity::class.java)
                startActivity(intent)
                return true
        //    }
        //}

        return super.onOptionsItemSelected(item)
    }
}