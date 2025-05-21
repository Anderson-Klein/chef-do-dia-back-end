package br.unipar.chef

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ChefApplication

fun main(args: Array<String>) {
	runApplication<ChefApplication>(*args)
}
