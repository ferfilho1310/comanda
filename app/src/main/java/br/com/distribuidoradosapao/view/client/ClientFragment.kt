package br.com.distribuidoradosapao.view.client

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.databinding.FragmentClientBinding
import br.com.distribuidoradosapao.model.Client
import br.com.distribuidoradosapao.util.AdapterViewEmpty
import br.com.distribuidoradosapao.view.client.adapter.ClientAdapter
import br.com.distribuidoradosapao.view.request.RequestClientActivity
import br.com.distribuidoradosapao.view.request.RequestClientFinishActivity
import br.com.distribuidoradosapao.viewmodels.client.ClientViewModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import org.koin.android.ext.android.inject

class ClientFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentClientBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClientViewModel by inject()

    private var adapterClient: ClientAdapter? = null
    private var options: FirestoreRecyclerOptions<Client>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientBinding.inflate(inflater, container, false)

        setViewModel()
        viewModel.loadClients()
        binding.fab.setOnClickListener(this)
        return binding.root
    }

    private fun setViewModel() {
        viewModel.loadClient.observe(viewLifecycleOwner) {

            options =
                FirestoreRecyclerOptions.Builder<Client>().setQuery(it, Client::class.java).build()

                adapterClient = ClientAdapter(
                    options!!,
                    ::navigateRequestClientFragment,
                    ::editDataClient
                )

                binding.rcClients.apply {
                    adapter = adapterClient
                    setHasFixedSize(true)
                    layoutManager = GridLayoutManager(context, 2)
                }
            adapterClient?.startListening()
        }
    }

    private fun navigateRequestClientFragment(idClient: String, client: Client) {
        val intent = Intent(requireContext(), RequestClientActivity::class.java)
        intent.putExtra("idClient", idClient)
        intent.putExtra("user", client)
        intent.putExtra("isClientRequestFinish", false)
        startActivity(intent)
    }

    private fun editDataClient(idClient: String) {
        val bottomSheet = InsertClientBottomSheet.newInstance(idClient)
        bottomSheet.show(childFragmentManager, "TAG")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): ClientFragment {
            return ClientFragment()
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.fab -> {
                val bottomSheet = InsertClientBottomSheet()
                bottomSheet.show(childFragmentManager, "TAG")
            }
        }
    }
}