package com.zeroboss.scoring500.domain.model

import com.zeroboss.scoring500.data.common.TrumpConverter
import com.zeroboss.scoring500.presentation.common.Trump
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
data class Hand(
    @Id
    var id: Long = 0,

    @Convert(converter = TrumpConverter::class, dbType = String::class)
    var bid: Trump = Trump(),
) {
    var game = ToOne<Game>(this, Hand_.game)
}
