package com.example.stitchcounterv3.domain.model

sealed class DismissalResult {
    object Allowed : DismissalResult()
    object Blocked : DismissalResult()
    object ShowDiscardDialog : DismissalResult()
}
