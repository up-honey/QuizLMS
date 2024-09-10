import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import api from '../api';

const QuizSolution = () => {
  const { id } = useParams();
  const [quiz, setQuiz] = useState(null);

  useEffect(() => {
    const fetchQuiz = async () => {
      try {
        const response = await api.get(`/quiz/${id}`);
        setQuiz(response.data);
      } catch (error) {
        console.error('Error fetching quiz:', error);
      }
    };

    fetchQuiz();
  }, [id]);

  if (!quiz) return <div>Loading...</div>;

  return (
    <div>
      <h1>{quiz.category.name} Quiz</h1>
      <form action="/quiz/submit" method="post">
        <input type="hidden" name="categoryName" value={quiz.category.name} />
        {quiz.questions.map((question, index) => (
          <div key={question.id}>
            <h3>{question.title}</h3>
            <input type="hidden" name={`quizId_${index}`} value={question.id} />
            {question.options.map(option => (
              <div key={option}>
                <input type="radio" name={`answer_${index}`} value={option} />
                <label>{option}</label>
              </div>
            ))}
          </div>
        ))}
        <button type="submit">Submit Quiz</button>
      </form>
    </div>
  );
};

export default QuizSolution;
