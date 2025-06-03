package br.unipar.chef.model

data class Chef(
    val emailChef: String = "",
    val nomeChef: String = "",
    val receitas: MutableList<Receita> = mutableListOf()
)
