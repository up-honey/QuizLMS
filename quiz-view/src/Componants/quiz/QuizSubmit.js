import React, { useState, useEffect, useCallback } from 'react';
import { useParams } from 'react-router-dom';
import api from '../api';

const QuizSubmit = () => {
    const { categoryName } = useParams();
    const [quizzes, setQuizzes] = useState([]);
    const [currentQuizIndex, setCurrentQuizIndex] = useState(0);
    const [userAnswers, setUserAnswers] = useState({});
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const [quizCompleted, setQuizCompleted] = useState(false);
    
    const fetchQuizzes = useCallback(async () => {
        if (categoryName) {
            try {
                const response = await api.get(`/api/quiz/category/${categoryName}`, {
                    params: { page: currentPage, size: 10 }
                });
                setQuizzes(response.data.content);
                setError(null);
            } catch (error) {
                console.error('Error fetching quizzes:', error);
                setError('Failed to fetch quizzes.');
            }
        } else {
            setError('Invalid category name.');
            setLoading(false);
        }
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
        setUserAnswers(prev => ({ ...prev, [quizId]: answer }));
        
        // Submit the answer immediately
        api.post('/api/quiz/submit', {
            categoryName: categoryName,
            quizId: quizId,
            answer: answer
        })
            .then(response => {
                console.log('Answer submitted successfully:', response.data);
                // Move to the next question
                if (currentQuizIndex < quizzes.length - 1) {
                    setCurrentQuizIndex(prevIndex => prevIndex + 1);
                } else {
                    setQuizCompleted(true);
                }
            })
            .catch(error => {
                console.error('Error submitting answer:', error);
                setError('Failed to submit answer. Please try again.');
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
                <div>Question: {currentQuizIndex + 1}/{quizzes.length}</div>
                <div>Correct Answers: {Object.values(userAnswers).filter(Boolean).length}</div>
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