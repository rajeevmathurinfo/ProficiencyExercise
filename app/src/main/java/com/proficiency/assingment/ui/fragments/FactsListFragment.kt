package com.proficiency.assingment.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.proficiency.assingment.R
import com.proficiency.assingment.adapter.FactsAdapter
import com.proficiency.assingment.api.RetrofitInstance
import com.proficiency.assingment.repository.Repository
import com.proficiency.assingment.ui.FactsViewModel
import com.proficiency.assingment.ui.ViewModelFactory
import com.proficiency.assingment.util.Resource
import kotlinx.android.synthetic.main.fragment_facts_list.*


class FactsListFragment : Fragment() {

    lateinit var viewModel: FactsViewModel
    private lateinit var factsAdapter: FactsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_facts_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // need to pass context for cache in retrofit instance
        activity?.application?.let { RetrofitInstance.setContext(it) }
        //initializing reposotory and view model
        val repository = Repository()
        val viewModelProviderFactory = ViewModelFactory(requireActivity().application,repository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(FactsViewModel::class.java)

        setupRecyclerView()

        factsAdapter.setOnItemClickListener {
            findNavController().navigate(R.id.action_FactsListFragment_to_SecondFragment)
        }

        viewModel.mutableLiveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { factResponse ->
                        setupToolBar(factResponse.title)
                        factsAdapter.differ.submitList(factResponse.rows)
                    }

                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        val preMessage: String = resources.getString(R.string.an_error)
                        Toast.makeText(
                            activity,
                            preMessage.plus(": ").plus(message),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

                is Resource.Loading -> {
                    showProgressBar()
                }

            }
        })
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.visibility = View.VISIBLE
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.setOnClickListener {
            viewModel.getFacts()
        }
    }

    private fun setupRecyclerView() {
        factsAdapter = FactsAdapter()
        recyclerView.apply {
            adapter = factsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    /**
     * title : String
     */
    private fun setupToolBar(title : String) {
        val appCompatActivity = activity as? AppCompatActivity
        (if (appCompatActivity != null) appCompatActivity.supportActionBar else null)?.title = title
    }


}