import React, { useState, useEffect } from 'react';
import api from '../api'; // api 모듈을 사용
import { useNavigate, useParams } from 'react-router-dom';

const QuizModify = () => {
    const [form, setForm] = useState({
        title: '',
        categoryName: '',
        answer: '',
        options: ['', '', '', ''] // 옵션을 4개로 초기화
    });
    const { id } = useParams(); // URL에서 ID 가져오기
    const navigate = useNavigate();

    useEffect(() => {
        // 컴포넌트 마운트 시 퀴즈 정보 로드
        const fetchQuiz = async () => {
            try {
                const response = await api.get(`/api/quiz/${id}`);
                const quiz = response.data;
                setForm({
                    title: quiz.title,
                    categoryName: quiz.category.name, // Assuming category is an object with a name property
                    answer: quiz.answer,
                    options: quiz.options
                });
            } catch (error) {
                console.error('Error fetching quiz:', error);
            }
        };

        fetchQuiz();
    }, [id]);

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

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await api.put(`/api/quiz/modify/${id}`, form);
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
                            data-index={index} // 각 입력 필드에 인덱스를 데이터로 전달
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
