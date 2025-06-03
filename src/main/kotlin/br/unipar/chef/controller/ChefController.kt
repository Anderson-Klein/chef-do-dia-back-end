package br.unipar.chef.controller

import br.unipar.chef.model.Chef
import br.unipar.chef.model.Receita
import br.unipar.chef.model.ReceitaComNomeChef
import br.unipar.chef.service.ChefService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chef")
@CrossOrigin(origins = ["http://localhost:3000", "http://localhost:5173"])
class ChefController(
    private val chefService: ChefService
) {

    @PostMapping
    fun cadastrarChef(@RequestBody chef: Chef): ResponseEntity<Chef> {
        val chefSalvo = chefService.registrarChef(chef)
        return ResponseEntity.ok(chefSalvo)
    }

    @GetMapping("/email/{emailChef}")
    fun buscarChefPorEmail(@PathVariable emailChef: String): ResponseEntity<Chef> {
        val chef = chefService.encontrarChef(emailChef)
        return if (chef != null) ResponseEntity.ok(chef) else ResponseEntity.notFound().build()
    }

    @GetMapping
    fun buscarTodos(): ResponseEntity<List<Chef>> {
        val chefs = chefService.encontrarTodosChefs()
        return ResponseEntity.ok(chefs)
    }

    @DeleteMapping("/email/{emailChef}")
    fun excluirChef(@PathVariable emailChef: String): ResponseEntity<Void> {
        return if (chefService.excluirRegistro(emailChef)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/email/{emailChef}/receitas")
    fun adicionarReceitaPorEmail(
        @PathVariable emailChef: String,
        @RequestBody receita: Receita
    ): ResponseEntity<Receita> {
        val novaReceita = chefService.adicionarReceita(emailChef, receita)
        return if (novaReceita != null) {
            ResponseEntity.ok(novaReceita)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/email/{emailChef}/receitas")
    fun listarReceitasPorEmail(@PathVariable emailChef: String): ResponseEntity<List<Receita>> {
        val receitas = chefService.listarReceitas(emailChef)
        return ResponseEntity.ok(receitas)
    }

    @DeleteMapping("/email/{emailChef}/receitas/{indice}")
    fun excluirReceita(
        @PathVariable emailChef: String,
        @PathVariable indice: Int
    ): ResponseEntity<Chef> {
        val chef = chefService.excluirReceita(emailChef, indice)
        return if (chef != null) ResponseEntity.ok(chef) else ResponseEntity.notFound().build()
    }

    @GetMapping("/todas-receitas")
    fun listarTodasReceitas(): ResponseEntity<List<Receita>> {
        val todasReceitas = chefService.listarTodasReceitas()
        return ResponseEntity.ok(todasReceitas)
    }

    @GetMapping("/todas-receitas-com-chef")
    fun listarReceitasComNomeChef(): ResponseEntity<List<ReceitaComNomeChef>> {
        val lista = chefService.listarReceitasComNomeChef()
        return ResponseEntity.ok(lista)
    }

    @PutMapping("/email/{emailChef}/receitas/{indice}")
    fun editarReceita(
        @PathVariable emailChef: String,
        @PathVariable indice: Int,
        @RequestBody receitaAtualizada: Receita
    ): ResponseEntity<Receita> {
        val receitaEditada = chefService.editarReceita(emailChef, indice, receitaAtualizada)
        return if (receitaEditada != null) ResponseEntity.ok(receitaEditada)
        else ResponseEntity.notFound().build()
    }
    
    @PutMapping("/email/{emailChef}/receitas/numeroReceita/{numeroReceita}")
    fun editarReceitaPorNumero(
        @PathVariable emailChef: String,
        @PathVariable numeroReceita: String,
        @RequestBody receitaAtualizada: Receita
    ): ResponseEntity<Receita> {
        val receitaEditada = chefService.editarReceitaPorNumero(emailChef, numeroReceita, receitaAtualizada)
        return if (receitaEditada != null) ResponseEntity.ok(receitaEditada)
        else ResponseEntity.notFound().build()
    }
}