package com.zeroboss.scoring500.data.common

import io.objectbox.converter.PropertyConverter

class NonBiddingPointsConverter : PropertyConverter<NonBiddingPointsType?, Int?> {
    override fun convertToEntityProperty(databaseValue: Int?): NonBiddingPointsType? {
        if (databaseValue == null) {
            return null
        }

        for (type in NonBiddingPointsType.values()) {
            if (type.ordinal == databaseValue) {
                return type
            }
        }

        return NonBiddingPointsType.ONLY_IF_LOSS
    }

    override fun convertToDatabaseValue(type: NonBiddingPointsType?): Int? {
        return type?.ordinal
    }
}