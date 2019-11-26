package es.miguelromeral.readysetgo.ui.home

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import es.miguelromeral.readysetgo.R
import es.miguelromeral.readysetgo.databinding.FragmentHomeBinding
import es.miguelromeral.readysetgo.ui.settings.SettingsActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var binding : FragmentHomeBinding

    private lateinit var lista: MutableList<ImageView>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        //val root = inflater.inflate(R.layout.fragment_home, container, false)

        binding.homeViewModel = homeViewModel
        binding.lifecycleOwner = this

        val textView: TextView = binding.textHome

        homeViewModel.countdown.observe(this, Observer {
            textView.text = "TAPPED: ${it}!"
        })
        homeViewModel.score.observe(this, Observer {
            textTime.text = if(it == HomeViewModel.NO_SCORE) "No score" else "Score: ${it.toString()}"
        })

        setHasOptionsMenu(true)

        lista = mutableListOf(binding.imSep0, binding.imSep1, binding.imSep2, binding.imSep3, binding.imSep4)

        homeViewModel.countdown.observe(this, Observer {
            it?.let{
                if(it == HomeViewModel.COUNTDOWN_LAUNCHED ||
                    it == HomeViewModel.COUNTDOWN_NO_STARTED){
                    paintLights(0)
                }else{
                    paintLights(it)
                }
            }
        })
        paintLights(0)

        return binding.root
    }

    private val IMG_OFF = R.drawable.semaphore_black
    private val IMG_ON = R.drawable.semaphore_red

    fun paintLights(number: Int){
        when(number){
            0 -> {
                lista.forEach{
                    it.setImageResource(IMG_OFF)
                }
            }
            in 1..5 -> {
                lista.take(number).forEach{
                    it.setImageResource(IMG_ON)
                }
                lista.takeLast(5 - number).forEach{
                    it.setImageResource(IMG_OFF)
                }
            }
            else -> {
                lista.forEach{
                    it.setImageResource(IMG_ON)
                }
            }
        }
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

        //return super.onOptionsItemSelected(item)
    }
}