WITH fts_result AS
(
       SELECT questions.question_head,
              questions.question_body ,
              Match(questions.question_head) against (what IS java) AS relevance_score
       FROM   questions
       WHERE  match(questions.question_head) against (what IS java) )
(
       SELECT *
       FROM   fts_result
       WHERE  fts_result.question_head LIKE %s java% )
UNION
      (
             SELECT *
             FROM   fts_result
             WHERE  NOT EXISTS
                    (
                           SELECT *
                           FROM   fts_result
                           WHERE  fts_result.question_head LIKE %s java%
                    )
      ) ORDER BY relevance_score DESC