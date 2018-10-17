package com.hedvig.backoffice.domain;

import java.time.Instant;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.hedvig.backoffice.web.dto.QuestionSortFields;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class QuestionGroup {

  public static final EnumMap<QuestionSortFields, String> SORT_FIELDS_MAPPING = new EnumMap<QuestionSortFields, String>(QuestionSortFields.class) {{
    put(QuestionSortFields.MEMBER_ID, "subscription.memberId");
    put(QuestionSortFields.CREATION_DATE, "date");
  }};

  @Id @GeneratedValue private Long id;

  private Instant date;

  private Instant answerDate;

  @Lob
  @Type(type = "org.hibernate.type.TextType")
  private String answer;

  @ManyToOne private Personnel personnel;

  @ManyToOne private Subscription subscription;

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "group")
  @OrderBy("date")
  private Set<Question> questions = new HashSet<>();

  public QuestionGroup(Subscription subscription) {
    this.subscription = subscription;
  }

  public void addQuestion(Question question) {
    question.setGroup(this);
    questions.add(question);
  }

  public void correctDate(Instant date) {
    if (this.date != null && this.date.isAfter(date)) {
      return;
    }

    this.date = date;
  }
}
