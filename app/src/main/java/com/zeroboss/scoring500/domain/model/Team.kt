package com.zeroboss.scoring500.domain.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

@Entity
data class Team(
    @Id
    var id: Long = 0,

    var name: String = "",
) {
    var players = ToMany<Player>(this, Team_.players)
    var matches = ToMany<Match>(this, Team_.matches)
}
