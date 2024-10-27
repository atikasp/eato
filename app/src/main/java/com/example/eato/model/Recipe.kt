package com.example.eato.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Recipe(
    @DrawableRes val imageRes: Int,
    @StringRes val dayRes: Int,
    @StringRes val nameRes: Int,
    @StringRes val ingredientRes: Int,
    @StringRes val instructionRes: Int,

)