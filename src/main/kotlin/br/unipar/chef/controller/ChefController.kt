package br.unipar.chef.controller

import br.unipar.chef.model.Chef
import br.unipar.chef.model.Receita
import br.unipar.chef.service.ChefService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chef")
@CrossOrigin(origins = ["http://localhost:3000", "http://localhost:5173"]) // ajuste conforme necessário
class ChefController(
    private val chefService: ChefService
) {

    @PostMapping
    fun cadastrarChef(@RequestBody chef: Chef): ResponseEntity<Chef> {
        val chefSalvo = chefService.registrarChef(chef)
        return ResponseEntity.ok(chefSalvo)
    }

    @GetMapping("/{numeroChef}")
    fun buscarChef(@PathVariable numeroChef: String): ResponseEntity<Chef> {
        val chef = chefService.encontrarChef(numeroChef)
        return if (chef != null) ResponseEntity.ok(chef) else ResponseEntity.notFound().build()
    }

    @GetMapping
    fun buscarTodos(): ResponseEntity<List<Chef>> {
        val chefs = chefService.encontrarTodos()
        return ResponseEntity.ok(chefs)
    }

    @DeleteMapping("/{numeroChef}")
    fun excluirChef(@PathVariable numeroChef: String): ResponseEntity<Void> {
        return if (chefService.excluirRegistro(numeroChef)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }



    // ✅ Adicionar receita
    @PostMapping("/{numeroChef}/receitas")
    fun adicionarReceita(
        @PathVariable numeroChef: String,
        @RequestBody receita: Receita
    ): ResponseEntity<Receita> {
        val novaReceita = chefService.adicionarReceita(numeroChef, receita)
        return ResponseEntity.ok(novaReceita)
    }


    // ✅ Listar receitas
    @GetMapping("/{numeroChef}/receitas")
    fun listarReceitas(@PathVariable numeroChef: String): ResponseEntity<List<Receita>> {
        val receitas = chefService.listarReceitas(numeroChef)
        return ResponseEntity.ok(receitas)
    }

    // ✅ Excluir receita por índice
    @DeleteMapping("/{numeroChef}/receitas/{indice}")
    fun excluirReceita(
        @PathVariable numeroChef: String,
        @PathVariable indice: Int
    ): ResponseEntity<Chef> {
        val chef = chefService.excluirReceita(numeroChef, indice)
        return if (chef != null) ResponseEntity.ok(chef) else ResponseEntity.notFound().build()
    }

    @GetMapping("/todas-receitas")
    fun listarTodasReceitas(): ResponseEntity<List<Receita>> {
        val todasReceitas = chefService.listarTodasReceitas()
        return ResponseEntity.ok(todasReceitas)
    }


}
