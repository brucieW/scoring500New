package com.zeroboss.scoring500.domain.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

@Entity
data class Player(
    @Id var id: Long = 0,

    var name: String = "",
) {
    var teams = ToMany<Team>(this, Player_.teams)
}
