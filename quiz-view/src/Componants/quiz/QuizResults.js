import React from 'react';
import { Link } from 'react-router-dom';
import api from '../api';

const QuizResults = ({ results = [], userId, correctCount = 0, count = 0 }) => {

  const handleSaveResults = async (event) => {
    event.preventDefault();
    
    if (results.length === 0) {
      alert('No results to save');
      return;
    }

    try {
      await api.post('/result/save', {
        userId,
        categoryName: results[0]?.quiz?.category?.name || '',
        results
      });
      alert('Results saved successfully!');
    } catch (error) {
      console.error('Error saving results:', error);
    }
  };

  return (
    <div>
      <h1>퀴즈 결과</h1>
      <div>
        <h2>Total Score: {correctCount} / {count}</h2>
      </div>
      <form onSubmit={handleSaveResults}>
        <input type="hidden" name="userId" value={userId} />
        <input type="hidden" name="categoryName" value={results[0]?.quiz?.category?.name || ''} />
        {results.map((result, index) => (
          <p key={index}>
            <strong>문제:</strong> <span>{result.quiz.title}</span><br />
            <strong>제출한 답:</strong> <input type="text" name="userAnswers" value={result.answerGiven} required /><br />
            <strong>정답:</strong> <span>{result.quiz.answer}</span><br />
            <strong>결과:</strong> <span>{result.correct ? '정답' : '오답'}</span>
          </p>
        ))}
        <button type="submit">Save Quiz Results</button>
      </form>
      <Link to={`/quiz/category/${results[0]?.quiz?.category?.name || ''}`}>Take Another Quiz</Link>
      <form action="/result/list" method="get">
        <button type="submit" className="nav-link btn btn-link">퀴즈 결과 조회</button>
      </form>
    </div>
  );
};

export default QuizResults;
