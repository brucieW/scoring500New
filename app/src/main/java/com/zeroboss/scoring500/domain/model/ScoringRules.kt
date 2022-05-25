package com.zeroboss.scoring500.domain.model

import com.zeroboss.scoring500.data.common.NonBiddingPointsConverter
import com.zeroboss.scoring500.data.common.NonBiddingPointsType
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class ScoringRules(
    @Id var id: Long = 0,

    @Convert(converter = NonBiddingPointsConverter::class, dbType = Int::class)
    var nonBiddingPointsType: NonBiddingPointsType = NonBiddingPointsType.ONLY_IF_LOSS,

    var isTenTricksBonus: Boolean = true,
)
