package com.kubekbreha.fastreader.theme.model

/**
 * Created by Pankaj on 03-11-2017.
 */

class Theme {
    var id: Int = 0
    var primaryColor: Int = 0
    var primaryDarkColor: Int = 0
    var accentColor: Int = 0

    constructor(primaryColor: Int, primaryDarkColor: Int, accentColor: Int) {
        this.primaryColor = primaryColor
        this.primaryDarkColor = primaryDarkColor
        this.accentColor = accentColor
    }

    constructor(id: Int, primaryColor: Int, primaryDarkColor: Int, accentColor: Int) {
        this.id = id
        this.primaryColor = primaryColor
        this.primaryDarkColor = primaryDarkColor
        this.accentColor = accentColor
    }
}
