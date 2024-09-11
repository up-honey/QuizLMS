import React, { useState, useEffect, useCallback } from 'react';
import { useParams } from 'react-router-dom';
import api from '../api';

const QuizSubmit = () => {
    const { categoryName } = useParams();
    console.log("Category Name:", categoryName);
    const [quizzes, setQuizzes] = useState([]);
    const [currentPage] = useState(0);
    const [error, setError] = useState(null);
    
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
        }
    }, [categoryName, currentPage]);

    useEffect(() => {
        fetchQuizzes();
    }, [fetchQuizzes]);

    const handleSubmit = (event) => {
        event.preventDefault();
        const formData = new FormData(event.target);
        api.post('/api/quiz/submit', formData)
            .then(response => {
                // Handle success
                console.log('Quiz submitted successfully:', response.data);
            })
            .catch(error => {
                console.error('Error submitting quiz:', error);
                setError('Failed to submit quiz.');
            });
    };

    return (
        <div className='wrapper'>
            <h1>{categoryName ? `${categoryName} Quiz` : 'Quiz'}</h1>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            <div className="timer-bar">
                <div className="timer" style={{ width: '50%' }}></div> {/* Example progress */}
            </div>
            <form onSubmit={handleSubmit}>
                <input type="hidden" name="categoryName" value={categoryName || ''} />
                {quizzes.map((quiz, index) => (
                    <div key={quiz.id} className="quiz-container">
                        <div className="quiz-question">
                            <h3>{quiz.title}</h3>
                        </div>
                        <input type="hidden" name={`quizId_${index}`} value={quiz.id} />
                        <div className="quiz-options">
                            {quiz.options.map(option => (
                                <button
                                    key={option}
                                    type="button"
                                    className="quiz-option"
                                >
                                    <input
                                        type="radio"
                                        id={`${quiz.id}_${option}`}
                                        name={`answer_${quiz.id}`}
                                        value={option}
                                        style={{ display: 'none' }} // Hide radio button
                                    />
                                    <label htmlFor={`${quiz.id}_${option}`}>{option}</label>
                                </button>
                            ))}
                        </div>
                    </div>
                ))}
                <button type="submit" className="quiz-submit">Submit Quiz</button>
            </form>
        </div>
    );
};

export default QuizSubmit;