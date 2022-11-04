package br.com.distribuidoradosapao.viewmodels.client

import br.com.distribuidoradosapao.model.Client

interface ClientViewModelContract {

    fun insertClient(client: Client)
    fun loadClients()
    fun deletClient(isClient: String)
    fun searchClient(idClient: String, isComandaFinalizada: Boolean = true)
    fun insertClientBeforeDelete(client: Client?, idClient: String, isComandaFinalizada: Boolean)
    fun loadOneClient(idClient: String)
    fun updateClient(idClient: String,client: Client)
    fun loadClientDeleted()
    fun updateClientId(idClient: String)
}