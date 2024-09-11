import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom'; // useHistory를 useNavigate로 변경
import api from '../api'; // axios 인스턴스

const QuizSubmit = () => {
    const { categoryName } = useParams();
    const navigate = useNavigate(); // useNavigate 훅을 사용하여 history 객체 가져오기
    const [quizzes, setQuizzes] = useState([]);
    const [currentQuizIndex, setCurrentQuizIndex] = useState(0);
    const [userAnswers, setUserAnswers] = useState({});
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const [quizCompleted, setQuizCompleted] = useState(false);
    
    useEffect(() => {
        if (categoryName) {
            fetchQuizzes();
        } else {
            setError('Invalid category name.');
            setLoading(false);
        }
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [categoryName]);

    const fetchQuizzes = () => {
        setLoading(true);
        api.get(`/api/quiz/category/${categoryName}`, {
            params: { page: 0, size: 10 }
        })
            .then(response => {
                setQuizzes(response.data.content);
                setError(null);
                setLoading(false);
            })
            .catch(error => {
                console.error('Error fetching quizzes:', error);
                setError('Failed to fetch quizzes.');
                setLoading(false);
            });
    };

    const handleAnswer = (quizId, answer) => {
        // 현재 퀴즈의 답변 저장
        setUserAnswers(prev => {
            const updatedAnswers = { ...prev, [quizId]: answer };
    
            // 현재 퀴즈가 마지막 퀴즈일 경우, 모든 답변을 제출
            if (currentQuizIndex === quizzes.length - 1) {
                submitAllAnswers(updatedAnswers); // 업데이트된 답변을 전달
            } else {
                // 다음 질문으로 이동
                setCurrentQuizIndex(prevIndex => prevIndex + 1);
            }
    
            return updatedAnswers; // 상태 업데이트
        });
    };
    
    const submitAllAnswers = (answers) => {
        const answersWithId = Object.entries(answers).map(([quizId, answer]) => ({
            quizId,
            answer,
        }));
    
        api.post('/api/quiz/submit', {
            categoryName: categoryName,
            answers: answersWithId // 각 퀴즈 ID와 답변을 포함
        })
            .then(response => {
                console.log('All answers submitted successfully:', response.data);
    
                const { correctCount, count, userId, results } = response.data;
    
                navigate('/quiz/submit', {
                    state: {
                        correctCount,
                        count,
                        userId,
                        results
                    }
                });
    
                setQuizCompleted(true);
            })
            .catch(error => {
                console.error('Error submitting answers:', error);
                setError('Failed to submit answers. Please try again.');
            });
    };
    

    if (loading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div style={{ color: 'red' }}>{error}</div>;
    }

    if (quizCompleted) {
        return <div>Quiz completed! Thank you for participating.</div>;
    }

    const currentQuiz = quizzes[currentQuizIndex];

    return (
        <div className='wrapper'>
            <h1>{categoryName ? `${categoryName} Quiz` : 'Quiz'}</h1>
            <div className="status-bar">
                <div>문제 개수: {currentQuizIndex + 1}/{quizzes.length}</div>
            </div>
            <div className="timer-bar">
                <div className="timer" style={{ width: `${((currentQuizIndex + 1) / quizzes.length) * 100}%` }}></div>
            </div>
            {currentQuiz && (
                <div className="quiz-container">
                    <div className="quiz-question">
                        <h3>{currentQuiz.title}</h3>
                    </div>
                    <div className="quiz-options">
                        {currentQuiz.options.map(option => (
                            <button
                                key={option}
                                type="button"
                                className="quiz-option"
                                onClick={() => handleAnswer(currentQuiz.id, option)}
                                disabled={userAnswers[currentQuiz.id] !== undefined}
                            >
                                {option}
                            </button>
                        ))}
                    </div>
                </div>
            )}
        </div>
    );
};

export default QuizSubmit;
