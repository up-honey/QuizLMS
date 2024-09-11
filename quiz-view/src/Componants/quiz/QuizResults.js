import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import api from '../api'; // axios 인스턴스
import '../../Css/Quiz.css';
const QuizResults = () => {
    const navigate = useNavigate(); // useHistory 대신 useNavigate 사용
    const location = useLocation();
    
    // location.state에서 퀴즈 결과 데이터를 받아옴
    const { correctCount, count, userId, results } = location.state || {
        correctCount: 0,
        count: 0,
        userId: null,
        results: []
    };

    const handleSaveResults = (event) => {
        event.preventDefault();
        const formData = new FormData(event.target);

        api.post('/api/quiz/submit', formData)
            .then(response => {
                console.log('Quiz results saved successfully:', response.data);
                // 성공 후 원하는 동작 추가
            })
            .catch(error => {
                console.error('Error saving quiz results:', error);
            });
    };

    const handleTakeAnotherQuiz = () => {
        if (results.length > 0) {
            navigate(`/quiz/category/${results[0].quiz.category.name}`); // history.push 대신 navigate 사용
        }
    };

    return (
        <div className="wrapper result">
            <h1>퀴즈 결과</h1>
            <div>
                <h2>
                    총 점수: <span>{correctCount}</span> / <span>{count}</span>
                </h2>
            </div>

            <form onSubmit={handleSaveResults}>
                <input type="hidden" name="userId" value={userId} />
                <input type="hidden" name="categoryName" value={results[0]?.quiz.category.name} />

                {results.map((result, index) => (
                    <div key={index} className="quiz-result">
                        <p>
                            <strong>문제:</strong> <span>{result.quiz.title}</span><br />
                            <strong>제출한 답:</strong>
                            <input type="text" name={`userAnswers[${index}]`} defaultValue={result.answerGiven} readOnly /><br />
                            <strong>정답:</strong> <span>{result.quiz.answer}</span><br />
                            <strong>결과:</strong> <span>{result.correct ? '정답' : '오답'}</span>
                        </p>
                    </div>
                ))}
            </form>

            <button onClick={handleTakeAnotherQuiz}>다른 퀴즈 풀기</button>

            <button onClick={() => navigate('/result/list')} className="nav-link btn btn-link">퀴즈 결과 조회</button>
        </div>
    );
};

export default QuizResults;
