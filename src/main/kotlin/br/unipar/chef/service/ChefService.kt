package br.unipar.chef.service

import br.unipar.chef.model.Chef
import br.unipar.chef.model.Receita
import br.unipar.chef.model.ReceitaComNomeChef
import br.unipar.chef.repository.ChefRepository
import org.springframework.stereotype.Service

@Service
class ChefService(
    private val chefRepository: ChefRepository
) {

    fun registrarChef(chef: Chef): Chef = chefRepository.salvar(chef)

    fun atualizarChef(chef: Chef): Chef = chefRepository.salvar(chef)

    fun encontrarChef(emailChef: String): Chef? = chefRepository.buscarId(emailChef)

    fun encontrarTodosChefs(): List<Chef> = chefRepository.buscarTodos()

    fun excluirRegistro(emailChef: String): Boolean = chefRepository.excluirId(emailChef)

    fun adicionarReceita(emailChef: String, receita: Receita): Receita? {
        val chef = chefRepository.buscarId(emailChef) ?: return null

        // Buscar o maior numeroReceita entre todas as receitas de todos os chefs
        val todosChefs = chefRepository.buscarTodos()
        val maiorNumeroGlobal = todosChefs.flatMap { it.receitas }
            .mapNotNull { it.numeroReceita.toIntOrNull() }
            .maxOrNull() ?: 0

        // Nova receita com numeroReceita único globalmente
        val receitaComNumero = receita.copy(numeroReceita = (maiorNumeroGlobal + 1).toString())

        chef.receitas.add(receitaComNumero)
        chefRepository.salvar(chef)
        return receitaComNumero
    }

    fun excluirReceita(emailChef: String, indice: Int): Chef? {
        return chefRepository.excluirReceita(emailChef, indice)
    }

    fun listarReceitas(emailChef: String): List<Receita> =
        chefRepository.listarReceitas(emailChef)

    fun listarTodasReceitas(): List<Receita> =
        chefRepository.buscarTodos().flatMap { it.receitas }

    fun listarReceitasComNomeChef(): List<ReceitaComNomeChef> {
        return chefRepository.buscarTodos().flatMap { chef ->
            chef.receitas.map { receita ->
                ReceitaComNomeChef(
                    numeroReceita = receita.numeroReceita,
                    descricao = receita.descricao,
                    modoPreparo = receita.modoPreparo,
                    nomeChef = chef.nomeChef
                )
            }
        }
    }

    fun editarReceita(emailChef: String, indice: Int, receitaAtualizada: Receita): Receita? {
        val chef = chefRepository.buscarId(emailChef)
        return if (chef != null && indice in chef.receitas.indices) {
            chef.receitas[indice] = receitaAtualizada
            chefRepository.salvar(chef)
            receitaAtualizada
        } else {
            null
        }
    }

    fun editarReceitaPorNumero(emailChef: String, numeroReceita: String, receitaAtualizada: Receita): Receita? {
        val chef = chefRepository.buscarId(emailChef)
        return if (chef != null) {
            val index = chef.receitas.indexOfFirst { it.numeroReceita == numeroReceita }
            if (index != -1) {
                // Atualizar somente descrição e modoPreparo
                val receitaExistente = chef.receitas[index]
                val receitaEditada = receitaExistente.copy(
                    descricao = receitaAtualizada.descricao,
                    modoPreparo = receitaAtualizada.modoPreparo
                )
                chef.receitas[index] = receitaEditada
                chefRepository.salvar(chef)
                receitaEditada
            } else {
                null
            }
        } else {
            null
        }
    }
}