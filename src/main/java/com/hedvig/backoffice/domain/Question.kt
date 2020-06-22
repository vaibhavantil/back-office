package com.hedvig.backoffice.domain

import org.hibernate.annotations.Type
import java.time.Instant
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.ManyToOne

@Entity
class Question(
  @Id var id: Long,
  @Type(type = "org.hibernate.type.TextType")
  @Lob var message: String,
  var date: Instant
) {
  @ManyToOne
  var group: QuestionGroup? = null
}
