package com.hedvig.backoffice.services.members.dto

import java.util.Locale

enum class PickedLocale {
  sv_SE {
    override val locale: Locale = Locale("sv", "SE")
  },
  en_SE {
    override val locale: Locale = Locale("en", "SE")
  },
  nb_NO{
    override val locale: Locale = Locale("nb", "NO")
  },
  en_NO {
    override val locale: Locale = Locale("en", "NO")
  };

  abstract val locale: Locale
}
