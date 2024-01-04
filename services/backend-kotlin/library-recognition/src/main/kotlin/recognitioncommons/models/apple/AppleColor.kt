package recognitioncommons.models.apple

import recognitioncommons.util.Presentation

enum class AppleColor {
    SPACE_GRAY,
    SPACE_GREY,
    GRAY,
    GREY,

    SPACE_BLACK,
    BLACK,


    MIDNIGHT_GREEN,
    ALPINE_GREEN,
    GREEN,

    DEEP_PURPLE,
    PURPLE,

    PACIFIC_BLUE,
    SIERRA_BLUE,
    BLUE,
    SKY_BLUE,

    RED,

    MIDNIGHT,
    STARLIGHT,
    GRAPHITE,

    SILVER,
    GOLD,
    YELLOW,
    WHITE,
    CORAL,
    PINK,
    ROSE,

    DEFAULT,
    ;

    override fun toString(): String {
        return Presentation.formatEnumName(this.name)
    }
}
