package br.unipar.chef.repository

import br.unipar.chef.model.Chef
import br.unipar.chef.model.Receita
import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import org.springframework.stereotype.Repository

@Repository
class ChefRepository(
    private val firestore: Firestore = FirestoreClient.getFirestore()
) {
    private val collectionName = "chef"

    fun salvar(chef: Chef): Chef {
        val documento = firestore.collection(collectionName).document(chef.emailChef)
        documento.set(chef)
        return chef
    }

    fun buscarId(emailChef: String): Chef? {
        val documento = firestore.collection(collectionName)
            .document(emailChef).get().get()
        return if (documento.exists())
            documento.toObject(Chef::class.java)
        else
            null
    }

    fun buscarTodos(): List<Chef> {
        val query = firestore.collection(collectionName).get().get()
        return query.documents.mapNotNull { it.toObject(Chef::class.java) }
    }

    fun excluirId(emailChef: String): Boolean {
        firestore.collection(collectionName).document(emailChef).delete()
        return true
    }

    fun excluirReceita(emailChef: String, indice: Int): Chef? {
        val chef = buscarId(emailChef)
        return if (chef != null && indice in chef.receitas.indices) {
            chef.receitas.removeAt(indice)
            salvar(chef)
        } else null
    }

    fun listarReceitas(emailChef: String): List<Receita> {
        return buscarId(emailChef)?.receitas ?: emptyList()
    }
}