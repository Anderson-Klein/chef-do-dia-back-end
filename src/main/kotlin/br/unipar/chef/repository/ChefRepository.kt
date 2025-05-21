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
        val documento = firestore.collection(collectionName).document(chef.numeroChef!!)
        val chefRegistrado = chef.copy(numeroChef = documento.id)
        documento.set(chefRegistrado)
        return chefRegistrado
    }

    fun buscarId(numeroChef: String): Chef? {
        val documento = firestore.collection(collectionName)
            .document(numeroChef).get().get()
        return if (documento.exists())
            documento.toObject(Chef::class.java)
        else
            null
    }

    fun buscarTodos(): List<Chef> {
        val query = firestore.collection(collectionName).get().get()
        return query.documents.mapNotNull { it.toObject(Chef::class.java) }
    }

    fun excluirId(numeroChef: String): Boolean {
        firestore.collection(collectionName).document(numeroChef).delete()
        return true
    }

    // ✅ Adiciona uma nova receita à lista do chef
    fun adicionarReceita(numeroChef: String, novaReceita: Receita): Chef? {
        val chef = buscarId(numeroChef)
        return if (chef != null) {
            chef.receitas.add(novaReceita)
            salvar(chef)
        } else null
    }

    // ✅ Remove uma receita por índice (ou outro identificador se preferir)
    fun excluirReceita(numeroChef: String, indice: Int): Chef? {
        val chef = buscarId(numeroChef)
        return if (chef != null && indice in chef.receitas.indices) {
            chef.receitas.removeAt(indice)
            salvar(chef)
        } else null
    }

    // ✅ Retorna todas as receitas do chef
    fun listarReceitas(numeroChef: String): List<Receita> {
        return buscarId(numeroChef)?.receitas ?: emptyList()
    }
}
