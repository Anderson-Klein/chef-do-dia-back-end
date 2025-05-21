package br.unipar.chef.model

data class Chef(
    val numeroChef: String?,
    val receitas: MutableList<Receita> = mutableListOf()
)
