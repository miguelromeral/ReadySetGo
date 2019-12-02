package es.miguelromeral.readysetgo.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import es.miguelromeral.readysetgo.R
import es.miguelromeral.readysetgo.databinding.FragmentHomeBinding
import es.miguelromeral.readysetgo.ui.database.ReadySetGoDatabase
import es.miguelromeral.readysetgo.ui.formatTime
import es.miguelromeral.readysetgo.ui.settings.SettingsActivity
import kotlinx.android.synthetic.main.fragment_history.*
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

        val application = requireNotNull(this.activity).application
        val dataSource = ReadySetGoDatabase.getInstance(application).database
        val viewModelFactory = HomeViewModelFactory(dataSource, application)

        homeViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        //val root = inflater.inflate(R.layout.fragment_home, container, false)

        binding.homeViewModel = homeViewModel
        binding.lifecycleOwner = this

        val textView: TextView = binding.textHome

        homeViewModel.countdown.observe(this, Observer {
            it?.let{
                when(it) {
                    HomeViewModel.COUNTDOWN_NO_STARTED ->
                        textView.text = resources.getString(R.string.home_begin)
                    HomeViewModel.COUNTDOWN_LAUNCHED ->
                        textView.text = resources.getString(R.string.home_go)
                    else ->
                        textView.text = resources.getString(R.string.home_ready_set)
                }
                return@Observer
            }
            textView.text = resources.getString(R.string.home_begin)
        })


        homeViewModel.score.observe(this, Observer { newTime ->
            if(newTime == null || newTime == HomeViewModel.NO_SCORE)
                tvScore.text = resources.getString(R.string.home_no_score)
            else {
                tvScore.text = resources.getString(R.string.home_last_score) + " " + formatTime(newTime)

                /*
                homeViewModel.bestRecord.value?.let{
                    tvScore.setTextColor(resources.getColor(R.color.colorWorse))
                }
                */
            }
        })

        setHasOptionsMenu(true)

        lista = mutableListOf(binding.imSep0, binding.imSep1, binding.imSep2, binding.imSep3, binding.imSep4)

        homeViewModel.countdown.observe(this, Observer {
            it?.let {
                paintLights(it)
            }
        })
        paintLights(0)

        return binding.root
    }

    private val IMG_OFF = R.drawable.semaphore_black
    private val IMG_ON = R.drawable.semaphore_red

    fun paintLights(state: Int){

        homeViewModel.gameMode.value?.let {
            when(it){
                resources.getString(R.string.preference_game_mode_one) -> {
                    paintLightsOne(state)
                }
                resources.getString(R.string.preference_game_mode_two) -> {
                    paintLightsTwo(state)
                }
            }
        }
    }

    fun paintLightsOne(number: Int){
        when(number){
            HomeViewModel.COUNTDOWN_NO_STARTED,
            HomeViewModel.COUNTDOWN_LAUNCHED -> {
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


    fun paintLightsTwo(number: Int){
        when(number){
            HomeViewModel.COUNTDOWN_NO_STARTED -> {
                lista.forEach{
                    it.setImageResource(IMG_OFF)
                }
            }
            HomeViewModel.COUNTDOWN_LAUNCHED -> {
                lista.forEach{
                    it.setImageResource(R.drawable.semaphore_green)
                }
            }
            in 1..4 -> {
                lista.take(number).forEach{
                    it.setImageResource(IMG_ON)
                }
                lista.takeLast(5 - number).forEach{
                    it.setImageResource(IMG_OFF)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        //homeViewModel.suspendTreat()
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.initSettings()
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