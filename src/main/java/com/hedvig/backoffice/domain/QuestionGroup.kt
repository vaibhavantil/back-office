package com.hedvig.backoffice.domain

import org.hibernate.annotations.Type
import java.time.Instant
import java.util.*
import javax.persistence.*

@Entity
class QuestionGroup(
  @ManyToOne
  var subscription: Subscription
) {
  @Id
  @GeneratedValue
  var id: Long = 0
  var date: Instant? = null
  var answerDate: Instant? = null
  @Lob
  @Type(type = "org.hibernate.type.TextType")
  var answer: String? = null
  @ManyToOne
  var personnel: Personnel? = null
  @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], mappedBy = "group")
  @OrderBy("date")
  var questions: MutableSet<Question> = HashSet()

  fun addQuestion(question: Question) {
    question.group = this
    questions.add(question)
  }

  fun correctDate(date: Instant) {
    if (this.date != null && date.isAfter(date)) {
      return
    }
    this.date = date
  }
}
