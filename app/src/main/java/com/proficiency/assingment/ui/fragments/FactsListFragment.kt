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
import androidx.recyclerview.widget.LinearLayoutManager
import com.proficiency.assingment.R
import com.proficiency.assingment.adapter.FactsAdapter
import com.proficiency.assingment.db.FactsDatabase
import com.proficiency.assingment.repository.Repository
import com.proficiency.assingment.ui.viewmodel.FactsViewModel
import com.proficiency.assingment.ui.viewmodel.ViewModelFactory
import com.proficiency.assingment.util.Resource.*
import kotlinx.android.synthetic.main.fragment_facts_list.*

class FactsListFragment : Fragment() {
    private lateinit var viewModel: FactsViewModel
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
        //initializing repository and view model
        val repository = Repository(FactsDatabase(requireActivity()))
        val viewModelProviderFactory =
            ViewModelFactory(
                requireActivity().application,
                repository
            )
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(FactsViewModel::class.java)
        setupRecyclerView()
        factsAdapter.setOnItemClickListener {
            Toast.makeText(activity, it.title, Toast.LENGTH_SHORT).show()
        }
        viewModel.mutableLiveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Success -> {
                    hideProgressBar()
                    response.data?.let { factResponse ->
                        setupToolBar(factResponse.title)
                    }
                }
                is Error -> {
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
                is Loading -> {
                    showProgressBar()
                }

            }
        })
        viewModel.getSavedFacts().observe(viewLifecycleOwner, Observer { row ->
            if (row.isEmpty()) {
                Toast.makeText(
                    activity, R.string.empty_msg,
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                factsAdapter.differ.submitList(row)
            }
        })
        itemsSwipeToRefresh.setOnRefreshListener {
            viewModel.getFacts()
            itemsSwipeToRefresh.isRefreshing = false
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
    private fun setupToolBar(title: String) {
        ((activity as? AppCompatActivity)?.supportActionBar)?.title = title
    }
}