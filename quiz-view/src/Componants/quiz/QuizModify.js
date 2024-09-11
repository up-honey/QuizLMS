import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import api from '../api'; // api 모듈을 사용

const QuizModify = () => {
    const [form, setForm] = useState({
        title: '',
        categoryName: '',
        answer: '',
        options: ['', '', '', ''] // 옵션을 4개로 초기화
    });
    const { id } = useParams(); // URL에서 ID 가져오기
    const navigate = useNavigate();

    // 컴포넌트 마운트 시 퀴즈 정보 로드
    useEffect(() => {
        const fetchQuiz = async () => {
            try {
                const response = await api.get(`/api/quiz/${id}`);
                const quiz = response.data;
                setForm({
                    title: quiz.title,
                    categoryName: quiz.category.name, // Assuming category is an object with a name property
                    answer: quiz.answer,
                    options: quiz.options || ['', '', '', ''] // options가 없는 경우 초기화
                });
            } catch (error) {
                console.error('Error fetching quiz:', error);
            }
        };
    
        fetchQuiz();
    }, [id]);

    // 입력 필드 변경 처리
    const handleChange = (e) => {
        const { name, value, dataset } = e.target;
        if (name === 'options') {
            const index = parseInt(dataset.index, 10);
            setForm(prevForm => {
                const newOptions = [...prevForm.options];
                newOptions[index] = value;
                return { ...prevForm, options: newOptions };
            });
        } else {
            setForm(prevForm => ({ ...prevForm, [name]: value }));
        }
    };

    // 폼 제출 처리
    const handleSubmit = async (e) => {
        e.preventDefault();
        console.log('Submitting form:', form); // 상태 확인
        try {
            const response = await api.put(`/api/quiz/modify/${id}`, {
                title: form.title,
                categoryName: form.categoryName,
                answer: form.answer,
                options: form.options // options 포함
            });
            console.log('Response from API:', response); // 응답 확인
            alert('Quiz modified successfully');
            navigate('/quiz/list');
        } catch (error) {
            console.error('Error modifying quiz:', error);
            alert('Failed to modify quiz');
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <div>
                <label>Title:</label>
                <input
                    type="text"
                    name="title"
                    value={form.title}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label>Category Name:</label>
                <input
                    type="text"
                    name="categoryName"
                    value={form.categoryName}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label>Answer:</label>
                <input
                    type="text"
                    name="answer"
                    value={form.answer}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <label>Options:</label>
                {form.options.map((option, index) => (
                    <div key={index}>
                        <input
                            type="text"
                            name="options"
                            data-index={index}
                            value={option}
                            onChange={handleChange}
                            required
                        />
                    </div>
                ))}
            </div>
            <button type="submit">Modify Quiz</button>
        </form>
    );
    
};

export default QuizModify;
