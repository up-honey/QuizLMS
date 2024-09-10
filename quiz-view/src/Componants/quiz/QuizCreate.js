import React, { useState } from 'react';
import api from '../api'; // axios 대신 api 사용
import { useNavigate } from 'react-router-dom';

const QuizCreate = () => {
    const [quizForm, setQuizForm] = useState({ categoryName: '', title: '', answer: '', options: '' });
    const navigate = useNavigate();

    const handleChange = (event) => {
        const { name, value } = event.target;
        setQuizForm({ ...quizForm, [name]: value });
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        // API 경로를 /quiz/create로 설정
        api.post('/quiz/create', quizForm) // axios 대신 api 사용
            .then(() => navigate('/quiz/list')) // 성공 시 목록으로 이동
            .catch(error => console.error('Error creating quiz:', error));
    };

    return (
        <div className="container">
            <h1>퀴즈 등록</h1>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="categoryName">카테고리 이름</label>
                    <input
                        type="text"
                        id="categoryName"
                        name="categoryName"
                        value={quizForm.categoryName}
                        onChange={handleChange}
                        className="form-control"
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="title">제목</label>
                    <input
                        type="text"
                        id="title"
                        name="title"
                        value={quizForm.title}
                        onChange={handleChange}
                        className="form-control"
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="answer">정답</label>
                    <input
                        type="text"
                        id="answer"
                        name="answer"
                        value={quizForm.answer}
                        onChange={handleChange}
                        className="form-control"
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="options">옵션 (쉼표로 구분)</label>
                    <input
                        type="text"
                        id="options"
                        name="options"
                        value={quizForm.options}
                        onChange={handleChange}
                        className="form-control"
                        placeholder="예: 옵션1, 옵션2, 옵션3"
                        required
                    />
                </div>
                <button type="submit" className="btn btn-primary">등록</button>
                <button type="button" className="btn btn-secondary" onClick={() => navigate('/quiz/list')}>목록으로 돌아가기</button>
            </form>
        </div>
    );
};

export default QuizCreate;
