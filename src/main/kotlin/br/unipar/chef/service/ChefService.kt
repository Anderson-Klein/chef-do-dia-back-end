package br.unipar.chef.service

import br.unipar.chef.model.Chef
import br.unipar.chef.model.Receita
import br.unipar.chef.repository.ChefRepository
import org.springframework.stereotype.Service

@Service
class ChefService(
    private val chefRepository: ChefRepository
) {

    fun registrarChef(chef: Chef): Chef {
        return chefRepository.salvar(chef)
    }

    fun atualizarChef(chef: Chef): Chef {
        return chefRepository.salvar(chef)
    }

    fun encontrarChef(numeroChef: String): Chef? {
        return chefRepository.buscarId(numeroChef)
    }

    fun encontrarTodos(): List<Chef> {
        return chefRepository.buscarTodos()
    }

    fun excluirRegistro(numeroChef: String): Boolean {
        return chefRepository.excluirId(numeroChef)
    }

    fun adicionarReceita(numeroChef: String, receita: Receita): Receita {
        val chef = chefRepository.buscarId(numeroChef)
            ?: throw Exception("Chef não encontrado")

        chef.receitas.add(receita) // adiciona a nova receita

        chefRepository.salvar(chef) // salva o chef atualizado
        return receita
    }


    fun excluirReceita(numeroChef: String, indice: Int): Chef? {
        return chefRepository.excluirReceita(numeroChef, indice)
    }

    fun listarReceitas(numeroChef: String): List<Receita> {
        return chefRepository.listarReceitas(numeroChef)
    }

    fun listarTodasReceitas(): List<Receita> {
        return chefRepository.buscarTodos().flatMap { it.receitas }
    }




}
