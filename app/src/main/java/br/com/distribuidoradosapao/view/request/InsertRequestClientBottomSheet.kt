package br.com.distribuidoradosapao.view.request

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.databinding.InsertRequestClientBottomSheetBinding
import br.com.distribuidoradosapao.model.Request
import br.com.distribuidoradosapao.viewmodels.request.RequestClientViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class InsertRequestClientBottomSheet(
    var idClient: String,
    var listener: (String) -> Unit,
) : BottomSheetDialogFragment(), View.OnClickListener {

    private var _binding: InsertRequestClientBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RequestClientViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = InsertRequestClientBottomSheetBinding.inflate(inflater, container, false)

        listener()
        setupViewModel()
        setupViewModelSum()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun listener() {
        binding.btInsertRequestClient.setOnClickListener(this)
    }

    private fun setupViewModelSum() {
        viewModel.somaRequestClient.observe(this) {
            listener.invoke(it.toString())
        }
    }

    private fun setupViewModel() {
        viewModel.insertRequestClient.observe(this) {
            if (it == true) {
                dismiss()
            } else {
                Log.e("TAG", "Erro ao inserir o pedido")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.bt_insert_request_client -> verifyDataRequest()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun verifyDataRequest() {
        binding.apply {
            when {
                edRequestClientQuantity.text.toString().isEmpty() -> edRequestClientQuantity.error =
                    "Preencha a quantidade"
                edRequestClientValue.text.toString().isEmpty() -> edRequestClientQuantity.error =
                    "Preencha o valor"
                edProductRequestClient.text.toString().isEmpty() -> edProductRequestClient.error =
                    "Preencha o produto"
                else -> {
                    viewModel.insertRequestClient(
                        Request(
                            idClient = idClient,
                            amount = edRequestClientQuantity.text.toString(),
                            nameProduct = edProductRequestClient.text.toString(),
                            valueUnit = edRequestClientValue.text.toString().toFloat(),
                            date = dateFormat()
                        )
                    )
                    viewModel.somaRequestsClient(idClient)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateFormat(): String {
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return current.format(formatter)
    }

    companion object {
        fun newInstance(
            idClient: String,
            listener: (String) -> Unit,
        ): InsertRequestClientBottomSheet {
            return InsertRequestClientBottomSheet(idClient, listener)
        }
    }
}