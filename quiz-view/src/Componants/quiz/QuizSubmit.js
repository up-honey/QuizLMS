import React, { useState, useEffect } from 'react';
import api from '../api'; // axios 대신 api 사용

const QuizSubmit = ({ categoryName }) => {
    const [quizzes, setQuizzes] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (categoryName) {
            api.get(`/quiz?categoryName=${categoryName}`) // axios 대신 api 사용
                .then(response => {
                    setQuizzes(response.data);
                    setError(null);
                })
                .catch(error => {
                    console.error('Error fetching quizzes:', error);
                    setError('Failed to fetch quizzes.');
                });
        } else {
            setError('Invalid category name.');
        }
    }, [categoryName]);

    const handleSubmit = (event) => {
        event.preventDefault();
        const formData = new FormData(event.target);
        api.post('/quiz/submit', formData) // axios 대신 api 사용
            .then(response => {
                // Handle success
            })
            .catch(error => {
                console.error('Error submitting quiz:', error);
                setError('Failed to submit quiz.');
            });
    };

    return (
        <div>
            <h1>{categoryName ? `${categoryName} Quiz` : 'Quiz'}</h1>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            <form onSubmit={handleSubmit}>
                <input type="hidden" name="categoryName" value={categoryName || ''} />
                {quizzes.map((quiz, index) => (
                    <div key={quiz.id}>
                        <h3>{quiz.title}</h3>
                        <input type="hidden" name={`quizId_${index}`} value={quiz.id} />
                        {quiz.options.map(option => (
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

export default QuizSubmit;
