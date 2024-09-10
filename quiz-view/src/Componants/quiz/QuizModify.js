import React, { useState, useEffect } from 'react';
import api from '../api'; // axios 대신 api 사용
import { useParams, useNavigate } from 'react-router-dom';

const QuizModify = () => {
    const [quizForm, setQuizForm] = useState({ categoryName: '', title: '', answer: '', options: '' });
    const { id } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        api.get(`/quiz/${id}`) // axios 대신 api 사용
            .then(response => setQuizForm(response.data))
            .catch(error => console.error('Error fetching quiz:', error));
    }, [id]);

    const handleChange = (event) => {
        const { name, value } = event.target;
        setQuizForm({ ...quizForm, [name]: value });
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        api.post(`/quiz/modify/${id}`, quizForm) // axios 대신 api 사용
            .then(() => navigate('/quiz/list'))
            .catch(error => console.error('Error modifying quiz:', error));
    };

    return (
        <div className="container">
            <h1>퀴즈 수정</h1>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="categoryName">카테고리 이름</label>
                    <input type="text" id="categoryName" name="categoryName" value={quizForm.categoryName} onChange={handleChange} className="form-control" required />
                </div>
                <div className="form-group">
                    <label htmlFor="title">제목</label>
                    <input type="text" id="title" name="title" value={quizForm.title} onChange={handleChange} className="form-control" required />
                </div>
                <div className="form-group">
                    <label htmlFor="answer">정답</label>
                    <input type="text" id="answer" name="answer" value={quizForm.answer} onChange={handleChange} className="form-control" required />
                </div>
                <button type="submit" className="btn btn-primary">수정</button>
                <button type="button" className="btn btn-secondary" onClick={() => navigate('/quiz/list')}>목록으로 돌아가기</button>
            </form>
        </div>
    );
};

export default QuizModify;
